package tbank.mr_irmag.tbank_kudago_task.services;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.entity.Location;

import java.util.*;

@Service
@Tag(name = "CategoriesService", description = "Сервис для управления категориями, включает получение, создание, обновление и удаление категорий.")
public class CategoriesService {
    private final Logger logger = LoggerFactory.getLogger(CategoriesService.class);
    private final StorageManager storageManager;

    @Value("${api.kuda_go.read.url_categories}")
    private String url_categories;

    private ReadKudaGo<Category> readKudaGo;

    @Autowired
    public CategoriesService(StorageManager storageManager, ReadKudaGo<Category> readKudaGo) {
        this.storageManager = storageManager;
        this.readKudaGo = readKudaGo;
    }

    @Autowired
    public void setReadKudaGo(ReadKudaGo<Category> readKudaGo) {
        this.readKudaGo = readKudaGo;
    }

    @Schema(description = "Получает все категории из хранилища и сохраняет их в локальное хранилище.")
    public List<Category> getAllCategories() {
        if(storageManager.getCategoriesStorage() != null){
            return storageManager
                    .getCategoriesStorage()
                    .getHashMap()
                    .values()
                    .stream().toList();
        } else {
            logger.warn("The storage is empty");
            return null;
        }
    }

    @Schema(description = "Получает категорию по её идентификатору.")
    public Category getCategoryById(int id) {
        Optional<Category> temp = storageManager.getCategoriesStorage().getEntry()
                .stream()
                .filter(info -> info.getKey() == id)
                .map(Map.Entry::getValue)
                .findFirst();

        if (temp.isPresent()) {
            logger.info("Category found: {}", temp);
            return temp.get();
        } else {
            logger.error("Error! The category was not found!");
            throw new NullPointerException("Error! The category was not found!");
        }
    }

    @Schema(description = "Создаёт новую категорию и сохраняет её в хранилище.")
    public Category createCategory(Category category) {
        storageManager.getCategoriesStorage().put(category.getId(), category);

        try {
            if (storageManager.getCategoriesStorage().get(category.getId()) == category) {
                logger.info("The category has been added successfully: {}", category);
                return category;
            }
        } catch (NoSuchElementException e) {
            logger.error("Error when adding a category: {}", category);
            throw new NoSuchElementException("Error when adding a category: " + category);
        }
        return null;
    }

    @Schema(description = "Обновляет категорию по её идентификатору.")
    public Category updateCategory(int id, Location locations) {
        try {
            getCategoryById(id);
            storageManager.getCategoriesStorage().put(id, new Category(id, locations.getSlug(), locations.getName()));
            logger.info("The category has been successfully updated: {}", storageManager.getCategoriesStorage().get(id));
            return storageManager.getCategoriesStorage().get(id);
        } catch (NullPointerException e) {
            logger.error("Error! The category was not found!");
            throw new NullPointerException("Error! The category was not found!");
        }
    }

    @Schema(description = "Удаляет категорию по её идентификатору.")
    public boolean deleteCategoryById(int id) {
        try {
            if (!storageManager.getCategoriesStorage().containsKey(id)) {
                throw new NullPointerException();
            }

            storageManager.getCategoriesStorage().remove(id);
            logger.info("The category was successfully deleted: {}", id);
            return true;

        } catch (NullPointerException e) {
            logger.error("Error! The category with this ID was not found: {}", id);
            throw new NullPointerException("Error! The category with this ID was not found: " + id);
        }
    }
}
