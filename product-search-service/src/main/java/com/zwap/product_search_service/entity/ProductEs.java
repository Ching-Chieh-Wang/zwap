package com.zwap.product_search_service.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;
import com.zwap.product_common.entity.Product ;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "mongo.product.products")
public class ProductEs extends Product {

    @GeoPointField
    @Field(name = "geoData")
    private GeoPoint geoPoint;
}
