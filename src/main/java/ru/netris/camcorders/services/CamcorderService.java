package ru.netris.camcorders.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ru.netris.camcorders.domain.Camcorder;

/**
 * Service for fetching camcorder data
 * 
 * @author Vitalii Kovalenko
 *
 */
public class CamcorderService {

    @Value("${camcorders.list-url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Fetches camcorder list
     * 
     * @return Camcorder[]
     */
    public Camcorder[] fetchList() throws RestClientException {
	return restTemplate.getForEntity(url, Camcorder[].class).getBody();
    }

    /**
     * Fetches camcorder source data
     * 
     * @param sourceDataUrl
     * @return SourceDataResponse
     */
    public SourceDataResponse fetchSourceData(Camcorder camcorder) throws RestClientException {
	SourceDataResponse response = restTemplate.getForObject(camcorder.getSourceDataUrl(), SourceDataResponse.class);
	response.setId(camcorder.getId());
	return response;
    }

    /**
     * 
     * Fetches camcorder token data
     * 
     * @param tokenDataUrl
     * @return TokenDataResponse
     */
    public TokenDataResponse fetchTokenData(Camcorder camcorder) throws RestClientException {
	TokenDataResponse response = restTemplate.getForObject(camcorder.getTokenDataUrl(), TokenDataResponse.class);
	response.setId(camcorder.getId());
	return response;
    }

}
