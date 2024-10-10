package tbank.mr_irmag.cbr_ru.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import tbank.mr_irmag.cbr_ru.DTO.ConvertCurrencyRequest;
import tbank.mr_irmag.cbr_ru.exception.ServiceUnavailableException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CurrencyApiServiceTest {

    private final String currencyCodesUrl = "https://example.com/currency-codes";
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private CurrencyApiService currencyApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyApiService.setRestTemplate(restTemplate);
        ReflectionTestUtils.setField(currencyApiService, "currencyCodesUrl", currencyCodesUrl);
    }

    @Test
    void fetchCurrencyDataWithRequest_PositiveCase_ShouldReturnCurrencyData() throws Exception {
        // Arrange
        ConvertCurrencyRequest request = new ConvertCurrencyRequest("USD", "RUB", 100.0);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("USD", HttpStatus.OK);
        when(restTemplate.getForEntity(currencyCodesUrl, String.class)).thenReturn(responseEntity);

        // Act
        String result = currencyApiService.fetchCurrencyDataWithRequest(request);

        // Assert
        assertEquals("USD", result);
    }

    @Test
    void fetchCurrencyDataWithRequest_NegativeCase_ServiceUnavailable_ShouldThrowException() {
        // Arrange
        ConvertCurrencyRequest request = new ConvertCurrencyRequest("USD", "RUB", 100.0);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        when(restTemplate.getForEntity(currencyCodesUrl, String.class)).thenReturn(responseEntity);

        // Act & Assert
        Exception exception = assertThrows(ServiceUnavailableException.class, () -> {
            currencyApiService.fetchCurrencyDataWithRequest(request);
        });

        assertEquals("Currency service is currently unavailable", exception.getMessage());
    }

    @Test
    void fetchCurrencyDataWithCode_PositiveCase_ShouldReturnCurrencyData() throws Exception {
        // Arrange
        String code = "USD";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Currency Data", HttpStatus.OK);
        when(restTemplate.getForEntity(currencyCodesUrl, String.class)).thenReturn(responseEntity);

        // Act
        String result = currencyApiService.fetchCurrencyDataWithCode(code);

        // Assert
        assertEquals("Currency Data", result);
    }

    @Test
    void fetchCurrencyDataWithCode_NegativeCase_NullCode_ShouldThrowNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            currencyApiService.fetchCurrencyDataWithCode(null);
        });
    }

    @Test
    void fetchCurrencyDataWithCode_NegativeCase_ServiceUnavailable_ShouldThrowException() {
        // Arrange
        String code = "USD";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        when(restTemplate.getForEntity(currencyCodesUrl, String.class)).thenReturn(responseEntity);

        // Act & Assert
        Exception exception = assertThrows(ServiceUnavailableException.class, () -> {
            currencyApiService.fetchCurrencyDataWithCode(code);
        });

        assertEquals("Currency service is currently unavailable", exception.getMessage());
    }

    @Test
    void fallbackFetchCurrencyData_ShouldReturnFallbackMessage() {
        // Act
        String result = currencyApiService.fallbackFetchCurrencyData("USD", "EUR", new Exception("Some error"));

        // Assert
        assertEquals("Currency data is currently unavailable. Please try again later.", result);
    }
}
