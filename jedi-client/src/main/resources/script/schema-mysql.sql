CREATE TABLE JEDI_TASK_EXECUTION (
  ID VARCHAR(36) NOT NULL PRIMARY KEY COMMENT 'id',
  NAMESPACE_NAME VARCHAR(32) NOT NULL COMMENT 'namespaceName',
  APP_ID VARCHAR(32) NOT NULL COMMENT 'appId',
  EXECUTOR_NAME VARCHAR(64) NOT NULL COMMENT 'executorName',
  TASK_NAME VARCHAR(64) NOT NULL COMMENT 'taskName',
  CREATE_TIME DATETIME(3) NOT NULL DEFAULT NOW(3) COMMENT 'createTime',
  START_TIME DATETIME(3) COMMENT 'startTime',
  END_TIME DATETIME(3) COMMENT 'endTime',
  STATUS TINYINT(4) NOT NULL DEFAULT 0 COMMENT '0:registered, 1:doing, 2:success, 3:fail, 4:rejected',
  EXIT_CODE VARCHAR(7) COMMENT 'exitCode',
  EXIT_MESSAGE VARCHAR(1024) COMMENT 'exitMessage',
  BEAN_NAME VARCHAR(64) NOT NULL COMMENT 'beanName',
  BEAN_TYPE_NAME VARCHAR(512) NOT NULL COMMENT 'beanTypeName',
  METHOD_NAME VARCHAR(64) NOT NULL COMMENT 'methodName',
  METHOD_PARAM_TYPES VARCHAR(1024) COMMENT 'methodParamTypes',
  METHOD_ARGUMENTS VARCHAR(4000) COMMENT 'methodArguments',
  TRACE_ID VARCHAR(32) COMMENT 'traceId',
  PREVIOUS_ID VARCHAR(36) COMMENT 'previousId',
  DATA_SOURCE_NAME VARCHAR(64) COMMENT 'dataSourceName',
  LAST_UPDATED DATETIME(3) NOT NULL DEFAULT NOW(3) COMMENT 'lastUpdated',
  KEY JEDI_TASK_EXECUTION_IDX_1 (NAMESPACE_NAME, APP_ID, EXECUTOR_NAME, TASK_NAME, STATUS),
  KEY JEDI_TASK_EXECUTION_IDX_2 (LAST_UPDATED)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务执行表';
