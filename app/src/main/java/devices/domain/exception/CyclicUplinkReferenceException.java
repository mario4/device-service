package devices.domain.exception;

public class CyclicUplinkReferenceException extends DomainException {

    public static final String ERROR_MESSAGE = "Cyclic device connection is not accepted in network topology";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
