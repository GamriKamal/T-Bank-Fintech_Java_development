package tbank.mr_irmag.cbr_ru.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyNotFoundExceptionTest {

    @Test
    void defaultConstructor_ShouldReturnDefaultMessage() {
        // Act
        CurrencyNotFoundException exception = new CurrencyNotFoundException();

        // Assert
        assertEquals("Currency by code is not exists!", exception.getMessage());
    }

    @Test
    void constructorWithMessage_ShouldReturnCustomMessage() {
        // Arrange
        String customMessage = "Custom currency not found message";

        // Act
        CurrencyNotFoundException exception = new CurrencyNotFoundException(customMessage);

        // Assert
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void constructorWithMessageAndCause_ShouldReturnCustomMessage() {
        // Arrange
        String customMessage = "Custom currency not found message";
        Throwable cause = new Exception("Underlying cause");

        // Act
        CurrencyNotFoundException exception = new CurrencyNotFoundException(customMessage, cause);

        // Assert
        assertEquals(customMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void constructorWithCause_ShouldReturnDefaultMessage() {
        // Arrange
        Throwable cause = new Exception("Underlying cause");

        // Act
        CurrencyNotFoundException exception = new CurrencyNotFoundException(cause);

        // Assert
        assertEquals("Currency by code is not exists!", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
