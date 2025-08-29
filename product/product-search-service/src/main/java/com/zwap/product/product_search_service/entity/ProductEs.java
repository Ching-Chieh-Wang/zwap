package com.zwap.product.product_search_service.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.ScriptedField;

@Data
@Document(indexName = "mongo.product.products.embedded")
public class ProductEs {
    
    private String id;
    private String userId;
    private String userName;
    private String userImgUrl;
    private String title;
    private String description;
    private String imageUrl;
    private Integer price;

    @ScriptedField
    private double distance;
}
