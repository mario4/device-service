package devices.domain;

import devices.domain.exception.CyclicUplinkReferenceException;
import devices.domain.exception.DuplicateDeviceException;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DevicesNetwork {

    private final Set<Device> registeredDevices = new LinkedHashSet<>();

    private final Device networkRoot = new Device(MacAddress.ROOT_ADDRESS, null, null);

    public void registerDevice(Device newDevice) {
        Device device = getRegisteredDevice(newDevice.getMacAddress().value());

        if (device != null) {
            throw new DuplicateDeviceException();
        }

        if (newDevice.getMacAddress().equals(newDevice.getUplinkMacAddress().orElse(null)))
            throw new CyclicUplinkReferenceException();

        deployDeviceToNetwork(newDevice);
    }

    public Device getRegisteredDevice(String string) {
        return registeredDevices.stream().filter(d -> d.getMacAddress().value().equals(string))
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
            Device uplinkDevice = getRegisteredDevice(device.getUplinkMacAddress().map(MacAddress::value).orElse(null));

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

        return newDevice.getMacAddress().equals(uplinkDevice.getUplinkMacAddress().orElse(null));
    }

    private boolean isUplinkEmpty(Device device) {
        return device.getUplinkMacAddress().map(MacAddress::value).isEmpty();
    }

    private void resolveOutOfOrderUplinkConnections(Device device) {
        for (Iterator<Device> i = networkRoot.getConnectedDevices().iterator(); i.hasNext();) {
            Device hangingDevice = i.next();

            if (isNoLongerHangingDevice(device.getMacAddress().value(), hangingDevice.getUplinkMacAddress())) {
                device.getConnectedDevices().add(hangingDevice);
                i.remove();
            }
        }
    }

    private boolean isNoLongerHangingDevice(String device, Optional<MacAddress> uplinkMacAddress) {
        return uplinkMacAddress.filter(addr -> addr.value().equals(device)).isPresent();
    }
}
