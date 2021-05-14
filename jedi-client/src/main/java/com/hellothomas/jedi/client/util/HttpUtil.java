package com.hellothomas.jedi.client.util;

import com.google.common.base.Function;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.hellothomas.jedi.client.exception.JediConfigException;
import com.hellothomas.jedi.client.exception.JediConfigStatusCodeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class HttpUtil {
    private static Gson gson = new Gson();

    /**
     * Do get operation for the http request.
     *
     * @param httpRequest  the request
     * @param responseType the response type
     * @return the response
     * @throws JediConfigException if any error happened or response code is neither 200 nor 304
     */
    public static <T> HttpResponse<T> doGet(HttpRequest httpRequest, final Class<T> responseType) {
        Function<String, T> convertResponse = new Function<String, T>() {
            @Override
            public T apply(String input) {
                return gson.fromJson(input, responseType);
            }
        };

        return doGetWithSerializeFunction(httpRequest, convertResponse);
    }

    /**
     * Do get operation for the http request.
     *
     * @param httpRequest  the request
     * @param responseType the response type
     * @return the response
     * @throws JediConfigException if any error happened or response code is neither 200 nor 304
     */
    public static <T> HttpResponse<T> doGet(HttpRequest httpRequest, final Type responseType) {
        Function<String, T> convertResponse = new Function<String, T>() {
            @Override
            public T apply(String input) {
                return gson.fromJson(input, responseType);
            }
        };

        return doGetWithSerializeFunction(httpRequest, convertResponse);
    }

    private static <T> HttpResponse<T> doGetWithSerializeFunction(HttpRequest httpRequest,
                                                                  Function<String, T> serializeFunction) {
        InputStreamReader isr = null;
        InputStreamReader esr = null;
        int statusCode;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(httpRequest.getUrl()).openConnection();

            conn.setRequestMethod("GET");

            Map<String, String> headers = httpRequest.getHeaders();
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            int connectTimeout = httpRequest.getConnectTimeout();
            int readTimeout = httpRequest.getReadTimeout();

            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);

            conn.connect();

            statusCode = conn.getResponseCode();
            String response;

            try {
                isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                response = CharStreams.toString(isr);
            } catch (IOException ex) {
                /**
                 * according to https://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html,
                 * we should clean up the connection by reading the response body so that the connection
                 * could be reused.
                 */
                InputStream errorStream = conn.getErrorStream();

                if (errorStream != null) {
                    esr = new InputStreamReader(errorStream, StandardCharsets.UTF_8);
                    try {
                        CharStreams.toString(esr);
                    } catch (IOException ioe) {
                        //ignore
                    }
                }

                // 200 and 304 should not trigger IOException, thus we must throw the original exception out
                if (statusCode == 200 || statusCode == 304) {
                    throw ex;
                }
                // for status codes like 404, IOException is expected when calling conn.getInputStream()
                throw new JediConfigStatusCodeException(statusCode, ex);
            }

            if (statusCode == 200) {
                return new HttpResponse<>(statusCode, serializeFunction.apply(response));
            }

            if (statusCode == 304) {
                return new HttpResponse<>(statusCode, null);
            }
        } catch (JediConfigStatusCodeException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new JediConfigException("Could not complete get operation", ex);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    // ignore
                }
            }

            if (esr != null) {
                try {
                    esr.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }

        throw new JediConfigStatusCodeException(statusCode,
                String.format("Get operation failed for %s", httpRequest.getUrl()));
    }

}
