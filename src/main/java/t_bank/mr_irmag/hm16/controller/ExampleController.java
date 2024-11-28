package t_bank.mr_irmag.hm16.controller;
import io.micrometer.core.instrument.MeterRegistry;


import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2

public class ExampleController {
    private final List<String> uuidList = new ArrayList<>();
    private MeterRegistry meterRegistry;

    @Autowired
    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping
    public String generateUUID() {
        String uuid = UUID.randomUUID().toString();

        MDC.put("requestId", uuid);
        log.info("Generated UUID and logged it with MDC");

        meterRegistry.counter("custom_requests_total").increment();
        return "Generated UUID: " + uuid;
    }

    @GetMapping("/stack-overflow")
    public String uuidStackOverflow() {
        return generateUUIDRecursively(0);
    }

    private String generateUUIDRecursively(int depth) {
        String uuid = UUID.randomUUID().toString();
        log.info("Generated UUID: " + uuid);
        return generateUUIDRecursively(depth + 1);
    }

    @GetMapping("/uuid-out-of-memory")
    public String generateUUIDsIndefinitely() {
        while (true) {
            uuidList.add(UUID.randomUUID().toString());
        }
    }
}
