package xyz.hellothomas.jedi.client.enums;

/**
 * @author Thomas
 * @date 2021/12/16 21:06
 * @description
 * @version 1.0
 */
public enum TaskStatusEnum {
    TODO("1"),
    SUCCESS("2"),
    FAIL("3");

    private String processStatus;

    TaskStatusEnum(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessStatus() {
        return processStatus;
    }
}
