ALTER TABLE config.release_history CHANGE `Id` id int unsigned auto_increment NOT NULL COMMENT '自增Id';
ALTER TABLE config.audit CHANGE `Id` id int unsigned auto_increment NOT NULL COMMENT '主键';

ALTER TABLE config.release_history MODIFY COLUMN operation_context VARCHAR(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发布上下文信息';
ALTER TABLE config.`release` MODIFY COLUMN configurations varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发布配置';
