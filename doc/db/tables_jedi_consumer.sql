CREATE database if NOT EXISTS `my_monitor` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `my_monitor`;

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `monitor_message` (
  `id` varchar(36) NOT NULL comment 'id',
  `app_id` varchar(32) NOT NULL comment 'appId',
  `namespace` varchar(32) NOT NULL comment 'namespace名称',
  `message_type` varchar(32) NOT NULL comment '消息名称',
  `content` varchar(4096) comment '消息内容',
  `host` varchar(15) NOT NULL comment '主机',
  `record_time` timestamp NOT NULL comment '记录时间',
  `create_time` timestamp default CURRENT_TIMESTAMP NOT NULL comment '生成时间',
  `update_time` timestamp default CURRENT_TIMESTAMP NOT NULL on update CURRENT_TIMESTAMP comment '最后更新时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='监控消息';

CREATE TABLE IF NOT EXISTS `executor_ticker_message` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `app_id` varchar(32) NOT NULL comment 'appId',
  `namespace` varchar(32) NOT NULL comment 'namespace名称',
  `message_type` varchar(32) NOT NULL comment '消息名称',
  `pool_name` varchar(64) NOT NULL comment '线程池名称',
  `core_pool_size` int(11) NOT NULL comment '核心线程数',
  `maximum_pool_size` int(11) NOT NULL comment '最大允许的线程数',
  `pool_size` int(11) NOT NULL comment '实时线程数',
  `active_count` int(11) NOT NULL comment '实时运行线程数',
  `largest_pool_size` int(11) NOT NULL comment '历史最大线程数',
  `queue_type` varchar(64) NOT NULL comment '队列类型',
  `queue_size` int(11) NOT NULL comment '队列已使用容量',
  `queue_remaining_capacity` int(11) NOT NULL comment '队列剩余容量',
  `task_count` bigint(20) NOT NULL comment '任务总数',
  `completed_task_count` bigint(20) NOT NULL comment '已执行任务数',
  `reject_count` bigint(20) NOT NULL comment '拒绝任务数',
  `is_shutdown` tinyint(1) unsigned NOT NULL comment '线程池是否关闭',
  `is_terminated` tinyint(1) unsigned NOT NULL comment '线程池是否终止',
  `host` varchar(15) NOT NULL comment '主机',
  `record_time` timestamp NOT NULL comment '记录时间',
  `create_time` timestamp default CURRENT_TIMESTAMP NOT NULL comment '生成时间',
  `update_time` timestamp default CURRENT_TIMESTAMP NOT NULL on update CURRENT_TIMESTAMP comment '最后更新时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线程池打点消息';

CREATE TABLE IF NOT EXISTS `executor_task_message` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `app_id` varchar(32) NOT NULL comment 'appId',
  `namespace` varchar(32) NOT NULL comment 'namespace名称',
  `message_type` varchar(32) NOT NULL comment '消息名称',
  `pool_name` varchar(64) NOT NULL comment '线程池名称',
  `task_name` varchar(64) NOT NULL comment '任务名称',
  `execution_time` bigint(20) NOT NULL comment '执行时间',
  `is_success` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: success, 0: fail',
  `failure_reason` varchar(1024) comment '失败原因',
  `task_extra_data` varchar(1024) comment '任务附加信息',
  `host` varchar(15) NOT NULL comment '主机',
  `record_time` timestamp NOT NULL comment '记录时间',
  `create_time` timestamp default CURRENT_TIMESTAMP NOT NULL comment '生成时间',
  `update_time` timestamp default CURRENT_TIMESTAMP NOT NULL on update CURRENT_TIMESTAMP comment '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线程池任务消息';

CREATE TABLE IF NOT EXISTS `executor_shutdown_message` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `app_id` varchar(32) NOT NULL comment 'appId',
  `namespace` varchar(32) NOT NULL comment 'namespace名称',
  `message_type` varchar(32) NOT NULL comment '消息名称',
  `pool_name` varchar(64) NOT NULL comment '线程池名称',
  `completed_task_count` bigint(20) NOT NULL comment '已执行任务数',
  `executing_task_count` bigint(20) NOT NULL comment '正在执行任务数',
  `to_execute_task_count` bigint(20) NOT NULL comment '未执行任务数',
  `host` varchar(15) NOT NULL comment '主机',
  `record_time` timestamp NOT NULL comment '记录时间',
  `create_time` timestamp default CURRENT_TIMESTAMP NOT NULL comment '生成时间',
  `update_time` timestamp default CURRENT_TIMESTAMP NOT NULL on update CURRENT_TIMESTAMP comment '最后更新时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线程池关闭消息';

CREATE TABLE IF NOT EXISTS `alarm_config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `namespace_name` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'namespaceName',
  `app_id` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'appId',
  `executor_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'executorName',
  `configuration` varchar(1024) NOT NULL COMMENT '配置项值',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) DEFAULT '' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IX_UNIQUE_KEY` (`namespace_name`,`app_id`,`executor_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警配置';

CREATE TABLE IF NOT EXISTS `executor_task_statistics` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `statistics_date` date NOT NULL COMMENT '统计日期',
  `namespace_name` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'namespaceName',
  `app_id` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'appId',
  `executor_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'executorName',
  `task_name` varchar(64) NOT NULL comment '任务名称',
  `total` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行总数',
  `failure` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行失败总数',
  `failure_ratio` decimal(5,4) unsigned NOT NULL DEFAULT 0 COMMENT '执行失败比例',
  `execution_time_max` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行最长时间',
  `execution_time_min` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行最短时间',
  `execution_time_line_95` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行时间95线',
  `execution_time_line_99` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行时间99线',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `version` int(11) unsigned NOT NULL DEFAULT 1 COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IX_UNIQUE_KEY` (`namespace_name`,`app_id`,`executor_name`, `task_name`, `statistics_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线程池任务每日统计表';

CREATE TABLE IF NOT EXISTS `executor_task_statistics_history` (
  `id` int(11) unsigned NOT NULL COMMENT 'Id',
  `statistics_date` date NOT NULL COMMENT '统计日期',
  `namespace_name` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'namespaceName',
  `app_id` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'appId',
  `executor_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'executorName',
  `task_name` varchar(64) NOT NULL comment '任务名称',
  `total` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行总数',
  `failure` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行失败总数',
  `failure_ratio` decimal(5,4) unsigned NOT NULL DEFAULT 0 COMMENT '执行失败比例',
  `execution_time_max` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行最长时间',
  `execution_time_min` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行最短时间',
  `execution_time_line_95` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行时间95线',
  `execution_time_line_99` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '执行时间99线',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `version` int(11) unsigned NOT NULL DEFAULT 1 COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `IX_KEY` (`namespace_name`,`app_id`,`executor_name`, `task_name`, `statistics_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线程池任务历史统计表';

CREATE TABLE IF NOT EXISTS `task_lock` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `task_date` date NOT NULL COMMENT '任务日期',
  `task_name` varchar(32) NOT NULL COMMENT '任务名称',
  `is_locked` bit(1) NOT NULL COMMENT '1: locked, 0: unlocked',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `version` int(11) unsigned NOT NULL DEFAULT 1 COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IX_UNIQUE_KEY` (`task_date`,`task_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务锁表';
