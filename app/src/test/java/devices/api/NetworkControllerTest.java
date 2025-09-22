package devices.api;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import devices.api.model.DeviceEntryResponse;
import devices.api.model.RegisterDeviceRequest;
import devices.model.Device;
import devices.model.DeviceType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NetworkControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void canRegisterDevice() {
        String url = "http://localhost:" + port + "/api/network/devices/register";
        String urlGetDevice = "http://localhost:" + port + "/api/network/devices/00:01";

        sendRegisterRequest(url, "00:01", DeviceType.SWITCH, "");

        DeviceEntryResponse response = restTemplate.getForObject(urlGetDevice, DeviceEntryResponse.class);

        Assertions.assertThat(response.getMacAddress()).isEqualTo("00:01");
    }

    @Test
    void can_List_Registered_Devices() {
        String url = "http://localhost:" + port + "/api/network/devices/register";
        String urlListDevice = "http://localhost:" + port + "/api/network/devices/list";

        sendRegisterRequest(url, "00:01", DeviceType.SWITCH, "");
        sendRegisterRequest(url, "00:02", DeviceType.GATEWAY, "");

        ResponseEntity<List<DeviceEntryResponse>> response = restTemplate.exchange(urlListDevice, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<DeviceEntryResponse>>() {
                });

        Assertions
                .assertThatIterable(
                        response.getBody().stream().map(d -> d.getMacAddress()).collect(Collectors.toList()))
                .contains("00:01", "00:02");

        sendRegisterRequest(url, "00:03", DeviceType.SWITCH, "");

        ResponseEntity<List<DeviceEntryResponse>> secondResponse = restTemplate.exchange(urlListDevice, HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DeviceEntryResponse>>() {
                });

        Assertions
                .assertThatIterable(
                        secondResponse.getBody().stream().map(d -> d.getMacAddress()).collect(Collectors.toList()))
                .contains("00:01", "00:02", "00:03");
    }

    @Test
    void should_Validate_Device_Registration_Parameters() {
        String url = "http://localhost:" + port + "/api/network/devices/register";

        Assertions.assertThat(sendRegisterRequest(url, null, null, "").getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    void can_Return_Network_Topology() {
        String url = "http://localhost:" + port + "/api/network/devices/register";

        sendRegisterRequest(url, "00:01", DeviceType.SWITCH, "");
        sendRegisterRequest(url, "00:02", DeviceType.GATEWAY, "00:01");
        sendRegisterRequest(url, "00:03", DeviceType.GATEWAY, "00:01");
        sendRegisterRequest(url, "00:04", DeviceType.GATEWAY, "00:01");

        String urlGetDevice = "http://localhost:" + port + "/api/network/topology";

        Device response = restTemplate.getForObject(urlGetDevice, Device.class);

        Assertions.assertThat(response.getConnectedDevices().size()).isEqualTo(1);
        Assertions.assertThat(response.getConnectedDevices().stream().findFirst().get().getConnectedDevices().size())
                .isEqualTo(3);

    }

    @Test
    void can_Return_Network_Topology_Starting_From_Internal_Device() {
        String url = "http://localhost:" + port + "/api/network/devices/register";

        sendRegisterRequest(url, "00:01", DeviceType.SWITCH, "");
        sendRegisterRequest(url, "00:02", DeviceType.GATEWAY, "00:01");
        sendRegisterRequest(url, "00:03", DeviceType.GATEWAY, "00:01");
        sendRegisterRequest(url, "00:04", DeviceType.GATEWAY, "00:01");
        sendRegisterRequest(url, "00:05", DeviceType.GATEWAY, "00:03");
        sendRegisterRequest(url, "00:06", DeviceType.GATEWAY, "00:03");

        String urlGetDevice = "http://localhost:" + port + "/api/network/devices/00:03/topology";

        Device response = restTemplate.getForObject(urlGetDevice, Device.class);

        Assertions.assertThat(response.getConnectedDevices().size())
                .isEqualTo(2);

    }

    private ResponseEntity<Void> sendRegisterRequest(String url, String macAddr, DeviceType deviceType,
            String upLinknMacAddr) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RegisterDeviceRequest deviceRequest = new RegisterDeviceRequest(macAddr, deviceType, upLinknMacAddr);

        HttpEntity<RegisterDeviceRequest> httpEntity = new HttpEntity<>(deviceRequest, headers);

        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, Void.class);
    }
}
