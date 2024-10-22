package tbank.mr_irmag.tbank_kudago_task.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.ErrorResponse;
import tbank.mr_irmag.tbank_kudago_task.exceptions.EventExceptions.EventAlreadyExistsException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.EventExceptions.EventNotFoundException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.EventExceptions.InvalidEventException;

import java.time.LocalDateTime;

@ControllerAdvice
@Order(1)
public class EventExceptionHandler {
    @ExceptionHandler(InvalidEventException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEventException(InvalidEventException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException(EventNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEventAlreadyExistsException(EventAlreadyExistsException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
