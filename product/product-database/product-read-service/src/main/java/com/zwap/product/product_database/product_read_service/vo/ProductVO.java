package com.zwap.product.product_database.product_read_service.vo;

import com.zwap.product.product_common.model.GeoData;
import com.zwap.product.product_common.model.ProductBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductVO extends ProductBase {
    public static final ProductVO NOT_FOUND = new ProductVO();
    private GeoData geoData;
}
