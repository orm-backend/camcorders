package ru.netris.camcorders.services;

abstract public class CamcorderResponse {

    protected Integer id;

    public Integer getId() {
        return id;
    }

    void setId(Integer id) {
        this.id = id;
    }

}
