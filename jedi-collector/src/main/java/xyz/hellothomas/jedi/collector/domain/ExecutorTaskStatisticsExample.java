package xyz.hellothomas.jedi.collector.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExecutorTaskStatisticsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ExecutorTaskStatisticsExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateIsNull() {
            addCriterion("statistics_date is null");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateIsNotNull() {
            addCriterion("statistics_date is not null");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateEqualTo(LocalDate value) {
            addCriterion("statistics_date =", value, "statisticsDate");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateNotEqualTo(LocalDate value) {
            addCriterion("statistics_date <>", value, "statisticsDate");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateGreaterThan(LocalDate value) {
            addCriterion("statistics_date >", value, "statisticsDate");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateGreaterThanOrEqualTo(LocalDate value) {
            addCriterion("statistics_date >=", value, "statisticsDate");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateLessThan(LocalDate value) {
            addCriterion("statistics_date <", value, "statisticsDate");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateLessThanOrEqualTo(LocalDate value) {
            addCriterion("statistics_date <=", value, "statisticsDate");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateIn(List<LocalDate> values) {
            addCriterion("statistics_date in", values, "statisticsDate");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateNotIn(List<LocalDate> values) {
            addCriterion("statistics_date not in", values, "statisticsDate");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateBetween(LocalDate value1, LocalDate value2) {
            addCriterion("statistics_date between", value1, value2, "statisticsDate");
            return (Criteria) this;
        }

        public Criteria andStatisticsDateNotBetween(LocalDate value1, LocalDate value2) {
            addCriterion("statistics_date not between", value1, value2, "statisticsDate");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameIsNull() {
            addCriterion("namespace_name is null");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameIsNotNull() {
            addCriterion("namespace_name is not null");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameEqualTo(String value) {
            addCriterion("namespace_name =", value, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameNotEqualTo(String value) {
            addCriterion("namespace_name <>", value, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameGreaterThan(String value) {
            addCriterion("namespace_name >", value, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameGreaterThanOrEqualTo(String value) {
            addCriterion("namespace_name >=", value, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameLessThan(String value) {
            addCriterion("namespace_name <", value, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameLessThanOrEqualTo(String value) {
            addCriterion("namespace_name <=", value, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameLike(String value) {
            addCriterion("namespace_name like", value, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameNotLike(String value) {
            addCriterion("namespace_name not like", value, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameIn(List<String> values) {
            addCriterion("namespace_name in", values, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameNotIn(List<String> values) {
            addCriterion("namespace_name not in", values, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameBetween(String value1, String value2) {
            addCriterion("namespace_name between", value1, value2, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andNamespaceNameNotBetween(String value1, String value2) {
            addCriterion("namespace_name not between", value1, value2, "namespaceName");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNull() {
            addCriterion("app_id is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("app_id is not null");
            return (Criteria) this;
        }

        public Criteria andAppIdEqualTo(String value) {
            addCriterion("app_id =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotEqualTo(String value) {
            addCriterion("app_id <>", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThan(String value) {
            addCriterion("app_id >", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("app_id >=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThan(String value) {
            addCriterion("app_id <", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThanOrEqualTo(String value) {
            addCriterion("app_id <=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLike(String value) {
            addCriterion("app_id like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotLike(String value) {
            addCriterion("app_id not like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(List<String> values) {
            addCriterion("app_id in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(List<String> values) {
            addCriterion("app_id not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdBetween(String value1, String value2) {
            addCriterion("app_id between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotBetween(String value1, String value2) {
            addCriterion("app_id not between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andExecutorNameIsNull() {
            addCriterion("executor_name is null");
            return (Criteria) this;
        }

        public Criteria andExecutorNameIsNotNull() {
            addCriterion("executor_name is not null");
            return (Criteria) this;
        }

        public Criteria andExecutorNameEqualTo(String value) {
            addCriterion("executor_name =", value, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameNotEqualTo(String value) {
            addCriterion("executor_name <>", value, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameGreaterThan(String value) {
            addCriterion("executor_name >", value, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameGreaterThanOrEqualTo(String value) {
            addCriterion("executor_name >=", value, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameLessThan(String value) {
            addCriterion("executor_name <", value, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameLessThanOrEqualTo(String value) {
            addCriterion("executor_name <=", value, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameLike(String value) {
            addCriterion("executor_name like", value, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameNotLike(String value) {
            addCriterion("executor_name not like", value, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameIn(List<String> values) {
            addCriterion("executor_name in", values, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameNotIn(List<String> values) {
            addCriterion("executor_name not in", values, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameBetween(String value1, String value2) {
            addCriterion("executor_name between", value1, value2, "executorName");
            return (Criteria) this;
        }

        public Criteria andExecutorNameNotBetween(String value1, String value2) {
            addCriterion("executor_name not between", value1, value2, "executorName");
            return (Criteria) this;
        }

        public Criteria andTaskNameIsNull() {
            addCriterion("task_name is null");
            return (Criteria) this;
        }

        public Criteria andTaskNameIsNotNull() {
            addCriterion("task_name is not null");
            return (Criteria) this;
        }

        public Criteria andTaskNameEqualTo(String value) {
            addCriterion("task_name =", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotEqualTo(String value) {
            addCriterion("task_name <>", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameGreaterThan(String value) {
            addCriterion("task_name >", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameGreaterThanOrEqualTo(String value) {
            addCriterion("task_name >=", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLessThan(String value) {
            addCriterion("task_name <", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLessThanOrEqualTo(String value) {
            addCriterion("task_name <=", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLike(String value) {
            addCriterion("task_name like", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotLike(String value) {
            addCriterion("task_name not like", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameIn(List<String> values) {
            addCriterion("task_name in", values, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotIn(List<String> values) {
            addCriterion("task_name not in", values, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameBetween(String value1, String value2) {
            addCriterion("task_name between", value1, value2, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotBetween(String value1, String value2) {
            addCriterion("task_name not between", value1, value2, "taskName");
            return (Criteria) this;
        }

        public Criteria andTotalIsNull() {
            addCriterion("total is null");
            return (Criteria) this;
        }

        public Criteria andTotalIsNotNull() {
            addCriterion("total is not null");
            return (Criteria) this;
        }

        public Criteria andTotalEqualTo(Long value) {
            addCriterion("total =", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotEqualTo(Long value) {
            addCriterion("total <>", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThan(Long value) {
            addCriterion("total >", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThanOrEqualTo(Long value) {
            addCriterion("total >=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThan(Long value) {
            addCriterion("total <", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThanOrEqualTo(Long value) {
            addCriterion("total <=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalIn(List<Long> values) {
            addCriterion("total in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotIn(List<Long> values) {
            addCriterion("total not in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalBetween(Long value1, Long value2) {
            addCriterion("total between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotBetween(Long value1, Long value2) {
            addCriterion("total not between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andFailureIsNull() {
            addCriterion("failure is null");
            return (Criteria) this;
        }

        public Criteria andFailureIsNotNull() {
            addCriterion("failure is not null");
            return (Criteria) this;
        }

        public Criteria andFailureEqualTo(Long value) {
            addCriterion("failure =", value, "failure");
            return (Criteria) this;
        }

        public Criteria andFailureNotEqualTo(Long value) {
            addCriterion("failure <>", value, "failure");
            return (Criteria) this;
        }

        public Criteria andFailureGreaterThan(Long value) {
            addCriterion("failure >", value, "failure");
            return (Criteria) this;
        }

        public Criteria andFailureGreaterThanOrEqualTo(Long value) {
            addCriterion("failure >=", value, "failure");
            return (Criteria) this;
        }

        public Criteria andFailureLessThan(Long value) {
            addCriterion("failure <", value, "failure");
            return (Criteria) this;
        }

        public Criteria andFailureLessThanOrEqualTo(Long value) {
            addCriterion("failure <=", value, "failure");
            return (Criteria) this;
        }

        public Criteria andFailureIn(List<Long> values) {
            addCriterion("failure in", values, "failure");
            return (Criteria) this;
        }

        public Criteria andFailureNotIn(List<Long> values) {
            addCriterion("failure not in", values, "failure");
            return (Criteria) this;
        }

        public Criteria andFailureBetween(Long value1, Long value2) {
            addCriterion("failure between", value1, value2, "failure");
            return (Criteria) this;
        }

        public Criteria andFailureNotBetween(Long value1, Long value2) {
            addCriterion("failure not between", value1, value2, "failure");
            return (Criteria) this;
        }

        public Criteria andFailureratioIsNull() {
            addCriterion("failure_ratio is null");
            return (Criteria) this;
        }

        public Criteria andFailureratioIsNotNull() {
            addCriterion("failure_ratio is not null");
            return (Criteria) this;
        }

        public Criteria andFailureratioEqualTo(BigDecimal value) {
            addCriterion("failure_ratio =", value, "failureRatio");
            return (Criteria) this;
        }

        public Criteria andFailureratioNotEqualTo(BigDecimal value) {
            addCriterion("failure_ratio <>", value, "failureRatio");
            return (Criteria) this;
        }

        public Criteria andFailureratioGreaterThan(BigDecimal value) {
            addCriterion("failure_ratio >", value, "failureRatio");
            return (Criteria) this;
        }

        public Criteria andFailureratioGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("failure_ratio >=", value, "failureRatio");
            return (Criteria) this;
        }

        public Criteria andFailureratioLessThan(BigDecimal value) {
            addCriterion("failure_ratio <", value, "failureRatio");
            return (Criteria) this;
        }

        public Criteria andFailureratioLessThanOrEqualTo(BigDecimal value) {
            addCriterion("failure_ratio <=", value, "failureRatio");
            return (Criteria) this;
        }

        public Criteria andFailureratioIn(List<BigDecimal> values) {
            addCriterion("failure_ratio in", values, "failureRatio");
            return (Criteria) this;
        }

        public Criteria andFailureratioNotIn(List<BigDecimal> values) {
            addCriterion("failure_ratio not in", values, "failureRatio");
            return (Criteria) this;
        }

        public Criteria andFailureratioBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("failure_ratio between", value1, value2, "failureRatio");
            return (Criteria) this;
        }

        public Criteria andFailureratioNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("failure_ratio not between", value1, value2, "failureRatio");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxIsNull() {
            addCriterion("total_time_max is null");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxIsNotNull() {
            addCriterion("total_time_max is not null");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxEqualTo(Long value) {
            addCriterion("total_time_max =", value, "totalTimeMax");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxNotEqualTo(Long value) {
            addCriterion("total_time_max <>", value, "totalTimeMax");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxGreaterThan(Long value) {
            addCriterion("total_time_max >", value, "totalTimeMax");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxGreaterThanOrEqualTo(Long value) {
            addCriterion("total_time_max >=", value, "totalTimeMax");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxLessThan(Long value) {
            addCriterion("total_time_max <", value, "totalTimeMax");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxLessThanOrEqualTo(Long value) {
            addCriterion("total_time_max <=", value, "totalTimeMax");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxIn(List<Long> values) {
            addCriterion("total_time_max in", values, "totalTimeMax");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxNotIn(List<Long> values) {
            addCriterion("total_time_max not in", values, "totalTimeMax");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxBetween(Long value1, Long value2) {
            addCriterion("total_time_max between", value1, value2, "totalTimeMax");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMaxNotBetween(Long value1, Long value2) {
            addCriterion("total_time_max not between", value1, value2, "totalTimeMax");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinIsNull() {
            addCriterion("total_time_min is null");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinIsNotNull() {
            addCriterion("total_time_min is not null");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinEqualTo(Long value) {
            addCriterion("total_time_min =", value, "totalTimeMin");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinNotEqualTo(Long value) {
            addCriterion("total_time_min <>", value, "totalTimeMin");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinGreaterThan(Long value) {
            addCriterion("total_time_min >", value, "totalTimeMin");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinGreaterThanOrEqualTo(Long value) {
            addCriterion("total_time_min >=", value, "totalTimeMin");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinLessThan(Long value) {
            addCriterion("total_time_min <", value, "totalTimeMin");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinLessThanOrEqualTo(Long value) {
            addCriterion("total_time_min <=", value, "totalTimeMin");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinIn(List<Long> values) {
            addCriterion("total_time_min in", values, "totalTimeMin");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinNotIn(List<Long> values) {
            addCriterion("total_time_min not in", values, "totalTimeMin");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinBetween(Long value1, Long value2) {
            addCriterion("total_time_min between", value1, value2, "totalTimeMin");
            return (Criteria) this;
        }

        public Criteria andTotalTimeMinNotBetween(Long value1, Long value2) {
            addCriterion("total_time_min not between", value1, value2, "totalTimeMin");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxIsNull() {
            addCriterion("wait_time_max is null");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxIsNotNull() {
            addCriterion("wait_time_max is not null");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxEqualTo(Long value) {
            addCriterion("wait_time_max =", value, "waitTimeMax");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxNotEqualTo(Long value) {
            addCriterion("wait_time_max <>", value, "waitTimeMax");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxGreaterThan(Long value) {
            addCriterion("wait_time_max >", value, "waitTimeMax");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxGreaterThanOrEqualTo(Long value) {
            addCriterion("wait_time_max >=", value, "waitTimeMax");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxLessThan(Long value) {
            addCriterion("wait_time_max <", value, "waitTimeMax");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxLessThanOrEqualTo(Long value) {
            addCriterion("wait_time_max <=", value, "waitTimeMax");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxIn(List<Long> values) {
            addCriterion("wait_time_max in", values, "waitTimeMax");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxNotIn(List<Long> values) {
            addCriterion("wait_time_max not in", values, "waitTimeMax");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxBetween(Long value1, Long value2) {
            addCriterion("wait_time_max between", value1, value2, "waitTimeMax");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMaxNotBetween(Long value1, Long value2) {
            addCriterion("wait_time_max not between", value1, value2, "waitTimeMax");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinIsNull() {
            addCriterion("wait_time_min is null");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinIsNotNull() {
            addCriterion("wait_time_min is not null");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinEqualTo(Long value) {
            addCriterion("wait_time_min =", value, "waitTimeMin");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinNotEqualTo(Long value) {
            addCriterion("wait_time_min <>", value, "waitTimeMin");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinGreaterThan(Long value) {
            addCriterion("wait_time_min >", value, "waitTimeMin");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinGreaterThanOrEqualTo(Long value) {
            addCriterion("wait_time_min >=", value, "waitTimeMin");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinLessThan(Long value) {
            addCriterion("wait_time_min <", value, "waitTimeMin");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinLessThanOrEqualTo(Long value) {
            addCriterion("wait_time_min <=", value, "waitTimeMin");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinIn(List<Long> values) {
            addCriterion("wait_time_min in", values, "waitTimeMin");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinNotIn(List<Long> values) {
            addCriterion("wait_time_min not in", values, "waitTimeMin");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinBetween(Long value1, Long value2) {
            addCriterion("wait_time_min between", value1, value2, "waitTimeMin");
            return (Criteria) this;
        }

        public Criteria andWaitTimeMinNotBetween(Long value1, Long value2) {
            addCriterion("wait_time_min not between", value1, value2, "waitTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxIsNull() {
            addCriterion("execution_time_max is null");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxIsNotNull() {
            addCriterion("execution_time_max is not null");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxEqualTo(Long value) {
            addCriterion("execution_time_max =", value, "executionTimeMax");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxNotEqualTo(Long value) {
            addCriterion("execution_time_max <>", value, "executionTimeMax");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxGreaterThan(Long value) {
            addCriterion("execution_time_max >", value, "executionTimeMax");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxGreaterThanOrEqualTo(Long value) {
            addCriterion("execution_time_max >=", value, "executionTimeMax");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxLessThan(Long value) {
            addCriterion("execution_time_max <", value, "executionTimeMax");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxLessThanOrEqualTo(Long value) {
            addCriterion("execution_time_max <=", value, "executionTimeMax");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxIn(List<Long> values) {
            addCriterion("execution_time_max in", values, "executionTimeMax");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxNotIn(List<Long> values) {
            addCriterion("execution_time_max not in", values, "executionTimeMax");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxBetween(Long value1, Long value2) {
            addCriterion("execution_time_max between", value1, value2, "executionTimeMax");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMaxNotBetween(Long value1, Long value2) {
            addCriterion("execution_time_max not between", value1, value2, "executionTimeMax");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinIsNull() {
            addCriterion("execution_time_min is null");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinIsNotNull() {
            addCriterion("execution_time_min is not null");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinEqualTo(Long value) {
            addCriterion("execution_time_min =", value, "executionTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinNotEqualTo(Long value) {
            addCriterion("execution_time_min <>", value, "executionTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinGreaterThan(Long value) {
            addCriterion("execution_time_min >", value, "executionTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinGreaterThanOrEqualTo(Long value) {
            addCriterion("execution_time_min >=", value, "executionTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinLessThan(Long value) {
            addCriterion("execution_time_min <", value, "executionTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinLessThanOrEqualTo(Long value) {
            addCriterion("execution_time_min <=", value, "executionTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinIn(List<Long> values) {
            addCriterion("execution_time_min in", values, "executionTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinNotIn(List<Long> values) {
            addCriterion("execution_time_min not in", values, "executionTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinBetween(Long value1, Long value2) {
            addCriterion("execution_time_min between", value1, value2, "executionTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeMinNotBetween(Long value1, Long value2) {
            addCriterion("execution_time_min not between", value1, value2, "executionTimeMin");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95IsNull() {
            addCriterion("execution_time_line_95 is null");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95IsNotNull() {
            addCriterion("execution_time_line_95 is not null");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95EqualTo(Long value) {
            addCriterion("execution_time_line_95 =", value, "executionTimeLine95");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95NotEqualTo(Long value) {
            addCriterion("execution_time_line_95 <>", value, "executionTimeLine95");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95GreaterThan(Long value) {
            addCriterion("execution_time_line_95 >", value, "executionTimeLine95");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95GreaterThanOrEqualTo(Long value) {
            addCriterion("execution_time_line_95 >=", value, "executionTimeLine95");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95LessThan(Long value) {
            addCriterion("execution_time_line_95 <", value, "executionTimeLine95");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95LessThanOrEqualTo(Long value) {
            addCriterion("execution_time_line_95 <=", value, "executionTimeLine95");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95In(List<Long> values) {
            addCriterion("execution_time_line_95 in", values, "executionTimeLine95");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95NotIn(List<Long> values) {
            addCriterion("execution_time_line_95 not in", values, "executionTimeLine95");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95Between(Long value1, Long value2) {
            addCriterion("execution_time_line_95 between", value1, value2, "executionTimeLine95");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine95NotBetween(Long value1, Long value2) {
            addCriterion("execution_time_line_95 not between", value1, value2, "executionTimeLine95");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99IsNull() {
            addCriterion("execution_time_line_99 is null");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99IsNotNull() {
            addCriterion("execution_time_line_99 is not null");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99EqualTo(Long value) {
            addCriterion("execution_time_line_99 =", value, "executionTimeLine99");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99NotEqualTo(Long value) {
            addCriterion("execution_time_line_99 <>", value, "executionTimeLine99");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99GreaterThan(Long value) {
            addCriterion("execution_time_line_99 >", value, "executionTimeLine99");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99GreaterThanOrEqualTo(Long value) {
            addCriterion("execution_time_line_99 >=", value, "executionTimeLine99");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99LessThan(Long value) {
            addCriterion("execution_time_line_99 <", value, "executionTimeLine99");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99LessThanOrEqualTo(Long value) {
            addCriterion("execution_time_line_99 <=", value, "executionTimeLine99");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99In(List<Long> values) {
            addCriterion("execution_time_line_99 in", values, "executionTimeLine99");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99NotIn(List<Long> values) {
            addCriterion("execution_time_line_99 not in", values, "executionTimeLine99");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99Between(Long value1, Long value2) {
            addCriterion("execution_time_line_99 between", value1, value2, "executionTimeLine99");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLine99NotBetween(Long value1, Long value2) {
            addCriterion("execution_time_line_99 not between", value1, value2, "executionTimeLine99");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(LocalDateTime value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(LocalDateTime value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<LocalDateTime> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(LocalDateTime value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(LocalDateTime value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(LocalDateTime value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(LocalDateTime value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<LocalDateTime> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<LocalDateTime> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Integer value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Integer value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Integer value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Integer value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Integer value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Integer> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Integer> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Integer value1, Integer value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
