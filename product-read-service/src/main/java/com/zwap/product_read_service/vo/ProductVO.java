package com.zwap.product_read_service.vo;

import com.zwap.product_common.product_common.VO.GeoData;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
