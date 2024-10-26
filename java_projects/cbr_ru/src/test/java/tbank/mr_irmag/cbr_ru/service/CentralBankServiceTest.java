package tbank.mr_irmag.cbr_ru.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tbank.mr_irmag.cbr_ru.DTO.ConvertCurrencyRequest;
import tbank.mr_irmag.cbr_ru.exception.CurrencyConversionException;
import tbank.mr_irmag.cbr_ru.exception.CurrencyFromAndToAreEqualException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CentralBankServiceTest {

    @Mock
    private CurrencyApiService apiService;

    @Mock
    private CurrencyParserService parserService;

    @InjectMocks
    private CentralBankService centralBankService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getValueOfCurrencyByCode_ValidCode_ReturnsValue() throws Exception {
        // Arrange
        String currencyCode = "USD";
        String expectedCurrencyData = "some_currency_data";
        String expectedValueForgetCurrencyValueByCode = "70.0";
        double expectedValue = 70.0;
        when(apiService.fetchCurrencyDataWithCode(currencyCode)).thenReturn(expectedCurrencyData);
        when(parserService.getCurrencyValueByCode(currencyCode, expectedCurrencyData)).thenReturn(expectedValueForgetCurrencyValueByCode);

        // Act
        double actualValue = centralBankService.getValueOfCurrencyByCode(currencyCode);

        // Assert
        assertEquals(expectedValue, actualValue);
        verify(apiService).fetchCurrencyDataWithCode(currencyCode);
        verify(parserService).getCurrencyValueByCode(currencyCode, expectedCurrencyData);
    }

    @Test
    void getValueOfCurrencyByCode_InvalidCode_ThrowsException() throws Exception {
        // Arrange
        String currencyCode = "INVALID_CODE";
        when(apiService.fetchCurrencyDataWithCode(currencyCode)).thenThrow(new Exception("Invalid currency code"));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            centralBankService.getValueOfCurrencyByCode(currencyCode);
        });

        assertEquals("Invalid currency code", exception.getMessage());
        verify(apiService).fetchCurrencyDataWithCode(currencyCode);
    }

    @Test
    void convertCurrency_ValidRequest_ReturnsConvertedAmount() throws Exception {
        // Arrange
        ConvertCurrencyRequest request = new ConvertCurrencyRequest("USD", "RUB", 100.0);
        String currencyData = "some_currency_data";
        String usdValue = "70.00";

        when(apiService.fetchCurrencyDataWithRequest(request)).thenReturn(currencyData);
        when(parserService.getCurrencyValueByCode("USD", currencyData)).thenReturn(usdValue);

        // Act
        double result = centralBankService.convertCurrency(request);

        // Assert
        assertEquals(7000.00000, result);
        verify(apiService).fetchCurrencyDataWithRequest(request);
        verify(parserService).getCurrencyValueByCode("USD", currencyData);
    }

    @Test
    void convertCurrency_FromAndToAreEqual_ThrowsException() {
        // Arrange
        ConvertCurrencyRequest request = new ConvertCurrencyRequest("USD", "USD", 100.0);

        // Act & Assert
        CurrencyFromAndToAreEqualException exception = assertThrows(CurrencyFromAndToAreEqualException.class, () -> {
            centralBankService.convertCurrency(request);
        });

        assertEquals("Currency from and currency to are equal! USDUSD", exception.getMessage());
    }

    @Test
    void convertCurrency_NegativeCase_FromCurrencyIsNull_ShouldThrowCurrencyConversionException() {
        // Arrange
        ConvertCurrencyRequest request = new ConvertCurrencyRequest(null, "RUB", 100.0);

        // Act & Assert
        CurrencyConversionException exception = assertThrows(CurrencyConversionException.class, () -> {
            centralBankService.convertCurrency(request);
        });

        assertEquals("From currency cannot be null!", exception.getMessage());
        assertEquals("fromCurrency", exception.getFieldName());
    }

    @Test
    void convertCurrency_NegativeCase_ToCurrencyIsNull_ShouldThrowCurrencyConversionException() {
        // Arrange
        ConvertCurrencyRequest request = new ConvertCurrencyRequest("USD", null, 100.0);

        // Act & Assert
        CurrencyConversionException exception = assertThrows(CurrencyConversionException.class, () -> {
            centralBankService.convertCurrency(request);
        });

        assertEquals("To currency cannot be null!", exception.getMessage());
        assertEquals("toCurrency", exception.getFieldName());
    }

    @Test
    void convertCurrency_PositiveCase_ShouldCalculateConversionSuccessfully() throws Exception {
        // Arrange
        ConvertCurrencyRequest request = new ConvertCurrencyRequest("USD", "RUB", 100.0);
        String currencyData = "<ValCurs>...</ValCurs>";  // Mocked currency data
        when(apiService.fetchCurrencyDataWithRequest(request)).thenReturn(currencyData);
        when(parserService.getCurrencyValueByCode("USD", currencyData)).thenReturn("75.50");
        when(parserService.getCurrencyValueByCode("RUB", currencyData)).thenReturn("1.00");

        // Act
        double result = centralBankService.convertCurrency(request);

        // Assert
        assertEquals(7550.0, result);
    }

    @Test
    void convertCurrency_NegativeCase_FromCurrencyEqualsToCurrency_ShouldThrowCurrencyFromAndToAreEqualException() {
        // Arrange
        ConvertCurrencyRequest request = new ConvertCurrencyRequest("USD", "USD", 100.0);

        // Act & Assert
        CurrencyFromAndToAreEqualException exception = assertThrows(CurrencyFromAndToAreEqualException.class, () -> {
            centralBankService.convertCurrency(request);
        });

        assertEquals("Currency from and currency to are equal! USDUSD", exception.getMessage());
    }
}
