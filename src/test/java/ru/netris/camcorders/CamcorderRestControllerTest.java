package ru.netris.camcorders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import ru.netris.camcorders.domain.Camcorder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CamcorderRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void request() {
	ResponseEntity<Camcorder[]> response = restTemplate.getForEntity("http://localhost:" + port + "/", Camcorder[].class);
	assertThat(response).isNotNull();
	Camcorder[] camcorders = response.getBody();
	assertThat(camcorders).isNotNull();
	assertThat(camcorders).isNotEmpty();
	Camcorder camcorder = camcorders[0];
	assertTrue(camcorder.getId() > 0);
	assertTrue(camcorder.getUrlType() != null);
	assertTrue(camcorder.getVideoUrl() != null);
	assertTrue(camcorder.getValue() != null);
	assertTrue(camcorder.getTtl() > 0);
    }
}
