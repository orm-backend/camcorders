package ru.netris.camcorders;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.netris.camcorders.domain.Camcorder;
import ru.netris.camcorders.services.CamcorderService;
import ru.netris.camcorders.services.SourceDataResponse;
import ru.netris.camcorders.services.TokenDataResponse;

@SpringBootTest
class CamcorderServiceTest {

    @Autowired
    private CamcorderService camcorderService;

    @Test
    public void contextLoads() throws Exception {
	assertTrue(camcorderService != null);
    }

    @Test
    void fetchListTest() {
	Camcorder[] camcorders = camcorderService.fetchList();
	assertTrue(camcorders != null);
	assertTrue(camcorders.length > 0);
	assertTrue(camcorders[0].getId() > 0);
	assertTrue(camcorders[0].getSourceDataUrl() != null);
	assertTrue(camcorders[0].getTokenDataUrl() != null);
    }

    @Test
    void fetchSourceDataTest() {
	Camcorder[] camcorders = camcorderService.fetchList();
	assertTrue(camcorders != null);
	assertTrue(camcorders.length > 0);
	SourceDataResponse response = camcorderService.fetchSourceData(camcorders[0]);
	assertTrue(response != null);
	assertTrue(response.getId() != null);
	assertTrue(response.getId() > 0);
	assertTrue(response.getUrlType() != null);
	assertTrue(response.getVideoUrl() != null);
    }

    @Test
    void fetchTokenDataTest() {
	Camcorder[] camcorders = camcorderService.fetchList();
	assertTrue(camcorders != null);
	assertTrue(camcorders.length > 0);
	TokenDataResponse response = camcorderService.fetchTokenData(camcorders[0]);
	assertTrue(response != null);
	assertTrue(response.getId() != null);
	assertTrue(response.getId() > 0);
	assertTrue(response.getValue() != null);
	assertTrue(response.getTtl() > 0);
    }

}
