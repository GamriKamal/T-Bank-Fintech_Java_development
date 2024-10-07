package tbank.mr_irmag.cbr_ru.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

class ParseXMLTest {

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
    private final String invalidXML = "<ValCurs><Valute><CharCode>USD</CharCode>";
    @InjectMocks
    private ParseXML parseXML;

    @BeforeEach
    void setUp() {
        parseXML = new ParseXML();
    }

    @Test
    void getValueOfCode_PositiveCase_ShouldReturnCorrectValue() {
        // Act
        String result = parseXML.getValueOfCode("AUD", validXML);

        // Assert
        assertEquals("16,0102", result);
    }

    @Test
    void getValueOfCode_NegativeCase_ShouldReturnNullIfCodeNotFound() {
        // Act
        String result = parseXML.getValueOfCode("DKK", validXML);

        // Assert
        assertNull(result);
    }

    @Test
    void getValueOfCode_ExceptionCase_ShouldThrowRuntimeExceptionOnInvalidXML() {
        // Assert
        assertThrows(RuntimeException.class, () -> {
            // Act
            parseXML.getValueOfCode("USD", invalidXML);
        });
    }

}
