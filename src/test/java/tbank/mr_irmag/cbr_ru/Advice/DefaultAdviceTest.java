package tbank.mr_irmag.cbr_ru.Advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tbank.mr_irmag.cbr_ru.DTO.ErrorResponse;
import tbank.mr_irmag.cbr_ru.exception.CurrencyConversionException;
import tbank.mr_irmag.cbr_ru.exception.CurrencyFromAndToAreEqualException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultAdviceTest {

    private DefaultAdvice defaultAdvice;

    @BeforeEach
    void setUp() {
        defaultAdvice = new DefaultAdvice();
    }


    @Test
    void handleCurrencyConversionException_PositiveCase_ShouldReturnErrorResponseWithCorrectDetails() {
        // Arrange
        String message = "Conversion failed";
        CurrencyConversionException ex = new CurrencyConversionException(message, "amount");

        // Act
        ResponseEntity<ErrorResponse> response = defaultAdvice.handleCurrencyConversionException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Conversion failed", response.getBody().getMessage());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(400, response.getBody().getCode());
    }

    @Test
    void handleCurrencyFromAndToAreEqualException_PositiveCase_ShouldReturnErrorResponseWithCorrectDetails() {
        // Arrange
        String message = "Currencies are the same";
        CurrencyFromAndToAreEqualException ex = new CurrencyFromAndToAreEqualException(message);

        // Act
        ResponseEntity<ErrorResponse> response = defaultAdvice.handleCurrencyFromAndToAreEqualException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Currencies are the same", response.getBody().getMessage());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(400, response.getBody().getCode());
    }

    @Test
    void handleConstraintViolation_PositiveCase_ShouldReturnBadRequestWithErrors() {
        // Arrange
        ConstraintViolation<Object> mockViolation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);

        when(mockViolation.getPropertyPath()).thenReturn(mockPath);
        when(mockPath.toString()).thenReturn("fieldName");
        when(mockViolation.getMessage()).thenReturn("Field must not be empty");

        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        when(ex.getConstraintViolations()).thenReturn(Set.of(mockViolation));

        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("fieldName", "Field must not be empty");

        // Act
        ResponseEntity<Map<String, String>> response = defaultAdvice.handleConstraintViolation(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedErrors, response.getBody());
    }

    @Test
    void handleNullPointerException_PositiveCase_ShouldReturnErrorResponseWithCorrectDetails() {
        // Arrange
        NullPointerException ex = new NullPointerException("This is a null pointer exception");

        // Act
        ResponseEntity<ErrorResponse> response = defaultAdvice.handleNullPointerException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("This is a null pointer exception", response.getBody().getMessage());
        assertEquals(400, response.getBody().getCode());
    }

    @Test
    void handleGeneralException_PositiveCase_ShouldReturnErrorResponseWithCorrectDetails() {
        // Arrange
        Exception ex = new Exception("An unexpected error occurred");

        // Act
        ResponseEntity<ErrorResponse> response = defaultAdvice.handleGeneralException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred An internal error occurred. Please try again later.", response.getBody().getMessage());
        assertEquals(500, response.getBody().getCode());
    }
}
