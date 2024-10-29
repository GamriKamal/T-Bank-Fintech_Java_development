package tbank.mr_irmag.tbank_kudago_task.services;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.exceptions.CategoryIdAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Tag(name = "CategoriesService", description = "Сервис для управления категориями, включает получение, создание, обновление и удаление категорий.")
@Log4j2
public class CategoryService {
    private final StorageManager storageManager;
    private final Map<Integer, Category> categoryMap = new HashMap<>();  // Replacing List with Map

    @Autowired
    public CategoryService(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public CategoryMemento save() {
        log.info("Saved state: {}", storageManager.getCategoriesStorage().getHashMap().values().toString());
        return new CategoryMemento(storageManager.getCategoriesStorage());
    }

    public void restore(CategoryMemento memento) {
        storageManager.getCategoriesStorage().clear();
        memento.getSavedState().getEntry().forEach(entry ->
                storageManager.getCategoriesStorage().put(entry.getKey(), entry.getValue().clone()));

        log.info("Restored categories from Memento. Current storage state: {}", storageManager.getCategoriesStorage().getHashMap().values().toString());
    }

    @Schema(description = "Получает все категории из хранилища и сохраняет их в локальное хранилище.")
    public Map<Integer, Category> getAllCategories() {
        return storageManager.getCategoriesStorage().getHashMap();
    }

    @Schema(description = "Получает категорию по её идентификатору.")
    public Category getCategoryById(int id) {
        return Optional.ofNullable(storageManager.getCategoriesStorage().get(id))
                .orElseThrow(() -> {
                    log.error("Error! The category was not found with ID: {}", id);
                    return new NullPointerException("Error! The category was not found with ID: " + id);
                });
    }

    @Schema(description = "Создаёт новую категорию и сохраняет её в хранилище.")
    public Category createCategory(Category category) {
        int categoryId = Optional.ofNullable(category.getId()).orElseGet(this::getNextId);

        if (storageManager.getCategoriesStorage().containsKey(categoryId)) {
            log.error("There is already an entity with this ID: {}", categoryId);
            throw new CategoryIdAlreadyExistsException("There is already an entity with this ID: " + categoryId);
        }

        category.setId(categoryId);
        storageManager.addCategory(categoryId, category);
        categoryMap.put(categoryId, category);  // Update local map

        if (storageManager.getCategoriesStorage().get(categoryId).equals(category)) {
            return category;
        } else {
            log.error("Error when adding category: {}", category);
            throw new IllegalStateException("Category was not added to the storage: " + category);
        }
    }

    @Schema(description = "Обновляет категорию по её идентификатору.")
    public Category updateCategory(int id, Location location) {
        getCategoryById(id);  // Ensure the category exists
        Category updatedCategory = new Category(id, location.getSlug(), location.getName());

        storageManager.getCategoriesStorage().put(id, updatedCategory);
        categoryMap.put(id, updatedCategory);  // Update local map as well

        log.info("The category has been successfully updated: {}", updatedCategory);
        return updatedCategory;
    }

    @Schema(description = "Удаляет категорию по её идентификатору.")
    public boolean deleteCategoryById(int id) {
        if (!storageManager.getCategoriesStorage().containsKey(id)) {
            log.error("Error! The category with this ID was not found: {}", id);
            throw new NullPointerException("Error! The category with this ID was not found: " + id);
        }

        storageManager.getCategoriesStorage().remove(id);
        categoryMap.remove(id);  // Remove from local map
        log.info("The category was successfully deleted: {}", id);
        return true;
    }

    private int getNextId() {
        return storageManager.getCategoriesStorage().keySet().stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
}
