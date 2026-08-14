package devices.domain;

import devices.domain.exception.InvalidMacAddressException;

public record MacAddress(String value) {
    public static final String ROOT_ADDRESS = "root";

    public MacAddress {
        if (value != null) {
            if (!ROOT_ADDRESS.equals(value)) {
                if (!value.toUpperCase().matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
                    throw new InvalidMacAddressException();
                }
            }
        }
    }
}
