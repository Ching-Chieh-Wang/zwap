package com.zwap.product_write_service.product_write_service.converter;

import com.zwap.product_common.product_common.entity.Product;
import com.zwap.product_write_service.product_write_service.dto.ProductCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public static Product toEntity(ProductCreateDTO dto, String userId) {
        Product product = new Product();
        product.setUserId(userId);
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setImagePath(dto.getImagePath());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setAvailableQuantity(dto.getQuantity());
        return product;
    }
}