package tbank.mr_irmag.tbank_kudago_task.exception;

import java.io.Serializable;

public class LocationSlugAlreadyExistsException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "Location slug already exists!";

    public LocationSlugAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public LocationSlugAlreadyExistsException(String message) {
        super(message);
    }

    public LocationSlugAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocationSlugAlreadyExistsException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
