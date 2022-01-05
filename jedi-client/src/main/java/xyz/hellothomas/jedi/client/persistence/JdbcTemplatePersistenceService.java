package xyz.hellothomas.jedi.client.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import xyz.hellothomas.jedi.client.model.JediTaskExecution;
import xyz.hellothomas.jedi.client.util.DateTimeUtil;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Thomas
 * @date 2021/12/16 21:11
 * @description
 * @version 1.0
 */
@Slf4j
public class JdbcTemplatePersistenceService implements PersistenceService, ApplicationContextAware {
    private volatile JdbcTemplate jdbcTemplate;
    private ConcurrentHashMap<String, JdbcTemplate> jdbcTemplateMap = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public int insertTaskExecution(TaskProperty taskProperty) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskProperty.getDataSourceName());
        int row = jdbcTemplate.update("INSERT INTO JEDI_TASK_EXECUTION (" +
                        "ID,NAMESPACE_NAME,APP_ID,EXECUTOR_NAME," +
                        "TASK_NAME,TASK_EXTRA_DATA,CREATE_TIME,START_TIME," +
                        "END_TIME,STATUS,EXIT_CODE,EXIT_MESSAGE," +
                        "BEAN_NAME,BEAN_TYPE_NAME,METHOD_NAME,METHOD_PARAM_TYPES," +
                        "METHOD_ARGUMENTS,RECOVERABLE,RECOVERED,HOST," +
                        "TRACE_ID,BY_RETRYER,PREVIOUS_ID,PARENT_ID," +
                        "DATA_SOURCE_NAME,LAST_UPDATED_USER,LAST_UPDATED_TIME) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                taskProperty.getId(), taskProperty.getNamespaceName(), taskProperty.getAppId(),
                taskProperty.getExecutorName(), taskProperty.getTaskName(), taskProperty.getTaskExtraData(),
                DateTimeUtil.localDateTimeToPattern2(taskProperty.getCreateTime()),
                DateTimeUtil.localDateTimeToPattern2(taskProperty.getStartTime()),
                DateTimeUtil.localDateTimeToPattern2(taskProperty.getEndTime()),
                taskProperty.getStatus(), taskProperty.getExitCode(), taskProperty.getExitMessage(),
                taskProperty.getBeanName(), taskProperty.getBeanTypeName(), taskProperty.getMethodName(),
                taskProperty.getMethodParamTypes(), taskProperty.getMethodArguments(), taskProperty.isRecoverable(),
                taskProperty.isRecovered(), taskProperty.getHost(), taskProperty.getTraceId(),
                taskProperty.isByRetryer(), taskProperty.getPreviousId(), taskProperty.getParentId(),
                taskProperty.getDataSourceName(), taskProperty.getLastUpdatedUser(),
                DateTimeUtil.getDateTimePattern2());
        log.trace("insert {} row: {}", row, taskProperty);
        return row;
    }

    @Override
    public int updateTaskExecution(TaskProperty taskProperty) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskProperty.getDataSourceName());

        int row = jdbcTemplate.update("UPDATE JEDI_TASK_EXECUTION SET START_TIME = ?, END_TIME = ?, status = ?, " +
                        "EXIT_CODE = ?, EXIT_MESSAGE = ?, LAST_UPDATED_TIME = ? WHERE ID = ?",
                DateTimeUtil.localDateTimeToPattern2(taskProperty.getStartTime()),
                DateTimeUtil.localDateTimeToPattern2(taskProperty.getEndTime()),
                taskProperty.getStatus(), taskProperty.getExitCode(), taskProperty.getExitMessage(),
                DateTimeUtil.getDateTimePattern2(), taskProperty.getId());
        log.trace("update {} row: {}", row, taskProperty);
        return row;
    }

    @Override
    public int deleteTaskExecution(TaskProperty taskProperty) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskProperty.getDataSourceName());
        int row = jdbcTemplate.update("DELETE FROM JEDI_TASK_EXECUTION WHERE ID = ?", taskProperty.getId());
        log.trace("delete {} row: {}", row, taskProperty);
        return row;
    }

    @Override
    public JediTaskExecution queryTaskExecutionById(String taskId, String dataSourceName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dataSourceName);
        List<JediTaskExecution> results = jdbcTemplate.query("SELECT ID,NAMESPACE_NAME,APP_ID,EXECUTOR_NAME," +
                        "TASK_NAME,TASK_EXTRA_DATA,CREATE_TIME,START_TIME," +
                        "END_TIME,STATUS,EXIT_CODE,EXIT_MESSAGE," +
                        "BEAN_NAME,BEAN_TYPE_NAME,METHOD_NAME,METHOD_PARAM_TYPES," +
                        "METHOD_ARGUMENTS,RECOVERABLE,RECOVERED,HOST," +
                        "TRACE_ID,BY_RETRYER,PREVIOUS_ID,PARENT_ID" +
                        "DATA_SOURCE_NAME,LAST_UPDATED_USER,LAST_UPDATED_TIME" +
                        " FROM JEDI_TASK_EXECUTION WHERE ID = ?",
                (rs, rowNum) -> buildJediTaskExecution(rs), taskId);
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public long queryCountByStatusAndRecoverable(String host, int status, boolean recoverable,
                                                 LocalDateTime appInitTime, String dataSourceName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dataSourceName);
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM JEDI_TASK_EXECUTION " +
                        "WHERE HOST = ? AND STATUS = ? AND RECOVERABLE = ? AND CREATE_TIME <= ?",
                Long.class, host, status, recoverable, DateTimeUtil.localDateTimeToPattern2(appInitTime));
    }

    @Override
    public List<JediTaskExecution> queryPageByStatusAndRecoverable(String host, int status, boolean recoverable,
                                                                   LocalDateTime appInitTime, int pageNum,
                                                                   int pageSize, String dataSourceName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dataSourceName);
        int offset = (pageNum - 1) * pageSize;
        return jdbcTemplate.query("SELECT ID,NAMESPACE_NAME,APP_ID,EXECUTOR_NAME," +
                        "TASK_NAME,TASK_EXTRA_DATA,CREATE_TIME,START_TIME," +
                        "END_TIME,STATUS,EXIT_CODE,EXIT_MESSAGE," +
                        "BEAN_NAME,BEAN_TYPE_NAME,METHOD_NAME,METHOD_PARAM_TYPES," +
                        "METHOD_ARGUMENTS,RECOVERABLE,RECOVERED,HOST," +
                        "TRACE_ID,BY_RETRYER,PREVIOUS_ID,DATA_SOURCE_NAME," +
                        "LAST_UPDATED_USER,LAST_UPDATED_TIME" +
                        " FROM JEDI_TASK_EXECUTION " +
                        "WHERE HOST = ? AND STATUS = ? AND RECOVERABLE = ? AND CREATE_TIME <= ? " +
                        "ORDER BY CREATE_TIME DESC LIMIT ?, ?",
                (rs, rowNum) -> buildJediTaskExecution(rs), host, status, recoverable,
                DateTimeUtil.localDateTimeToPattern2(appInitTime), offset, pageSize);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private JdbcTemplate getJdbcTemplate(String dataSourceName) {
        if (StringUtils.isBlank(dataSourceName)) {
            if (this.jdbcTemplate == null) {
                return buildDefaultJdbcTemplate();
            } else {
                return this.jdbcTemplate;
            }
        } else {
            JdbcTemplate jdbcTemplate = jdbcTemplateMap.get(dataSourceName);
            if (jdbcTemplate == null) {
                return buildJdbcTemplateByDb(dataSourceName);
            } else {
                return jdbcTemplate;
            }
        }
    }

    private synchronized JdbcTemplate buildJdbcTemplateByDb(String dataSourceName) {
        JdbcTemplate jdbcTemplate = jdbcTemplateMap.get(dataSourceName);
        if (jdbcTemplate == null) {
            DataSource dataSource = this.applicationContext.getBean(dataSourceName, DataSource.class);
            jdbcTemplate = new NoTxJdbcTemplate(dataSource);
            jdbcTemplateMap.put(dataSourceName, jdbcTemplate);
            return jdbcTemplate;
        } else {
            return jdbcTemplate;
        }
    }

    private synchronized JdbcTemplate buildDefaultJdbcTemplate() {
        if (this.jdbcTemplate == null) {
            DataSource dataSource = this.applicationContext.getBean(DataSource.class);
            this.jdbcTemplate = new NoTxJdbcTemplate(dataSource);
            return this.jdbcTemplate;
        } else {
            return this.jdbcTemplate;
        }
    }

    private JediTaskExecution buildJediTaskExecution(ResultSet rs) throws SQLException {
        JediTaskExecution jediTaskExecution = new JediTaskExecution();
        jediTaskExecution.setId(rs.getString("ID"));
        jediTaskExecution.setNamespaceName(rs.getString("NAMESPACE_NAME"));
        jediTaskExecution.setAppId(rs.getString("APP_ID"));
        jediTaskExecution.setExecutorName(rs.getString("EXECUTOR_NAME"));
        jediTaskExecution.setTaskName(rs.getString("TASK_NAME"));
        jediTaskExecution.setTaskExtraData(rs.getString("TASK_EXTRA_DATA"));
        String createTime = rs.getString("CREATE_TIME");
        jediTaskExecution.setCreateTime(createTime == null ? null :
                DateTimeUtil.pattern2ToLocalDateTime(createTime));
        String startTime = rs.getString("START_TIME");
        jediTaskExecution.setStartTime(startTime == null ? null :
                DateTimeUtil.pattern2ToLocalDateTime(startTime));
        String endTime = rs.getString("END_TIME");
        jediTaskExecution.setEndTime(endTime == null ? null :
                DateTimeUtil.pattern2ToLocalDateTime(endTime));
        jediTaskExecution.setStatus(rs.getInt("STATUS"));
        jediTaskExecution.setExitCode(rs.getString("EXIT_CODE"));
        jediTaskExecution.setExitMessage(rs.getString("EXIT_MESSAGE"));
        jediTaskExecution.setBeanName(rs.getString("BEAN_NAME"));
        jediTaskExecution.setBeanTypeName(rs.getString("BEAN_TYPE_NAME"));
        jediTaskExecution.setMethodName(rs.getString("METHOD_NAME"));
        jediTaskExecution.setMethodParamTypes(rs.getString("METHOD_PARAM_TYPES"));
        jediTaskExecution.setMethodArguments(rs.getString("METHOD_ARGUMENTS"));
        jediTaskExecution.setRecoverable(rs.getBoolean("RECOVERABLE"));
        jediTaskExecution.setRecovered(rs.getBoolean("RECOVERED"));
        jediTaskExecution.setHost(rs.getString("HOST"));
        jediTaskExecution.setTraceId(rs.getString("TRACE_ID"));
        jediTaskExecution.setByRetryer(rs.getBoolean("BY_RETRYER"));
        jediTaskExecution.setPreviousId(rs.getString("PREVIOUS_ID"));
        jediTaskExecution.setParentId(rs.getString("PARENT_ID"));
        jediTaskExecution.setDataSourceName(rs.getString("DATA_SOURCE_NAME"));
        jediTaskExecution.setLastUpdatedUser(rs.getString("LAST_UPDATED_USER"));
        String lastUpdated = rs.getString("LAST_UPDATED_TIME");
        jediTaskExecution.setLastUpdatedTime(lastUpdated == null ? null :
                DateTimeUtil.pattern2ToLocalDateTime(lastUpdated));
        return jediTaskExecution;
    }
}
