package tbank.mr_irmag.tbank_kudago_task.services;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.interfaces.DataInitializationCommand;

@Service
@Tag(name = "DataInitializer", description = "Сервис для инициализации данных категорий и локаций.")
public class DataInitializer {

    private final DataInitializationCommand initializeCategoriesCommand;
    private final DataInitializationCommand initializeLocationsCommand;

    @Autowired
    public DataInitializer(StorageManager storageManager,
                           ReadKudaGo<Category> categoriesReader,
                           ReadKudaGo<Location> locationsReader,
                           @Value("${api.kuda_go.read.url_categories}") String categoryUrl,
                           @Value("${api.kuda_go.read.url_locations}") String locationsUrl){
        this.initializeCategoriesCommand = new InitializeCategoriesCommand(storageManager, categoriesReader, categoryUrl);
        this.initializeLocationsCommand = new InitializeLocationsCommand(storageManager, locationsReader, locationsUrl);
    }

    public void initializeData() {
        initializeCategoriesCommand.execute();
        initializeLocationsCommand.execute();
    }
}

