package devices.api.model;

import devices.model.DeviceType;

public class RegisterDeviceRequest {

    private String macAddress;

    private DeviceType type;

    private String uplinkMacAddress;

    public RegisterDeviceRequest(String macAddress, DeviceType type, String uplinkMacAddress) {
        this.macAddress = macAddress;
        this.type = type;
        this.uplinkMacAddress = uplinkMacAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public DeviceType getType() {
        return type;
    }

    public String getUplinkMacAddress() {
        return uplinkMacAddress;
    }

}
