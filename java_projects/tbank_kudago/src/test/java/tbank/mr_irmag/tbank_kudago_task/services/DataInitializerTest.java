package tbank.mr_irmag.tbank_kudago_task.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.ParameterizedStorage;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;

public class DataInitializerTest {

    @Mock
    private StorageManager storageManager;

    @Mock
    private ReadKudaGo<Category> categoriesReader;

    @Mock
    private ReadKudaGo<Location> locationsReader;

    @Mock
    private Logger logger;

    @Mock
    private ExecutorService fixedThreadPool;

    @Mock
    private ParameterizedStorage<Integer, Category> categoriesStorage;

    @Mock
    private ParameterizedStorage<String, Location> locationsStorage;

    @InjectMocks
    private DataInitializer dataInitializer;

    private final String url_categories = "test_categories_url";
    private final String url_locations = "test_locations_url";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(storageManager.getCategoriesStorage()).thenReturn(categoriesStorage);
        when(storageManager.getLocationsStorage()).thenReturn(locationsStorage);
    }

    @Test
    public void initializeData_PositiveCase_ShouldInitializeCategoriesAndLocations() throws Exception {
        // Arrange
        List<Category> mockCategories = Arrays.asList(
                new Category(1, "art", "Art"),
                new Category(2, "science", "Science")
        );

        List<Location> mockLocations = Arrays.asList(
                new Location("slug1", "Moscow"),
                new Location("slug2", "Saint Petersburg")
        );

        when(categoriesReader.convertJsonToList(url_categories, Category.class))
                .thenReturn(mockCategories);
        when(locationsReader.convertJsonToList(url_locations, Location.class))
                .thenReturn(mockLocations);

        when(fixedThreadPool.submit(any(Runnable.class)))
                .thenAnswer(invocation -> {
                    Runnable task = invocation.getArgument(0);
                    task.run();
                    return CompletableFuture.completedFuture(null);
                });

        // Act
        dataInitializer.initializeData();

        // Assert
        verify(categoriesStorage, times(1)).put(1, mockCategories.get(0));
        verify(categoriesStorage, times(1)).put(2, mockCategories.get(1));

        verify(locationsStorage, times(1)).put("slug1", mockLocations.get(0));
        verify(locationsStorage, times(1)).put("slug2", mockLocations.get(1));

        verify(logger, times(1)).info("Initialized categories data.");
        verify(logger, times(1)).info("Initialized locations data.");
    }
}