CREATE
database if NOT EXISTS `jedi_demo` default character set utf8mb4 collate utf8mb4_bin;
use
jedi_demo;
CREATE TABLE JEDI_TASK_EXECUTION
(
    ID                                VARCHAR(36)  NOT NULL PRIMARY KEY COMMENT 'id',
    NAMESPACE_NAME                    VARCHAR(32)  NOT NULL COMMENT 'namespaceName',
    APP_ID                            VARCHAR(32)  NOT NULL COMMENT 'appId',
    EXECUTOR_NAME                     VARCHAR(64)  NOT NULL COMMENT 'executorName',
    TASK_NAME                         VARCHAR(64)  NOT NULL COMMENT 'taskName',
    TASK_EXTRA_DATA                   VARCHAR(1024) COMMENT 'taskExtraData',
    CREATE_TIME                       DATETIME(3) NOT NULL COMMENT 'createTime',
    START_TIME                        DATETIME(3) COMMENT 'startTime',
    END_TIME                          DATETIME(3) COMMENT 'endTime',
    STATUS                            TINYINT      NOT NULL DEFAULT 0 COMMENT '0:registered, 1:doing, 2:success, 3:fail, 4:rejected',
    EXIT_CODE                         VARCHAR(7) COMMENT 'exitCode',
    EXIT_MESSAGE                      VARCHAR(1024) COMMENT 'exitMessage',
    BEAN_NAME                         VARCHAR(64)  NOT NULL COMMENT 'beanName',
    BEAN_TYPE_NAME                    VARCHAR(512) NOT NULL COMMENT 'beanTypeName',
    METHOD_NAME                       VARCHAR(64)  NOT NULL COMMENT 'methodName',
    METHOD_PARAM_TYPES                VARCHAR(1024) COMMENT 'methodParamTypes',
    METHOD_ARGUMENTS                  VARCHAR(4000) COMMENT 'methodArguments',
    IS_RECOVERABLE                    TINYINT      NOT NULL DEFAULT 0 COMMENT '1:recoverable, 0:not recoverable',
    IS_RECOVERED                      TINYINT      NOT NULL DEFAULT 0 COMMENT '1:recovered, 0:normal',
    HOST                              VARCHAR(15)  NOT NULL COMMENT 'host',
    MACHINE_ID                        VARCHAR(15)  NOT NULL COMMENT 'machineId',
    TRACE_ID                          VARCHAR(32) COMMENT 'traceId',
    IS_BY_RETRYER                     TINYINT      NOT NULL DEFAULT 0 COMMENT '1:byRetryer, 0:normal',
    PREVIOUS_ID                       VARCHAR(36) COMMENT 'previousId',
    PARENT_ID                         VARCHAR(36) COMMENT 'parentId',
    IS_EXECUTED_BY_PARENT_TASK_THREAD TINYINT      NOT NULL DEFAULT 0 COMMENT '1:true, 0:false',
    DATA_SOURCE_NAME                  VARCHAR(64) COMMENT 'dataSourceName',
    LAST_UPDATED_USER                 VARCHAR(32) COMMENT 'lastUpdatedUser',
    LAST_UPDATED_TIME                 DATETIME(3) NOT NULL DEFAULT NOW(3) COMMENT 'lastUpdatedTime',
    KEY                               JEDI_TASK_EXECUTION_IDX_1 (STATUS, MACHINE_ID, IS_RECOVERABLE, CREATE_TIME),
    KEY                               JEDI_TASK_EXECUTION_IDX_2 (LAST_UPDATED_TIME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务执行表';