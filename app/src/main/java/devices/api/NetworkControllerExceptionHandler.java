package devices.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import devices.api.NetworkController.InvalidDeviceRegistrationParameters;
import devices.api.model.ErrorResponse;
import devices.network.NetworkDeployment.CyclicUplinkReferenceException;
import devices.network.NetworkDeployment.DuplicateDeviceException;

@ControllerAdvice
public class NetworkControllerExceptionHandler {

    @ExceptionHandler(DuplicateDeviceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDeviceException(DuplicateDeviceException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Bad Request",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CyclicUplinkReferenceException.class)
    public ResponseEntity<ErrorResponse> handleCyclicUplinkReferenceException(CyclicUplinkReferenceException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDeviceRegistrationParameters.class)
    public ResponseEntity<ErrorResponse> handleBadParameters(InvalidDeviceRegistrationParameters ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
