package com.zwap.product_write_service.product_write_service.service.impl;

import com.zwap.product_common.product_common.entity.Product;
import com.zwap.product_common.product_common.mapper.ProductMapper;
import com.zwap.product_write_service.product_write_service.converter.ProductConverter;
import com.zwap.product_write_service.product_write_service.dto.ProductCreateDTO;
import com.zwap.product_write_service.product_write_service.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;


    @Override
    public void create(String userId, ProductCreateDTO productCreateDTO) {
        Product product = ProductConverter.toEntity(productCreateDTO, userId);
        productMapper.save(product);
    }
}
