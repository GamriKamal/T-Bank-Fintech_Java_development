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
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.exceptions.ErrorResponse;
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
    @Operation(summary = "Получить все локации", description = "Возвращает список локаций")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены локации"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getAllLocations() {
        try {
            List<Location> locations = locationsService.getAllLocations();
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка получения локаций", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Получить локацию по slug", description = "Возвращает объект Locations по конкретному slug")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получена локация"),
            @ApiResponse(responseCode = "404", description = "Локация не найдена", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getLocationBySlug(@PathVariable("slug") String slug) {
        try {
            Location location = locationsService.getLocationBySlug(slug);
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка получения локации", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping
    @Operation(summary = "Создать локацию", description = "Создает локацию и сохраняет в хранилище")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Локация успешно создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка при создании локации", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> createLocation(@RequestBody Location location) {
        try {
            Location createdLocation = locationsService.createLocation(location);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка создания локации", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{slug}")
    @Operation(summary = "Обновляет локацию по slug", description = "Возвращает измененный объект Locations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Локация успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка при обновлении локации", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> updateLocation(@PathVariable("slug") String slug, @RequestBody String name) {
        try {
            Location updatedLocation = locationsService.updateLocation(slug, name);
            return ResponseEntity.ok(updatedLocation);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка обновления локации", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/{slug}")
    @Operation(summary = "Удалить локацию по slug", description = "Удаляет объект Locations из хранилища по slug")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Локация успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Локация не найдена", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> deleteLocation(@PathVariable("slug") String slug) {
        try {
            locationsService.deleteLocationBySlug(slug);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка удаления локации", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
