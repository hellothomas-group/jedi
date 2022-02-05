--0.0.10->0.0.11
ALTER TABLE jedi_consumer.executor_task_message ADD is_executed_by_parent_task_thread bit(1) DEFAULT b'0' NOT NULL comment '1: true, 0: false' AFTER parent_id;