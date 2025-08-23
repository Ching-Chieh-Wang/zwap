package com.zwap.product.product_database.product_read_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zwap.product.product_database.product_database_common.entity.ProductMongo;
import com.zwap.product.product_database.product_database_common.exception.ProductNotFoundException;
import com.zwap.product.product_database.product_database_common.exception.ProductPendingException;
import com.zwap.product.product_database.product_database_common.mapper.IProductMapper;
import com.zwap.product.product_database.product_database_common.variable.RedisPrefix;
import com.zwap.product.product_database.product_database_common.variable.RedisTTL;
import com.zwap.product.product_database.product_read_service.converter.ProductConverter;
import com.zwap.product.product_database.product_read_service.service.IProductService;
import com.zwap.product.product_database.product_read_service.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;


import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.List;

@Primary
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductMapper productMapper;

    @Autowired
    private ObjectMapper objMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DefaultRedisScript<Boolean> casScript;

    @Autowired
    private DefaultRedisScript<Boolean> unlockScript;

    // Sentinels
    private static final String LOCKED = "__LOCKED__";
    private static final String NOT_FOUND = "__NOT_FOUND__";

    @Override
    public ProductVO getVOById(String id) {
        final String productKey = RedisPrefix.PRODUCT.getPrefix() + id;

        // 1) Fast path: read cache
        String cachedProduct = stringRedisTemplate.opsForValue().get(productKey);

        if (cachedProduct == null) {
            log.debug("Cache miss for product ID: {}", id);
            // Cache miss, proceed to load from database
            final String token = Thread.currentThread().getName() + "-" + UUID.randomUUID();
            final String lockValue = LOCKED + ":" + token;

            Boolean isLockSuccess = stringRedisTemplate.opsForValue()
                    .setIfAbsent(productKey, lockValue, RedisTTL.LOCKED.getJitteredDuration());
            if (Boolean.TRUE.equals(isLockSuccess)) {
                try {
                    // Double-check cache after lock (another instance may have finished)
                    cachedProduct = stringRedisTemplate.opsForValue().get(productKey);
                    if (cachedProduct != null && !cachedProduct.startsWith(LOCKED) && !NOT_FOUND.equals(cachedProduct)) {
                        return parseJson(cachedProduct);
                    }

                    // Load from Mongo
                    ProductMongo product = productMapper.findById(id).orElse(null);
                    if (product == null) {
                        stringRedisTemplate.opsForValue().set(productKey, NOT_FOUND, RedisTTL.NOT_FOUND.getJitteredDuration());
                        throw new ProductNotFoundException("Product not found with id: " + id);
                    }

                    ProductVO productVO = ProductConverter.toVO(product);

                    // Use Lua compare-and-set to only write if lock is still held
                    stringRedisTemplate.execute(casScript, List.of(productKey), lockValue, writeJson(productVO));
                    return productVO;
                } finally {
                    // Compare-and-DEL unlock (Lua) to avoid deleting someone else's lock
                    stringRedisTemplate.execute(unlockScript, List.of(productKey), lockValue);
                }
            } else {
                // Another instance is/was the loader; either poll briefly or return 202
                ProductVO vo = pollCache(productKey, 400);
                if (vo != null) return vo;
                throw new ProductPendingException("Product is being prepared, please retry.");
            }
        }
        else{
            if (NOT_FOUND.equals(cachedProduct)) {
                throw new ProductNotFoundException("Product not found with id: " + id);
            }
            if (cachedProduct.startsWith(LOCKED) ) {
                ProductVO productVO = pollCache(productKey, 400);
                if (productVO != null) return productVO;
                throw new ProductPendingException("Product is being prepared, please retry.");
            }
            return parseJson(cachedProduct);
        }

    }

    @Override
    public boolean isProductOwnder(String id, String userId) {
        ProductVO productVO = getVOById(id);
        return userId.equals(productVO.getUserId());
    }

    private ProductVO pollCache(String cacheKey, int maxMillis) {
        long end = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(maxMillis);
        while (System.nanoTime() < end) {
            String cachedProduct = stringRedisTemplate.opsForValue().get(cacheKey);
            if (cachedProduct != null && !(cachedProduct.startsWith(LOCKED)) && !NOT_FOUND.equals(cachedProduct)) {
                return parseJson(cachedProduct);
            }
            if (NOT_FOUND.equals(cachedProduct)) {
                throw new ProductNotFoundException("Product not found");
            }
            try { Thread.sleep(20); } catch (InterruptedException ignored) {}
        }
        return null;
    }

    private ProductVO parseJson(String json) {
        try {
            return objMapper.readValue(json, ProductVO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse ProductVO from cache", e);
        }
    }

    private String writeJson(Object obj) {
        try {
            return objMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

}
