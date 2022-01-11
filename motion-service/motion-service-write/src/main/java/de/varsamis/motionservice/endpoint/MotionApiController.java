package de.varsamis.motionservice.endpoint;

import de.varsamis.motion.endpoint.MotionApi;
import de.varsamis.motion.model.Projection3d;
import de.varsamis.motion.model.Success;
import de.varsamis.motionservice.event.Projection3dEvent;
import de.varsamis.motionservice.service.MotionService;
import lombok.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/motion-api/v1")
@Slf4j
@RequiredArgsConstructor
public class MotionApiController implements MotionApi {

    @NonNull
    private final MotionService motionService;

    @Override
    public ResponseEntity<Success> setProjection3d(Projection3d projection3d) {

        Projection3dEvent event =  Projection3dEvent
                .builder()
                .device(projection3d.getDevice())
                .timestamp(projection3d.getTimestamp())
                .x(projection3d.getxPos())
                .y(projection3d.getyPos())
                .z(projection3d.getzPos())
                .dist(projection3d.getDist())
                .build();
        motionService.writeProjection3d(event);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Success().info("data received"));
    }
}
