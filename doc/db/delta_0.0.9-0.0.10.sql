--0.0.9->0.0.10
ALTER TABLE jedi_config.app ADD column owner_name varchar(64) NOT NULL DEFAULT 'default' COMMENT 'ownerName' after app_description;
ALTER TABLE jedi_consumer.app ADD column owner_name varchar(64) NOT NULL DEFAULT 'default' COMMENT 'ownerName' after app_description;

ALTER TABLE jedi_config.app CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.audit CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.`release` CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.release_history CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.executor CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.item CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.executor_lock CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.namespace CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.role CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.user_role CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.permission CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.role_permission CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';

ALTER TABLE jedi_config.app CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.audit CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.`release` CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.release_history CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.executor CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.item CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.executor_lock CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.namespace CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.role CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.user_role CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.permission CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.role_permission CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';


ALTER TABLE jedi_config.app CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.audit CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.`release` CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.release_history CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.executor CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.item CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.executor_lock CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.namespace CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.role CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.user_role CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.permission CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.role_permission CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.instance CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.instance_config CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';

ALTER TABLE jedi_config.app CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.audit CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.`release` CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.release_history CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.executor CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.item CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.executor_lock CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.namespace CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.role CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.user_role CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.permission CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.role_permission CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.instance CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_config.instance_config CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';

ALTER TABLE jedi_config.release_message CHANGE data_change_last_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NOT NULL COMMENT '最后修改时间';

ALTER TABLE jedi_config.`user` ADD create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_config.`user` ADD create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_config.`user` ADD update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_config.`user` ADD update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';


ALTER TABLE jedi_config.release_message RENAME INDEX data_change_last_time TO updateTime;
ALTER TABLE jedi_config.app RENAME INDEX data_change_last_modified_time TO updateTime;
ALTER TABLE jedi_config.`release` RENAME INDEX data_change_last_modified_time TO updateTime;
ALTER TABLE jedi_config.release_history RENAME INDEX IX_DataChange_LastTime TO IX_UpdateTime;
ALTER TABLE jedi_config.executor RENAME INDEX data_change_last_modified_time TO updateTime;
ALTER TABLE jedi_config.item RENAME INDEX data_change_last_modified_time TO updateTime;
ALTER TABLE jedi_config.instance RENAME INDEX IX_data_change_last_modified_time TO IX_updateTime;
ALTER TABLE jedi_config.instance_config RENAME INDEX IX_data_change_last_modified_time TO IX_updateTime;
ALTER TABLE jedi_config.executor_lock RENAME INDEX data_change_last_modified_time TO updateTime;
ALTER TABLE jedi_config.audit RENAME INDEX data_change_last_modified_time TO updateTime;
ALTER TABLE jedi_config.namespace RENAME INDEX data_change_last_modified_time TO updateTime;
ALTER TABLE jedi_config.app RENAME INDEX nnamespaceName_AppId TO namespaceName_AppId;
ALTER TABLE jedi_config.role RENAME INDEX IX_dataChange_LastTime TO IX_updateTime;
ALTER TABLE jedi_config.user_role RENAME INDEX IX_dataChange_LastTime TO IX_updateTime;
ALTER TABLE jedi_config.permission RENAME INDEX IX_dataChange_LastTime TO IX_updateTime;
ALTER TABLE jedi_config.role_permission RENAME INDEX IX_dataChange_LastTime TO IX_updateTime;

ALTER TABLE jedi_config.release ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.release_history ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.executor ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.item ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.instance ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.instance_config ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.executor_lock ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.audit ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.namespace ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.app ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.user ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.role ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.user_role ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.permission ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_config.role_permission ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';












ALTER TABLE jedi_consumer.app CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_consumer.alarm_config CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_consumer.executor_task CHANGE data_change_created_by create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';

ALTER TABLE jedi_consumer.app CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_consumer.alarm_config CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_consumer.executor_task CHANGE data_change_last_modified_by update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';

ALTER TABLE jedi_consumer.app CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_consumer.alarm_config CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_consumer.executor_task CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_consumer.executor_task_statistics CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_consumer.executor_task_statistics_history CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_consumer.task_lock CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_consumer.executor_instance CHANGE data_change_created_time create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';

