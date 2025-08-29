package com.zwap.product.product_database.product_database_common.entity;

import com.zwap.product.product_common.model.Location;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Data
@Document(collection = "products")
public class ProductMongo  {
    @Id
    private String id;
    private String userId;
    private String title;
    private String description;
    private String imageUrl;
    private Integer price;
    private Location location;
    private GeoJsonPoint geoData;
}
