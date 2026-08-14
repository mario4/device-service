package devices.domain.exception;

public final class DuplicateDeviceException extends DomainException {

    public static final String ERROR_MESSAGE = "A device with the same macAddress is already deployed to network";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}