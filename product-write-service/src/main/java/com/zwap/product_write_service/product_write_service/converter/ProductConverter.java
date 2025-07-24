package com.zwap.product_write_service.product_write_service.converter;

import com.zwap.product_common.product_common.entity.Product;
import com.zwap.product_write_service.product_write_service.dto.ProductCreateDTO;
import com.zwap.product_write_service.product_write_service.dto.ProductUpdateDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public static Product toEntity(ProductCreateDTO dto, String userId) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        product.setUserId(userId);
        return product;
    }

    public static Product toEntity(ProductUpdateDTO dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        return product;
    }
}