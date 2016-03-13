package org.bongiorno.latency;


import com.espertech.esper.client.EPRuntime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LatencyAspect {

    @Autowired
    private EPRuntime runtime;

    @Around("@annotation(Monitor)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Object target = joinPoint.getTarget();
        long start = System.currentTimeMillis();
        Throwable t = null;
        Object retVal = null;
        try {
            retVal = joinPoint.proceed(); //continue on the intercepted method
        } catch (Throwable throwable) {
            t = throwable;
        }
        long delta = System.currentTimeMillis() - start;

        String method = joinPoint.getSignature().getName();
        runtime.sendEvent(new Latency(target.getClass(), method,delta,t));
        if(t != null)
            throw t;


        return retVal;
    }
}
