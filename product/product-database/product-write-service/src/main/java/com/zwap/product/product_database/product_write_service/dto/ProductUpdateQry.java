package com.zwap.product.product_database.product_write_service.dto;

import lombok.Data;

@Data
public class ProductUpdateQry {
    private String title;
    private String description;
    private String imageUrl;
    private Integer price;
}