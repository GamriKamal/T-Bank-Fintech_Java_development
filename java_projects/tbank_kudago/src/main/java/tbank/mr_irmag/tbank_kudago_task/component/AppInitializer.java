package tbank.mr_irmag.tbank_kudago_task.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable;
import tbank.mr_irmag.tbank_kudago_task.services.DataInitializer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@TimeMeasurable
@RequiredArgsConstructor
@Log4j2
public class AppInitializer {
    private final DataInitializer dataInitializer;

    private final ScheduledExecutorService scheduledThreadPool;
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";
    public static final String RESET = "\033[0m";
    @Value("${threads.scheduled.init.time}")
    private int timeForSchedule;

    @Value("${threads.scheduled.init.timeUnit}")
    private String timeUnitForSchedule;

    @Value("${threads.scheduled.notification.time}")
    private int timeForNotification;
    @PostConstruct
    public void init() {
        scheduledThreadPool.schedule(
                () -> dataInitializer.initializeData(), timeForSchedule, TimeUnit.valueOf(timeUnitForSchedule)
        );
    }

    @PostConstruct
    public void notification(){
        scheduledThreadPool.scheduleAtFixedRate(
                () -> log.info("{}Example of scheduled thread pool {}", BLUE_BOLD_BRIGHT, RESET),
                0, timeForNotification, TimeUnit.valueOf(timeUnitForSchedule)
        );
    }
}
