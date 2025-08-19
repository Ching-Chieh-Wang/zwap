package com.zwap.product_common.vo;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@ToString
@EqualsAndHashCode
@Data
@NoArgsConstructor(force = true)
public class GeoData {
    @NotNull(message = "latitude cannot be null")
    private double lat;
    @NotNull(message = "longitude cannot be null")
    private double lon;
}
