package de.varsamis.motionservice.service;

import de.varsamis.motionservice.configuration.PropertiesProvider.AppConfigurationProperties;
import de.varsamis.motionservice.event.Projection3dEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doReturn;

import org.mockito.ArgumentCaptor;


public class WriteDataServiceTest {

    private WriteDataService underTest;
    private KafkaTemplate<String, Projection3dEvent> mockProjection3dEventTemplate;
    private AppConfigurationProperties mockAppConfigurationProperties;
    private ArgumentCaptor<Projection3dEvent> projection3dEventArgumentCaptor;

    @BeforeEach
    void setup(){
        mockProjection3dEventTemplate = mock(KafkaTemplate.class);
        projection3dEventArgumentCaptor = ArgumentCaptor.forClass(Projection3dEvent.class);

        mockAppConfigurationProperties = mock(AppConfigurationProperties.class);
        doReturn("test-topic").when(mockAppConfigurationProperties).getProjection3dTopic();

        underTest = new WriteDataService(mockProjection3dEventTemplate, mockAppConfigurationProperties);
    }


    @Test
    void testDeleteTarget() {
        Projection3dEvent orgEvent = Projection3dEvent
                .builder()
                .device("dev1")
                .x(0.0)
                .y(0.0)
                .y(0.0)
                .dist(10.0)
                .build();
        underTest.writeProjection3d(orgEvent);
        verify(mockProjection3dEventTemplate).send(eq("test-topic"), projection3dEventArgumentCaptor.capture());

        Projection3dEvent resEvent = projection3dEventArgumentCaptor.getValue();
        Assertions.assertEquals(orgEvent.getDevice(), resEvent.getDevice());
    }

}
