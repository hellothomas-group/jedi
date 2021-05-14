package com.hellothomas.jedi.client.util;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@Getter
@ToString
public class HttpResponse<T> {
    private final int statusCode;
    private final T body;

    public HttpResponse(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }
}
