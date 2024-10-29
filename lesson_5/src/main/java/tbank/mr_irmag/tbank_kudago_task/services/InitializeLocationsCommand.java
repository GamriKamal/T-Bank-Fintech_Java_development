package tbank.mr_irmag.tbank_kudago_task.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.interfaces.DataInitializationCommand;

public class InitializeLocationsCommand implements DataInitializationCommand {
    private final StorageManager storageManager;
    private final ReadKudaGo<Location> locationsReader;
    private final String urlLocations;
    private final Logger logger = LoggerFactory.getLogger(InitializeLocationsCommand.class);

    public InitializeLocationsCommand(StorageManager storageManager, ReadKudaGo<Location> locationsReader, String urlLocations) {
        this.storageManager = storageManager;
        this.locationsReader = locationsReader;
        this.urlLocations = urlLocations;
    }

    @Override
    public void execute() {
        List<Location> locations = locationsReader.convertJsonToList(urlLocations, Location.class);
        if (!locations.isEmpty()) {
            locations.forEach(location -> storageManager.getLocationsStorage().put(location.getSlug(), location));
            logger.info("Initialized locations data.");
        } else {
            logger.debug("Locations list is empty!");
        }
    }
}
