package com.zwap.product.product_database.product_write_service.converter;

import com.zwap.product.product_database.product_database_common.entity.ProductMongo;
import com.zwap.product.product_database.product_write_service.dto.ProductCreateQry;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;



@Component
public class ProductConverter {
    public static ProductMongo toMongo(ProductCreateQry qry, String userId) {
        ProductMongo product = new ProductMongo();
        BeanUtils.copyProperties(qry, product);
        product.setUserId(userId);
        return product;
    }
}