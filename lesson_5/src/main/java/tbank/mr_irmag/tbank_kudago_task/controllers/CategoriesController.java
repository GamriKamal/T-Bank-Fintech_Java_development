package tbank.mr_irmag.tbank_kudago_task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable;
import tbank.mr_irmag.tbank_kudago_task.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.exceptions.ErrorResponse;
import tbank.mr_irmag.tbank_kudago_task.component.CategoriesCaretaker;
import tbank.mr_irmag.tbank_kudago_task.services.CategoryService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/places/categories")
@Tag(name = "CategoriesController", description = "Контроллер для обработки категорий")
@TimeMeasurable
public class CategoriesController {
    private CategoryService categoryService;
    private final CategoriesCaretaker caretaker;

    @Autowired
    public CategoriesController(CategoryService categoryService, CategoriesCaretaker caretaker) {
        this.categoryService = categoryService;
        this.caretaker = caretaker;
    }

    @GetMapping
    @Operation(summary = "Получить все категории", description = "Возвращает список категорий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены категории"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getAllCategories() {
        try {
            Map<Integer, Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories.entrySet().toString());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error getting categories", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить категорию по id", description = "Возвращает объект Categories по конкретному id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получена категория"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getCategoryById(@PathVariable("id") int id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error getting the category", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping
    @Operation(summary = "Создать категорию", description = "Создает категорию и сохраняет в хранилище.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Категория успешно создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка при создании категории", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error creating a category", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновляет категорию по id", description = "Возвращает измененный объект Categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка при обновлении категории", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> updateCategoryById(@PathVariable("id") int id, @RequestBody Location location) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, location);
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Category update error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить категорию по id", description = "Удаляет объект Categories из хранилища по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> deleteCategoryById(@PathVariable("id") int id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error deleting a category", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping("/save")
    @Operation(summary = "Сохранить текущее состояние категорий", description = "Сохраняет текущее состояние категорий в Memento.")
    public ResponseEntity<?> saveCategoriesState() {
        try {
            caretaker.save(categoryService);
            return ResponseEntity.ok("The status of the categories has been successfully saved.");
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error saving the status of categories", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/restore")
    @Operation(summary = "Восстановить предыдущее состояние категорий", description = "Восстанавливает предыдущее состояние категорий из Memento.")
    public ResponseEntity<?> restoreCategoriesState() {
        try {
            if (caretaker.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("There are no states available for recovery.");
            }
            caretaker.restore(categoryService);
            return ResponseEntity.ok("The status of the categories has been successfully restored.");
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error restoring the status of categories", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
