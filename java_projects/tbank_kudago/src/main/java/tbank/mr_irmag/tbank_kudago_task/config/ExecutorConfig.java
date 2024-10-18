package tbank.mr_irmag.tbank_kudago_task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
public class ExecutorConfig {

    @Value("${threads.size.fixedSize}")
    private int numberOfFixedThreads;

    @Value("${threads.size.scheduledSize}")
    private int numberOfScheduleThreads;


    @Bean
    public ExecutorService fixedThreadPool(){
        return Executors.newFixedThreadPool(numberOfFixedThreads);
    }

    @Bean
    public ScheduledExecutorService scheduledThreadPool(){
        return Executors.newScheduledThreadPool(numberOfScheduleThreads);
    }
}
