package xyz.hellothomas.jedi.admin.common.enums;

public enum PermissonTypeEnum {
    // app
    CREATE_DELETE_APP("CreateOrDeleteApp"),
    GRANT_APP("GrantApp"),
    MODIFY_APP("ModifyApp"),

    // executor
    CREATE_DELETE_EXECUTOR("CreateOrDeleteExecutor"),
    MODIFY_EXECUTOR_ALARM_CONFIG("ModifyExecutorAlarmConfig"),
    RELEASE_EXECUTOR_CONFIG("ReleaseExecutorConfig"),
    MODIFY_EXECUTOR_CONFIG("ModifyExecutorConfig"),
    GRANT_EXECUTOR("GrantExecutor"),
    ;

    private String value;

    PermissonTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
