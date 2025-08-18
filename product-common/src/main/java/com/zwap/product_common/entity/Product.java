package com.zwap.product_common.entity;

import com.zwap.product_common.vo.GeoData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    private String userId;
    private String title;
    private String description;
    private String imagePath;
    private Integer price;
    private String status= "ACTIVE";

    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    private String city;
    private String locationName;
    private String address;
    private GeoData geoData;
    private String placeId; // Google Maps Place ID

    private Integer viewCnt;
}
