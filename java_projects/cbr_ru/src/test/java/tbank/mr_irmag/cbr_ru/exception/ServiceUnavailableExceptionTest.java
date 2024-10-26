package tbank.mr_irmag.cbr_ru.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceUnavailableExceptionTest {

    @Test
    void constructor_ShouldReturnCorrectMessage() {
        // Arrange
        String customMessage = "Service is currently unavailable";

        // Act
        ServiceUnavailableException exception = new ServiceUnavailableException(customMessage);

        // Assert
        assertEquals(customMessage, exception.getMessage());
    }
}
