CREATE TABLE IF NOT EXISTS `JEDI_TASK_EXECUTION` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `namespace_name` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'namespaceName',
  `app_id` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'appId',
  `executor_name` varchar(64) NOT NULL COMMENT 'executorName',
  `task_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'taskName',
  `process_status` tinyint(4) unsigned NOT NULL DEFAULT 0 COMMENT '0: created, 1: success, 2: fail',
  `fail_reason` varchar(1024) NOT NULL DEFAULT '' COMMENT 'failReason',
  `bean_name` varchar(64) NOT NULL COMMENT 'beanName',
  `method_name` varchar(64) NOT NULL COMMENT 'methodName',
  `arguments` varchar(2048) NOT NULL DEFAULT '' COMMENT 'arguments',
  `trace_id` char(32) NOT NULL DEFAULT '' COMMENT 'traceId',
  `last_id` char(36) NOT NULL DEFAULT '' COMMENT 'lastId',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `IX_Namespace` (`namespace_name`,`app_id`,`executor_name`, `task_name`, `process_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线程池任务表';
