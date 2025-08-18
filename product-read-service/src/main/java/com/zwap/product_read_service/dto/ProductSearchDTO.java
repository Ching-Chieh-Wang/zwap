package com.zwap.product_read_service.dto;

import lombok.Data;

@Data
public class ProductSearchDTO {
    private String key;
    private Integer minPrice;
    private Integer maxPrice;
    private String city;
    private String locationName;
    private GeoData geoData;
    private String page;
    private Integer pageSize;
    private String sortBy;

    public static class GeoData {
        private Double latitude;
        private Double longitude;
        private Double radiusKm; // optional: search radius
    }
}
