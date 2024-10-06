package tbank.mr_irmag.tbank_kudago_task.component;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable;
import tbank.mr_irmag.tbank_kudago_task.services.DataInitializer;

@Component
@TimeMeasurable
public class AppInitializer {
    private final DataInitializer dataInitializer;

    @Autowired
    public AppInitializer(DataInitializer dataInitializer) {
        this.dataInitializer = dataInitializer;
    }

    @PostConstruct
    public void init() {
        dataInitializer.initializeData();
    }
}
