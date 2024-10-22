package tbank.mr_irmag.tbank_kudago_task.exceptions.PlaceException;

public class PlaceAlreadyExistsException extends RuntimeException {
    public PlaceAlreadyExistsException() {
    }

    public PlaceAlreadyExistsException(String message) {
        super(message);
    }
}
