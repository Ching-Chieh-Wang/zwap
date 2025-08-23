package com.zwap.product.product_common.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductBase {
    private String id;

    private String userId;
    private String title;
    private String description;
    private String imagePath;
    @Min(0)
    @Max(1000000)
    private Integer price;
    private String status= "ACTIVE";

    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    private String city;
    private String locationName;
    private String address;
    private String placeId; // Google Maps Place ID
    private GeoData geoData;

    private Integer viewCnt;
}
