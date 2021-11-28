package xyz.hellothomas.jedi.admin.common.enums;

public enum RoleTypeEnum {
    SYSTEM_MANAGER("SystemManager"),
    APP_MANAGER("AppManager"),
    EXECUTOR_MANAGER("ExecutorManager"),
    ;

    private String value;

    RoleTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
