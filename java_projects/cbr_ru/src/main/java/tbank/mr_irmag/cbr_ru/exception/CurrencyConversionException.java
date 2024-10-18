package tbank.mr_irmag.cbr_ru.exception;

public class CurrencyConversionException extends RuntimeException {
    private final String fieldName;

    public CurrencyConversionException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

