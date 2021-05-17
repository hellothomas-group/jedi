package xyz.hellothomas.jedi.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.hellothomas.jedi.core.dto.ReturnT;
import xyz.hellothomas.jedi.core.dto.consumer.AbstractNotification;
import xyz.hellothomas.jedi.core.enums.ErrorCodeEnum;
import xyz.hellothomas.jedi.core.exception.JediCoreException;

import java.io.IOException;
import java.util.List;

/**
 * @author 80234613 唐圆
 * @date 2020-8-4 15:20
 * @descripton
 * @version 1.0
 */
public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.findAndRegisterModules();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static String serialize(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JediCoreException(ErrorCodeEnum.JSON_SERIALIZE_ERROR, e);
        }
    }

    public static <T> T deserialize(String content, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(content, valueType);
        } catch (IOException e) {
            throw new JediCoreException(ErrorCodeEnum.JSON_DESERIALIZE_ERROR, content, e);
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
            throw new JediCoreException(ErrorCodeEnum.JSON_DESERIALIZE_ERROR, content, e);
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
            throw new JediCoreException(ErrorCodeEnum.JSON_DESERIALIZE_ERROR, listJson, e);
        }
    }
}
