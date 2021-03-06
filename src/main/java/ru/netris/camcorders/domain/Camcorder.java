package ru.netris.camcorders.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Camcorder model
 * 
 * @author Vitalii Kovalenko
 *
 */
public class Camcorder {

    private int id;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String sourceDataUrl;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String tokenDataUrl;
    
    private UrlType urlType;
    
    private String videoUrl;
    
    private String value;
    
    private int ttl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSourceDataUrl(String sourceDataUrl) {
        this.sourceDataUrl = sourceDataUrl;
    }

    public void setTokenDataUrl(String tokenDataUrl) {
        this.tokenDataUrl = tokenDataUrl;
    }

    public String getSourceDataUrl() {
        return sourceDataUrl;
    }

    public String getTokenDataUrl() {
        return tokenDataUrl;
    }

    public UrlType getUrlType() {
        return urlType;
    }

    public void setUrlType(UrlType urlType) {
        this.urlType = urlType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + id;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Camcorder other = (Camcorder) obj;
	if (id != other.id)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Camcorder [id=" + id + "]";
    }
    
}
