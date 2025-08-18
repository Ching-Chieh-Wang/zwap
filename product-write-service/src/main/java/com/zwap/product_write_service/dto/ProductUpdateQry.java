package com.zwap.product_write_service.dto;

import com.zwap.product_common.vo.GeoData;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
public class ProductUpdateQry {
    private String title;
    private String description;
    private String imagePath;
    @Min(0)
    @Max(1000000)
    private Integer price;
    @Min(1)
    @Max(1000000)
    private Integer quantity;
    private String status;
    private String city;
    private String locationName;
    private String address;
    private GeoData geoData;
    private String placeId;
}