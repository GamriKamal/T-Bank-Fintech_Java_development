package tbank.mr_irmag.cbr_ru.exception;

public class CurrencyNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Currency by code is not exists!";

    public CurrencyNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public CurrencyNotFoundException(String message) {
        super(message);
    }

    public CurrencyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
