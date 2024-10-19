package tbank.mr_irmag.tbank_kudago_task.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import tbank.mr_irmag.tbank_kudago_task.domain.DTO.EventRequest;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Event;
import tbank.mr_irmag.tbank_kudago_task.services.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "EventController", description = "Контроллер для обработки событий")
public class EventController {
    private EventService eventService;

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<?> getEventsByBudget(@RequestBody EventRequest eventRequest){
        Mono<List<Event>> result = eventService.findEventsByBudget(eventRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
