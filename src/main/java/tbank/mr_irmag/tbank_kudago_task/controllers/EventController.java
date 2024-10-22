package tbank.mr_irmag.tbank_kudago_task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Event;
import tbank.mr_irmag.tbank_kudago_task.services.EventService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v2/events")
@RequiredArgsConstructor
@Tag(name = "EventController", description = "Контроллер для обработки событий")
@Log4j2
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск событий", description = "Возвращает список событий по параметрам поиска")
    public ResponseEntity<?> searchEvents(
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate) {

        List<Event> events = eventService.searchEvents(placeId, name, fromDate, toDate);
        return ResponseEntity.ok(events);
    }


    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        log.info(event.toString());
        log.info(event.getPlace().toString());
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.status(201).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Event updatedEvent = eventService.updateEventById(id, event);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEventById(id);
        return ResponseEntity.noContent().build();
    }
}
