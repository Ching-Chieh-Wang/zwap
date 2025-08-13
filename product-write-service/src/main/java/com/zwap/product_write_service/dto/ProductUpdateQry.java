package com.zwap.product_write_service.dto;

import com.zwap.product_common.vo.GeoData;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateQry {
    private String title;
    private String description;
    private String imagePath;
    private BigDecimal price;
    private Integer quantity;
    private String status;
    private String city;
    private String locationName;
    private String address;
    private GeoData geoData;
    private String placeId;
}