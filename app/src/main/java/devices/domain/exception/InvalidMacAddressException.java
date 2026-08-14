package devices.domain.exception;

public class InvalidMacAddressException extends DomainException{

    @Override
    public String getMessage() {
        return "Invalid MAC Address format";
    }
}