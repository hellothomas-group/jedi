package xyz.hellothomas.jedi.consumer.common.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Thomas
 */

@Getter
@ToString
public enum ConsumerTypeEnum {
    HTTP("HTTP"),
    KAFKA("KAFKA"),
    ;

    private String enumValue;

    ConsumerTypeEnum(String enumValue) {
        this.enumValue = enumValue;
    }
}
