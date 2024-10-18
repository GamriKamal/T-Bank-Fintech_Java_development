package tbank.mr_irmag.tbank_kudago_task.component;

import lombok.Getter;
import org.springframework.stereotype.Component;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.ParameterizedStorage;

@Component
@Getter
public class StorageManager {
    private final ParameterizedStorage<Integer, Category> categoriesStorage = new ParameterizedStorage<>();
    private final ParameterizedStorage<String, Location> locationsStorage = new ParameterizedStorage<>();

    public void clear(){
        categoriesStorage.getHashMap().clear();
        locationsStorage.getHashMap().clear();
    }


}
