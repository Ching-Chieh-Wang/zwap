package com.zwap.product_write_service.service.impl;

import com.zwap.product_common.entity.Product;
import com.zwap.product_write_service.client.ProductReadGrpcClient;
import com.zwap.product_write_service.converter.ProductConverter;
import com.zwap.product_write_service.dto.ProductCreateQry;
import com.zwap.product_write_service.dto.ProductUpdateQry;
import com.zwap.product_write_service.service.ProductService;
import com.zwap.product_common.mapper.ProductMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
        boolean isOwner = productReadGrpcClient.isUserProductOwner(userId, id);
        if (!isOwner) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.FORBIDDEN, "You do not own this product"
            );
        }
        Product product = ProductConverter.toEntity(productUpdateQry);
        productMapper.save(product);
    }
}

