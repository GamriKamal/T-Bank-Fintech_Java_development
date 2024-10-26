package tbank.mr_irmag.cbr_ru.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyFromAndToAreEqualExceptionTest {

    @Test
    void defaultConstructor_ShouldReturnDefaultMessage() {
        // Act
        CurrencyFromAndToAreEqualException exception = new CurrencyFromAndToAreEqualException();

        // Assert
        assertEquals("Currency from and currency to are equal!", exception.getMessage());
    }

    @Test
    void messageConstructor_ShouldReturnCustomMessage() {
        // Arrange
        String customMessage = "Custom error message";

        // Act
        CurrencyFromAndToAreEqualException exception = new CurrencyFromAndToAreEqualException(customMessage);

        // Assert
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void messageAndCauseConstructor_ShouldReturnCustomMessageAndCause() {
        // Arrange
        String customMessage = "Custom error message";
        Throwable cause = new Exception("Underlying cause");

        // Act
        CurrencyFromAndToAreEqualException exception = new CurrencyFromAndToAreEqualException(customMessage, cause);

        // Assert
        assertEquals(customMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void causeConstructor_ShouldReturnDefaultMessageAndCause() {
        // Arrange
        Throwable cause = new Exception("Underlying cause");

        // Act
        CurrencyFromAndToAreEqualException exception = new CurrencyFromAndToAreEqualException(cause);

        // Assert
        assertEquals("Currency from and currency to are equal!", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
