--0.0.9
ALTER TABLE jedi_consumer.executor_task_message ADD column wait_time bigint(20) NOT NULL comment '等待时间' after task_name;
ALTER TABLE jedi_consumer.executor_task_statistics ADD column total_time_max bigint(20) NOT NULL DEFAULT 0 comment '最长总时间'
 after failure_ratio;
ALTER TABLE jedi_consumer.executor_task_statistics ADD column total_time_min bigint(20) NOT NULL DEFAULT 0 comment '最短总时间'
 after total_time_max;
ALTER TABLE jedi_consumer.executor_task_statistics ADD column wait_time_max bigint(20) NOT NULL DEFAULT 0 comment '等待最长时间'
 after total_time_min;
ALTER TABLE jedi_consumer.executor_task_statistics ADD column wait_time_min bigint(20) NOT NULL DEFAULT 0 comment '等待最长时间'
 after wait_time_max;
ALTER TABLE jedi_consumer.executor_task_statistics_history ADD column total_time_max bigint(20) NOT NULL DEFAULT 0 comment '最长总时间'
 after failure_ratio;
ALTER TABLE jedi_consumer.executor_task_statistics_history ADD column total_time_min bigint(20) NOT NULL DEFAULT 0 comment '最短总时间'
 after total_time_max;
ALTER TABLE jedi_consumer.executor_task_statistics_history ADD column wait_time_max bigint(20) NOT NULL DEFAULT 0 comment '等待最长时间'
 after total_time_min;
ALTER TABLE jedi_consumer.executor_task_statistics_history ADD column wait_time_min bigint(20) NOT NULL DEFAULT 0 comment '等待最长时间'
 after wait_time_max;

CREATE INDEX `executor_host_recordTime_IDX` ON jedi_consumer.executor_ticker_message (namespace, app_id, pool_name,
host, record_time);
CREATE INDEX `updateTime_IDX` ON jedi_consumer.executor_ticker_message (update_time);
CREATE INDEX `executor_host_task_recordTime_IDX` ON jedi_consumer.executor_task_message (namespace, app_id,
pool_name, task_name, record_time);
CREATE INDEX `updateTime_IDX` ON jedi_consumer.executor_task_message (update_time);

CREATE TABLE IF NOT EXISTS `executor_task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `namespace_name` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'namespaceName',
  `app_id` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'appId',
  `executor_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'executorName',
  `task_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'taskName',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IX_Namespace` (`namespace_name`,`app_id`,`executor_name`, `task_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线程池任务表';
