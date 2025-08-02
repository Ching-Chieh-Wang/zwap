package com.zwap.product_write_service.product_write_service.dto;

import java.math.BigDecimal;

import com.zwap.product_common.product_common.VO.GeoData;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductCreateQry {
    @NotBlank
    private String userId;

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    private String imagePath;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotBlank
    private String city;

    private String locationName;

    @NotBlank
    private String address;

    @NotNull
    private GeoData geoData;

    @NotBlank
    private String placeId; // Google Maps Place ID
}