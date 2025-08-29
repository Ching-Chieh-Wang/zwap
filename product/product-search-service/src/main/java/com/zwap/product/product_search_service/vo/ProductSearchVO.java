package com.zwap.product.product_search_service.vo;

import lombok.Data;

@Data
public class ProductSearchVO {
    private String id;
    private String userId;
    private String userName;
    private String userImgUrl;
    private String title;
    private String description;
    private String imageUrl;
    Integer price;
    double distance;
}
