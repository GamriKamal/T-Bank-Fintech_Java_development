package tbank.mr_irmag.tbank_kudago_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class TbankKudaGoTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(TbankKudaGoTaskApplication.class, args);
    }
}
