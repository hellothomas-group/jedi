package xyz.hellothomas.jedi.core.enums;

/**
 * @author Thomas
 * @date 2021/12/16 21:06
 * @description
 * @version 1.0
 */
public enum TaskStatusEnum {
    REGISTERED(0),
    DOING(1),
    SUCCESS(2),
    FAIL(3),
    REJECTED(4),
    ;

    private int value;

    TaskStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
