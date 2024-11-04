package tbank.mr_irmag.tbank_kudago_task.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tbank.mr_irmag.tbank_kudago_task.domain.dto.ErrorResponse;
import tbank.mr_irmag.tbank_kudago_task.exceptions.PlaceException.InvalidPlaceException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.PlaceException.PlaceAlreadyExistsException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.PlaceException.PlaceNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
@Order(2)
public class PlaceExceptionHandler {
    @ExceptionHandler(InvalidPlaceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPlaceException(InvalidPlaceException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlaceNotFoundException(PlaceNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlaceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePlaceAlreadyExistsException(PlaceAlreadyExistsException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getLocalizedMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
