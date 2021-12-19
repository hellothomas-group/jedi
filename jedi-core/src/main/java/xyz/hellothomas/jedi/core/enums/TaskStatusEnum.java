package xyz.hellothomas.jedi.core.enums;

/**
 * @author Thomas
 * @date 2021/12/16 21:06
 * @description
 * @version 1.0
 */
public enum TaskStatusEnum {
    REGISTERED("1"),
    DOING("2"),
    SUCCESS("3"),
    FAIL("4"),
    REJECTED("5"),
    ;

    private String value;

    TaskStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
