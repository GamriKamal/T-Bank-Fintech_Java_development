package tbank.mr_irmag.tbank_kudago_task.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.interfaces.DataInitializationCommand;

public class InitializeCategoriesCommand implements DataInitializationCommand {
    private final StorageManager storageManager;
    private final ReadKudaGo<Category> categoriesReader;

    private final String urlCategories;
    private final Logger logger = LoggerFactory.getLogger(InitializeCategoriesCommand.class);

    public InitializeCategoriesCommand(StorageManager storageManager, ReadKudaGo<Category> categoriesReader, String urlCategories) {
        this.storageManager = storageManager;
        this.categoriesReader = categoriesReader;
        this.urlCategories = urlCategories;
    }

    @Override
    public void execute() {
        List<Category> categories = categoriesReader.convertJsonToList(urlCategories, Category.class);
        if (!categories.isEmpty()) {
            categories.forEach(category -> storageManager.getCategoriesStorage().put(category.getId(), category));
            logger.info("Initialized categories data.");
        } else {
            logger.debug("Categories list is empty!");
        }
    }
}
