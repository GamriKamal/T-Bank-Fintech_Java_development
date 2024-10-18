package tbank.mr_irmag.cbr_ru.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tbank.mr_irmag.cbr_ru.DTO.ConvertCurrencyRequest;
import tbank.mr_irmag.cbr_ru.exception.CurrencyConversionException;
import tbank.mr_irmag.cbr_ru.exception.CurrencyFromAndToAreEqualException;

import java.text.DecimalFormat;

@Service
@Slf4j
@Validated
public class CentralBankService {
    private final CurrencyApiService apiService;
    private final CurrencyParserService parserService;
    private final DecimalFormat df = new DecimalFormat("###.###");

    @Autowired
    public CentralBankService(CurrencyApiService apiService, CurrencyParserService parserService) {
        this.apiService = apiService;
        this.parserService = parserService;
    }

    public double getValueOfCurrencyByCode(String code) throws Exception {
        String currencyData = apiService.fetchCurrencyDataWithCode(code);
        String result = parserService.getCurrencyValueByCode(code, currencyData);
        return Double.parseDouble(result.replace(",", "."));
    }

    public double convertCurrency(ConvertCurrencyRequest request) throws Exception {

        if (request.getFromCurrency() == null) {
            throw new CurrencyConversionException("From currency cannot be null!", "fromCurrency");
        }

        if (request.getToCurrency() == null) {
            throw new CurrencyConversionException("To currency cannot be null!", "toCurrency");
        }

        if (request.getFromCurrency().equals(request.getToCurrency())) {
            log.error("Currency from and currency to are equal! {} {}", request.getFromCurrency(), request.getToCurrency());
            throw new CurrencyFromAndToAreEqualException("Currency from and currency to are equal! " + request.getFromCurrency() + request.getToCurrency());
        }

        String currencyData = apiService.fetchCurrencyDataWithRequest(request);

        double result = calculateConversion(request, currencyData);
        return result;
    }

    private double calculateConversion(ConvertCurrencyRequest request, String currencyData) {

        if (request.getToCurrency().equals("RUB")) {
            String currencyValue = parserService.getCurrencyValueByCode(request.getFromCurrency(), currencyData);

            double currencyInNum = Double.parseDouble(currencyValue.replace(",", "."));
            double resultValue = currencyInNum * request.getAmount();
            return resultValue;
        } else {
            String fromCurrencyValue = parserService.getCurrencyValueByCode(request.getFromCurrency(), currencyData);
            String toCurrencyValue = parserService.getCurrencyValueByCode(request.getToCurrency(), currencyData);

            double fromValue = Double.parseDouble(fromCurrencyValue.replace(",", "."));
            double toValue = Double.parseDouble(toCurrencyValue.replace(",", "."));
            double resultValue = (fromValue / toValue) * request.getAmount();
            return Double.parseDouble(df.format(resultValue).replace(",", "."));
        }
    }

}
