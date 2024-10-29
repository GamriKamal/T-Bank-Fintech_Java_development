package tbank.mr_irmag.tbank_kudago_task.services;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.entity.ParameterizedStorage;
import tbank.mr_irmag.tbank_kudago_task.exceptions.LocationSlugAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Tag(name = "LocationsService", description = "Сервис для управления данными локаций.")
@Log4j2
public class LocationsService {
    private final Logger logger = LoggerFactory.getLogger(LocationsService.class);
    private final StorageManager storageManager;
    private final ParameterizedStorage<String, Location> locationsStorage = new ParameterizedStorage<>();

    @Autowired
    public LocationsService(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public LocationMemento save() {
        log.info("Saved state: {}", storageManager.getLocationsStorage().getHashMap().values().toString());
        return new LocationMemento(storageManager.getLocationsStorage());
    }

    @Schema(description = "Восстанавливает состояние локаций из Memento.")
    public void restore(LocationMemento memento) {
        storageManager.getLocationsStorage().clear();

        memento.getSavedState().getEntry().forEach(entry ->
                storageManager.getLocationsStorage().put(entry.getKey(), entry.getValue().clone()));

        logger.info("Restored locations from Memento: {}", storageManager.getCategoriesStorage().getHashMap().values());
    }

    @Schema(description = "Получает все локации из хранилища и сохраняет их в локальное хранилище.")
    public List<Location> getAllLocations() {
        return storageManager.getLocationsStorage().getHashMap().values().stream().toList();
    }

    @Schema(description = "Получает локацию по её slug.")
    public Location getLocationBySlug(String slug) {
        Optional<Location> temp = storageManager.getLocationsStorage().getEntry()
                .stream()
                .filter(loc -> loc.getKey().equals(slug))
                .map(Map.Entry::getValue)
                .findFirst();

        if (temp.isPresent()) {
            logger.info("Location found: {}", temp);
            return temp.get();
        } else {
            logger.error("Error! The location was not found!");
            throw new NullPointerException("Error! The location was not found!");
        }
    }

    @Schema(description = "Создаёт новую локацию и сохраняет её в хранилище.")
    public Location createLocation(Location location) {
        if (locationsStorage.containsKey(location.getSlug())) {
            logger.error("There is already an entity with this slug: {}", location.getSlug());
            throw new LocationSlugAlreadyExistsException("There is already an entity with this slug: " + location.getSlug());
        }

        locationsStorage.put(location.getSlug(), location);
        storageManager.addLocation(location.getSlug(), location);
        return location;
    }

    @Schema(description = "Обновляет локацию по её slug.")
    public Location updateLocation(String slug, String name) {
        try {
            getLocationBySlug(slug);
            Location updatedLocation = new Location(slug, name);
            locationsStorage.put(slug, updatedLocation);
            storageManager.getLocationsStorage().put(slug, updatedLocation);
            logger.info("The location has been successfully updated: {}", storageManager.getLocationsStorage().get(slug));
            return storageManager.getLocationsStorage().get(slug);
        } catch (NullPointerException e) {
            logger.error("Error! The location was not found!");
            throw new NullPointerException("Error! The location was not found!");
        }
    }

    @Schema(description = "Удаляет локацию по её slug.")
    public boolean deleteLocationBySlug(String slug) {
        try {
            if (!storageManager.getLocationsStorage().containsKey(slug)) {
                logger.error("Error! The location with this slug was not found: {}", slug);
                throw new NullPointerException("Error! The location with this slug was not found: " + slug);
            }

            storageManager.getLocationsStorage().remove(slug);
            locationsStorage.remove(slug);
            logger.info("The location was successfully deleted: {}", slug);
            return true;

        } catch (NullPointerException e) {
            logger.error("Error! The location with this slug was not found: {}", slug);
            throw new NullPointerException("Error! The location with this slug was not found: " + slug);
        }
    }
}
