package de.varsamis.motionservice.configuration;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Generated;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Generated
@Configuration
public class PropertiesProvider {

    @Bean
    @ConfigurationProperties(prefix = "appconf")
    public AppConfigurationProperties appConfigurationProperties() {
        return new AppConfigurationProperties();
    }


    @Data
    public static class AppConfigurationProperties {
        private String projection3dTopic;
    }

}
