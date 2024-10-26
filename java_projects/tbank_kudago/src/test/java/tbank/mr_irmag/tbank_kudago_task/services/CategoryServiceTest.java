package tbank.mr_irmag.tbank_kudago_task.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tbank.mr_irmag.tbank_kudago_task.component.StorageManager;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.ParameterizedStorage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private StorageManager storageManager;

    @Mock
    private ParameterizedStorage<Integer, Category> categoriesStorage;

    @InjectMocks
    private CategoriesService categoriesService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(storageManager.getCategoriesStorage()).thenReturn(categoriesStorage);
    }

    @Test
    void getAllCategories_SuccessReturningCollection_ShouldReturnListOfCategories() {
        // Arrange
        Category category1 = new Category(1, "test-slug", "Test Category");
        Category category2 = new Category(2, "test-slug2", "Test Category2");
        Category category3 = new Category(3, "test-slug3", "Test Category3");

        ConcurrentHashMap<Integer, Category> mockMap = new ConcurrentHashMap<>();
        mockMap.put(category1.getId(), category1);
        mockMap.put(category2.getId(), category2);
        mockMap.put(category3.getId(), category3);

        when(categoriesStorage.getHashMap()).thenReturn(mockMap);

        // Act
        List<Category> list = categoriesService.getAllCategories();

        // Assert
        assertNotNull(list);
        assertEquals(3, list.size());
        assertTrue(list.contains(category1));
        assertTrue(list.contains(category2));
        assertTrue(list.contains(category3));
    }

    @Test
    void getAllCategories_EmptyStorage_ShouldReturnEmptyList() {
        // Arrange
        ConcurrentHashMap<Integer, Category> mockMap = new ConcurrentHashMap<>();
        when(categoriesStorage.getHashMap()).thenReturn(mockMap);
        when(storageManager.getCategoriesStorage()).thenReturn(null);

        // Act
        List<Category> list = categoriesService.getAllCategories();

        // Assert
        assertNull(list);
    }

    @Test
    void getCategoryById_SuccessReturn_ShouldReturnItemByID() {
        // Arrange
        Category category = new Category(1, "test-slug", "Test Category");
        ConcurrentHashMap<Integer, Category> mockMap = new ConcurrentHashMap<>();
        mockMap.put(1, category);
        when(categoriesStorage.getEntry()).thenReturn(mockMap.entrySet());

        // Act
        Category category1 = categoriesService.getCategoryById(1);

        // Assert
        assertEquals(category.getName(), category1.getName());
    }

    @Test
    void getCategoryById_NotSuccessReturn_ShouldNotReturnItemByID() {
        // Arrange
        ConcurrentHashMap<Integer, Category> mockMap = new ConcurrentHashMap<>();
        when(categoriesStorage.getEntry()).thenReturn(mockMap.entrySet());

        //Act and Assert
        assertThrows(NullPointerException.class, () -> categoriesService.getCategoryById(1));
    }

    @Test
    void createCategory_SuccessCreation_ShouldCreate() {
        // Arrange
        Category category = new Category(1, "test-slug", "Test Category");
        doNothing().when(categoriesStorage).put(anyInt(), any(Category.class));
        when(categoriesStorage.get(category.getId())).thenReturn(category);

        // Act
        Category result = categoriesService.createCategory(category);

        // Assert
        assertNotNull(result);
        assertEquals(category, result);
        verify(categoriesStorage).put(category.getId(), category);
    }

    @Test
    void createCategory_NotSuccessCreation_ReturnNull() {
        // Arrange
        Category category = new Category(1, "testId", "testName");
        when(categoriesService.createCategory(category)).thenReturn(null);

        // Act
        Category result = categoriesService.createCategory(category);

        // Assert
        assertNull(result);
    }

    @Test
    void createCategory_GetThrowsNoSuchElementException_ShouldLogErrorAndThrowException() {
        // Arrange
        Category category = new Category(1, "test-slug", "Test Category");

        doNothing().when(categoriesStorage).put(anyInt(), any(Category.class));
        when(categoriesStorage.get(category.getId())).thenThrow(new NoSuchElementException("Category not found"));

        // Act & Assert
        NoSuchElementException thrownException = assertThrows(NoSuchElementException.class, () -> {
            categoriesService.createCategory(category);
        });

        assertEquals("Error when adding a category: " + category, thrownException.getMessage());
    }

    @Test
    void updateLocation_GetUpdatedLocationSuccess_ShouldReturnUpdatedLocation() {
        // Arrange
        String slug = "location-1";
        String newName = "New Location Name";
        Category existingCategory = new Category(1, slug, "Old Location Name");
        Category expectedLocation = new Category(1, slug, newName);

        ConcurrentHashMap<Integer, Category> mockMap = new ConcurrentHashMap<>();
        mockMap.put(1, existingCategory);
        when(categoriesStorage.getEntry()).thenReturn(mockMap.entrySet());
        when(storageManager.getCategoriesStorage().get(1)).thenReturn(expectedLocation);

        // Act
        Category updatedCategory = categoriesService.updateCategory(1, new Location(slug, newName));

        // Assert
        assertEquals(slug, updatedCategory.getSlug());
        assertEquals(newName, updatedCategory.getName());
        verify(storageManager.getCategoriesStorage()).put(1, expectedLocation);
    }

    @Test
    void updateCategory_GetUpdatedLocationNotSuccess_ThrowNullPointerException() {
        // Arrange
        String slug = "location-1";
        String newName = "New Location Name";
        when(categoriesStorage.getEntry()).thenReturn(null);

        // Act
        NullPointerException exception =
                assertThrows(NullPointerException.class, () ->
                        categoriesService.updateCategory(1, new Location(slug, newName)));
        // Assert
        assertEquals("Error! The category was not found!", exception.getMessage());
    }

    @Test
    void deleteCategoryById_SuccessfullyDeleteCategory_ShouldDeleteCategory() {
        // Arrange
        Category category = new Category(1, "test-slug", "Test Category");
        ConcurrentHashMap<Integer, Category> mockMap = new ConcurrentHashMap<>();
        mockMap.put(1, category);
        when(categoriesStorage.containsKey(1)).thenReturn(true);

        // Act
        boolean answer = categoriesService.deleteCategoryById(1);

        // Assert
        assertTrue(answer);
    }


    @Test
    void deleteCategoryById_NotSuccess_ThrowsCategoryNotFoundException() {
        // Arrange
        int nonExistentId = 1;
        when(storageManager.getCategoriesStorage()).thenReturn(new ParameterizedStorage<>());

        // Act
        NullPointerException exception = assertThrows(NullPointerException.class, () -> categoriesService.deleteCategoryById(nonExistentId));

        // Assert
        assertEquals("Error! The category with this ID was not found: " + nonExistentId, exception.getMessage());
    }

}
