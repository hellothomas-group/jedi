package xyz.hellothomas.jedi.core.utils;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author Thomas
 * @date 2021/7/9 16:21
 * @descripton
 * @version 1.0
 */
public class Base64Test {
    private final Base64.Decoder decoder = Base64.getDecoder();
    private final Base64.Encoder encoder = Base64.getEncoder();

    @Test
    public void testEncode() throws UnsupportedEncodingException {
        System.out.println(encoder.encodeToString("123456".getBytes("UTF-8")));
        System.out.println(new String(encoder.encode("123456".getBytes("UTF-8")), "UTF-8"));
    }

    @Test
    public void testDecode() throws UnsupportedEncodingException {
        System.out.println(new String(decoder.decode("MTIzNDU2"), "UTF-8"));
        System.out.println(new String(decoder.decode("MTIzNDU2".getBytes("UTF-8")), "UTF-8"));
    }
}
