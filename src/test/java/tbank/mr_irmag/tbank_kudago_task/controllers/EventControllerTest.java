package tbank.mr_irmag.tbank_kudago_task.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.core.type.TypeReference;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Event;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Place;
import tbank.mr_irmag.tbank_kudago_task.repository.EventRepository;
import tbank.mr_irmag.tbank_kudago_task.repository.PlaceRepository;
import tbank.mr_irmag.tbank_kudago_task.services.EventService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventControllerTest {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private EventService eventService;

    private EventRepository eventRepository;

    private PlaceRepository placeRepository;

    @Autowired
    public EventControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                               EventService eventService, EventRepository eventRepository,
                               PlaceRepository placeRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.placeRepository = placeRepository;
    }

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("lesson9")
            .withUsername("postgresql")
            .withPassword("root");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    @Order(1)
    public void createEvent_PositiveCase_ShouldSaveToDatabaseThroughService() throws Exception {
        Place place = Place.builder()
                .id(1L)
                .slug("slug")
                .name("name")
                .build();

        mockMvc.perform(post("/api/v2/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(place)))
                .andExpect(status().isCreated());

        Place savedPlace = placeRepository.findById(1L).get();

        Event event = Event.builder()
                .name("name")
                .date(LocalDate.now())
                .place(savedPlace)
                .build();

       mockMvc.perform(post("/api/v2/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated());

        List<Event> eventList = eventService.getAllEvents();
        assertThat(eventList).hasSize(1);
        assertThat(eventList.get(0).getName()).isEqualTo(event.getName());
    }

    @Test
    @Order(2)
    public void createEvent_NegativeCase_ShouldReturnBadRequestForNonExistentPlace() throws Exception {
        Event event = Event.builder()
                .name("name")
                .date(LocalDate.now())
                .place(Place.builder().id(100L).build())
                .build();

        MvcResult result = mockMvc.perform(post("/api/v2/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertThat(response).contains("Place with id 100 not found!");
    }

    @Test
    @Order(3)
    public void updateEvent_PositiveCase_ShouldUpdateEventInDatabase() throws Exception {
        Event updatedEvent = Event.builder()
                .name("Updated Event")
                .date(LocalDate.now().plusDays(1))
                .place(Place.builder().id(1L).build())
                .build();

        mockMvc.perform(put("/api/v2/events/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEvent)))
                .andExpect(status().isOk());

        Event eventFromDb = eventService.getEventById(1L);
        assertThat(eventFromDb.getName()).isEqualTo(updatedEvent.getName());
        assertThat(eventFromDb.getDate()).isEqualTo(updatedEvent.getDate());
    }

    @Test
    @Order(4)
    public void updateEvent_NegativeCase_ShouldReturnNotFound_WhenEventDoesNotExist() throws Exception {

        mockMvc.perform(put("/api/v2/events/" + 1337L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Event())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    public void deleteEvent_ExistingId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v2/events/" + 1L))
                .andExpect(status().isNoContent());

        assertThat(eventRepository.findById(1L)).isEmpty();
    }

    @Test
    @Order(6)
    public void deleteEvent_NonExistentId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/v2/events/" + 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    public void searchEvents_PositiveCase_ShouldReturnFilteredEvents() throws Exception {
        Event event1 = Event.builder()
                .name("Event 1")
                .date(LocalDate.now())
                .place(Place.builder().id(1L).build())
                .build();

        Event event2 = Event.builder()
                .name("Event 2")
                .date(LocalDate.now().plusDays(1))
                .place(Place.builder().id(1L).build())
                .build();

        Event event3 = Event.builder()
                .name("Event 3")
                .date(LocalDate.now().plusDays(2))
                .place(Place.builder().id(1L).build())
                .build();

        Event event4 = Event.builder()
                .name("Event 4")
                .date(LocalDate.now().plusDays(3))
                .place(Place.builder().id(1L).build())
                .build();

        mockMvc.perform(post("/api/v2/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v2/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v2/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event3)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v2/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event4)))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(get("/api/v2/events/search")
                        .param("placeId", String.valueOf(1L))
                        .param("name", "Event")
                        .param("fromDate", LocalDate.now().toString())
                        .param("toDate", LocalDate.now().plusDays(3).toString()))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        List<Event> returnedEvents = objectMapper.readValue(response, new TypeReference<>() {});

        assertThat(returnedEvents).hasSize(4);
        assertThat(returnedEvents).extracting("name").containsExactlyInAnyOrder("Event 1", "Event 2", "Event 3", "Event 4");
    }


}