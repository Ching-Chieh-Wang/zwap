package com.zwap.product_write_service.product_write_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductUpdateQry {
    private String title;
    private String description;
    private String imagePath;
    private BigDecimal price;
    private Integer quantity;
    private String status;
    private LocalDateTime expiredAt;
}