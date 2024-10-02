package tbank.mr_irmag.tbank_kudago_task.exception;

import java.io.Serializable;

public class CategoryIdAlreadyExistsException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Category Id already exists!";

    public CategoryIdAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public CategoryIdAlreadyExistsException(String message) {
        super(message);
    }

    public CategoryIdAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryIdAlreadyExistsException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
