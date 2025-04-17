package com.zwap.product_service.product_service.converter;

import com.zwap.common.common.entity.Product;
import com.zwap.product_service.product_service.dto.ProductCreateDTO;
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
        product.setExpiredAt(dto.getExpiredAt());
        return product;
    }
}