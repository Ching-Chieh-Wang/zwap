package com.zwap.product_write_service.service.impl;

import com.zwap.product_common.entity.Product;
import com.zwap.product_write_service.client.ProductReadGrpcClient;
import com.zwap.product_write_service.converter.ProductConverter;
import com.zwap.product_write_service.dto.ProductCreateQry;
import com.zwap.product_write_service.dto.ProductUpdateQry;
import com.zwap.product_write_service.service.ProductService;
import com.zwap.product_common.mapper.ProductMapper;
import com.zwap.product_write_service.utils.BeanCopyUtils;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductServiceImpl implements ProductService {



    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductReadGrpcClient productReadGrpcClient;



    @Override
    public void create(String userId, ProductCreateQry productCreateQry) {
        Product product = ProductConverter.toEntity(productCreateQry, userId);
        productMapper.save(product);
    }

    @Override
    public void update(String userId, String id, ProductUpdateQry productUpdateQry) {
        Product product = productReadGrpcClient.getProductById(id);
        if (!product.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "You do not own this product"
            );
        }
        BeanCopyUtils.copyNonNullProperties(productUpdateQry, product);
        productMapper.save(product);
    }
}

