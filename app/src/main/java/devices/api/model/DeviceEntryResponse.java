package devices.api.model;

import devices.model.DeviceType;

public class DeviceEntryResponse {

    private String macAddress;

    private DeviceType type;

    public DeviceEntryResponse(String macAddress, DeviceType type) {
        this.macAddress = macAddress;
        this.type = type;
    }

     public String getMacAddress() {
        return macAddress;
    }

    public DeviceType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DeviceEntryResponse other = (DeviceEntryResponse) obj;
        if (macAddress == null) {
            if (other.macAddress != null)
                return false;
        } else if (!macAddress.equals(other.macAddress))
            return false;
        return true;
    }

    
}
