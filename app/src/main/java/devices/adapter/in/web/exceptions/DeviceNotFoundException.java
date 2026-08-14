package devices.adapter.in.web.exceptions;

import devices.domain.exception.DomainException;

public class DeviceNotFoundException extends DomainException     {

    private static final String ERROR_MESSAGE = "device not found";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
