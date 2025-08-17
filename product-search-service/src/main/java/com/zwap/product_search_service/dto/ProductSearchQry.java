package com.zwap.product_search_service.dto;

import com.zwap.product_search_service.enums.ProductSortEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductSearchQry {
    private String searchParam;
    @Min(value = 0, message = "min price must be at least 0")
    @Max(value = 1000000, message = "min price must not exceed 1,000,000")
    private BigDecimal minPrice;
    @Min(value = 0, message = "max price must be at least 0")
    @Max(value = 1000000, message = "max price must not exceed 1,000,000")
    private BigDecimal maxPrice;
    @Min(value = 0, message = "min distance must be at least 0")
    @Max(value = 1000000, message = "max distance must not exceed 1,000,000")
    private Integer distance;
    private ProductSortEnum sortBy;
    private boolean sortAsc = false;
}
