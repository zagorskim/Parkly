package pw.react.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pw.react.backend.services.HttpService;

import java.util.Collections;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles(profiles = {"mysql-dev"})
class SampleBackendApplicationTests {

	@Autowired
	private HttpService httpService;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void contextLoads() {
	}
}
