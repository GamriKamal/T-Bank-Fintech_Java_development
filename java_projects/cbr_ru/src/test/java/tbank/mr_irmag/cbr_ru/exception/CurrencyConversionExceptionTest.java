package tbank.mr_irmag.cbr_ru.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyConversionExceptionTest {

    @Test
    void constructor_ShouldReturnCorrectMessageAndFieldName() {
        // Arrange
        String expectedMessage = "From currency cannot be null!";
        String expectedFieldName = "fromCurrency";

        // Act
        CurrencyConversionException exception = new CurrencyConversionException(expectedMessage, expectedFieldName);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedFieldName, exception.getFieldName());
    }
}
