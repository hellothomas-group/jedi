package xyz.hellothomas.jedi.client.persistence;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import xyz.hellothomas.jedi.client.model.JediTaskExecution;
import xyz.hellothomas.jedi.client.util.DateTimeUtil;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Thomas
 * @date 2021/12/16 21:11
 * @description
 * @version 1.0
 */
public class JdbcTemplatePersistenceService implements PersistenceService, ApplicationContextAware {
    private JdbcTemplate jdbcTemplate;
    private ConcurrentHashMap<String, JdbcTemplate> jdbcTemplateMap = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public int insertTaskExecution(TaskProperty taskProperty) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskProperty);
        return jdbcTemplate.update("INSERT INTO JEDI_TASK_EXECUTION (" +
                        "ID,NAMESPACE_NAME,APP_ID,EXECUTOR_NAME," +
                        "TASK_NAME,CREATE_TIME,START_TIME,END_TIME," +
                        "STATUS,EXIT_CODE,EXIT_MESSAGE,BEAN_NAME," +
                        "BEAN_TYPE_NAME,METHOD_NAME,METHOD_PARAM_TYPES,METHOD_ARGUMENTS," +
                        "TRACE_ID,PREVIOUS_ID,DATA_SOURCE_NAME,LAST_UPDATED) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                taskProperty.getId(), taskProperty.getNamespaceName(), taskProperty.getAppId(),
                taskProperty.getExecutorName(), taskProperty.getTaskName(),
                DateTimeUtil.localDateTimeToPattern2(taskProperty.getCreateTime()),
                DateTimeUtil.localDateTimeToPattern2(taskProperty.getStartTime()),
                DateTimeUtil.localDateTimeToPattern2(taskProperty.getEndTime()),
                taskProperty.getStatus(), taskProperty.getExitCode(), taskProperty.getExitMessage(),
                taskProperty.getBeanName(), taskProperty.getBeanTypeName(), taskProperty.getMethodName(),
                taskProperty.getMethodParamTypes(), taskProperty.getMethodArguments(), taskProperty.getTraceId(),
                taskProperty.getPreviousId(), taskProperty.getDataSourceName(), DateTimeUtil.getDateTimePattern2());
    }

    @Override
    public int updateTaskExecution(TaskProperty taskProperty) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskProperty);

        return jdbcTemplate.update("UPDATE JEDI_TASK_EXECUTION SET START_TIME = ?, END_TIME = ?, status = ?, " +
                        "EXIT_CODE = ?, EXIT_MESSAGE = ?, LAST_UPDATED = ? WHERE ID = ?",
                DateTimeUtil.localDateTimeToPattern2(taskProperty.getStartTime()),
                DateTimeUtil.localDateTimeToPattern2(taskProperty.getEndTime()),
                taskProperty.getStatus(), taskProperty.getExitCode(), taskProperty.getExitMessage(),
                DateTimeUtil.getDateTimePattern2(), taskProperty.getId());
    }

    @Override
    public int deleteTaskExecution(TaskProperty taskProperty) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskProperty);
        return jdbcTemplate.update("DELETE FROM JEDI_TASK_EXECUTION WHERE ID = ?", taskProperty.getId());
    }

    @Override
    public JediTaskExecution queryTaskExecution(TaskProperty taskProperty) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskProperty);
        List<JediTaskExecution> results = jdbcTemplate.query("select * from JEDI_TASK_EXECUTION",
                new RowMapper<JediTaskExecution>() {
                    @Override
                    public JediTaskExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
                        JediTaskExecution jediTaskExecution = new JediTaskExecution();
                        jediTaskExecution.setId(rs.getString("ID"));
                        jediTaskExecution.setCreateTime(DateTimeUtil.pattern2ToLocalDateTime(rs.getString(
                                "CREATE_TIME")));
                        return jediTaskExecution;
                    }
                });
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private JdbcTemplate getJdbcTemplate(TaskProperty taskProperty) {
        JdbcTemplate jdbcTemplate;
        if (StringUtils.isBlank(taskProperty.getDataSourceName())) {
            if (this.jdbcTemplate == null) {
                jdbcTemplate = this.applicationContext.getBean(JdbcTemplate.class);
                this.jdbcTemplate = jdbcTemplate;
            } else {
                return this.jdbcTemplate;
            }
        } else {
            jdbcTemplate = jdbcTemplateMap.get(taskProperty.getDataSourceName());
            if (jdbcTemplate == null) {
                DataSource dataSource = this.applicationContext.getBean(taskProperty.getDataSourceName(),
                        DataSource.class);
                jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplateMap.put(taskProperty.getDataSourceName(), jdbcTemplate);
            }
        }
        return jdbcTemplate;
    }
}
