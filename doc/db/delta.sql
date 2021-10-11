ALTER TABLE consumer.executor_task_message ADD column wait_time bigint(20) NOT NULL comment '等待时间' after task_name;
