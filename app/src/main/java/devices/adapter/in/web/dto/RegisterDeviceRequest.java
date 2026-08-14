package devices.adapter.in.web.dto;

import devices.domain.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDeviceRequest(
        @NotBlank(message = RegisterDeviceRequest.MAC_ADDRESS_REQUIRED)
        String macAddress,

        @NotNull(message = RegisterDeviceRequest.TYPE_REQUIRED)
        DeviceType type,

        String uplinkMacAddress
) {
        public static final String MAC_ADDRESS_REQUIRED = "mac address is required";
        public static final String TYPE_REQUIRED = "device type is required";
}
