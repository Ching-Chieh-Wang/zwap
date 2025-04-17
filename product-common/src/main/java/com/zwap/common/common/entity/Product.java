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
@Table(name = "products") // Note: Consider table partitioning strategy
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
    private String status;

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
