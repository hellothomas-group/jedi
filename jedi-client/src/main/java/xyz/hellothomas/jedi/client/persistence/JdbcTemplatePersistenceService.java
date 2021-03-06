package xyz.hellothomas.jedi.client.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import xyz.hellothomas.jedi.client.model.JediTaskExecution;
import xyz.hellothomas.jedi.client.util.DBUtil;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
                        "METHOD_ARGUMENTS,IS_RECOVERABLE,IS_RECOVERED,HOST," +
                        "MACHINE_ID,TRACE_ID,IS_BY_RETRYER,PREVIOUS_ID," +
                        "PARENT_ID,IS_EXECUTED_BY_PARENT_TASK_THREAD,DATA_SOURCE_NAME,LAST_UPDATED_USER," +
                        "LAST_UPDATED_TIME) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?)",
                taskProperty.getId(), taskProperty.getNamespaceName(), taskProperty.getAppId(),
                taskProperty.getExecutorName(), taskProperty.getTaskName(), taskProperty.getTaskExtraData(),
                taskProperty.getCreateTime(),
                taskProperty.getStartTime(),
                taskProperty.getEndTime(),
                taskProperty.getStatus(), taskProperty.getExitCode(), taskProperty.getExitMessage(),
                taskProperty.getBeanName(), taskProperty.getBeanTypeName(), taskProperty.getMethodName(),
                taskProperty.getMethodParamTypes(), taskProperty.getMethodArguments(), taskProperty.isRecoverable(),
                taskProperty.isRecovered(), taskProperty.getHost(), taskProperty.getMachineId(),
                taskProperty.getTraceId(), taskProperty.isByRetryer(), taskProperty.getPreviousId(),
                taskProperty.getParentId(), taskProperty.isExecutedByParentTaskThread(),
                taskProperty.getDataSourceName(), taskProperty.getLastUpdatedUser(),
                LocalDateTime.now());
        log.trace("insert {} row: {}", row, taskProperty);
        return row;
    }

    @Override
    public int updateTaskExecution(TaskProperty taskProperty) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(taskProperty.getDataSourceName());

        int row = jdbcTemplate.update("UPDATE JEDI_TASK_EXECUTION SET START_TIME = ?, END_TIME = ?, status = ?, " +
                        "EXIT_CODE = ?, EXIT_MESSAGE = ?, IS_EXECUTED_BY_PARENT_TASK_THREAD = ?," +
                        " LAST_UPDATED_TIME = ? WHERE ID = ?",
                taskProperty.getStartTime(),
                taskProperty.getEndTime(),
                taskProperty.getStatus(), taskProperty.getExitCode(), taskProperty.getExitMessage(),
                taskProperty.isExecutedByParentTaskThread(), LocalDateTime.now(), taskProperty.getId());
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
                        "METHOD_ARGUMENTS,IS_RECOVERABLE,IS_RECOVERED,HOST," +
                        "MACHINE_ID,TRACE_ID,IS_BY_RETRYER,PREVIOUS_ID," +
                        "PARENT_ID,IS_EXECUTED_BY_PARENT_TASK_THREAD,DATA_SOURCE_NAME,LAST_UPDATED_USER," +
                        "LAST_UPDATED_TIME" +
                        " FROM JEDI_TASK_EXECUTION WHERE ID = ?",
                (rs, rowNum) -> buildJediTaskExecution(rs), taskId);
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public long queryCountByStatusAndRecoverable(String machineId, int status, boolean isRecoverable,
                                                 LocalDateTime appInitTime, String dataSourceName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dataSourceName);
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM JEDI_TASK_EXECUTION " +
                        "WHERE STATUS = ? AND MACHINE_ID = ? AND IS_RECOVERABLE = ? AND CREATE_TIME <= ?",
                Long.class, status, machineId, isRecoverable, appInitTime);
    }

    @Override
    public List<JediTaskExecution> queryPageByStatusAndRecoverable(String machineId, int status, boolean isRecoverable,
                                                                   LocalDateTime appInitTime, int pageNum,
                                                                   int pageSize, String dataSourceName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dataSourceName);
        if (DBUtil.DBType.ORACLE == ((NoTxJdbcTemplate) jdbcTemplate).getDbType()) {
            return jdbcTemplate.query("SELECT ROWNUM ROW_NUM, t1.* FROM (" +
                            "SELECT ID,NAMESPACE_NAME,APP_ID,EXECUTOR_NAME," +
                            "TASK_NAME,TASK_EXTRA_DATA,CREATE_TIME,START_TIME," +
                            "END_TIME,STATUS,EXIT_CODE,EXIT_MESSAGE," +
                            "BEAN_NAME,BEAN_TYPE_NAME,METHOD_NAME,METHOD_PARAM_TYPES," +
                            "METHOD_ARGUMENTS,IS_RECOVERABLE,IS_RECOVERED,HOST," +
                            "MACHINE_ID,TRACE_ID,IS_BY_RETRYER,PREVIOUS_ID," +
                            "PARENT_ID,IS_EXECUTED_BY_PARENT_TASK_THREAD,DATA_SOURCE_NAME,LAST_UPDATED_USER," +
                            "LAST_UPDATED_TIME" +
                            " FROM JEDI_TASK_EXECUTION " +
                            "WHERE STATUS = ? AND MACHINE_ID = ? AND IS_RECOVERABLE = ? AND CREATE_TIME <= ? " +
                            "ORDER BY CREATE_TIME DESC) t1 " +
                            "WHERE ROWNUM <= ?",
                    (rs, rowNum) -> buildJediTaskExecution(rs), status, machineId, isRecoverable,
                    appInitTime, pageSize);
        } else {
            return jdbcTemplate.query("SELECT ID,NAMESPACE_NAME,APP_ID,EXECUTOR_NAME," +
                            "TASK_NAME,TASK_EXTRA_DATA,CREATE_TIME,START_TIME," +
                            "END_TIME,STATUS,EXIT_CODE,EXIT_MESSAGE," +
                            "BEAN_NAME,BEAN_TYPE_NAME,METHOD_NAME,METHOD_PARAM_TYPES," +
                            "METHOD_ARGUMENTS,IS_RECOVERABLE,IS_RECOVERED,HOST," +
                            "MACHINE_ID,TRACE_ID,IS_BY_RETRYER,PREVIOUS_ID," +
                            "PARENT_ID,IS_EXECUTED_BY_PARENT_TASK_THREAD,DATA_SOURCE_NAME,LAST_UPDATED_USER," +
                            "LAST_UPDATED_TIME" +
                            " FROM JEDI_TASK_EXECUTION " +
                            "WHERE STATUS = ? AND MACHINE_ID = ? AND IS_RECOVERABLE = ? AND CREATE_TIME <= ? " +
                            "ORDER BY CREATE_TIME DESC LIMIT ?",
                    (rs, rowNum) -> buildJediTaskExecution(rs), status, machineId, isRecoverable,
                    appInitTime, pageSize);
        }
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
            DBUtil.DBType dbType = DBUtil.getDBType(dataSource);
            jdbcTemplate = new NoTxJdbcTemplate(dataSource, dbType);
            jdbcTemplateMap.put(dataSourceName, jdbcTemplate);
            return jdbcTemplate;
        } else {
            return jdbcTemplate;
        }
    }

    private synchronized JdbcTemplate buildDefaultJdbcTemplate() {
        if (this.jdbcTemplate == null) {
            DataSource dataSource = this.applicationContext.getBean(DataSource.class);
            DBUtil.DBType dbType = DBUtil.getDBType(dataSource);
            this.jdbcTemplate = new NoTxJdbcTemplate(dataSource, dbType);
            return this.jdbcTemplate;
        } else {
            return this.jdbcTemplate;
        }
    }

    private JediTaskExecution buildJediTaskExecution(ResultSet rs) throws SQLException {
        ZoneId zone = ZoneId.systemDefault();
        JediTaskExecution jediTaskExecution = new JediTaskExecution();
        jediTaskExecution.setId(rs.getString("ID"));
        jediTaskExecution.setNamespaceName(rs.getString("NAMESPACE_NAME"));
        jediTaskExecution.setAppId(rs.getString("APP_ID"));
        jediTaskExecution.setExecutorName(rs.getString("EXECUTOR_NAME"));
        jediTaskExecution.setTaskName(rs.getString("TASK_NAME"));
        jediTaskExecution.setTaskExtraData(rs.getString("TASK_EXTRA_DATA"));
        Timestamp createTime = rs.getTimestamp("CREATE_TIME");
        jediTaskExecution.setCreateTime(createTime == null ? null :
                LocalDateTime.ofInstant(Instant.ofEpochMilli(createTime.getTime()), zone));
        Timestamp startTime = rs.getTimestamp("START_TIME");
        jediTaskExecution.setStartTime(startTime == null ? null :
                LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime.getTime()), zone));
        Timestamp endTime = rs.getTimestamp("END_TIME");
        jediTaskExecution.setEndTime(endTime == null ? null :
                LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime.getTime()), zone));
        jediTaskExecution.setStatus(rs.getInt("STATUS"));
        jediTaskExecution.setExitCode(rs.getString("EXIT_CODE"));
        jediTaskExecution.setExitMessage(rs.getString("EXIT_MESSAGE"));
        jediTaskExecution.setBeanName(rs.getString("BEAN_NAME"));
        jediTaskExecution.setBeanTypeName(rs.getString("BEAN_TYPE_NAME"));
        jediTaskExecution.setMethodName(rs.getString("METHOD_NAME"));
        jediTaskExecution.setMethodParamTypes(rs.getString("METHOD_PARAM_TYPES"));
        jediTaskExecution.setMethodArguments(rs.getString("METHOD_ARGUMENTS"));
        jediTaskExecution.setRecoverable(rs.getBoolean("IS_RECOVERABLE"));
        jediTaskExecution.setRecovered(rs.getBoolean("IS_RECOVERED"));
        jediTaskExecution.setHost(rs.getString("HOST"));
        jediTaskExecution.setMachineId(rs.getString("MACHINE_ID"));
        jediTaskExecution.setTraceId(rs.getString("TRACE_ID"));
        jediTaskExecution.setByRetryer(rs.getBoolean("IS_BY_RETRYER"));
        jediTaskExecution.setPreviousId(rs.getString("PREVIOUS_ID"));
        jediTaskExecution.setParentId(rs.getString("PARENT_ID"));
        jediTaskExecution.setExecutedByParentTaskThread(rs.getBoolean("IS_EXECUTED_BY_PARENT_TASK_THREAD"));
        jediTaskExecution.setDataSourceName(rs.getString("DATA_SOURCE_NAME"));
        jediTaskExecution.setLastUpdatedUser(rs.getString("LAST_UPDATED_USER"));
        Timestamp lastUpdated = rs.getTimestamp("LAST_UPDATED_TIME");
        jediTaskExecution.setLastUpdatedTime(lastUpdated == null ? null :
                LocalDateTime.ofInstant(Instant.ofEpochMilli(lastUpdated.getTime()), zone));
        return jediTaskExecution;
    }
}
