CREATE database if NOT EXISTS `my_monitor` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `my_monitor`;

SET NAMES utf8mb4;

CREATE TABLE `monitor_message` (
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

CREATE TABLE `executor_ticker_message` (
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

CREATE TABLE `executor_task_message` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `app_id` varchar(32) NOT NULL comment 'appId',
  `namespace` varchar(32) NOT NULL comment 'namespace名称',
  `message_type` varchar(32) NOT NULL comment '消息名称',
  `pool_name` varchar(64) NOT NULL comment '线程池名称',
  `task_name` varchar(64) NOT NULL comment '任务名称',
  `execution_time` bigint(20) NOT NULL comment '执行时间',
  `task_extra_data` varchar(2048) comment '任务附加信息',
  `host` varchar(15) NOT NULL comment '主机',
  `record_time` timestamp NOT NULL comment '记录时间',
  `create_time` timestamp default CURRENT_TIMESTAMP NOT NULL comment '生成时间',
  `update_time` timestamp default CURRENT_TIMESTAMP NOT NULL on update CURRENT_TIMESTAMP comment '最后更新时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线程池任务消息';

CREATE TABLE `executor_shutdown_message` (
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