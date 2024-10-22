package tbank.mr_irmag.tbank_kudago_task.exceptions.EventExceptions;

public class EventAlreadyExistsException extends RuntimeException {
    private static final String defaultMessage = "Entity already exist!";

    public EventAlreadyExistsException() {
        super(defaultMessage);
    }

    public EventAlreadyExistsException(String message) {
        super(message);
    }
}
