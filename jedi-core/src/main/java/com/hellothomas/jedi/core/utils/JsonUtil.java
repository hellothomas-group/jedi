package com.hellothomas.jedi.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellothomas.jedi.core.dto.ReturnT;
import com.hellothomas.jedi.core.dto.consumer.AbstractNotification;
import com.hellothomas.jedi.core.enums.ErrorCodeEnum;
import com.hellothomas.jedi.core.exception.MyException;

import java.io.IOException;
import java.util.List;

/**
 * @className JsonUtil
 * @author 80234613 唐圆
 * @date 2020-8-4 15:20
 * @descripton
 * @version 1.0
 */
public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.findAndRegisterModules();
    }

    private JsonUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static String serialize(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new MyException(ErrorCodeEnum.JSON_SERIALIZE_ERROR, e);
        }
    }

    public static <T> T deserialize(String content, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(content, valueType);
        } catch (IOException e) {
            throw new MyException(ErrorCodeEnum.JSON_DESERIALIZE_ERROR, content, e);
        }
    }

    public static <T> ReturnT<T> deserializeReturnT(String content, Class<T> valueType) {
        try {
            ReturnT<T> returnT = OBJECT_MAPPER.readValue(content, new TypeReference<ReturnT<T>>() {
            });

            T body = OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(returnT.getContent()), valueType);
            returnT.setContent(body);
            return returnT;
        } catch (JsonProcessingException e) {
            throw new MyException(ErrorCodeEnum.JSON_DESERIALIZE_ERROR, content, e);
        }
    }

    public static <T> List<T> deserializeToList(String listJson, Class<T> valueType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //反序列化List
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, AbstractNotification.class);
            List<T> list = mapper.readValue(listJson, type);
            return list;
        } catch (JsonProcessingException e) {
            throw new MyException(ErrorCodeEnum.JSON_DESERIALIZE_ERROR, listJson, e);
        }
    }
}
