package com.zwap.product_write_service.product_write_service.converter;

import com.zwap.product_common.product_common.entity.Product;
import com.zwap.product_write_service.product_write_service.dto.ProductCreateQry;
import com.zwap.product_write_service.product_write_service.dto.ProductUpdateQry;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public static Product toEntity(ProductCreateQry qry, String userId) {
        Product product = new Product();
        BeanUtils.copyProperties(qry, product);
        product.setUserId(userId);
        return product;
    }

    public static Product toEntity(ProductUpdateQry qry) {
        Product product = new Product();
        BeanUtils.copyProperties(qry, product);
        return product;
    }
}