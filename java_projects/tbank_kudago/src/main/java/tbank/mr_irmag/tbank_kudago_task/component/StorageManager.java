package tbank.mr_irmag.tbank_kudago_task.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.ParameterizedStorage;

@Component
@Getter
public class StorageManager {
    private final ParameterizedStorage<Integer, Category> categoriesStorage;
    private final ParameterizedStorage<String, Location> locationsStorage;

    @Autowired
    public StorageManager(ParameterizedStorage<Integer, Category> categoriesStorage,
                          ParameterizedStorage<String, Location> locationsStorage) {
        this.categoriesStorage = categoriesStorage;
        this.locationsStorage = locationsStorage;
    }
}

