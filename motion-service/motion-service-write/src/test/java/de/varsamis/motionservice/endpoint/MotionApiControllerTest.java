package de.varsamis.motionservice.endpoint;

import de.varsamis.motion.model.Projection3d;
import de.varsamis.motion.model.Success;
import de.varsamis.motionservice.event.Projection3dEvent;
import de.varsamis.motionservice.service.MotionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MotionApiControllerTest {

    private MotionApiController underTest;
    private MotionService motionServiceMock;
    private ArgumentCaptor<Projection3dEvent> projection3dEventArgumentCaptor;


    @BeforeEach
    void setup() {
        motionServiceMock = mock(MotionService.class);
        projection3dEventArgumentCaptor = ArgumentCaptor.forClass(Projection3dEvent.class);
        underTest = new MotionApiController(motionServiceMock);
    }

    @Test
    void test_writeProjection3d() {

        Projection3d projection3d = new Projection3d();
        projection3d.setDevice("Dev1");
        projection3d.setxPos(BigDecimal.ZERO);
        projection3d.setyPos(BigDecimal.ZERO);
        projection3d.setzPos(BigDecimal.ZERO);
        projection3d.setDist(BigDecimal.TEN);
        projection3d.setTimestamp(Instant.now().getEpochSecond());
        Projection3dEvent expEvent = Projection3dEvent
                .builder()
                .device(projection3d.getDevice())
                .timestamp(projection3d.getTimestamp())
                .x(projection3d.getxPos())
                .y(projection3d.getyPos())
                .z(projection3d.getzPos())
                .dist(projection3d.getDist())
                .build();
        ResponseEntity<Success> lActualResponse = underTest.setProjection3d(projection3d);

        verify(motionServiceMock).writeProjection3d(projection3dEventArgumentCaptor.capture());
        Projection3dEvent actEvent = projection3dEventArgumentCaptor.getValue();

        assertEquals(HttpStatus.OK, lActualResponse.getStatusCode());
        Assertions.assertEquals(expEvent, actEvent);
    }
}
