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
import tbank.mr_irmag.tbank_kudago_task.entity.Categories;
import tbank.mr_irmag.tbank_kudago_task.entity.Locations;
import tbank.mr_irmag.tbank_kudago_task.exceptions.ErrorResponse;
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
    @Operation(summary = "Получить все категории", description = "Возвращает список категорий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены категории"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Categories> categories = categoriesService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка получения категорий", e.getMessage());
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
            Categories category = categoriesService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка получения категории", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping
    @Operation(summary = "Создать категорию", description = "Создает категорию и сохраняет в хранилище.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Категория успешно создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка при создании категории", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> createCategory(@RequestBody Categories category) {
        try {
            Categories createdCategory = categoriesService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка создания категории", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновляет категорию по id", description = "Возвращает измененный объект Categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка при обновлении категории", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> updateCategoryById(@PathVariable("id") int id, @RequestBody Locations locations) {
        try {
            Categories updatedCategory = categoriesService.updateCategory(id, locations);
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка обновления категории", e.getMessage());
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
            categoriesService.deleteCategoryById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка удаления категории", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
