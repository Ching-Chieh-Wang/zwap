package com.zwap.product_search_service.vo;

import com.zwap.product_common.vo.GeoData;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSearchVO {
    String id;
    String name;
    String imagePath;
    BigDecimal price;
    GeoData geoData;
}
