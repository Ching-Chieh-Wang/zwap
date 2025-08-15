package com.zwap.product_common.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductVO {
    public static final ProductVO NOT_FOUND = new ProductVO();
    private String id;
    private String title;
    private String description;
    private String imagePath;
    private BigDecimal price;
    private String city;
    private String locationName;
    private String address;
    private GeoData geoData;
    private String placeId; // Google Maps Place ID
    private Integer viewCnt;
    private String userId;
    private String status;
}
