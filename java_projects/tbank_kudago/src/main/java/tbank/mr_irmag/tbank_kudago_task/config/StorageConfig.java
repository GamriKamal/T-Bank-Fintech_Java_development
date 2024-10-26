package tbank.mr_irmag.tbank_kudago_task.config;

import lombok.Getter;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.ParameterizedStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class StorageConfig {

    @Bean
    public ParameterizedStorage<Integer, Category> categoriesStorage() {
        return new ParameterizedStorage<>();
    }

    @Bean
    public ParameterizedStorage<String, Location> locationsStorage() {
        return new ParameterizedStorage<>();
    }
}

