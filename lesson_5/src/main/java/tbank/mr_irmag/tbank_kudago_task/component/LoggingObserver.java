package tbank.mr_irmag.tbank_kudago_task.component;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import tbank.mr_irmag.tbank_kudago_task.interfaces.Observer;

@Component
@Log4j2
public class LoggingObserver implements Observer {
    public static final String RESET = "\033[0m";
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";

    @Override
    public void update(String message) {
        log.info("{}Notification received: {}{}", CYAN_BOLD_BRIGHT, message, RESET);
    }
}