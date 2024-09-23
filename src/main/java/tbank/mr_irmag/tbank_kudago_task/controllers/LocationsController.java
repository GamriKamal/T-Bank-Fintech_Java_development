package tbank.mr_irmag.tbank_kudago_task.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable;
import tbank.mr_irmag.tbank_kudago_task.entity.Locations;
import tbank.mr_irmag.tbank_kudago_task.services.LocationsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@Tag(name = "LocationsController", description = "Контроллер для обработки локаций")
@TimeMeasurable
public class LocationsController {
    private LocationsService locationsService;

    @Autowired
    public void setLocationsService(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @GetMapping
    @Operation(summary = "Получить все локации с endpoint'a",
            description = "Возвращает список локаций с endpoint'a [https://kudago.com/public-api/v1.4/locations]")
    public ResponseEntity<String> getAllCategories() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Locations> categories = locationsService.getAllLocations();
            String jsonCategories = objectMapper.writeValueAsString(categories);

            return ResponseEntity.ok().body(jsonCategories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Получить локацию по slug", description = "Возвращает объект Locations по конкретному slug")
    public ResponseEntity<String> getCategoryById(@PathVariable("slug") String slug) {
        try {
            Locations location = locationsService.getLocationBySlug(slug);
            return ResponseEntity.ok().body("Successfully retrieved item: " + location);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something get wrong while adding item in storage: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Создать локацию", description = "Создает локацию и сохранаят в параметризованный класс хранилище.")
    public ResponseEntity<String> createCategory(@RequestBody Locations location) {
        try {
            locationsService.createLocation(location);
            return ResponseEntity.ok().body("Successfully added item: " + location);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something get wrong while adding item in storage: " + e.getMessage());
        }
    }

    @PutMapping("/{slug}")
    @Operation(summary = "Обновляет локацию по slug", description = "Возвращает изменный объект Locations")
    public ResponseEntity<String> updateCategoryById(@PathVariable("slug") String slug, @RequestBody String name) {
        try {
            Locations location = locationsService.updateLocation(slug, name);
            return ResponseEntity.ok().body("Successfully updated item: " + location);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something get wrong while updating item in storage:" + e.getMessage());
        }
    }

    @DeleteMapping("/{slug}")
    @Operation(summary = "Удалить локацию по slug", description = "Удаляет объект Locations из хранилища по slug")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("slug") String slug) {
        try {
            locationsService.deleteLocationBySlug(slug);
            return ResponseEntity.ok().body("Successfully deleted item by slug: " + slug);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something get wrong while deleting item in storage: " + e.getMessage());
        }
    }
}
