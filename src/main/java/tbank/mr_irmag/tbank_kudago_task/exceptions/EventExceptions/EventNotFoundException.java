package tbank.mr_irmag.tbank_kudago_task.exceptions.EventExceptions;

public class EventNotFoundException extends RuntimeException {
    private static final String defaultMessage = "Event not found!";

    public EventNotFoundException() {
        super(defaultMessage);
    }


    public EventNotFoundException(String message) {
        super(message);
    }
}
