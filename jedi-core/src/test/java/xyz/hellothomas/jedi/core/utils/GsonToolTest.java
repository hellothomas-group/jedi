package xyz.hellothomas.jedi.core.utils;

import org.junit.Test;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.dto.consumer.CustomNotification;

public class GsonToolTest {

    @Test
    public void fromJson() {
        String apiResponseString = "{\"code\":\"CODE000\",\"data\":{\"id\":\"1\",\"appId\":null,\"namespace\":null," +
                "\"messageType\":\"executor-ticker\",\"recordTime\":\"2021-09-10 20:41:18.707\",\"host\":\"127.0.0" +
                ".1\",\"content\":\"content\"},\"message\":\"操作成功\"}";
        ApiResponse<CustomNotification> apiResponse = GsonTool.fromJson(apiResponseString,
                ApiResponse.class, CustomNotification.class);
        System.out.println(apiResponse);
        CustomNotification CustomNotification = apiResponse.getData();
        System.out.println(CustomNotification);
    }
}