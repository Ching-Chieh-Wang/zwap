package com.zwap.product_common.product_common.VO;

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
