package tbank.mr_irmag.tbank_kudago_task.services;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Tag(name = "DataInitializer", description = "Сервис для инициализации данных категорий и локаций.")
@Service
@TimeMeasurable
@RequiredArgsConstructor
public class DataInitializer {
    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final StorageManager storageManager;
    private final ReadKudaGo<Category> categoriesReader;
    private final ReadKudaGo<Location> locationsReader;
    private final ExecutorService fixedThreadPool;

    @Value("${api.kuda_go.read.url_categories}")
    private String url_categories;

    @Value("${api.kuda_go.read.url_locations}")
    private String url_locations;


    @Schema(description = "Инициализирует данные, вызывая методы инициализации категорий и локаций.")
    public void initializeData() {
        List<Future<?>> futures = new ArrayList<>();

        futures.add(fixedThreadPool.submit(this::initializeCategories));
        futures.add(fixedThreadPool.submit(this::initializeLocations));

        futures.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                logger.error("Error during data initialization", e);
            }
        });
    }

    @Schema(description = "Инициализирует данные категорий, загружая их из внешнего источника.")
    private void initializeCategories() {
        List<Category> categories = categoriesReader.convertJsonToList(url_categories, Category.class);
        if (!categories.isEmpty()) {
            categories.forEach(category -> storageManager.getCategoriesStorage().put(category.getId(), category));
            logger.info("Initialized categories data.");
        } else {
            logger.debug("Categories list is empty!");
        }
    }

    @Schema(description = "Инициализирует данные локаций, загружая их из внешнего источника.")
    private void initializeLocations() {
        List<Location> locations = locationsReader.convertJsonToList(url_locations, Location.class);
        if (!locations.isEmpty()) {
            locations.forEach(location -> storageManager.getLocationsStorage().put(location.getSlug(), location));
            logger.info("Initialized locations data.");
        } else {
            logger.debug("Locations list is empty!");
        }
    }
}

