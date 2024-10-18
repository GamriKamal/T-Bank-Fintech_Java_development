package tbank.mr_irmag.cbr_ru.exception;

public class CurrencyFromAndToAreEqualException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Currency from and currency to are equal!";

    public CurrencyFromAndToAreEqualException() {
        super(DEFAULT_MESSAGE);
    }

    public CurrencyFromAndToAreEqualException(String message) {
        super(message);
    }

    public CurrencyFromAndToAreEqualException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyFromAndToAreEqualException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
