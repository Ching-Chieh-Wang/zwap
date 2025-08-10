package com.zwap.product_common.vo;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@Data
@NoArgsConstructor(force = true)
public class GeoData {
    @NotNull(message = "latitude cannot be null")
    private final BigDecimal latitude;
    @NotNull(message = "longitude cannot be null")
    private final BigDecimal longitude;
}
