package tbank.mr_irmag.tbank_kudago_task.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.ParameterizedStorage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LocationsServiceTest {
    @Mock
    private StorageManager storageManager;

    @Mock
    private ParameterizedStorage<String, Location> locationStorage;

    @InjectMocks
    private LocationsService locationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(storageManager.getLocationsStorage()).thenReturn(locationStorage);
    }

    @Test
    void getAllLocations_SuccessReturningCollection_ShouldReturnListOfLocations() {
        // Arrange
        Location locations1 = new Location("test-slug", "Test Location");
        Location locations2 = new Location("test-slug2", "Test Location2");
        Location locations3 = new Location("test-slug3", "Test Location3");

        ConcurrentHashMap<String, Location> mockMap = new ConcurrentHashMap<>();
        mockMap.put(locations1.getSlug(), locations1);
        mockMap.put(locations2.getSlug(), locations2);
        mockMap.put(locations3.getSlug(), locations3);

        when(locationStorage.getHashMap()).thenReturn(mockMap);

        // Act
        List<Location> list = locationsService.getAllLocations();

        // Assert
        assertNotNull(list);
        assertEquals(3, list.size());
        assertTrue(list.contains(locations1));
        assertTrue(list.contains(locations2));
        assertTrue(list.contains(locations3));
    }

    @Test
    void getAllCategories_EmptyStorage_ShouldReturnEmptyList() {
        // Arrange
        ConcurrentHashMap<String, Location> mockMap = new ConcurrentHashMap<>();
        when(locationStorage.getHashMap()).thenReturn(mockMap);
        when(storageManager.getLocationsStorage()).thenReturn(null);

        // Act
        List<Location> list = locationsService.getAllLocations();

        // Assert
        assertNull(list);
    }

    @Test
    void getLocationBySlug_SuccessReturn_ShouldReturnItemBySlug() {
        // Arrange
        Location location = new Location("test-slug", "Test Location");
        ConcurrentHashMap<String, Location> mockMap = new ConcurrentHashMap<>();
        mockMap.put(location.getSlug(), location);
        when(locationStorage.getEntry()).thenReturn(mockMap.entrySet());

        // Act
        Location location1 = locationsService.getLocationBySlug(location.getSlug());

        // Assert
        assertEquals(location.getName(), location1.getName());
    }

    @Test
    void getLocationBySlug_NotSuccessReturn_ShouldNotReturnItemBySLug() {
        // Arrange
        ConcurrentHashMap<String, Location> mockMap = new ConcurrentHashMap<>();
        when(locationStorage.getEntry()).thenReturn(mockMap.entrySet());

        //Act and Assert
        assertThrows(NullPointerException.class, () -> locationsService.getLocationBySlug("some"));
    }

    @Test
    void createLocation_SuccessCreation_ShouldCreate() {
        // Arrange
        Location location = new Location("test-slug", "Test Location");
        doNothing().when(locationStorage).put(anyString(), any(Location.class));
        when(locationStorage.get(location.getSlug())).thenReturn(location);

        // Act
        Location result = locationsService.createLocation(location);

        // Assert
        assertNotNull(result);
        assertEquals(location, result);
        verify(locationStorage).put(location.getSlug(), location);
    }

    @Test
    void createLocation_NotSuccessCreation_ReturnNull() {
        // Arrange
        Location location = new Location("test-slug", "Test Location");
        when(locationsService.createLocation(location)).thenReturn(null);

        // Act
        Location result = locationsService.createLocation(location);

        // Assert
        assertNull(result);
    }

    @Test
    void createLocation_GetThrowsNoSuchElementException_ShouldLogErrorAndThrowException() {
        // Arrange
        Location location = new Location("test-slug", "Test Location");

        doNothing().when(locationStorage).put(anyString(), any(Location.class));
        when(locationStorage.get(location.getSlug())).thenThrow(new NoSuchElementException("Location not found"));

        // Act & Assert
        NoSuchElementException thrownException = assertThrows(NoSuchElementException.class, () -> {
            locationsService.createLocation(location);
        });

        assertEquals("An error occurred while adding an item to the repository: " + location, thrownException.getMessage());
    }

    @Test
    void updateLocation_GetUpdatedLocationSuccess_ShouldReturnUpdatedLocation() {
        // Arrange
        String slug = "location-1";
        String newName = "New Location Name";
        Location existingLocation = new Location(slug, "Old Location Name");
        Location expectedLocation = new Location(slug, newName);

        ConcurrentHashMap<String, Location> mockMap = new ConcurrentHashMap<>();
        mockMap.put(slug, existingLocation);
        when(locationStorage.getEntry()).thenReturn(mockMap.entrySet());
        when(storageManager.getLocationsStorage().get(slug)).thenReturn(expectedLocation);

        // Act
        Location updatedLocation = locationsService.updateLocation(slug, newName);

        // Assert
        assertEquals(slug, updatedLocation.getSlug());
        assertEquals(newName, updatedLocation.getName());
        verify(storageManager.getLocationsStorage()).put(slug, new Location(slug, newName));
    }

    @Test
    void updateLocation_GetUpdatedLocationNotSuccess_ThrowNullPointerException() {
        // Arrange
        String slug = "location-1";
        String newName = "New Location Name";
        when(locationStorage.getEntry()).thenReturn(null);

        // Act and Assert
        NullPointerException exception =
                assertThrows(NullPointerException.class, () ->
                        locationsService.updateLocation(slug, newName));

        assertEquals("Error! The element is null!", exception.getMessage());
    }

    @Test
    void deleteLocationById_SuccessfullyDeleteLocation_ShouldDeleteLocation() {
        // Arrange
        Location location = new Location("test-slug", "Test Location");
        ConcurrentHashMap<String, Location> mockMap = new ConcurrentHashMap<>();
        mockMap.put(location.getSlug(), location);
        when(locationStorage.containsKey(location.getSlug())).thenReturn(true);

        // Act
        boolean answer = locationsService.deleteLocationBySlug(location.getSlug());

        // Assert
        assertTrue(answer);
    }

    @Test
    void testDeleteLocationById_NotSuccess_ThrowsNotFoundException() {
        // Arrange
        String nonExistentSlug = "some";
        when(storageManager.getCategoriesStorage()).thenReturn(new ParameterizedStorage<>());

        // Act
        NullPointerException exception = assertThrows(NullPointerException.class, () -> locationsService.deleteLocationBySlug(nonExistentSlug));

        // Assert
        assertEquals("There is no element with such a slug: " + nonExistentSlug, exception.getMessage());
    }
}