package com.zwap.product.product_search_service.entity;

import com.zwap.product.product_common.model.GeoData;
import com.zwap.product.product_common.model.ProductBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "mongo.product.products.embedded")
public class ProductEs extends ProductBase {

    @GeoPointField
    private GeoData geoData;
}
