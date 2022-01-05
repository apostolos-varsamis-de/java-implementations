package de.varsamis.motionservice.service;

import de.varsamis.motionservice.configuration.PropertiesProvider;
import de.varsamis.motionservice.event.Projection3dEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class WriteDataService {

    @NonNull
    private final KafkaTemplate<String, Projection3dEvent> projection3dEventProducer;

    @NonNull
    private final PropertiesProvider.AppConfigurationProperties props;

    public void writeProjection3d(Projection3dEvent event){
        projection3dEventProducer.send(props.getProjection3dTopic(), event);
    }

}
