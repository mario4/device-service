package devices.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devices.api.model.DeviceEntryResponse;
import devices.api.model.RegisterDeviceRequest;
import devices.model.Device;
import devices.network.NetworkDeployment;

@RestController
@RequestMapping("/api/network")
public class NetworkController {
    private NetworkDeployment networkDeployment = new NetworkDeployment();;

    @GetMapping("/topology")
    public Device getNetworkTopology() {
        return networkDeployment.getTopology();
    }

    @PostMapping("/devices/register")
    public void registerDevice(@RequestBody RegisterDeviceRequest request) {

        validateRegisterDeviceRequest(request);

        networkDeployment
                .registerDevice(new Device(request.getMacAddress(), request.getType(), request.getUplinkMacAddress()));
    }

    @GetMapping("/devices/{macAddress}")
    public DeviceEntryResponse getRegisteredDevice(@PathVariable String macAddress) {
        if(macAddress == null)
            throw new RuntimeException("Invalid mac address");

        Device device = networkDeployment.getRegisteredDevice(macAddress);
        return new DeviceEntryResponse(device.getMacAddress(), device.getType());
    }

    @GetMapping("/devices/{macAddress}/topology")
    public Device getRegisteredDeviceTopology(@PathVariable String macAddress) {
        Device device = networkDeployment.getRegisteredDevice(macAddress);
        return device;
    }

    @GetMapping("/devices/list")
    public List<DeviceEntryResponse> listRegisteredDevices() {
        return networkDeployment.getRegisteredDevices().stream()
                .map(d -> new DeviceEntryResponse(d.getMacAddress(), d.getType())).collect(Collectors.toList());
    }

    private void validateRegisterDeviceRequest(RegisterDeviceRequest request) {

        if (request.getMacAddress() == null)
            throw new InvalidDeviceRegistrationParameters("invalid mac address");
        if (request.getMacAddress().isEmpty())
            throw new InvalidDeviceRegistrationParameters("invalid mac address");
        if (request.getType() == null)
            throw new InvalidDeviceRegistrationParameters("invalid device type");
    }

    public static final class InvalidDeviceRegistrationParameters extends RuntimeException{

        public InvalidDeviceRegistrationParameters( String message) {
            super(message);
        }

    }
}