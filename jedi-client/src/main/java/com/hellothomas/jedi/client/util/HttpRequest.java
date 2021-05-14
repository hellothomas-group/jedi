package com.hellothomas.jedi.client.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@Getter
@Setter
@ToString
public class HttpRequest {
    private String url;
    private Map<String, String> headers;
    private int connectTimeout;
    private int readTimeout;

    /**
     * Create the request for the url.
     * @param url the url
     */
    public HttpRequest(String url) {
        this.url = url;
        connectTimeout = 1000;
        readTimeout = 5000;
    }
}
