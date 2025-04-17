package com.zwap.common.common.VO;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(force = true)
@Embeddable
public class GeoData {
    private final BigDecimal latitude;
    private final BigDecimal longitude;
}
