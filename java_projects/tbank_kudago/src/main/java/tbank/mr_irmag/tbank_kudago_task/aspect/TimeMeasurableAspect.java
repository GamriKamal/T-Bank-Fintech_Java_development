package tbank.mr_irmag.tbank_kudago_task.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimeMeasurableAspect {
    public static final String RESET = "\033[0m";
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m";
    private static final Logger logger = LoggerFactory.getLogger(TimeMeasurableAspect.class);

    @Around("@annotation(tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable) || @within(tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        logger.info("{}The {} method of the {} class is called. Execution time: {} ms {}", GREEN_BOLD_BRIGHT, methodName, className, executionTime, RESET);

        return proceed;
    }
}
