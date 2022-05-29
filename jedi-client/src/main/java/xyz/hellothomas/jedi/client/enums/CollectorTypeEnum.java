package xyz.hellothomas.jedi.client.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Thomas
 */

@Getter
@ToString
public enum CollectorTypeEnum {
    HTTP("HTTP"),
    KAFKA("KAFKA"),
    ;

    private String enumValue;

    CollectorTypeEnum(String enumValue) {
        this.enumValue = enumValue;
    }
}
