package devices.adapter.in.web.exceptions;

import devices.adapter.in.web.dto.ErrorResponse;
import devices.domain.exception.CyclicUplinkReferenceException;
import devices.domain.exception.DomainException;
import devices.domain.exception.DuplicateDeviceException;
import devices.domain.exception.InvalidMacAddressException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Map<Class<? extends DomainException>, HttpStatus> statusByException =
            Map.of(
                    DuplicateDeviceException.class, HttpStatus.CONFLICT,
                    DeviceNotFoundException.class, HttpStatus.NOT_FOUND,
                    CyclicUplinkReferenceException.class, HttpStatus.BAD_REQUEST,
                    InvalidMacAddressException.class,HttpStatus.BAD_REQUEST
            );

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
            DomainException ex) {

        HttpStatus status = statusByException.getOrDefault(
                ex.getClass(),
                HttpStatus.BAD_REQUEST
        );

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(new ErrorResponse(400, "Validation failed", details), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse error = new ErrorResponse(
                status.value(),
                "Internal Server Error",
                "An unexpected server error occurred: Please contact support");

        return new ResponseEntity<>(error, status);
    }
}
