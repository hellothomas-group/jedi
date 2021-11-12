CREATE database if NOT EXISTS `jedi_config` default character set utf8mb4 collate utf8mb4_bin;
use `jedi_config`;

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `release_message` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `message` varchar(1024) NOT NULL DEFAULT '' COMMENT '发布的消息内容',
  `data_change_last_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `data_change_last_time` (`data_change_last_time`),
  KEY `IX_message` (`message`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发布消息';

CREATE TABLE IF NOT EXISTS `release` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `release_key` varchar(64) NOT NULL DEFAULT '' COMMENT '发布的Key',
  `name` varchar(64) NOT NULL DEFAULT 'default' COMMENT '发布名字',
  `comment` varchar(256) DEFAULT NULL COMMENT '发布说明',
  `namespace_name` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'namespaceName',
  `app_id` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'appId',
  `executor_name` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'executorName',
  `configurations` varchar(4096) NOT NULL COMMENT '发布配置',
  `is_abandoned` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否废弃',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `namespaceName_appId_executorName` (`namespace_name`(191),`app_id`(191),`executor_name`(191)),
  KEY `data_change_last_modified_time` (`data_change_last_modified_time`),
  KEY `IX_release_key` (`release_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发布';

CREATE TABLE IF NOT EXISTS `release_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `namespace_name` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'namespaceName',
  `app_id` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'appId',
  `executor_name` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'executorName',
  `release_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '关联的releaseId',
  `previous_release_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '前一次发布的releaseId',
  `operation` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '发布类型，0: 普通发布，1: 回滚',
  `operation_context` varchar(4096) NOT NULL COMMENT '发布上下文信息',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `IX_Namespace` (`namespace_name`,`app_id`,`executor_name`),
  KEY `IX_ReleaseId` (`release_id`),
  KEY `IX_DataChange_LastTime` (`data_change_last_modified_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发布历史';

CREATE TABLE IF NOT EXISTS `executor` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `namespace_name` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'namespaceName',
  `app_id` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'appId',
  `executor_name` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'executorName',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `namespaceName_appId_executorName` (`namespace_name`(191),`app_id`(191),`executor_name`(191)),
  KEY `data_change_last_modified_time` (`data_change_last_modified_time`),
  KEY `IX_executor_name` (`executor_name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='执行器';

CREATE TABLE IF NOT EXISTS `item` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `executor_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'executorId',
  `configuration` varchar(1024) NOT NULL COMMENT '配置项值',
  `comment` varchar(1024) DEFAULT '' COMMENT '注释',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `IX_executor_id` (`executor_id`),
  KEY `data_change_last_modified_time` (`data_change_last_modified_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配置项目';

CREATE TABLE IF NOT EXISTS `instance` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `namespace_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'namespaceName',
  `app_id` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'appId',
  `ip` varchar(32) NOT NULL DEFAULT '' COMMENT 'instance ip',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IX_UNIQUE_KEY` (`namespace_name`,`app_id`,`ip`),
  KEY `IX_ip` (`ip`),
  KEY `IX_data_change_last_modified_time` (`data_change_last_modified_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='使用配置的应用实例';

CREATE TABLE IF NOT EXISTS `instance_config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `instance_id` int(11) unsigned DEFAULT NULL COMMENT 'instanceId',
  `config_namespace_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'configNamespaceName',
  `config_app_id` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'configAppId',
  `config_executor_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'configExecutorName',
  `release_key` varchar(64) NOT NULL DEFAULT '' COMMENT '发布的Key',
  `release_delivery_time` timestamp NULL DEFAULT NULL COMMENT '配置获取时间',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IX_UNIQUE_KEY` (`instance_id`,`config_app_id`,`config_executor_name`),
  KEY `IX_release_key` (`release_key`),
  KEY `IX_data_change_last_modified_time` (`data_change_last_modified_time`),
  KEY `IX_valid_exectuor` (`config_namespace_name`,`config_app_id`,`config_executor_name`,`data_change_last_modified_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用实例的配置信息';

CREATE TABLE IF NOT EXISTS `executor_lock` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `executor_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'executorId',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IX_executor_id` (`executor_id`),
  KEY `data_change_last_modified_time` (`data_change_last_modified_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='executor的编辑锁';

CREATE TABLE IF NOT EXISTS `audit` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `entity_name` varchar(50) NOT NULL DEFAULT 'default' COMMENT '表名',
  `entity_id` int(11) unsigned DEFAULT NULL COMMENT '记录ID',
  `operation_name` varchar(50) NOT NULL DEFAULT 'default' COMMENT '操作类型',
  `comment` varchar(500) DEFAULT NULL COMMENT '备注',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `data_change_last_modified_time` (`data_change_last_modified_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志审计表';

CREATE TABLE IF NOT EXISTS `namespace` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'namespace名字，注意，需要全局唯一',
  `description` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'namespace描述',
  `org_id` varchar(32) NOT NULL DEFAULT 'default' COMMENT '部门Id',
  `org_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT '部门名字',
  `owner_name` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'ownerName',
  `owner_email` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'ownerEmail',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `name` (`name`(191)),
  KEY `data_change_last_modified_time` (`data_change_last_modified_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='命名空间表';

CREATE TABLE IF NOT EXISTS `app` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `namespace_name` varchar(32) NOT NULL DEFAULT '' COMMENT 'namespace名字，注意，需要全局唯一',
  `app_id` varchar(32) NOT NULL DEFAULT '' COMMENT 'appId',
  `app_description` varchar(64) NOT NULL DEFAULT '' COMMENT 'app描述',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `IX_app_id` (`app_id`),
  KEY `nnamespaceName_AppId` (`namespace_name`,`app_id`),
  KEY `data_change_last_modified_time` (`data_change_last_modified_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用定义';

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `user_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT '用户名',
  `real_name` varchar(64) NOT NULL DEFAULT 'default' COMMENT '用户真实名',
  `password` varchar(64) NOT NULL DEFAULT 'default' COMMENT '密码',
  `email` varchar(64) NOT NULL DEFAULT 'default' COMMENT '邮箱地址',
  `is_manual` bit(1) NOT NULL DEFAULT b'0' COMMENT '0: 系统生成, 1: 手工生成 ',
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '1: 有效, 0: 无效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IX_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `role_name` varchar(256) NOT NULL DEFAULT '' COMMENT 'roleName',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) DEFAULT '' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `IX_roleName` (`role_name`(191)),
  KEY `IX_dataChange_LastTime` (`data_change_last_modified_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `user_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `user_id` varchar(128) DEFAULT '' COMMENT '用户身份标识',
  `role_id` int(10) unsigned DEFAULT NULL COMMENT 'roleId',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `data_change_created_by` varchar(32) DEFAULT '' COMMENT '创建人邮箱前缀',
  `data_change_created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_change_last_modified_by` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `data_change_last_modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `IX_dataChange_lastTime` (`data_change_last_modified_time`),
  KEY `IX_roleId` (`role_id`),
  KEY `IX_userId_roleId` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和role的绑定表';
