package com.zwap.common.common.entity;

import com.zwap.common.common.VO.GeoData;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String title;
    private String description;
    private String imagePath;
    private BigDecimal price;
    private Integer quantity;
    private Integer availableQuantity;
    @Column(nullable = false)
    private String status= "ACTIVE";

    @Column(insertable = false, updatable = false)
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String city;
    private String locationName;
    private String address;
    private GeoData geoData;
    private String placeId; // Google Maps Place ID

    private Integer viewCnt;
}
