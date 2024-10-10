package tbank.mr_irmag.cbr_ru.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tbank.mr_irmag.cbr_ru.DTO.ConvertCurrencyRequest;
import tbank.mr_irmag.cbr_ru.exception.CurrencyNotFoundException;
import tbank.mr_irmag.cbr_ru.exception.ServiceUnavailableException;
import tbank.mr_irmag.cbr_ru.service.CentralBankService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CentralBankController.class)
public class CentralBankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CentralBankService centralBankService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCurrencyExchangeRate_SuccessGiveCurrency_ShouldGiveValidCode() throws Exception {
        // Arrange
        String currencyCode = "USD";
        double rate = 75.00;
        Mockito.when(centralBankService.getValueOfCurrencyByCode(currencyCode)).thenReturn(rate);

        // Act & Assert
        mockMvc.perform(get("/currencies/rates/{code}", currencyCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value(currencyCode))
                .andExpect(jsonPath("$.rate").value(rate));
    }

    @Test
    public void getCurrencyExchangeRate_NotSuccess_ShouldReturnInvalidCode() throws Exception {
        // Arrange
        String invalidCode = "INVALID";
        Mockito.when(centralBankService.getValueOfCurrencyByCode(invalidCode)).thenThrow(new CurrencyNotFoundException("Invalid currency code"));

        // Act & Assert
        mockMvc.perform(get("/currencies/rates/{code}", invalidCode))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void convertCurrency_NotSuccess_InvalidRequest() throws Exception {
        // Arrange
        ConvertCurrencyRequest request = new ConvertCurrencyRequest();
        request.setFromCurrency("USD");
        request.setToCurrency("EUR");
        request.setAmount(-100.0);

        // Act & Assert
        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(centralBankService, never()).convertCurrency(any());
    }

    @Test
    public void convertCurrency_SuccessConvertingCurrency_ShouldReturnValidRequest() throws Exception {
        // Arrange
        ConvertCurrencyRequest request = new ConvertCurrencyRequest();
        request.setFromCurrency("USD");
        request.setToCurrency("EUR");
        request.setAmount(100.0);

        double convertedAmount = 85.00;
        Mockito.when(centralBankService.convertCurrency(request)).thenReturn(convertedAmount);

        // Act & Assert
        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCurrency").value(request.getFromCurrency()))
                .andExpect(jsonPath("$.toCurrency").value(request.getToCurrency()))
                .andExpect(jsonPath("$.convertedAmount").value(convertedAmount));
    }


    @Test
    void handleServiceUnavailableException_SuccessHandleException_ShouldThrowsExcpetion() throws Exception {
        // Arrange
        Mockito.when(centralBankService.convertCurrency(any(ConvertCurrencyRequest.class)))
                .thenThrow(new ServiceUnavailableException("Service is temporarily unavailable"));

        ConvertCurrencyRequest request = new ConvertCurrencyRequest();
        request.setFromCurrency("USD");
        request.setToCurrency("EUR");
        request.setAmount(100.0);

        // Act & Assert
        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(header().string("Retry-After", "3600"))
                .andExpect(jsonPath("$.message").value("Service is temporarily unavailable"))
                .andExpect(jsonPath("$.code").value(HttpStatus.SERVICE_UNAVAILABLE.value()));
    }
}
