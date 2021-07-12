package xyz.hellothomas.jedi.consumer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
public class ExecutorTaskStatisticsHistory implements Serializable {
    /**
     * Id
     */
    private Integer id;

    /**
     * namespaceName
     */
    private String namespaceName;

    /**
     * appId
     */
    private String appId;

    /**
     * executorName
     */
    private String executorName;

    /**
     * 执行总数
     */
    private Integer total;

    /**
     * 执行失败总数
     */
    private Integer failure;

    /**
     * 执行失败比例
     */
    private BigDecimal failureratio;

    /**
     * 执行最长时间
     */
    private Integer executionTimeMax;

    /**
     * 执行最短时间
     */
    private Integer executionTimeMin;

    /**
     * 执行时间95线
     */
    private Integer executionTimeLine95;

    /**
     * 执行时间99线
     */
    private Integer executionTimeLine99;

    /**
     * 创建时间
     */
    private Date dataChangeCreatedTime;

    /**
     * 最后修改时间
     */
    private Date dataChangeLastModifiedTime;

    /**
     * 版本号
     */
    private Integer version;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public BigDecimal getFailureratio() {
        return failureratio;
    }

    public void setFailureratio(BigDecimal failureratio) {
        this.failureratio = failureratio;
    }

    public Integer getExecutionTimeMax() {
        return executionTimeMax;
    }

    public void setExecutionTimeMax(Integer executionTimeMax) {
        this.executionTimeMax = executionTimeMax;
    }

    public Integer getExecutionTimeMin() {
        return executionTimeMin;
    }

    public void setExecutionTimeMin(Integer executionTimeMin) {
        this.executionTimeMin = executionTimeMin;
    }

    public Integer getExecutionTimeLine95() {
        return executionTimeLine95;
    }

    public void setExecutionTimeLine95(Integer executionTimeLine95) {
        this.executionTimeLine95 = executionTimeLine95;
    }

    public Integer getExecutionTimeLine99() {
        return executionTimeLine99;
    }

    public void setExecutionTimeLine99(Integer executionTimeLine99) {
        this.executionTimeLine99 = executionTimeLine99;
    }

    public Date getDataChangeCreatedTime() {
        return dataChangeCreatedTime;
    }

    public void setDataChangeCreatedTime(Date dataChangeCreatedTime) {
        this.dataChangeCreatedTime = dataChangeCreatedTime;
    }

    public Date getDataChangeLastModifiedTime() {
        return dataChangeLastModifiedTime;
    }

    public void setDataChangeLastModifiedTime(Date dataChangeLastModifiedTime) {
        this.dataChangeLastModifiedTime = dataChangeLastModifiedTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}