package ru.netris.camcorders.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Vitalii Kovalenko
 *
 */
public enum UrlType {
    @JsonProperty("LIVE") LIVE,
    @JsonProperty("ARCHIVE") ARCHIVE
}
