package ru.netris.camcorders.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import ru.netris.camcorders.services.CamcorderService;

/**
 * Main application context configuration
 * 
 * @author Vitalii Kovalenko
 *
 */
@Configuration
public class ApplicationContextConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }
    
    @Bean
    public CamcorderService camcorderService() {
	return new CamcorderService();
    }

}
