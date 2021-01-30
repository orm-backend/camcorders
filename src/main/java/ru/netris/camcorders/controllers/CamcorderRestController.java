package ru.netris.camcorders.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.netris.camcorders.domain.Camcorder;
import ru.netris.camcorders.services.CamcorderResponse;
import ru.netris.camcorders.services.CamcorderService;
import ru.netris.camcorders.services.SourceDataResponse;
import ru.netris.camcorders.services.TokenDataResponse;

/**
 * Controller
 * 
 * @author Vitalii Kovalenko
 *
 */
@RestController
public class CamcorderRestController {

    private static Logger log = LogManager.getLogger(CamcorderRestController.class);

    @Autowired
    private CamcorderService camcorderService;

    @Autowired
    private ExecutorService threadPoolExecutor;

    @GetMapping("/")
    public Camcorder[] camcorders() {
	Camcorder[] camcorders = null;
	Map<Integer, Camcorder> map = new HashMap<Integer, Camcorder>();

	try {
	    camcorders = camcorderService.fetchList();
	    List<Callable<CamcorderResponse>> tasks = new ArrayList<Callable<CamcorderResponse>>();

	    for (Camcorder camcorder : camcorders) {
		tasks.add(() -> camcorderService.fetchSourceData(camcorder));
		tasks.add(() -> camcorderService.fetchTokenData(camcorder));
		map.put(camcorder.getId(), camcorder);
	    }

	    for (Future<CamcorderResponse> future : threadPoolExecutor.invokeAll(tasks)) {
		try {
		    CamcorderResponse response = future.get();
		    Camcorder camcorder = map.get(response.getId());
		    
		    if (response instanceof SourceDataResponse) {
			SourceDataResponse sourceDataResponse = (SourceDataResponse) response;
			camcorder.setVideoUrl(sourceDataResponse.getVideoUrl());
			camcorder.setUrlType(sourceDataResponse.getUrlType());
		    } else if (response instanceof TokenDataResponse) {
			TokenDataResponse tokenDataResponse = (TokenDataResponse) response;
			camcorder.setValue(tokenDataResponse.getValue());
			camcorder.setTtl(tokenDataResponse.getTtl());
		    } 
		} catch (Exception e) {
		    log.error(e);
		}
	    }
	} catch (Exception e) {
	    log.error(e);
	}

	return camcorders;
    }

}
