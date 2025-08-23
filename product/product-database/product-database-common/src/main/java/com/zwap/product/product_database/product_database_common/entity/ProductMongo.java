package com.zwap.product.product_database.product_database_common.entity;

import com.zwap.product.product_common.model.ProductBase;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "products")
public class ProductMongo extends ProductBase {
    @Id
    private String id;
}
