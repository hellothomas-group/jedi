package xyz.hellothomas.jedi.core.utils;

import xyz.hellothomas.jedi.core.dto.ReturnT;
import xyz.hellothomas.jedi.core.dto.consumer.AbstractNotification;
import xyz.hellothomas.jedi.core.dto.consumer.CustomNotification;
import xyz.hellothomas.jedi.core.enums.MessageType;
import org.junit.Test;

import java.time.LocalDateTime;

public class JsonUtilTest {

    @Test
    public void deserializeReturnT() {
        String returnTString = "{\"code\":200,\"msg\":null,\"content\":{\"id\":\"1\"," +
                "\"messageType\":\"EXECUTOR_TICKER\"," +
                "\"content\":\"content\",\"recordTime\":\"2021-01-10 22:14:56\",\"host\":\"127.0.0.1\"}}";
        ReturnT<CustomNotification> returnT = JsonUtil.deserializeReturnT(returnTString, CustomNotification.class);
        System.out.println(returnT);
        // java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to xyz.hellothomas.jedi.core.dto
        // .AbstractNotification
        CustomNotification CustomNotification = returnT.getContent();
        System.out.println(CustomNotification);
    }

    @Test
    public void serialize() {
        CustomNotification CustomNotification = new CustomNotification();
        CustomNotification.setId("1");
        CustomNotification.setHost("127.0.0.1");
        CustomNotification.setMessageType(MessageType.EXECUTOR_TICKER.getTypeValue());
        CustomNotification.setRecordTime(LocalDateTime.now());
        CustomNotification.setContent("content");
        // {"id":"1","messageType":"executor-ticker","content":"content","recordTime":"2021-01-10 22:11:25",
        // "host":"127.0.0.1"}
        System.out.println(JsonUtil.serialize(CustomNotification));
        ReturnT returnT = ReturnT.SUCCESS;
        returnT.setContent(CustomNotification);
        // {"code":200,"msg":null,"content":{"id":"1","messageType":"executor-ticker","content":"content",
        // "recordTime":"2021-01-10 22:14:56","host":"127.0.0.1"}}
        System.out.println(JsonUtil.serialize(returnT));
    }

    @Test
    public void deserialize() {
        String returnTString = "{\"code\":200,\"msg\":null,\"content\":{\"id\":\"1\"," +
                "\"messageType\":\"executor-ticker\"," +
                "\"content\":\"content\",\"recordTime\":\"2021-01-10 22:14:56\",\"host\":\"127.0.0.1\"}}";
        ReturnT<CustomNotification> returnT = JsonUtil.deserialize(returnTString, ReturnT.class);
        System.out.println(returnT);
        // java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to xyz.hellothomas.jedi.core.dto
        // .AbstractNotification
        AbstractNotification message1 = returnT.getContent();
        System.out.println(message1);
    }
}