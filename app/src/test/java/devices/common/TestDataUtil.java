package devices.common;

import devices.domain.Device;
import devices.domain.DeviceType;
import devices.domain.DevicesNetwork;

import java.util.Optional;

public final class TestDataUtil {
    private TestDataUtil(){
    }

    public static void registerDevices(DevicesNetwork network, Device... devices) {
        for (Device device : devices) {
            network.registerDevice(device);
        }
    }

    public static Device aGateway(String macAddress, String uplinkAddress) {
        return new Device(givenMacAddress(macAddress), DeviceType.GATEWAY, givenMacAddress(uplinkAddress));
    }

    public static Device aSwitch(String macAddress, String uplinkAddress) {
        return new Device(givenMacAddress(macAddress), DeviceType.SWITCH, givenMacAddress(uplinkAddress));
    }

    public static Device anAccesspoint(String macAddress, String uplinkAddress) {
        return new Device(givenMacAddress(macAddress), DeviceType.ACCESS_POINT, givenMacAddress(uplinkAddress));
    }

    public static String givenMacAddress(String suffix){
        return Optional.ofNullable(suffix).map((s) -> "CB:0B:B6:9B:34:" + s).orElse(null);
    }
}