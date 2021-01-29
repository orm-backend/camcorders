package ru.netris.camcorders.services;

/**
 * Response of fetching camcorder token
 * 
 * @author Vitalii Kovalenko
 *
 */
public class TokenDataResponse {

    private String value;
    
    private int ttl;

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

}
