package xyz.hellothomas.jedi.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.dto.consumer.AbstractNotification;
import xyz.hellothomas.jedi.core.dto.consumer.CustomNotification;
import xyz.hellothomas.jedi.core.enums.MessageType;

import java.time.LocalDateTime;

public class JsonUtilTest {

    @Test
    public void deserializeApiResponse() throws JsonProcessingException {
        String apiResponseString = "{\"code\":\"CODE000\",\"data\":{\"id\":\"1\",\"appId\":null,\"namespace\":null," +
                "\"messageType\":\"executor-ticker\",\"recordTime\":\"2021-09-10 20:41:18.707\",\"host\":\"127.0.0" +
                ".1\",\"content\":\"content\"},\"message\":\"操作成功\"}";
        ApiResponse<CustomNotification> apiResponse = JsonUtil.deserializeApiResponse(apiResponseString,
                CustomNotification.class);
        System.out.println(apiResponse);
        CustomNotification CustomNotification = apiResponse.getData();
        System.out.println(CustomNotification);
    }

    @Test
    public void serialize() {
        CustomNotification customNotification = new CustomNotification();
        customNotification.setId("1");
        customNotification.setHost("127.0.0.1");
        customNotification.setMessageType(MessageType.EXECUTOR_TICKER.getTypeValue());
        customNotification.setRecordTime(LocalDateTime.now());
        customNotification.setContent("content");
        // {"id":"1","appId":null,"namespace":null,"messageType":"executor-ticker","recordTime":"2021-09-10 22:50:16
        // .905","host":"127.0.0.1","content":"content"}
        System.out.println(JsonUtil.serialize(customNotification));

        ApiResponse<CustomNotification> apiResponse = ApiResponse.success(customNotification);
        // {"code":"CODE000","data":{"id":"1","appId":null,"namespace":null,"messageType":"executor-ticker",
        // "recordTime":"2021-09-10 22:50:16.905","host":"127.0.0.1","content":"content"},"message":"操作成功"}
        System.out.println(JsonUtil.serialize(apiResponse));
    }

    @Test
    public void deserialize() {
        String returnTString = "{\"code\":\"CODE000\",\"data\":{\"id\":\"1\",\"appId\":null,\"namespace\":null," +
                "\"messageType\":\"executor-ticker\",\"recordTime\":\"2021-09-10 20:41:18.707\",\"host\":\"127.0.0" +
                ".1\",\"content\":\"content\"},\"message\":\"操作成功\"}";
        ApiResponse<CustomNotification> apiResponse = JsonUtil.deserialize(returnTString, ApiResponse.class);
        System.out.println(apiResponse);
        // java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to xyz.hellothomas.jedi.core.dto
        // .AbstractNotification
        AbstractNotification message = apiResponse.getData();
        System.out.println(message);
    }
}