package tbank.mr_irmag.tbank_kudago_task.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import tbank.mr_irmag.tbank_kudago_task.component.LoggingObserver;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;

@Configuration
public class ObserverConfig {

    private final StorageManager storageManager;
    private final LoggingObserver loggingObserver;

    @Autowired
    public ObserverConfig(StorageManager storageManager, LoggingObserver loggingObserver) {
        this.storageManager = storageManager;
        this.loggingObserver = loggingObserver;
    }

    @PostConstruct
    public void init() {
        storageManager.addObserver(loggingObserver);
    }
}

