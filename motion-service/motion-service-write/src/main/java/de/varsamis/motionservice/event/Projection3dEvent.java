package de.varsamis.motionservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Projection3dEvent {
    private String device;
    private long timestamp;
    private double x;
    private double y;
    private double z;
    private double dist;
}