ALTER TABLE jedi_consumer.app CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_consumer.alarm_config CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_consumer.executor_task CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_consumer.executor_task_statistics CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_consumer.executor_task_statistics_history CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_consumer.task_lock CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';
ALTER TABLE jedi_consumer.executor_instance CHANGE data_change_last_modified_time update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';

ALTER TABLE jedi_consumer.`user` ADD create_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'default' NOT NULL COMMENT '创建人';
ALTER TABLE jedi_consumer.`user` ADD create_time timestamp DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '创建时间';
ALTER TABLE jedi_consumer.`user` ADD update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' NULL COMMENT '最后修改人';
ALTER TABLE jedi_consumer.`user` ADD update_time timestamp DEFAULT CURRENT_TIMESTAMP  on update CURRENT_TIMESTAMP NULL COMMENT '最后修改时间';

ALTER TABLE jedi_consumer.app RENAME INDEX data_change_last_modified_time TO updateTime;
ALTER TABLE jedi_consumer.app RENAME INDEX nnamespaceName_AppId TO namespaceName_AppId;
ALTER TABLE jedi_consumer.executor_instance RENAME INDEX IX_data_change_last_modified_time TO IX_updateTime;

ALTER TABLE jedi_consumer.alarm_config ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_consumer.app ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_consumer.user ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
ALTER TABLE jedi_consumer.executor_task ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';

ALTER TABLE jedi_consumer.executor_task_message CHANGE is_success status tinyint(3) NOT NULL DEFAULT 0 COMMENT '0:registered, 1:doing, 2:success, 3:fail, 4:rejected';
ALTER TABLE jedi_consumer.executor_task_message CHANGE failure_reason exit_message varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin comment 'exitMessage';

ALTER TABLE jedi_consumer.executor_task_message ADD exit_code varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin comment 'exitCode' AFTER status;
ALTER TABLE jedi_consumer.executor_task_message ADD end_time timestamp NOT NULL COMMENT '结束时间' AFTER task_extra_data;
update jedi_consumer.executor_task_message set end_time = record_time;
ALTER TABLE jedi_consumer.executor_task_message ADD is_recoverable bit(1) DEFAULT b'0' NOT NULL comment '1: recoverable, 0: no recoverable' AFTER end_time;
ALTER TABLE jedi_consumer.executor_task_message ADD is_recovered bit(1) DEFAULT b'0' NOT NULL comment '1: recovered, 0: no recovered' AFTER is_recoverable;
ALTER TABLE jedi_consumer.executor_task_message ADD trace_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin comment 'traceId' AFTER host;
ALTER TABLE jedi_consumer.executor_task_message ADD is_by_retryer bit(1) DEFAULT b'0' NOT NULL comment '1: byRetryer, 0: no byRetryer' AFTER trace_id;
ALTER TABLE jedi_consumer.executor_task_message ADD previous_id varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin comment 'previousId' AFTER is_by_retryer;
ALTER TABLE jedi_consumer.executor_task_message ADD parent_id varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin comment 'parentId' AFTER previous_id;
ALTER TABLE jedi_consumer.executor_task_message ADD data_source_name varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin comment 'dataSourceName' AFTER parent_id;
ALTER TABLE jedi_consumer.executor_task_message ADD is_persistent bit(1) DEFAULT b'0' NOT NULL comment '1: persistent, 0: no persistent' AFTER data_source_name;
ALTER TABLE jedi_consumer.executor_task_message ADD is_retried bit(1) DEFAULT b'0' NOT NULL comment '1: retried, 0: no retried' AFTER record_time;
ALTER TABLE jedi_consumer.executor_task_message ADD retry_id varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin comment 'retryId' AFTER is_retried;
ALTER TABLE jedi_consumer.executor_task_message ADD update_user varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin comment '最后修改人' AFTER retry_id;
ALTER TABLE jedi_consumer.executor_task_message ADD version int unsigned DEFAULT 1 NOT NULL COMMENT '版本号';
