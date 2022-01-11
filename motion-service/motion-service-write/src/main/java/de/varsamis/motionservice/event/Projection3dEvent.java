package de.varsamis.motionservice.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Projection3dEvent {
    private String device;
    private long timestamp;
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal z;
    private BigDecimal dist;
}
