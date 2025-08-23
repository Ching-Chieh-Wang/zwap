package com.zwap.product.product_common.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class GeoData {
    @NotNull(message = "latitude cannot be null")
    @Min(value = -90, message = "latitude must be >= -90")
    @Max(value = 90, message = "latitude must be <= 90")
    private Double lat;

    @NotNull(message = "longitude cannot be null")
    @Min(value = -180, message = "longitude must be >= -180")
    @Max(value = 180, message = "longitude must be <= 180")
    private Double lon;
}
