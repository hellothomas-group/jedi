--0.0.10
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
