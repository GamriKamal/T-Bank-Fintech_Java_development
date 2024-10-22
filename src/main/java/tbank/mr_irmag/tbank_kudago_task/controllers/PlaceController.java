package tbank.mr_irmag.tbank_kudago_task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Place;
import tbank.mr_irmag.tbank_kudago_task.services.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/places")
@RequiredArgsConstructor
@Tag(name = "PlaceController", description = "Контроллер для обработки мест")
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping
    @Operation(summary = "Получить все места", description = "Возвращает список всех мест")
    public ResponseEntity<?> getAllPlaces() {
        List<Place> places = placeService.getAllPlaces();
        return ResponseEntity.ok(places);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить место по id", description = "Возвращает объект Place по конкретному id")
    public ResponseEntity<?> getPlaceById(@Valid @PathVariable("id") Long id) {
        Place place = placeService.getPlaceById(id);
        return ResponseEntity.ok(place);
    }


    @PostMapping
    @Operation(summary = "Создать место", description = "Сохраняет новое место в базе данных.")
    public ResponseEntity<?> createPlace(@Valid @RequestBody Place place) {
        Place response = placeService.createPlace(place);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить место по id", description = "Возвращает измененный объект Place")
    public ResponseEntity<?> updatePlaceById(@Valid @PathVariable("id") Long id, @RequestBody Place place) {
        Place response = placeService.updatePlaceById(id, place);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить место по id", description = "Удаляет объект Place из хранилища по id")
    public ResponseEntity<?> deletePlaceById(@Valid @PathVariable("id") Long id) {
        placeService.deletePlaceById(id);
        return ResponseEntity.noContent().build();
    }
}
