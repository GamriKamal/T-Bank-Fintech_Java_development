package tbank.mr_irmag.cbr_ru.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.cbr_ru.exception.CurrencyNotFoundException;

@Service
public class CurrencyParserService {
    private ParseXML parse;

    @Autowired
    public void setParse(ParseXML parse) {
        this.parse = parse;
    }

    public String getCurrencyValueByCode(String code, String xmlData) throws CurrencyNotFoundException {
        String result = parse.getValueOfCode(code, xmlData);
        if (result == null || result.isBlank()) {
            throw new CurrencyNotFoundException("Currency not found by code: " + code);
        }

        return result;
    }
}
