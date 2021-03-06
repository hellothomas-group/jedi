package xyz.hellothomas.jedi.client.persistence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import xyz.hellothomas.jedi.client.util.DBUtil;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @author 80234613 唐圆
 * @date 2021/12/21 16:19
 * @descripton
 * @version 1.0
 */
@Slf4j
public class NoTxJdbcTemplate extends JdbcTemplate {

    private DBUtil.DBType dbType;

    public NoTxJdbcTemplate() {
    }

    public NoTxJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public NoTxJdbcTemplate(DataSource dataSource, boolean lazyInit) {
        super(dataSource, lazyInit);
    }

    public NoTxJdbcTemplate(DataSource dataSource, DBUtil.DBType dbType) {
        super(dataSource);
        this.dbType = dbType;
    }

    public NoTxJdbcTemplate(DataSource dataSource, boolean lazyInit, DBUtil.DBType dbType) {
        super(dataSource, lazyInit);
        this.dbType = dbType;
    }

    /**
     * Obtain a Connection from the given DataSource. Translates SQLExceptions into
     * the Spring hierarchy of unchecked generic data access exceptions, simplifying
     * calling code and making any exception that is thrown more meaningful.
     * @param dataSource the DataSource to obtain Connections from
     * @return a JDBC Connection from the given DataSource
     * @throws CannotGetJdbcConnectionException
     * if the attempt to get a Connection failed
     * @see DataSourceUtils#getConnection(DataSource)
     */
    public static Connection getConnection(DataSource dataSource) throws CannotGetJdbcConnectionException {
        try {
            return fetchConnection(dataSource);
        } catch (SQLException ex) {
            throw new CannotGetJdbcConnectionException("Failed to obtain JDBC Connection", ex);
        } catch (IllegalStateException ex) {
            throw new CannotGetJdbcConnectionException("Failed to obtain JDBC Connection: " + ex.getMessage());
        }
    }

    /**
     * Actually fetch a {@link Connection} from the given {@link DataSource},
     * defensively turning an unexpected {@code null} return value from
     * {@link DataSource#getConnection()} into an {@link IllegalStateException}.
     * @param dataSource the DataSource to obtain Connections from
     * @return a JDBC Connection from the given DataSource (never {@code null})
     * @throws SQLException if thrown by JDBC methods
     * @throws IllegalStateException if the DataSource returned a null value
     * @see DataSource#getConnection()
     * @see DataSourceUtils#doGetConnection(DataSource)
     * @see DataSourceUtils#getConnection(DataSource)
     */
    private static Connection fetchConnection(DataSource dataSource) throws SQLException {
        Assert.notNull(dataSource, "No DataSource specified");

        Connection con = dataSource.getConnection();
        if (con == null) {
            throw new IllegalStateException("DataSource returned null from getConnection(): " + dataSource);
        }
        return con;
    }

    @Override
    @Nullable
    public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
        Assert.notNull(action, "Callback object must not be null");

        Connection con = getConnection(obtainDataSource());
        boolean autoCommitModified = false;
        try {
            // 设置autoCommit
            autoCommitModified = setupAutoCommit(con);
            // Create close-suppressing Connection proxy, also preparing returned Statements.
            Connection conToUse = createConnectionProxy(con);
            return action.doInConnection(conToUse);
        } catch (SQLException ex) {
            // 重置autoCommit
            resetAutoCommit(con, autoCommitModified);
            // Release Connection early, to avoid potential connection pool deadlock
            // in the case when the exception translator hasn't been initialized yet.
            String sql = getSql(action);
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            throw translateException("ConnectionCallback", sql, ex);
        } finally {
            // 重置autoCommit
            resetAutoCommit(con, autoCommitModified);
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }

