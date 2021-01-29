package ru.netris.camcorders.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.netris.camcorders.domain.Camcorder;
import ru.netris.camcorders.services.CamcorderService;

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
    
    @GetMapping("/")
    public Camcorder[] camcorders() {
	Camcorder[] camcorders = camcorderService.fetchList();
	
	if (camcorders == null) {
	    return null;
	}
	
	List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
	
	for (Camcorder camcorder : camcorders) {
	    tasks.add(() -> camcorderService.aggregateCamcorderData(camcorder));
	}
	
	int completed = 0;
	int successfully = 0;
	int failed = 0;
	
	ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
	
	try {
	    List<Future<Boolean>> results = executor.invokeAll(tasks);
	    
	    for (Future<Boolean> future : results) {
		completed ++;
		
		if (future.get().booleanValue()) {
		    successfully ++;
		} else {
		    failed ++;
		}
	    }
	    
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    executor.shutdown();
	}
	
	log.info("Completed " + completed +  ", Successfully " + successfully + ", Failed " + failed);
	
	return camcorders;
    }

}
