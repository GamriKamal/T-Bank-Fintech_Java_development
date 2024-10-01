package tbank.mr_irmag.tbank_kudago_task.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable;
import tbank.mr_irmag.tbank_kudago_task.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.services.CategoriesService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places/categories")
@Tag(name = "CategoriesController", description = "Контроллер для обработки категорий")
@TimeMeasurable
public class CategoriesController {
    private CategoriesService categoriesService;

    @Autowired
    public void setCategoriesService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    @Operation(summary = "Получить все категории с endpoint'a",
            description = "Возвращает список категорий с endpoint'a [https://kudago.com/public-api/v1.4/place-categories]")
    public ResponseEntity<String> getAllCategories() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Category> categories = categoriesService.getAllCategories();
            String jsonCategories = objectMapper.writeValueAsString(categories);

            return ResponseEntity.ok().body(jsonCategories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить категорию по id", description = "Возвращает объект Categories по конкретному id")
    public ResponseEntity<String> getCategoryById(@PathVariable("id") int id) {
        try {
            Category category = categoriesService.getCategoryById(id);
            return ResponseEntity.ok().body("Successfully retrieved item: " + category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something get wrong while adding item in storage: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Создать категорию", description = "Создает категорию и сохранаят в параметризованный класс хранилище.")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        try {
            categoriesService.createCategory(category);
            return ResponseEntity.ok().body("Successfully added item: " + category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something get wrong while adding item in storage: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновляет категорию по id", description = "Возвращает изменный объект Categories")
    public ResponseEntity<String> updateCategoryById(@PathVariable("id") int id, @RequestBody Location locations) {
        try {
            Category category = categoriesService.updateCategory(id, locations);
            return ResponseEntity.ok().body("Succsessfully updated item: " + category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something get wrong while updating item in storage:" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить категорию по id", description = "Удаляет объект Categories из хранилища по id")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("id") int id) {
        try {
            categoriesService.deleteCategoryById(id);
            return ResponseEntity.ok().body("Succsessfully deleted item by id: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something get wrong while deleting item in storage: " + e.getMessage());
        }
    }
}
