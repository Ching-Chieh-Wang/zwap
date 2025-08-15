package com.zwap.product_write_service.dto;

import java.math.BigDecimal;

import com.zwap.product_common.vo.GeoData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
public class ProductCreateQry {

    @NotBlank(message = "title cannot be blank")
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    private String imagePath;

    @NotNull(message = "price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull(message = "quantity cannot be null")
    @Min(1)
    private Integer quantity;

    @NotBlank (message = "city cannot be blank")
    private String city;

    private String locationName;

    @NotBlank
    private String address;

    @NotNull(message = "geoData cannot be null")
    @Valid
    private GeoData geoData;

    @NotBlank(message = "placeId cannot be blank")
    private String placeId; // Google Maps Place ID
}