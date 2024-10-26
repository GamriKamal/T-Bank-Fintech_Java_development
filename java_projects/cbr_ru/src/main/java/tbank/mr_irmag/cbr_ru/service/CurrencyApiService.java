package tbank.mr_irmag.cbr_ru.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tbank.mr_irmag.cbr_ru.DTO.ConvertCurrencyRequest;
import tbank.mr_irmag.cbr_ru.exception.CurrencyNotFoundException;
import tbank.mr_irmag.cbr_ru.exception.ServiceUnavailableException;

import java.util.Currency;

@Service
@Log4j2
public class CurrencyApiService {
    private RestTemplate restTemplate;
    @Value("${cbr.url.currencyCodes}")
    private String currencyCodesUrl;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "currencyDataCache", key = "#request.fromCurrency + '-' + #request.toCurrency")
    @CircuitBreaker(name = "currencyApiService", fallbackMethod = "fallbackFetchCurrencyData")
    public String fetchCurrencyDataWithRequest(ConvertCurrencyRequest request) throws Exception {

        var response = restTemplate.getForEntity(currencyCodesUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Successfully retrieved response {} {}", response.getHeaders(), response.getStatusCode());
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            throw new ServiceUnavailableException("Currency service is currently unavailable");
        } else {
            throw new Exception("Failed to retrieve currency data");
        }
    }

    @Cacheable(value = "currencyDataCache", key = "#code")
    @CircuitBreaker(name = "currencyApiService", fallbackMethod = "fallbackFetchCurrencyData")
    public String fetchCurrencyDataWithCode(String code) throws Exception {
        if (code == null || code.isBlank()) {
            throw new NullPointerException("Provided currency code is null!");
        }


        var response = restTemplate.getForEntity(currencyCodesUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Successfully retrieved response {} {}", response.getHeaders(), response.getStatusCode());
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            throw new ServiceUnavailableException("Currency service is currently unavailable");
        } else {
            throw new Exception("Failed to retrieve currency data");
        }
    }

    public String fallbackFetchCurrencyData(String fromCurrency, String toCurrency, Exception e) {
        log.warn("Fallback executed due to: {}", e.getMessage());
        return "Currency data is currently unavailable. Please try again later.";
    }
}

