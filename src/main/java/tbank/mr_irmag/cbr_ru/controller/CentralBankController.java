package tbank.mr_irmag.cbr_ru.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tbank.mr_irmag.cbr_ru.DTO.ConvertCurrencyRequest;
import tbank.mr_irmag.cbr_ru.DTO.ConvertCurrencyResponse;
import tbank.mr_irmag.cbr_ru.DTO.CurrencyExchangeRateResponse;
import tbank.mr_irmag.cbr_ru.service.CentralBankService;

@RestController
@RequestMapping("/currencies")
@Tag(name = "CentralBankController", description = "API для обработки курсов валют и конвертации")
@Log4j2
public class CentralBankController {

    private final CentralBankService centralBankService;

    @Autowired
    public CentralBankController(CentralBankService centralBankService) {
        this.centralBankService = centralBankService;
    }

    @GetMapping("/rates/{code}")
    @Operation(summary = "Получить курс валюты", description = "Возвращает текущий курс валюты по коду")
    public ResponseEntity<CurrencyExchangeRateResponse> getCurrencyExchangeRate(
            @Valid @PathVariable("code") String code) throws Exception {

        double currencyValue = centralBankService.getValueOfCurrencyByCode(code);
        CurrencyExchangeRateResponse response = new CurrencyExchangeRateResponse(code, currencyValue);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/convert")
    @Operation(summary = "Конвертировать валюту", description = "Конвертирует одну валюту в другую на основе текущих курсов")
    public ResponseEntity<ConvertCurrencyResponse> convertCurrency(
            @Valid @RequestBody ConvertCurrencyRequest request) throws Exception {

        double convertedAmount = centralBankService.convertCurrency(request);
        ConvertCurrencyResponse response = new ConvertCurrencyResponse(
                request.getFromCurrency(),
                request.getToCurrency(),
                request.getAmount(),
                convertedAmount
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


