package tbank.mr_irmag.tbank_kudago_task.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.entity.Location;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DataInitializerTest {

    @Mock
    private StorageManager storageManager;

    @Mock
    private ReadKudaGo<Category> categoriesReader;

    @Mock
    private ReadKudaGo<Location> locationsReader;

    @Mock
    private Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private DataInitializer dataInitializerSpy;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        dataInitializerSpy = spy(new DataInitializer(storageManager, categoriesReader, locationsReader));

        Field loggerField = DataInitializer.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(dataInitializerSpy, logger);
    }

    @Test
    void initializeData_WithEmptyCategories_ShouldReturnMessage() {
        // Arrange
        when(categoriesReader.convertJsonToList(anyString(), eq(Category.class)))
                .thenReturn(Collections.emptyList());

        // Act
        dataInitializerSpy.initializeData();

        // Assert
        verify(logger).debug("Categories list is empty!");
    }

    @Test
    void initializeCategories_NotSuccessInit_ThrowsWithNullPointer() {
        // Arrange
        StorageManager storageManager = mock(StorageManager.class);
        ReadKudaGo<Location> locationsReader = mock(ReadKudaGo.class);
        DataInitializer dataInitializer = new DataInitializer(storageManager, null, locationsReader);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> dataInitializer.initializeData());
    }

}
