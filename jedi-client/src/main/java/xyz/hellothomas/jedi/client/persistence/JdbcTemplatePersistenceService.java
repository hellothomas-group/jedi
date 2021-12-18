package xyz.hellothomas.jedi.client.persistence;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import xyz.hellothomas.jedi.client.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.client.util.DateTimeUtil;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

import static xyz.hellothomas.jedi.core.constants.Constants.EMPTY_STRING;

/**
 * @author Thomas
 * @date 2021/12/16 21:11
 * @description
 * @version 1.0
 */
public class JdbcTemplatePersistenceService implements PersistenceService, ApplicationContextAware {
    private ConcurrentHashMap<String, JdbcTemplate> jdbcTemplateMap = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public int insertTaskExecution(TaskPersistProperty taskPersistProperty) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskPersistProperty);
        String currentTime = DateTimeUtil.getDateTimePattern1();
        return jdbcTemplate.update("INSERT INTO JEDI_TASK_EXECUTION (id,namespace_name,app_id,executor_name,task_name," +
                        "process_status,fail_reason,bean_name,method_name,arguments,trace_id,last_id," +
                        "data_change_created_time,data_change_last_modified_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?)",
                taskPersistProperty.getId(), taskPersistProperty.getNamespaceName(), taskPersistProperty.getAppId(),
                taskPersistProperty.getExecutorName(), taskPersistProperty.getTaskName(),
                taskPersistProperty.getProcessStatus(), EMPTY_STRING, taskPersistProperty.getBeanName(),
                taskPersistProperty.getMethodName(), taskPersistProperty.getArguments(),
                taskPersistProperty.getTraceId(), taskPersistProperty.getLastId(), currentTime, currentTime);
    }

    @Override
    public int updateTaskExecution(TaskPersistProperty taskPersistProperty, TaskStatusEnum taskStatusEnum,
                                   Exception e) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskPersistProperty);
        String currentTime = DateTimeUtil.getDateTimePattern1();
        String exceptionString;
        if (e == null) {
            exceptionString = EMPTY_STRING;
        } else {
            exceptionString = e.getMessage().length() > 300 ? e.getMessage().substring(0,
                    300) : e.getMessage();
        }
        return jdbcTemplate.update("UPDATE JEDI_TASK_EXECUTION SET process_status = ?, fail_reason = ?, " +
                        "data_change_last_modified_time = ? WHERE id = ?", taskStatusEnum.getProcessStatus(),
                exceptionString, currentTime, taskPersistProperty.getId());
    }

    @Override
    public int deleteTaskExecution(TaskPersistProperty taskPersistProperty) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskPersistProperty);
        return jdbcTemplate.update("DELETE FROM JEDI_TASK_EXECUTION WHERE id = ?", taskPersistProperty.getId());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private JdbcTemplate getJdbcTemplate(TaskPersistProperty taskPersistProperty) {
        JdbcTemplate jdbcTemplate;
        if (StringUtils.isBlank(taskPersistProperty.getDataSourceName())) {
            jdbcTemplate = this.applicationContext.getBean(JdbcTemplate.class);
        } else {
            jdbcTemplate = jdbcTemplateMap.get(taskPersistProperty.getDataSourceName());
            if (jdbcTemplate == null) {
                DataSource dataSource = this.applicationContext.getBean(taskPersistProperty.getDataSourceName(),
                        DataSource.class);
                jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplateMap.put(taskPersistProperty.getDataSourceName(), jdbcTemplate);
            }
        }
        return jdbcTemplate;
    }
}
