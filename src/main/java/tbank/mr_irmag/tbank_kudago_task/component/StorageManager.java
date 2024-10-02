package tbank.mr_irmag.tbank_kudago_task.component;

import lombok.Getter;
import org.springframework.stereotype.Component;
import tbank.mr_irmag.tbank_kudago_task.entity.Categories;
import tbank.mr_irmag.tbank_kudago_task.entity.Locations;
import tbank.mr_irmag.tbank_kudago_task.entity.ParameterizedStorage;

@Component
@Getter
public class StorageManager {
    private final ParameterizedStorage<Integer, Categories> categoriesStorage = new ParameterizedStorage<>();
    private final ParameterizedStorage<String, Locations> locationsStorage = new ParameterizedStorage<>();


}
