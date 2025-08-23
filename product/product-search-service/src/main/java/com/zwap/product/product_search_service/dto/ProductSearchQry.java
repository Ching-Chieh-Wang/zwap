package com.zwap.product.product_search_service.dto;

import com.zwap.product.product_common.model.GeoData;
import com.zwap.product.product_search_service.enums.ProductSortEnum;
import jakarta.validation.constraints.*;
import lombok.Data;



@Data
public class ProductSearchQry {
    private String searchParam;
    @Min(value = 0, message = "min price must be at least 0")
    @Max(value = 1000000, message = "min price must not exceed 1,000,000")
    private Integer minPrice;
    @Min(value = 0, message = "max price must be at least 0")
    @Max(value = 1000000, message = "max price must not exceed 1,000,000")
    private Integer maxPrice;
    @Min(value = 0, message = "min distance must be at least 0")
    @Max(value = 1000000, message = "max distance must not exceed 1,000,000")
    private Integer distance;
    private GeoData location;
    private ProductSortEnum sortBy;
    private boolean sortAsc = false;
}
