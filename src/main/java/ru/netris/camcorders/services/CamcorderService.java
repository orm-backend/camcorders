package ru.netris.camcorders.services;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ru.netris.camcorders.controllers.CamcorderRestController;
import ru.netris.camcorders.domain.Camcorder;

/**
 * Service for fetching camcorder data
 * 
 * @author Vitalii Kovalenko
 *
 */
public class CamcorderService {
    
    private static Logger log = LogManager.getLogger(CamcorderRestController.class);

    @Value("${camcorders.list-url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Fetches camcorder list
     * 
     * @return Camcorder[]
     */
    public Camcorder[] fetchList() {
	ResponseEntity<Camcorder[]> response = null;

	try {
	    response = restTemplate.getForEntity(url, Camcorder[].class);
	} catch (Exception e) {
	    log.error(e);
	}

	return response == null ? null : response.getBody();
    }

    /**
     * Fetches camcorder source data
     * 
     * @param sourceDataUrl
     * @return SourceDataResponse
     */
    public SourceDataResponse fetchSourceData(String sourceDataUrl) {
	SourceDataResponse response = null;

	try {
	    response = restTemplate.getForObject(sourceDataUrl, SourceDataResponse.class);
	} catch (Exception e) {
	    log.error(e);
	}

	return response;
    }

    /**
     * 
     * Fetches camcorder token data
     * 
     * @param tokenDataUrl
     * @return TokenDataResponse
     */
    public TokenDataResponse fetchTokenData(String tokenDataUrl) {
	TokenDataResponse response = null;

	try {
	    response = restTemplate.getForObject(tokenDataUrl, TokenDataResponse.class);
	} catch (Exception e) {
	    log.error(e);
	}

	return response;
    }

    /**
     * Aggregates camcorder data
     * 
     * @param camcorder
     * @return boolean True if success, false otherwise
     */
    public boolean aggregateCamcorderData(Camcorder camcorder) {
	boolean success = false;
	Callable<SourceDataResponse> sourceDataCallable = () -> fetchSourceData(camcorder.getSourceDataUrl());
	Callable<TokenDataResponse> tokenDataCallable = () -> fetchTokenData(camcorder.getTokenDataUrl());
	FutureTask<SourceDataResponse> fetchSourceDataTask = new FutureTask<>(sourceDataCallable);
	FutureTask<TokenDataResponse> fetchTokenDataTask = new FutureTask<>(tokenDataCallable);
	ExecutorService executor = Executors.newFixedThreadPool(2);

	try {
	    executor.execute(fetchSourceDataTask);
	    executor.execute(fetchTokenDataTask);
	    SourceDataResponse sourceDataResponse = fetchSourceDataTask.get();
	    TokenDataResponse tokenDataResponse = fetchTokenDataTask.get();

	    if (sourceDataResponse != null) {
		camcorder.setVideoUrl(sourceDataResponse.getVideoUrl());
		camcorder.setUrlType(sourceDataResponse.getUrlType());
	    }

	    if (tokenDataResponse != null) {
		camcorder.setValue(tokenDataResponse.getValue());
		camcorder.setTtl(tokenDataResponse.getTtl());
	    }

	    success = true;
	} catch (Exception e) {
	    log.error(e);
	} finally {
	    executor.shutdown();
	}

	return success;
    }

}
