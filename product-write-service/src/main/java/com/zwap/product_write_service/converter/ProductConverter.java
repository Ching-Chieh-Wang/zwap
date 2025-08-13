package com.zwap.product_write_service.converter;

import com.zwap.product_common.entity.Product;
import com.zwap.product_write_service.dto.ProductCreateQry;
import com.zwap.product_write_service.dto.ProductUpdateQry;
import org.springframework.stereotype.Component;

import static com.zwap.product_write_service.utils.BeanCopyUtils.copyNonNullProperties;

@Component
public class ProductConverter {
    public static Product toEntity(ProductCreateQry qry, String userId) {
        Product product = new Product();
        copyNonNullProperties(qry, product);
        product.setUserId(userId);
        return product;
    }

    public static Product toEntity(String id, ProductUpdateQry qry) {
        Product product = new Product();
        product.setId(id);
        copyNonNullProperties(qry, product);
        return product;
    }
}