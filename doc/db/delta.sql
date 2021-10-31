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
