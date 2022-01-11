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

import java.math.BigDecimal;
import java.time.Instant;

public class MotionServiceTest {

    private MotionService underTest;
    private KafkaTemplate<String, Projection3dEvent> mockProjection3dEventTemplate;
    private AppConfigurationProperties mockAppConfigurationProperties;
    private ArgumentCaptor<Projection3dEvent> projection3dEventArgumentCaptor;

    @BeforeEach
    void setup(){
        mockProjection3dEventTemplate = mock(KafkaTemplate.class);
        projection3dEventArgumentCaptor = ArgumentCaptor.forClass(Projection3dEvent.class);

        mockAppConfigurationProperties = mock(AppConfigurationProperties.class);
        doReturn("test-topic").when(mockAppConfigurationProperties).getProjection3dTopic();

        underTest = new MotionService(mockProjection3dEventTemplate, mockAppConfigurationProperties);
    }


    @Test
    void testWriteProjection3dData() {
        Projection3dEvent orgEvent = Projection3dEvent
                .builder()
                .device("dev1")
                .timestamp(Instant.now().getEpochSecond())
                .x(BigDecimal.ZERO)
                .y(BigDecimal.ZERO)
                .y(BigDecimal.ZERO)
                .dist(BigDecimal.TEN)
                .build();
        underTest.writeProjection3d(orgEvent);
        verify(mockProjection3dEventTemplate).send(eq("test-topic"), projection3dEventArgumentCaptor.capture());

        Projection3dEvent resEvent = projection3dEventArgumentCaptor.getValue();
        Assertions.assertEquals(orgEvent.getDevice(), resEvent.getDevice());
    }

}
