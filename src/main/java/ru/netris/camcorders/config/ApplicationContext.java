package ru.netris.camcorders.config;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.SpringProperties;
import org.springframework.web.client.RestTemplate;

import ru.netris.camcorders.services.CamcorderService;

/**
 * Main application context configuration
 * 
 * @author Vitalii Kovalenko
 *
 */
@Configuration
public class ApplicationContext {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
	return builder.setConnectTimeout(Duration.ofMillis(3000)).setReadTimeout(Duration.ofMillis(3000)).build();
    }

    @Bean
    public CamcorderService camcorderService() {
	return new CamcorderService();
    }

    @Bean
    public ExecutorService threadPoolExecutor() {
	int corePoolSize = Runtime.getRuntime().availableProcessors();
	String property = SpringProperties.getProperty("camcorders.max-concurrent");
	int maximumPoolSize = property == null ? Integer.MAX_VALUE : Integer.parseInt(property);

	return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

}
