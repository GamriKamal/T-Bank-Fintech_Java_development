package tbank.mr_irmag.tbank_kudago_task.services;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.entity.Categories;
import tbank.mr_irmag.tbank_kudago_task.entity.Locations;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Tag(name = "CategoriesService", description = "Сервис для управления категориями, включает получение, создание, обновление и удаление категорий.")
public class CategoriesService {
    private final Logger logger = LoggerFactory.getLogger(CategoriesService.class);
    private final StorageManager storageManager;

    @Value("${api.kuda_go.read.url_categories}")
    private String url_categories;

    private ReadKudaGo<Categories> readKudaGo;

    @Autowired
    public CategoriesService(StorageManager storageManager, ReadKudaGo<Categories> readKudaGo) {
        this.storageManager = storageManager;
        this.readKudaGo = readKudaGo;
    }

    @Autowired
    public void setReadKudaGo(ReadKudaGo<Categories> readKudaGo) {
        this.readKudaGo = readKudaGo;
    }

    @Schema(description = "Получает все категории из хранилища и сохраняет их в локальное хранилище.")
    public List<Categories> getAllCategories() {
        List<Categories> list = readKudaGo.convertJsonToList(url_categories, Categories.class);
        if (!list.isEmpty()) {
            list.forEach(info -> storageManager.getCategoriesStorage().put(info.getId(), info));
        } else {
            logger.debug("The list is empty!");
            throw new NullPointerException("Error! The list is empty!");
        }
        return list;
    }

    @Schema(description = "Получает категорию по её идентификатору.")
    public Categories getCategoryById(int id) {
        Optional<Categories> temp = storageManager.getCategoriesStorage().getEntry()
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
    public Categories createCategory(Categories category) {
        int newId = getNextId();
        category.setId(newId);
        storageManager.getCategoriesStorage().put(newId, category);

        try {
            if (storageManager.getCategoriesStorage().get(newId) == category) {
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
    public Categories updateCategory(int id, Locations locations) {
        try {
            getCategoryById(id);
            storageManager.getCategoriesStorage().put(id, new Categories(id, locations.getSlug(), locations.getName()));
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

    private int getNextId() {
        return storageManager.getCategoriesStorage().keySet().stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
}
