package tbank.mr_irmag.tbank_kudago_task.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
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
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Place;
import tbank.mr_irmag.tbank_kudago_task.repository.PlaceRepository;
import tbank.mr_irmag.tbank_kudago_task.services.PlaceService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlaceControllerTest {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private PlaceService placeService;

    private PlaceRepository placeRepository;

    @Autowired
    public PlaceControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                               PlaceService placeService, PlaceRepository placeRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.placeService = placeService;
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
    public void createPlace_PositiveCase_ShouldSaveToDatabase() throws Exception {
        Place place = Place.builder()
                .slug("slug")
                .name("name")
                .build();

        mockMvc.perform(post("/api/v2/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(place)))
                .andExpect(status().isCreated());

        List<Place> placeList = placeService.getAllPlaces();
        assertThat(placeList).hasSize(1);
        assertThat(placeList.get(0).getName()).isEqualTo(place.getName());
    }

    @Test
    @Order(2)
    public void getAllPlaces_ShouldReturnAllPlaces() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v2/places"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        List<Place> returnedPlaces = objectMapper.readValue(response, new TypeReference<>() {});

        assertThat(returnedPlaces).hasSize(1);
    }

    @Test
    @Order(3)
    public void getPlaceById_PositiveCase_ShouldReturnPlace() throws Exception {
        mockMvc.perform(get("/api/v2/places/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void getPlaceById_NegativeCase_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v2/places/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void updatePlace_PositiveCase_ShouldUpdatePlace() throws Exception {
        Place updatedPlace = Place.builder()
                .slug("updated-slug")
                .name("updated-name")
                .build();

        mockMvc.perform(put("/api/v2/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPlace)))
                .andExpect(status().isOk());

        Place placeFromDb = placeService.getPlaceById(1L);
        assertThat(placeFromDb.getName()).isEqualTo(updatedPlace.getName());
    }

    @Test
    @Order(6)
    public void deletePlace_ExistingId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v2/places/1"))
                .andExpect(status().isNoContent());

        assertThat(placeRepository.findById(1L)).isEmpty();
    }

    @Test
    @Order(7)
    public void deletePlace_NonExistentId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/v2/places/999"))
                .andExpect(status().isBadRequest());
    }

}
