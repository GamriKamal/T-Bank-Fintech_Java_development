package tbank.mr_irmag.tbank_kudago_task.services;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.entity.Locations;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Tag(name = "LocationsService", description = "Сервис для управления данными локаций.")
public class LocationsService {
    private final Logger logger = LoggerFactory.getLogger(LocationsService.class);
    private final StorageManager storageManager;
    private final ReadKudaGo<Locations> readKudaGo;
    @Value("${api.kuda_go.read.url_locations}")
    private String url_locations;

    @Autowired
    public LocationsService(StorageManager storageManager, ReadKudaGo<Locations> readKudaGo) {
        this.storageManager = storageManager;
        this.readKudaGo = readKudaGo;
    }

    @Operation(description = "Получает все локации из внешнего источника и сохраняет их в хранилище.")
    public List<Locations> getAllLocations() {
        List<Locations> list = readKudaGo.convertJsonToList(url_locations, Locations.class);
        if (!list.isEmpty()) {
            list.forEach(locations -> storageManager.getLocationsStorage().put(locations.getSlug(), locations));
        } else {
            logger.debug("List is empty!");
            throw new NullPointerException("List is empty!");
        }
        return list;
    }

    @Operation(description = "Получает локацию по ее уникальному идентификатору (slug).")
    public Locations getLocationBySlug(String slug) {
        Optional<Locations> temp = storageManager.getLocationsStorage().getEntry()
                .stream()
                .filter(loc -> loc.getKey().equals(slug))
                .map(Map.Entry::getValue)
                .findFirst();

        if (temp.isPresent()) {
            logger.info("The element was received: {}", temp);
            return temp.get();
        } else {
            logger.error("Error! The element is null!");
            throw new NullPointerException("Mistake! The element is null!");
        }
    }

    @Operation(description = "Создает новую локацию и сохраняет ее в хранилище.")
    public Locations createLocation(Locations location) {
        storageManager.getLocationsStorage().put(location.getSlug(), location);

        try {
            if (storageManager.getLocationsStorage().get(location.getSlug()) == location) {
                logger.info("The element was successfully added: {}", location);
                return location;
            }
        } catch (NoSuchElementException e) {
            logger.error("An error occurred while adding an item to the repository: {}", location);
            throw new NoSuchElementException("An error occurred while adding an item to the repository: " + location);
        }
        return null;
    }

    @Operation(description = "Обновляет существующую локацию по ее slug.")
    public Locations updateLocation(String slug, String name) {
        try {
            getLocationBySlug(slug);
            storageManager.getLocationsStorage().put(slug, new Locations(slug, name));
            logger.info("The element was successfully updated: {}", storageManager.getLocationsStorage().get(slug));
            return storageManager.getLocationsStorage().get(slug);
        } catch (NullPointerException e) {
            logger.error("Error! The element is null!");
            throw new NullPointerException("Error! The element is null!");
        }
    }

    @Operation(description = "Удаляет локацию по ее slug.")
    public boolean deleteLocationBySlug(String slug) {
        try {
            if (!storageManager.getLocationsStorage().containsKey(slug)) {
                throw new NullPointerException();
            }

            storageManager.getLocationsStorage().remove(slug);
            logger.info("The slug item was successfully deleted: {}", slug);
            return true;

        } catch (NullPointerException e) {
            logger.error("There is no element with such a slug: {}", slug);
            throw new NullPointerException("There is no element with such a slug: " + slug);
        }
    }
}
