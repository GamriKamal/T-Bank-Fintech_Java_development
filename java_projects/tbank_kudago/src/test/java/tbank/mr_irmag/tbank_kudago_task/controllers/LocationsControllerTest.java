package tbank.mr_irmag.tbank_kudago_task.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.services.LocationsService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationsController.class)
class LocationsControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @MockBean
    private LocationsService locationsService;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLocations_SuccessResponse_GetJsonResponse() throws Exception {
        // Arrange
        List<Location> locations = List.of(
                new Location("slug1", "name1"),
                new Location("slug2", "name2"));
        when(locationsService.getAllLocations()).thenReturn(locations);

        // Act
        mockMvc.perform(get("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(locations)));

        // Assert
        verify(locationsService, times(1)).getAllLocations();
    }

    @Test
    void getAllLocations_ErrorResponse_ReturnBadRequest() throws Exception {
        // Arrange
        when(locationsService.getAllLocations()).thenThrow(new RuntimeException("Database error"));

        // Act
        mockMvc.perform(get("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Database error"));

        // Assert
        verify(locationsService, times(1)).getAllLocations();
    }

    @Test
    void getLocationBySlug_SuccessResponse_GetItemBySlug() throws Exception {
        // Arrange
        String slug = "slug1";
        Location location = new Location("slug1", "name1");
        when(locationsService.getLocationBySlug(slug)).thenReturn(location);

        // Act
        mockMvc.perform(get("/api/v1/locations/{slug}", slug)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully retrieved item: " + location));

        // Assert
        verify(locationsService, times(1)).getLocationBySlug(slug);
    }

    @Test
    void getLocationBySlug_ErrorResponse_InvalidSlug() throws Exception {
        // Arrange
        String slug = "invalid_slug";
        when(locationsService.getLocationBySlug(slug)).thenThrow(new RuntimeException("Location not found"));

        // Act
        mockMvc.perform(get("/api/v1/locations/{slug}", slug)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something get wrong while adding item in storage: Location not found"));

        // Assert
        verify(locationsService, times(1)).getLocationBySlug(slug);
    }

    @Test
    void createLocation_SuccessCreation_GetOkStatus() throws Exception {
        // Arrange
        Location location = new Location("slug1", "name1");
        when(locationsService.createLocation(any(Location.class))).thenReturn(location);

        // Act
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully added item: " + location));

        // Assert
        verify(locationsService, times(1)).createLocation(any(Location.class));
    }

    @Test
    void createLocation_FailureInvalidInput_ReturnsBadRequest() throws Exception {
        // Arrange
        Location invalidCategory = new Location();
        when(locationsService.createLocation(any(Location.class))).thenThrow(new NoSuchElementException("Invalid Location"));

        // Act
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something get wrong while adding item in storage: Invalid Location"));

        // Assert
        verify(locationsService, times(1)).createLocation(any(Location.class));
    }

    @Test
    void updateLocationBySlug_Success() throws Exception {
        // Arrange
        String slug = "location1";
        String name = "New Location Name";
        Location mockLocation = new Location(slug, name); // Assuming Location has a constructor

        when(locationsService.updateLocation(slug, name)).thenReturn(mockLocation);

        // Act and Assert
        mockMvc.perform(put("/api/v1/locations/{slug}", slug)
                        .content(name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully updated item: " + mockLocation));
    }

    @Test
    void updateLocationBySlug_NullPointerException() throws Exception {
        // Arrange
        String slug = "location2";
        String name = "Invalid Location Name";

        when(locationsService.updateLocation(slug, name)).thenThrow(new NullPointerException("Location not found"));

        // Act and Assert
        mockMvc.perform(put("/api/v1/locations/{slug}", slug)
                        .content(name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something get wrong while updating item in storage:Location not found"));
    }


    @Test
    void deleteLocationBySlug_SuccessDeletedItem_RemoveItemBySlug() throws Exception {
        // Arrange
        String slug = "slug1";
        when(locationsService.deleteLocationBySlug(slug)).thenReturn(true);

        // Act
        mockMvc.perform(delete("/api/v1/locations/{slug}", slug)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted item by slug: " + slug));

        // Assert
        verify(locationsService, times(1)).deleteLocationBySlug(slug);
    }

    @Test
    void deleteLocationBySlug_ErrorResponse_InvalidSlug() throws Exception {
        // Arrange
        String slug = "invalid_slug";
        doThrow(new RuntimeException("Location not found")).when(locationsService).deleteLocationBySlug(slug);

        // Act
        mockMvc.perform(delete("/api/v1/locations/{slug}", slug)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something get wrong while deleting item in storage: Location not found"));

        // Assert
        verify(locationsService, times(1)).deleteLocationBySlug(slug);
    }
}
