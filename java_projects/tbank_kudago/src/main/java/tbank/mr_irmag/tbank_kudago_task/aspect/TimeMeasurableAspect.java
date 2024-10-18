package tbank.mr_irmag.tbank_kudago_task.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Aspect
public class TimeMeasurableAspect {
    public static final String RESET = "\033[0m";
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m";
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";
    private static final Logger logger = LoggerFactory.getLogger(TimeMeasurableAspect.class);

    @Around("@annotation(tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable) || @within(tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        if(methodName.equals("clearMap")){
            logger.info("{}The {} method of the {} class is called. Execution time: {} ms {}", YELLOW_BOLD_BRIGHT, methodName, className, executionTime, RESET);

        }
        logger.info("{}The {} method of the {} class is called. Execution time: {} ms {}", GREEN_BOLD_BRIGHT, methodName, className, executionTime, RESET);

        return proceed;
    }

    @Before("@annotation(tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable) || @within(tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable)")
    public Object logStartOfMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        Object proceed = joinPoint.proceed();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        logger.debug("{} Started {} method from class {} time {} {}", BLUE_BOLD_BRIGHT,
                methodName, className,
                LocalDateTime.now(), RESET);
        return proceed;
    }
}
