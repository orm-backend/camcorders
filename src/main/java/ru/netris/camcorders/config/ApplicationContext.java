package ru.netris.camcorders.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
    
    @Value("${camcorders.threads-max:0}")
    private int threadsMax;

    @Bean
    public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
	return new RestTemplate(factory);
    }

    @Bean
    public CamcorderService camcorderService() {
	return new CamcorderService();
    }
    
    @Bean
    public BlockingQueue<Runnable> taskQueue() {
	//return new LinkedBlockingQueue<Runnable>();
	return new SynchronousQueue<Runnable>(true);
    }

    @Bean
    public ExecutorService threadPoolExecutor() {
	int nThreads = threadsMax > 0 ? threadsMax : Integer.MAX_VALUE;
	return new ThreadPoolExecutor(0, nThreads, 60L, TimeUnit.SECONDS, taskQueue());
    }

}
