package tbank.mr_irmag.tbank_kudago_task.services;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.entity.Categories;
import tbank.mr_irmag.tbank_kudago_task.entity.Locations;

import java.util.List;

@Tag(name = "DataInitializer", description = "Сервис для инициализации данных категорий и локаций.")
@Service
@TimeMeasurable
public class DataInitializer {
    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final StorageManager storageManager;
    private final ReadKudaGo<Categories> categoriesReader;
    private final ReadKudaGo<Locations> locationsReader;

    @Value("${api.kuda_go.read.url_categories}")
    private String url_categories;

    @Value("${api.kuda_go.read.url_locations}")
    private String url_locations;

    @Autowired
    public DataInitializer(StorageManager storageManager,
                           ReadKudaGo<Categories> categoriesReader,
                           ReadKudaGo<Locations> locationsReader) {
        this.storageManager = storageManager;
        this.categoriesReader = categoriesReader;
        this.locationsReader = locationsReader;
    }

    @Schema(description = "Инициализирует данные, вызывая методы инициализации категорий и локаций.")
    public void initializeData() {
        initializeCategories();
        initializeLocations();
    }

    @Schema(description = "Инициализирует данные категорий, загружая их из внешнего источника.")
    private void initializeCategories() {
        List<Categories> categories = categoriesReader.convertJsonToList(url_categories, Categories.class);
        if (!categories.isEmpty()) {
            categories.forEach(category -> storageManager.getCategoriesStorage().put(category.getId(), category));
            logger.info("Initialized categories data.");
        } else {
            logger.debug("Categories list is empty!");
        }
    }

    @Schema(description = "Инициализирует данные локаций, загружая их из внешнего источника.")
    private void initializeLocations() {
        List<Locations> locations = locationsReader.convertJsonToList(url_locations, Locations.class);
        if (!locations.isEmpty()) {
            locations.forEach(location -> storageManager.getLocationsStorage().put(location.getSlug(), location));
            logger.info("Initialized locations data.");
        } else {
            logger.debug("Locations list is empty!");
        }
    }
}

