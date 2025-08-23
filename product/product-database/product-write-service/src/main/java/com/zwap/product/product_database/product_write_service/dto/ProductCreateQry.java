package com.zwap.product.product_database.product_write_service.dto;


import com.zwap.product.product_common.model.GeoData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductCreateQry {

    @NotBlank(message = "title cannot be blank")
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    private String imagePath;

    @NotNull(message = "price cannot be null")
    @Min(0)
    @Max(1000000)
    private Integer price;

    @NotNull(message = "quantity cannot be null")
    @Min(1)
    @Max(1000000)
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