package tbank.mr_irmag.tbank_kudago_task.exceptions.EventExceptions;

public class InvalidEventException extends RuntimeException {
    public InvalidEventException() {
    }

    public InvalidEventException(String message) {
        super(message);
    }
}
