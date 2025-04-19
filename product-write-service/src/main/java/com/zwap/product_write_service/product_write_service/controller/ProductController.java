package com.zwap.product_write_service.product_write_service.controller;

import com.zwap.common.common.entity.Product;
import com.zwap.product_write_service.product_write_service.dto.ProductCreateDTO;
import com.zwap.product_write_service.product_write_service.converter.ProductConverter;
import com.zwap.product_write_service.product_write_service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductConverter productConverter;

    @PostMapping("/")
    public Product addProduct(@RequestHeader("user-id") String userId, @RequestBody ProductCreateDTO productDTO) {
        Product product = ProductConverter.toEntity(productDTO, userId);
        productMapper.save(product);
        return product;
    }
}
