package com.zwap.product_common.product_common.VO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(force = true)
public class GeoData {
    private final BigDecimal latitude;
    private final BigDecimal longitude;
}
