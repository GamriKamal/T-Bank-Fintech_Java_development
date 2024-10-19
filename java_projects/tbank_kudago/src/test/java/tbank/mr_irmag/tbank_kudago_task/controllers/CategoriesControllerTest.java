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
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.services.CategoriesService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoriesController.class)
class CategoriesControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @MockBean
    private CategoriesService categoriesService;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_SuccessResponse_GetJsonResponse() throws Exception {
        // Arrange
        List<Category> categories = List.of(
                new Category(1, "slug1", "name1"),
                new Category(2, "slug2", "name2"));
        when(categoriesService.getAllCategories()).thenReturn(categories);

        // Act
        mockMvc.perform(get("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categories)));

        // Assert
        verify(categoriesService, times(1)).getAllCategories();
    }

    @Test
    void getAllCategories_FailureServiceException_ReturnsBadRequest() throws Exception {
        // Arrange
        when(categoriesService.getAllCategories()).thenThrow(new NullPointerException("Service Error"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Service Error"));

        verify(categoriesService, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById_SuccessResponse_GetItemById() throws Exception {
        // Arrange
        int categoryId = 1;
        Category category = new Category(1, "slug1", "name1");
        when(categoriesService.getCategoryById(categoryId)).thenReturn(category);

        // Act
        mockMvc.perform(get("/api/v1/places/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully retrieved item: " + category));

        // Assert
        verify(categoriesService, times(1)).getCategoryById(categoryId);
    }

    @Test
    void getCategoryById_FailureInvalidId_ReturnsBadRequest() throws Exception {
        // Act
        int invalidId = 999;
        when(categoriesService.getCategoryById(invalidId)).thenThrow(new NullPointerException("Category not found"));

        // Act
        mockMvc.perform(get("/api/v1/places/categories/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something get wrong while adding item in storage: Category not found"));

        // Assert
        verify(categoriesService, times(1)).getCategoryById(invalidId);
    }

    @Test
    void createCategory_SuccessCreation_GetOkStatus() throws Exception {
        // Arrange
        Category category = new Category(1, "slug1", "name1");
        when(categoriesService.createCategory(any(Category.class))).thenReturn(category);

        // Act
        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully added item: " + category));

        // Assert
        verify(categoriesService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void createCategory_FailureInvalidInput_ReturnsBadRequest() throws Exception {
        // Arrange
        Category invalidCategory = new Category();
        when(categoriesService.createCategory(any(Category.class))).thenThrow(new NoSuchElementException("Invalid Category"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something get wrong while adding item in storage: Invalid Category"));

        verify(categoriesService, times(1)).createCategory(any(Category.class));
    }


    @Test
    void updateCategoryById_SuccessUpdatedItem_UpdateItemById() throws Exception {
        // Arrange
        int categoryId = 1;
        Location location = new Location("slug", "name");
        Category updatedCategory = new Category(categoryId, "slug1", "name1");
        when(categoriesService.updateCategory(eq(categoryId), any(Location.class))).thenReturn(updatedCategory);

        // Act
        mockMvc.perform(put("/api/v1/places/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isOk())
                .andExpect(content().string("Succsessfully updated item: " + updatedCategory));

        // Assert
        verify(categoriesService, times(1)).updateCategory(eq(categoryId), any(Location.class));
    }

    @Test
    void updateCategoryById_FailureNonExistentId_ReturnsBadRequest() throws Exception {
        // Arrange
        int nonExistentId = 999;
        Location location = new Location("slug", "name");
        when(categoriesService.updateCategory(eq(nonExistentId), any(Location.class))).thenThrow(new NullPointerException("Category not found"));

        //Act
        mockMvc.perform(put("/api/v1/places/categories/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something get wrong while updating item in storage:Category not found"));

        // Assert
        verify(categoriesService, times(1)).updateCategory(eq(nonExistentId), any(Location.class));
    }

    @Test
    void deleteCategoryById_SuccessDeletedItem_RemoveItemById() throws Exception {
        // Arrange
        int categoryId = 1;
        when(categoriesService.deleteCategoryById(categoryId)).thenReturn(true);

        // Act
        mockMvc.perform(delete("/api/v1/places/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Succsessfully deleted item by id: " + categoryId));

        // Assert
        verify(categoriesService, times(1)).deleteCategoryById(categoryId);
    }

    @Test
    void deleteCategoryById_FailureCategoryNotFound_ReturnsBadRequest() throws Exception {
        // Arrange
        int nonExistentId = 1;
        doThrow(new RuntimeException("Category not found")).when(categoriesService).deleteCategoryById(nonExistentId);

        // Act
        mockMvc.perform(delete("/api/v1/places/categories/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something get wrong while deleting item in storage: Category not found"));

        // Assert
        verify(categoriesService, times(1)).deleteCategoryById(nonExistentId);
    }

}