    @Override
    @Nullable
    public <T> T execute(StatementCallback<T> action) throws DataAccessException {
        Assert.notNull(action, "Callback object must not be null");

        Connection con = getConnection(obtainDataSource());
        boolean autoCommitModified = false;
        Statement stmt = null;
        try {
            // 设置autoCommit
            autoCommitModified = setupAutoCommit(con);
            stmt = con.createStatement();
            applyStatementSettings(stmt);
            T result = action.doInStatement(stmt);
            handleWarnings(stmt);
            return result;
        } catch (SQLException ex) {
            // Release Connection early, to avoid potential connection pool deadlock
            // in the case when the exception translator hasn't been initialized yet.
            String sql = getSql(action);
            JdbcUtils.closeStatement(stmt);
            stmt = null;
            // 重置autoCommit
            resetAutoCommit(con, autoCommitModified);
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            throw translateException("StatementCallback", sql, ex);
        } finally {
            JdbcUtils.closeStatement(stmt);
            // 重置autoCommit
            resetAutoCommit(con, autoCommitModified);
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }

    @Override
    @Nullable
    public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException {
        Assert.notNull(psc, "PreparedStatementCreator must not be null");
        Assert.notNull(action, "Callback object must not be null");
        if (logger.isDebugEnabled()) {
            String sql = getSql(psc);
            logger.debug("Executing prepared SQL statement" + (sql != null ? " [" + sql + "]" : ""));
        }

        Connection con = getConnection(obtainDataSource());
        boolean autoCommitModified = false;
        PreparedStatement ps = null;
        try {
            // 设置autoCommit
            autoCommitModified = setupAutoCommit(con);
            ps = psc.createPreparedStatement(con);
            applyStatementSettings(ps);
            T result = action.doInPreparedStatement(ps);
            handleWarnings(ps);
            return result;
        } catch (SQLException ex) {
            // Release Connection early, to avoid potential connection pool deadlock
            // in the case when the exception translator hasn't been initialized yet.
            if (psc instanceof ParameterDisposer) {
                ((ParameterDisposer) psc).cleanupParameters();
            }
            String sql = getSql(psc);
            psc = null;
            JdbcUtils.closeStatement(ps);
            ps = null;
            // 重置autoCommit
            resetAutoCommit(con, autoCommitModified);
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            throw translateException("PreparedStatementCallback", sql, ex);
        } finally {
            if (psc instanceof ParameterDisposer) {
                ((ParameterDisposer) psc).cleanupParameters();
            }
            JdbcUtils.closeStatement(ps);
            // 重置autoCommit
            resetAutoCommit(con, autoCommitModified);
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }

    @Override
    @Nullable
    public <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action) throws DataAccessException {
        Assert.notNull(csc, "CallableStatementCreator must not be null");
        Assert.notNull(action, "Callback object must not be null");
        if (logger.isDebugEnabled()) {
            String sql = getSql(csc);
            logger.debug("Calling stored procedure" + (sql != null ? " [" + sql + "]" : ""));
        }

        Connection con = getConnection(obtainDataSource());
        boolean autoCommitModified = false;
        CallableStatement cs = null;
        try {
            // 设置autoCommit
            autoCommitModified = setupAutoCommit(con);
            cs = csc.createCallableStatement(con);
            applyStatementSettings(cs);
            T result = action.doInCallableStatement(cs);
            handleWarnings(cs);
            return result;
        } catch (SQLException ex) {
            // Release Connection early, to avoid potential connection pool deadlock
            // in the case when the exception translator hasn't been initialized yet.
            if (csc instanceof ParameterDisposer) {
                ((ParameterDisposer) csc).cleanupParameters();
            }
            String sql = getSql(csc);
            csc = null;
            JdbcUtils.closeStatement(cs);
            cs = null;
            // 重置autoCommit
            resetAutoCommit(con, autoCommitModified);
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            throw translateException("CallableStatementCallback", sql, ex);
        } finally {
            if (csc instanceof ParameterDisposer) {
                ((ParameterDisposer) csc).cleanupParameters();
            }
            JdbcUtils.closeStatement(cs);
            // 重置autoCommit
            resetAutoCommit(con, autoCommitModified);
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }

    public DBUtil.DBType getDbType() {
        return dbType;
    }

    public void setDbType(DBUtil.DBType dbType) {
        this.dbType = dbType;
    }

    /**
     * Determine SQL from potential provider object.
     * @param sqlProvider object which is potentially an SqlProvider
     * @return the SQL string, or {@code null} if not known
     * @see SqlProvider
     */
    @Nullable
    private static String getSql(Object sqlProvider) {
        if (sqlProvider instanceof SqlProvider) {
            return ((SqlProvider) sqlProvider).getSql();
        } else {
            return null;
        }
    }

    /**
     * 设置自动提交
     *
     * @param connection
     * @return autoCommitModified
     * @throws SQLException
     */
    private boolean setupAutoCommit(Connection connection) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();

        if (autoCommit) {
            return false;
        } else {
            connection.setAutoCommit(true);
            return true;
        }
    }

    /**
     * 重置自动提交
     *
     * @param connection
     * @param autoCommitModified
     */
    private void resetAutoCommit(Connection connection, boolean autoCommitModified) {
        if (connection == null || !autoCommitModified) {
            return;
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            log.debug("resetAutoCommit error", e);
        }
    }
}
