package tbank.mr_irmag.tbank_kudago_task.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Event;
import tbank.mr_irmag.tbank_kudago_task.exceptions.EventExceptions.EventAlreadyExistsException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.EventExceptions.EventNotFoundException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.EventExceptions.InvalidEventException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.PlaceException.PlaceNotFoundException;
import tbank.mr_irmag.tbank_kudago_task.repository.EventRepository;
import tbank.mr_irmag.tbank_kudago_task.repository.PlaceRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {
    private EventRepository eventRepository;
    private PlaceRepository placeRepository;

    @Autowired
    public EventService(EventRepository eventRepository, PlaceRepository placeRepository) {
        this.eventRepository = eventRepository;
        this.placeRepository = placeRepository;
    }

    public List<Event> searchEvents(Long placeId, String name, LocalDate fromDate, LocalDate toDate) {
        Specification<Event> specification = Specification.where(
                        EventSpecification.hasPlace(placeId))
                .and(EventSpecification.hasName(name))
                .and(EventSpecification.betweenDates(fromDate, toDate))
                .and(EventSpecification.fetchPlace());

        return eventRepository.findAll(specification);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event createEvent(Event event) {
        if (event == null) {
            throw new InvalidEventException("Event is null!");
        }

        if (event.getPlace() == null || event.getPlace().getId() == null || !placeRepository.existsById(event.getPlace().getId())) {
            throw new PlaceNotFoundException("Place with id " + event.getPlace().getId() + " not found!");
        }

        if (event.getId() == null) {
            if (eventRepository.existsByName(event.getName())) {
                throw new EventAlreadyExistsException("Event by this name: " + event.getName() + " already exists!");
            }
            return eventRepository.save(event);
        } else {
            if (!eventRepository.existsById(event.getId())) {
                if (eventRepository.existsByName(event.getName())) {
                    throw new EventAlreadyExistsException("Event by this name: " + event.getName() + " already exists!");
                }
                return eventRepository.save(event);
            } else {
                throw new EventAlreadyExistsException("Event by this id: " + event.getId() + " already exists!");
            }
        }
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("There is no event by this id: " + id));
    }

    public Event getEventByName(String name) {
        return eventRepository.findByName(name)
                .orElseThrow(() -> new EventNotFoundException("There is no event by this name: " + name));
    }

    public Event updateEventById(Long id, Event event) {
        Event existedEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("There is no event by this id: " + id));

        existedEvent.setName(event.getName());
        existedEvent.setPlace(event.getPlace());
        existedEvent.setDate(event.getDate());

        return eventRepository.save(existedEvent);
    }

    public void deleteEventById(Long id) {
        Event existedEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("There is no event by this id: " + id));

        eventRepository.delete(existedEvent);
    }


}
