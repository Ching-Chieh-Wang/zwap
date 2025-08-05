package com.zwap.product_read_service.service.impl;

import com.zwap.product_common.entity.Product;
import com.zwap.product_common.exception.ProductNotFoundException;
import com.zwap.product_common.mapper.ProductMapper;
import com.zwap.product_read_service.converter.ProductConverter;
import com.zwap.product_read_service.service.IProductService;
import com.zwap.product_read_service.vo.ProductVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private IProductService self;

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductVO getById(String id) {
        Product product = productMapper.findById(id).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return ProductConverter.toProductVO(product);
    }

    @Override
    public boolean isUserOwner(String userId, String productId) {
        ProductVO product = self.getById(productId);
        return userId.equals(product.getUserId());
    }
}
