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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${camcorders.pool-max-size}")
    private int poolMaxSize;

    @GetMapping("/")
    public Camcorder[] camcorders() {
	int completed = 0;
	int successfully = 0;
	int failed = 0;
	Camcorder[] camcorders = camcorderService.fetchList();

	// The exception has already been thrown and logged
	if (camcorders == null) {
	    return null;
	}

	List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();

	for (Camcorder camcorder : camcorders) {
	    tasks.add(() -> camcorderService.aggregateCamcorderData(camcorder));
	    
	    // Please edit properties if you want to run more threads.
	    // But there must be a limit to avoid running a billion threads.
	    if (tasks.size() == poolMaxSize || completed == camcorders.length - 1) {
		ExecutorService executor = Executors.newFixedThreadPool(tasks.size());

		try {
		    List<Future<Boolean>> results = executor.invokeAll(tasks);

		    for (Future<Boolean> future : results) {
			if (future.get().booleanValue()) {
			    successfully++;
			} else {
			    failed++;
			}
		    }
		} catch (Exception e) {
		    log.error(e);
		} finally {
		    executor.shutdown();
		}
		
		tasks.clear();
	    }
	    
	    completed ++;
	}

	log.info("Completed " + completed + ", Successfully " + successfully + ", Failed " + failed);

	return camcorders;
    }

}
