package ru.netris.camcorders.services;

import ru.netris.camcorders.domain.UrlType;

/**
 * Response of fetching camcorder source data
 * 
 * @author Vitalii Kovalenko
 *
 */
public class SourceDataResponse extends CamcorderResponse {
    
    private UrlType urlType;
    
    private String videoUrl;

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
    
}
