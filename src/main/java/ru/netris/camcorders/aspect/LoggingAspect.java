package ru.netris.camcorders.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * 
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(ru.netris.camcorders.aspect.LogExecutionTime)")
    public Object methodTimeLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
	MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
	//String className = methodSignature.getDeclaringType().getSimpleName();
	String methodName = methodSignature.getName();

	StopWatch stopWatch = new StopWatch(methodName);
	stopWatch.start(methodName);
	Object result = proceedingJoinPoint.proceed();
	stopWatch.stop();

	if (stopWatch.getTotalTimeMillis() > 1000) {
	    LOG.warn(stopWatch.shortSummary());
	}

	return result;
    }
}
