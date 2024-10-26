package tbank.mr_irmag.cbr_ru.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tbank.mr_irmag.cbr_ru.exception.CurrencyNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CurrencyParserServiceTest {

    private final String validXML = """
            <ValCurs Date="02.03.2002" name="Foreign Currency Market">
                 <Valute ID="R01010">
                 <NumCode>036</NumCode>
                 <CharCode>AUD</CharCode>
                 <Nominal>1</Nominal>
                 <Name>Австралийский доллар</Name>
                 <Value>16,0102</Value>
                 <VunitRate>16,0102</VunitRate>
                 </Valute>
                <Valute ID="R01035">
                 <NumCode>826</NumCode>
                 <CharCode>GBP</CharCode>
                 <Nominal>1</Nominal>
                 <Name>Фунт стерлингов Соединенного королевства</Name>
                 <Value>43,8254</Value>
                 <VunitRate>43,8254</VunitRate>
                 </Valute>
            </ValCurs>
            """;
    @InjectMocks
    private CurrencyParserService currencyParserService;
    @Mock
    private ParseXML parseXML;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrencyValueByCode_PositiveCase_ShouldReturnCurrencyValue() throws Exception {
        // Arrange
        String code = "AUD";
        String expectedValue = "16,0102";
        when(parseXML.getValueOfCode(code, validXML)).thenReturn(expectedValue);

        // Act
        String result = currencyParserService.getCurrencyValueByCode(code, validXML);

        // Assert
        assertEquals(expectedValue, result);
    }

    @Test
    void getCurrencyValueByCode_NegativeCase_ShouldThrowCurrencyNotFoundException() {
        // Arrange
        String code = "DKK";
        when(parseXML.getValueOfCode(code, validXML)).thenReturn(null);

        // Act & Assert
        CurrencyNotFoundException exception = assertThrows(CurrencyNotFoundException.class, () -> {
            currencyParserService.getCurrencyValueByCode(code, validXML);
        });

        assertEquals("Currency not found by code: DKK", exception.getMessage());
    }

    @Test
    void getCurrencyValueByCode_NegativeCase_ShouldThrowCurrencyNotFoundExceptionForBlankResult() {
        // Arrange
        String code = "AUD";
        when(parseXML.getValueOfCode(code, validXML)).thenReturn("  ");

        // Act & Assert
        CurrencyNotFoundException exception = assertThrows(CurrencyNotFoundException.class, () -> {
            currencyParserService.getCurrencyValueByCode(code, validXML);
        });

        assertEquals("Currency not found by code: AUD", exception.getMessage());
    }
}
