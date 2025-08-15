package com.zwap.product_common.entity;

import com.zwap.product_common.convertor.Time;
import com.zwap.product_common.vo.GeoData;
import com.zwap.product_read_service.grpc.ProductMessage;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.zwap.product_common.convertor.Time.toTs;

@Data
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    private String userId;
    private String title;
    private String description;
    private String imagePath;
    private BigDecimal price;
    private String status= "ACTIVE";

    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    private String city;
    private String locationName;
    private String address;
    private GeoData geoData;
    private String placeId; // Google Maps Place ID

    private Integer viewCnt;

    public Product(ProductMessage m) {
        if (m == null) return;
        this.id = m.getId();
        this.userId = m.getUserId();
        this.title = m.getTitle();
        this.description = m.getDescription();
        this.imagePath = m.getImagePath();
        this.price =  new BigDecimal(m.getPrice());
        this.status = m.getStatus();
        this.createdAt = Time.fromTs(m.getCreatedAt());
        this.expiredAt = Time.fromTs(m.getExpiredAt());
        this.city = m.getCity();
        this.locationName = m.getLocationName();
        this.address = m.getAddress();
        this.placeId = m.getPlaceId();
        this.viewCnt = m.getViewCnt();
        this.geoData = new GeoData();
        this.geoData.setLatitude(m.getGeoData().getLatitude());
        this.geoData.setLongitude(m.getGeoData().getLongitude());
    }


    public ProductMessage toGrpcMessage() {
        ProductMessage.Builder b = ProductMessage.newBuilder();
        if (id != null) b.setId(id);
        if (userId != null) b.setUserId(userId);
        if (title != null) b.setTitle(title);
        if (description != null) b.setDescription(description);
        if (imagePath != null) b.setImagePath(imagePath);
        if (price != null) b.setPrice(price.toPlainString());
        if (status != null) b.setStatus(status);
        if (createdAt != null) b.setCreatedAt(Time.toTs(createdAt));
        if (expiredAt != null) b.setExpiredAt(toTs(expiredAt));
        if (city != null) b.setCity(city);
        if (locationName != null) b.setLocationName(locationName);
        if (address != null) b.setAddress(address);
        if (placeId != null) b.setPlaceId(placeId);
        if (viewCnt != null) b.setViewCnt(viewCnt);
        if (geoData != null) {
            ProductMessage.GeoData.Builder g = ProductMessage.GeoData.newBuilder();
            g.setLatitude(geoData.getLatitude());
            g.setLongitude(geoData.getLongitude());
            b.setGeoData(g.build());
        }

        return b.build();
    }

}
