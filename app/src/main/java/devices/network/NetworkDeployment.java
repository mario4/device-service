package devices.network;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import devices.model.Device;

public class NetworkDeployment {

    private Set<Device> registeredDevices = new LinkedHashSet<>();

    private Device networkRoot = new Device("root", null, null);

    public void registerDevice(Device newDevice) {
        Device device = getRegisteredDevice(newDevice.getMacAddress());
        
        if (device != null) {
            throw new DuplicateDeviceException();
        }
        
        if (newDevice.getMacAddress().equals(newDevice.getUplinkMacAddress()))
            throw new CyclicUplinkReferenceException();

        deployDeviceToNetwork(newDevice);
    }

    public Device getRegisteredDevice(String string) {
        return registeredDevices.stream().filter(d -> d.getMacAddress().equals(string))
                .findFirst()
                .orElse(null);
    }

    public Set<Device> getRegisteredDevices() {
        return registeredDevices.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Device getTopology() {
        return networkRoot;
    }

    private void deployDeviceToNetwork(Device device) {
        if (isUplinkEmpty(device)) {
            networkRoot.getConnectedDevices().add(device);
        } else {
            Device uplinkDevice = getRegisteredDevice(device.getUplinkMacAddress());

            if (uplinkDevice == null) {
                networkRoot.getConnectedDevices().add(device);
            } else {
                if (createsCyclicReference(device, uplinkDevice)) {
                    throw new CyclicUplinkReferenceException();
                }
                uplinkDevice.getConnectedDevices().add(device);
            }
        }

        resolveOutOfOrderUplinkConnections(device);
        registeredDevices.add(device);
    }

    private boolean createsCyclicReference(Device newDevice, Device uplinkDevice) {

        return newDevice.getMacAddress().equals(uplinkDevice.getUplinkMacAddress());
    }

    private boolean isUplinkEmpty(Device device) {
        return device.getUplinkMacAddress() == null || device.getUplinkMacAddress().isEmpty();
    }

    private void resolveOutOfOrderUplinkConnections(Device device) {
        for (Iterator<Device> i = networkRoot.getConnectedDevices().iterator(); i.hasNext();) {
            Device hangingDevice = i.next();

            if (isNoLongerHangingDevice(device.getMacAddress(), hangingDevice.getUplinkMacAddress())) {
                device.getConnectedDevices().add(hangingDevice);
                i.remove();
            }
        }
    }

    private boolean isNoLongerHangingDevice(String device, String UplinkMacAddress) {
        return Optional.ofNullable(UplinkMacAddress).filter(addr -> addr.equals(device)).isPresent();
    }

    public static final class CyclicUplinkReferenceException extends RuntimeException {
        @Override
        public String getMessage() {
            return "Cyclic device connection is not accepted in network topology";
        }
    }

    public static final class DuplicateDeviceException extends RuntimeException {
        @Override
        public String getMessage() {
            return "A device with the same macAddress is already deployed to network";
        }
    }
}
