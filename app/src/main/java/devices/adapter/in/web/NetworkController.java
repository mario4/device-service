package devices.adapter.in.web;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import devices.adapter.in.web.dto.DeviceEntryResponse;
import devices.adapter.in.web.dto.DevicesNetworkTopologyResponse;
import devices.adapter.in.web.dto.RegisterDeviceRequest;
import devices.adapter.in.web.exceptions.DeviceNotFoundException;
import devices.adapter.in.web.mapper.DevicesNetworkTopologyMapper;
import devices.application.DevicesNetworkQueryUseCase;
import devices.application.RegisterDeviceCommand;
import devices.application.RegisterDeviceUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devices.domain.Device;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/network/devices")
public class NetworkController {

    private final RegisterDeviceUseCase registerDeviceUseCase;

    private final DevicesNetworkQueryUseCase devicesNetworkQueryUseCase;

    public NetworkController(RegisterDeviceUseCase registerDeviceUseCase, DevicesNetworkQueryUseCase devicesNetworkQueryUseCase) {
        this.registerDeviceUseCase = registerDeviceUseCase;
        this.devicesNetworkQueryUseCase = devicesNetworkQueryUseCase;
    }

    @GetMapping("/topology")
    public DevicesNetworkTopologyResponse getNetworkTopology() {
        return DevicesNetworkTopologyMapper.map(devicesNetworkQueryUseCase.getTopology());
    }

    @PostMapping
    public ResponseEntity<String> registerDevice(@Valid @RequestBody RegisterDeviceRequest request) {
        registerDeviceUseCase
                .execute(new RegisterDeviceCommand(request.macAddress(), request.type(), request.uplinkMacAddress()));

        // 2. Build the Location URI dynamically
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()          // Gets the current URL (e.g., http://localhost:8080/api/devices)
                .path("/{macAddress}")         // Appends a path variable template
                .buildAndExpand(request.macAddress())  // Injects the actual MAC address into the template
                .toUri();                      // Converts it to a java.net.URI object

        // 3. Return a 201 Created status code along with the Location header
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public List<DeviceEntryResponse> listRegisteredDevices() {
        return devicesNetworkQueryUseCase.getRegisteredDevices().stream()
                .map(d -> new DeviceEntryResponse(d.getMacAddress().value(), d.getType())).collect(Collectors.toList());
    }

    @GetMapping("/{macAddress}")
    public DeviceEntryResponse getRegisteredDevice(@PathVariable String macAddress) {
        Device device = devicesNetworkQueryUseCase.getRegisteredDevice(macAddress);
        if(device == null){
            throw new DeviceNotFoundException();
        }
        return new DeviceEntryResponse(Optional.ofNullable(device.getMacAddress().value()).orElse(""), device.getType());
    }

    @GetMapping("/{macAddress}/topology")
    public DevicesNetworkTopologyResponse getRegisteredDeviceTopology(@PathVariable String macAddress) {
        Device device = devicesNetworkQueryUseCase.getRegisteredDevice(macAddress);
        if(device == null){
            throw new DeviceNotFoundException();
        }
        return DevicesNetworkTopologyMapper.map(device);
    }
}