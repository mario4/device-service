package devices.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Device implements Comparable<Device>, Serializable{

    private String macAddress;

    @JsonIgnore
    private DeviceType type;

    @JsonIgnore
    private String uplinkMacAddress;

    private List<Device> connectedDevices = new ArrayList<>();

    public Device(String macAddress, DeviceType type, String uplinkMacAddress) {
        this.macAddress = macAddress;
        this.type = type;
        this.uplinkMacAddress = uplinkMacAddress;
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
        Device other = (Device) obj;
        if (macAddress == null) {
            if (other.macAddress != null)
                return false;
        } else if (!macAddress.equals(other.macAddress))
            return false;
        return true;
    }

    @Override
    public int compareTo(Device o) {
        return Integer.compare(this.getType().getOrderPrecedence(), o.getType().getOrderPrecedence());
    }

    @Override
    public String toString() {
        return "Device [macAddress=" + macAddress + ", type=" + type + "]";
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

    public List<Device> getConnectedDevices() {
        return connectedDevices;
    }
}