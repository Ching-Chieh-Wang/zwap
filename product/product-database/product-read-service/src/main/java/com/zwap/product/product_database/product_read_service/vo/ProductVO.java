package com.zwap.product.product_database.product_read_service.vo;

import com.zwap.product.product_common.model.GeoData;
import com.zwap.product.product_common.model.Location;
import lombok.Data;


@Data
public class ProductVO {
    public static final ProductVO NOT_FOUND = new ProductVO();
    private String id;
    private String userId;
    private String userName;
    private String userImgUrl;
    private String title;
    private String description;
    private String imageUrl;
    private Integer price;
    private Location location;
    private GeoData geoData;
}
