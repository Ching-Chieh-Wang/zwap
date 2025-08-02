package com.zwap.product_write_service.product_write_service.service.impl;

import com.zwap.product_common.product_common.entity.Product;
import com.zwap.product_common.product_common.mapper.ProductMapper;
import com.zwap.product_write_service.product_write_service.converter.ProductConverter;
import com.zwap.product_write_service.product_write_service.dto.ProductCreateQry;
import com.zwap.product_write_service.product_write_service.dto.ProductUpdateQry;
import com.zwap.product_write_service.product_write_service.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;


    @Override
    public void create(String userId, ProductCreateQry productCreateQry) {
        Product product = ProductConverter.toEntity(productCreateQry, userId);
        productMapper.save(product);
    }

    @Override
    public void update(String userId, String id, ProductUpdateQry productUpdateQry) {
//        Optional<Product> optionalProduct = productMapper.findByIdAndUserId(id, userId);
//        if (optionalProduct.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this product");
//        }
//        Product product = optionalProduct.get();
//        Product product = ProductConverter.toEntity(productDTO, userId);
//        productMapper.save(product);
//        return product;
    }
}
