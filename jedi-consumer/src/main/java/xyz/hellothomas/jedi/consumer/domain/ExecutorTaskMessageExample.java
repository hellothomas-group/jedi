package xyz.hellothomas.jedi.consumer.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExecutorTaskMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ExecutorTaskMessageExample() {
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

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andNamespaceIsNull() {
            addCriterion("namespace is null");
            return (Criteria) this;
        }

        public Criteria andNamespaceIsNotNull() {
            addCriterion("namespace is not null");
            return (Criteria) this;
        }

        public Criteria andNamespaceEqualTo(String value) {
            addCriterion("namespace =", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceNotEqualTo(String value) {
            addCriterion("namespace <>", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceGreaterThan(String value) {
            addCriterion("namespace >", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceGreaterThanOrEqualTo(String value) {
            addCriterion("namespace >=", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceLessThan(String value) {
            addCriterion("namespace <", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceLessThanOrEqualTo(String value) {
            addCriterion("namespace <=", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceLike(String value) {
            addCriterion("namespace like", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceNotLike(String value) {
            addCriterion("namespace not like", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceIn(List<String> values) {
            addCriterion("namespace in", values, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceNotIn(List<String> values) {
            addCriterion("namespace not in", values, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceBetween(String value1, String value2) {
            addCriterion("namespace between", value1, value2, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceNotBetween(String value1, String value2) {
            addCriterion("namespace not between", value1, value2, "namespace");
            return (Criteria) this;
        }

        public Criteria andMessageTypeIsNull() {
            addCriterion("message_type is null");
            return (Criteria) this;
        }

        public Criteria andMessageTypeIsNotNull() {
            addCriterion("message_type is not null");
            return (Criteria) this;
        }

        public Criteria andMessageTypeEqualTo(String value) {
            addCriterion("message_type =", value, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeNotEqualTo(String value) {
            addCriterion("message_type <>", value, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeGreaterThan(String value) {
            addCriterion("message_type >", value, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeGreaterThanOrEqualTo(String value) {
            addCriterion("message_type >=", value, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeLessThan(String value) {
            addCriterion("message_type <", value, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeLessThanOrEqualTo(String value) {
            addCriterion("message_type <=", value, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeLike(String value) {
            addCriterion("message_type like", value, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeNotLike(String value) {
            addCriterion("message_type not like", value, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeIn(List<String> values) {
            addCriterion("message_type in", values, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeNotIn(List<String> values) {
            addCriterion("message_type not in", values, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeBetween(String value1, String value2) {
            addCriterion("message_type between", value1, value2, "messageType");
            return (Criteria) this;
        }

        public Criteria andMessageTypeNotBetween(String value1, String value2) {
            addCriterion("message_type not between", value1, value2, "messageType");
            return (Criteria) this;
        }

        public Criteria andPoolNameIsNull() {
            addCriterion("pool_name is null");
            return (Criteria) this;
        }

        public Criteria andPoolNameIsNotNull() {
            addCriterion("pool_name is not null");
            return (Criteria) this;
        }

        public Criteria andPoolNameEqualTo(String value) {
            addCriterion("pool_name =", value, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameNotEqualTo(String value) {
            addCriterion("pool_name <>", value, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameGreaterThan(String value) {
            addCriterion("pool_name >", value, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameGreaterThanOrEqualTo(String value) {
            addCriterion("pool_name >=", value, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameLessThan(String value) {
            addCriterion("pool_name <", value, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameLessThanOrEqualTo(String value) {
            addCriterion("pool_name <=", value, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameLike(String value) {
            addCriterion("pool_name like", value, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameNotLike(String value) {
            addCriterion("pool_name not like", value, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameIn(List<String> values) {
            addCriterion("pool_name in", values, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameNotIn(List<String> values) {
            addCriterion("pool_name not in", values, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameBetween(String value1, String value2) {
            addCriterion("pool_name between", value1, value2, "poolName");
            return (Criteria) this;
        }

        public Criteria andPoolNameNotBetween(String value1, String value2) {
            addCriterion("pool_name not between", value1, value2, "poolName");
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

        public Criteria andWaitTimeIsNull() {
            addCriterion("wait_time is null");
            return (Criteria) this;
        }

        public Criteria andWaitTimeIsNotNull() {
            addCriterion("wait_time is not null");
            return (Criteria) this;
        }

        public Criteria andWaitTimeEqualTo(Long value) {
            addCriterion("wait_time =", value, "waitTime");
            return (Criteria) this;
        }

        public Criteria andWaitTimeNotEqualTo(Long value) {
            addCriterion("wait_time <>", value, "waitTime");
            return (Criteria) this;
        }

        public Criteria andWaitTimeGreaterThan(Long value) {
            addCriterion("wait_time >", value, "waitTime");
            return (Criteria) this;
        }

        public Criteria andWaitTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("wait_time >=", value, "waitTime");
            return (Criteria) this;
        }

        public Criteria andWaitTimeLessThan(Long value) {
            addCriterion("wait_time <", value, "waitTime");
            return (Criteria) this;
        }

        public Criteria andWaitTimeLessThanOrEqualTo(Long value) {
            addCriterion("wait_time <=", value, "waitTime");
            return (Criteria) this;
        }

        public Criteria andWaitTimeIn(List<Long> values) {
            addCriterion("wait_time in", values, "waitTime");
            return (Criteria) this;
        }

        public Criteria andWaitTimeNotIn(List<Long> values) {
            addCriterion("wait_time not in", values, "waitTime");
            return (Criteria) this;
        }

        public Criteria andWaitTimeBetween(Long value1, Long value2) {
            addCriterion("wait_time between", value1, value2, "waitTime");
            return (Criteria) this;
        }

        public Criteria andWaitTimeNotBetween(Long value1, Long value2) {
            addCriterion("wait_time not between", value1, value2, "waitTime");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeIsNull() {
            addCriterion("execution_time is null");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeIsNotNull() {
            addCriterion("execution_time is not null");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeEqualTo(Long value) {
            addCriterion("execution_time =", value, "executionTime");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeNotEqualTo(Long value) {
            addCriterion("execution_time <>", value, "executionTime");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeGreaterThan(Long value) {
            addCriterion("execution_time >", value, "executionTime");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("execution_time >=", value, "executionTime");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLessThan(Long value) {
            addCriterion("execution_time <", value, "executionTime");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeLessThanOrEqualTo(Long value) {
            addCriterion("execution_time <=", value, "executionTime");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeIn(List<Long> values) {
            addCriterion("execution_time in", values, "executionTime");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeNotIn(List<Long> values) {
            addCriterion("execution_time not in", values, "executionTime");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeBetween(Long value1, Long value2) {
            addCriterion("execution_time between", value1, value2, "executionTime");
            return (Criteria) this;
        }

        public Criteria andExecutionTimeNotBetween(Long value1, Long value2) {
            addCriterion("execution_time not between", value1, value2, "executionTime");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andExitCodeIsNull() {
            addCriterion("exit_code is null");
            return (Criteria) this;
        }

        public Criteria andExitCodeIsNotNull() {
            addCriterion("exit_code is not null");
            return (Criteria) this;
        }

        public Criteria andExitCodeEqualTo(String value) {
            addCriterion("exit_code =", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeNotEqualTo(String value) {
            addCriterion("exit_code <>", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeGreaterThan(String value) {
            addCriterion("exit_code >", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeGreaterThanOrEqualTo(String value) {
            addCriterion("exit_code >=", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeLessThan(String value) {
            addCriterion("exit_code <", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeLessThanOrEqualTo(String value) {
            addCriterion("exit_code <=", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeLike(String value) {
            addCriterion("exit_code like", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeNotLike(String value) {
            addCriterion("exit_code not like", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeIn(List<String> values) {
            addCriterion("exit_code in", values, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeNotIn(List<String> values) {
            addCriterion("exit_code not in", values, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeBetween(String value1, String value2) {
            addCriterion("exit_code between", value1, value2, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeNotBetween(String value1, String value2) {
            addCriterion("exit_code not between", value1, value2, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitMessageIsNull() {
            addCriterion("exit_message is null");
            return (Criteria) this;
        }

        public Criteria andExitMessageIsNotNull() {
            addCriterion("exit_message is not null");
            return (Criteria) this;
        }

        public Criteria andExitMessageEqualTo(String value) {
            addCriterion("exit_message =", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageNotEqualTo(String value) {
            addCriterion("exit_message <>", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageGreaterThan(String value) {
            addCriterion("exit_message >", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageGreaterThanOrEqualTo(String value) {
            addCriterion("exit_message >=", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageLessThan(String value) {
            addCriterion("exit_message <", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageLessThanOrEqualTo(String value) {
            addCriterion("exit_message <=", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageLike(String value) {
            addCriterion("exit_message like", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageNotLike(String value) {
            addCriterion("exit_message not like", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageIn(List<String> values) {
            addCriterion("exit_message in", values, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageNotIn(List<String> values) {
            addCriterion("exit_message not in", values, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageBetween(String value1, String value2) {
            addCriterion("exit_message between", value1, value2, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageNotBetween(String value1, String value2) {
            addCriterion("exit_message not between", value1, value2, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataIsNull() {
            addCriterion("task_extra_data is null");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataIsNotNull() {
            addCriterion("task_extra_data is not null");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataEqualTo(String value) {
            addCriterion("task_extra_data =", value, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataNotEqualTo(String value) {
            addCriterion("task_extra_data <>", value, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataGreaterThan(String value) {
            addCriterion("task_extra_data >", value, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataGreaterThanOrEqualTo(String value) {
            addCriterion("task_extra_data >=", value, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataLessThan(String value) {
            addCriterion("task_extra_data <", value, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataLessThanOrEqualTo(String value) {
            addCriterion("task_extra_data <=", value, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataLike(String value) {
            addCriterion("task_extra_data like", value, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataNotLike(String value) {
            addCriterion("task_extra_data not like", value, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataIn(List<String> values) {
            addCriterion("task_extra_data in", values, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataNotIn(List<String> values) {
            addCriterion("task_extra_data not in", values, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataBetween(String value1, String value2) {
            addCriterion("task_extra_data between", value1, value2, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andTaskExtraDataNotBetween(String value1, String value2) {
            addCriterion("task_extra_data not between", value1, value2, "taskExtraData");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(LocalDateTime value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(LocalDateTime value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(LocalDateTime value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(LocalDateTime value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<LocalDateTime> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<LocalDateTime> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableIsNull() {
            addCriterion("is_recoverable is null");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableIsNotNull() {
            addCriterion("is_recoverable is not null");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableEqualTo(Boolean value) {
            addCriterion("is_recoverable =", value, "isRecoverable");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableNotEqualTo(Boolean value) {
            addCriterion("is_recoverable <>", value, "isRecoverable");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableGreaterThan(Boolean value) {
            addCriterion("is_recoverable >", value, "isRecoverable");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_recoverable >=", value, "isRecoverable");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableLessThan(Boolean value) {
            addCriterion("is_recoverable <", value, "isRecoverable");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableLessThanOrEqualTo(Boolean value) {
            addCriterion("is_recoverable <=", value, "isRecoverable");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableIn(List<Boolean> values) {
            addCriterion("is_recoverable in", values, "isRecoverable");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableNotIn(List<Boolean> values) {
            addCriterion("is_recoverable not in", values, "isRecoverable");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableBetween(Boolean value1, Boolean value2) {
            addCriterion("is_recoverable between", value1, value2, "isRecoverable");
            return (Criteria) this;
        }

        public Criteria andIsRecoverableNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_recoverable not between", value1, value2, "isRecoverable");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredIsNull() {
            addCriterion("is_recovered is null");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredIsNotNull() {
            addCriterion("is_recovered is not null");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredEqualTo(Boolean value) {
            addCriterion("is_recovered =", value, "isRecovered");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredNotEqualTo(Boolean value) {
            addCriterion("is_recovered <>", value, "isRecovered");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredGreaterThan(Boolean value) {
            addCriterion("is_recovered >", value, "isRecovered");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_recovered >=", value, "isRecovered");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredLessThan(Boolean value) {
            addCriterion("is_recovered <", value, "isRecovered");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredLessThanOrEqualTo(Boolean value) {
            addCriterion("is_recovered <=", value, "isRecovered");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredIn(List<Boolean> values) {
            addCriterion("is_recovered in", values, "isRecovered");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredNotIn(List<Boolean> values) {
            addCriterion("is_recovered not in", values, "isRecovered");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredBetween(Boolean value1, Boolean value2) {
            addCriterion("is_recovered between", value1, value2, "isRecovered");
            return (Criteria) this;
        }

        public Criteria andIsRecoveredNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_recovered not between", value1, value2, "isRecovered");
            return (Criteria) this;
        }

        public Criteria andHostIsNull() {
            addCriterion("host is null");
            return (Criteria) this;
        }

        public Criteria andHostIsNotNull() {
            addCriterion("host is not null");
            return (Criteria) this;
        }

        public Criteria andHostEqualTo(String value) {
            addCriterion("host =", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostNotEqualTo(String value) {
            addCriterion("host <>", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostGreaterThan(String value) {
            addCriterion("host >", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostGreaterThanOrEqualTo(String value) {
            addCriterion("host >=", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostLessThan(String value) {
            addCriterion("host <", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostLessThanOrEqualTo(String value) {
            addCriterion("host <=", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostLike(String value) {
            addCriterion("host like", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostNotLike(String value) {
            addCriterion("host not like", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostIn(List<String> values) {
            addCriterion("host in", values, "host");
            return (Criteria) this;
        }

        public Criteria andHostNotIn(List<String> values) {
            addCriterion("host not in", values, "host");
            return (Criteria) this;
        }

        public Criteria andHostBetween(String value1, String value2) {
            addCriterion("host between", value1, value2, "host");
            return (Criteria) this;
        }

        public Criteria andHostNotBetween(String value1, String value2) {
            addCriterion("host not between", value1, value2, "host");
            return (Criteria) this;
        }

        public Criteria andTraceIdIsNull() {
            addCriterion("trace_id is null");
            return (Criteria) this;
        }

        public Criteria andTraceIdIsNotNull() {
            addCriterion("trace_id is not null");
            return (Criteria) this;
        }

        public Criteria andTraceIdEqualTo(String value) {
            addCriterion("trace_id =", value, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdNotEqualTo(String value) {
            addCriterion("trace_id <>", value, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdGreaterThan(String value) {
            addCriterion("trace_id >", value, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdGreaterThanOrEqualTo(String value) {
            addCriterion("trace_id >=", value, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdLessThan(String value) {
            addCriterion("trace_id <", value, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdLessThanOrEqualTo(String value) {
            addCriterion("trace_id <=", value, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdLike(String value) {
            addCriterion("trace_id like", value, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdNotLike(String value) {
            addCriterion("trace_id not like", value, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdIn(List<String> values) {
            addCriterion("trace_id in", values, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdNotIn(List<String> values) {
            addCriterion("trace_id not in", values, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdBetween(String value1, String value2) {
            addCriterion("trace_id between", value1, value2, "traceId");
            return (Criteria) this;
        }

        public Criteria andTraceIdNotBetween(String value1, String value2) {
            addCriterion("trace_id not between", value1, value2, "traceId");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerIsNull() {
            addCriterion("is_by_retryer is null");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerIsNotNull() {
            addCriterion("is_by_retryer is not null");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerEqualTo(Boolean value) {
            addCriterion("is_by_retryer =", value, "isByRetryer");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerNotEqualTo(Boolean value) {
            addCriterion("is_by_retryer <>", value, "isByRetryer");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerGreaterThan(Boolean value) {
            addCriterion("is_by_retryer >", value, "isByRetryer");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_by_retryer >=", value, "isByRetryer");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerLessThan(Boolean value) {
            addCriterion("is_by_retryer <", value, "isByRetryer");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerLessThanOrEqualTo(Boolean value) {
            addCriterion("is_by_retryer <=", value, "isByRetryer");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerIn(List<Boolean> values) {
            addCriterion("is_by_retryer in", values, "isByRetryer");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerNotIn(List<Boolean> values) {
            addCriterion("is_by_retryer not in", values, "isByRetryer");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerBetween(Boolean value1, Boolean value2) {
            addCriterion("is_by_retryer between", value1, value2, "isByRetryer");
            return (Criteria) this;
        }

        public Criteria andIsByRetryerNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_by_retryer not between", value1, value2, "isByRetryer");
            return (Criteria) this;
        }

        public Criteria andPreviousIdIsNull() {
            addCriterion("previous_id is null");
            return (Criteria) this;
        }

        public Criteria andPreviousIdIsNotNull() {
            addCriterion("previous_id is not null");
            return (Criteria) this;
        }

        public Criteria andPreviousIdEqualTo(String value) {
            addCriterion("previous_id =", value, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdNotEqualTo(String value) {
            addCriterion("previous_id <>", value, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdGreaterThan(String value) {
            addCriterion("previous_id >", value, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdGreaterThanOrEqualTo(String value) {
            addCriterion("previous_id >=", value, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdLessThan(String value) {
            addCriterion("previous_id <", value, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdLessThanOrEqualTo(String value) {
            addCriterion("previous_id <=", value, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdLike(String value) {
            addCriterion("previous_id like", value, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdNotLike(String value) {
            addCriterion("previous_id not like", value, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdIn(List<String> values) {
            addCriterion("previous_id in", values, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdNotIn(List<String> values) {
            addCriterion("previous_id not in", values, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdBetween(String value1, String value2) {
            addCriterion("previous_id between", value1, value2, "previousId");
            return (Criteria) this;
        }

        public Criteria andPreviousIdNotBetween(String value1, String value2) {
            addCriterion("previous_id not between", value1, value2, "previousId");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNull() {
            addCriterion("parent_id is null");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parent_id is not null");
            return (Criteria) this;
        }

        public Criteria andParentIdEqualTo(String value) {
            addCriterion("parent_id =", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotEqualTo(String value) {
            addCriterion("parent_id <>", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThan(String value) {
            addCriterion("parent_id >", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(String value) {
            addCriterion("parent_id >=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThan(String value) {
            addCriterion("parent_id <", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThanOrEqualTo(String value) {
            addCriterion("parent_id <=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLike(String value) {
            addCriterion("parent_id like", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotLike(String value) {
            addCriterion("parent_id not like", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdIn(List<String> values) {
            addCriterion("parent_id in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotIn(List<String> values) {
            addCriterion("parent_id not in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdBetween(String value1, String value2) {
            addCriterion("parent_id between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotBetween(String value1, String value2) {
            addCriterion("parent_id not between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadIsNull() {
            addCriterion("is_executed_by_parent_task_thread is null");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadIsNotNull() {
            addCriterion("is_executed_by_parent_task_thread is not null");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadEqualTo(Boolean value) {
            addCriterion("is_executed_by_parent_task_thread =", value, "isExecutedByParentTaskThread");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadNotEqualTo(Boolean value) {
            addCriterion("is_executed_by_parent_task_thread <>", value, "isExecutedByParentTaskThread");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadGreaterThan(Boolean value) {
            addCriterion("is_executed_by_parent_task_thread >", value, "isExecutedByParentTaskThread");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_executed_by_parent_task_thread >=", value, "isExecutedByParentTaskThread");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadLessThan(Boolean value) {
            addCriterion("is_executed_by_parent_task_thread <", value, "isExecutedByParentTaskThread");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadLessThanOrEqualTo(Boolean value) {
            addCriterion("is_executed_by_parent_task_thread <=", value, "isExecutedByParentTaskThread");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadIn(List<Boolean> values) {
            addCriterion("is_executed_by_parent_task_thread in", values, "isExecutedByParentTaskThread");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadNotIn(List<Boolean> values) {
            addCriterion("is_executed_by_parent_task_thread not in", values, "isExecutedByParentTaskThread");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadBetween(Boolean value1, Boolean value2) {
            addCriterion("is_executed_by_parent_task_thread between", value1, value2, "isExecutedByParentTaskThread");
            return (Criteria) this;
        }

        public Criteria andIsExecutedByParentTaskThreadNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_executed_by_parent_task_thread not between", value1, value2, "isExecutedByParentTaskThread");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameIsNull() {
            addCriterion("data_source_name is null");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameIsNotNull() {
            addCriterion("data_source_name is not null");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameEqualTo(String value) {
            addCriterion("data_source_name =", value, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameNotEqualTo(String value) {
            addCriterion("data_source_name <>", value, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameGreaterThan(String value) {
            addCriterion("data_source_name >", value, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameGreaterThanOrEqualTo(String value) {
            addCriterion("data_source_name >=", value, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameLessThan(String value) {
            addCriterion("data_source_name <", value, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameLessThanOrEqualTo(String value) {
            addCriterion("data_source_name <=", value, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameLike(String value) {
            addCriterion("data_source_name like", value, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameNotLike(String value) {
            addCriterion("data_source_name not like", value, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameIn(List<String> values) {
            addCriterion("data_source_name in", values, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameNotIn(List<String> values) {
            addCriterion("data_source_name not in", values, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameBetween(String value1, String value2) {
            addCriterion("data_source_name between", value1, value2, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andDataSourceNameNotBetween(String value1, String value2) {
            addCriterion("data_source_name not between", value1, value2, "dataSourceName");
            return (Criteria) this;
        }

        public Criteria andIsPersistentIsNull() {
            addCriterion("is_persistent is null");
            return (Criteria) this;
        }

        public Criteria andIsPersistentIsNotNull() {
            addCriterion("is_persistent is not null");
            return (Criteria) this;
        }

        public Criteria andIsPersistentEqualTo(Boolean value) {
            addCriterion("is_persistent =", value, "isPersistent");
            return (Criteria) this;
        }

        public Criteria andIsPersistentNotEqualTo(Boolean value) {
            addCriterion("is_persistent <>", value, "isPersistent");
            return (Criteria) this;
        }

        public Criteria andIsPersistentGreaterThan(Boolean value) {
            addCriterion("is_persistent >", value, "isPersistent");
            return (Criteria) this;
        }

        public Criteria andIsPersistentGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_persistent >=", value, "isPersistent");
            return (Criteria) this;
        }

        public Criteria andIsPersistentLessThan(Boolean value) {
            addCriterion("is_persistent <", value, "isPersistent");
            return (Criteria) this;
        }

        public Criteria andIsPersistentLessThanOrEqualTo(Boolean value) {
            addCriterion("is_persistent <=", value, "isPersistent");
            return (Criteria) this;
        }

        public Criteria andIsPersistentIn(List<Boolean> values) {
            addCriterion("is_persistent in", values, "isPersistent");
            return (Criteria) this;
        }

        public Criteria andIsPersistentNotIn(List<Boolean> values) {
            addCriterion("is_persistent not in", values, "isPersistent");
            return (Criteria) this;
        }

        public Criteria andIsPersistentBetween(Boolean value1, Boolean value2) {
            addCriterion("is_persistent between", value1, value2, "isPersistent");
            return (Criteria) this;
        }

        public Criteria andIsPersistentNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_persistent not between", value1, value2, "isPersistent");
            return (Criteria) this;
        }

        public Criteria andRecordTimeIsNull() {
            addCriterion("record_time is null");
            return (Criteria) this;
        }

        public Criteria andRecordTimeIsNotNull() {
            addCriterion("record_time is not null");
            return (Criteria) this;
        }

        public Criteria andRecordTimeEqualTo(LocalDateTime value) {
            addCriterion("record_time =", value, "recordTime");
            return (Criteria) this;
        }

        public Criteria andRecordTimeNotEqualTo(LocalDateTime value) {
            addCriterion("record_time <>", value, "recordTime");
            return (Criteria) this;
        }

        public Criteria andRecordTimeGreaterThan(LocalDateTime value) {
            addCriterion("record_time >", value, "recordTime");
            return (Criteria) this;
        }

        public Criteria andRecordTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("record_time >=", value, "recordTime");
            return (Criteria) this;
        }

        public Criteria andRecordTimeLessThan(LocalDateTime value) {
            addCriterion("record_time <", value, "recordTime");
            return (Criteria) this;
        }

        public Criteria andRecordTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("record_time <=", value, "recordTime");
            return (Criteria) this;
        }

        public Criteria andRecordTimeIn(List<LocalDateTime> values) {
            addCriterion("record_time in", values, "recordTime");
            return (Criteria) this;
        }

        public Criteria andRecordTimeNotIn(List<LocalDateTime> values) {
            addCriterion("record_time not in", values, "recordTime");
            return (Criteria) this;
        }

        public Criteria andRecordTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("record_time between", value1, value2, "recordTime");
            return (Criteria) this;
        }

        public Criteria andRecordTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("record_time not between", value1, value2, "recordTime");
            return (Criteria) this;
        }

        public Criteria andIsRetriedIsNull() {
            addCriterion("is_retried is null");
            return (Criteria) this;
        }

        public Criteria andIsRetriedIsNotNull() {
            addCriterion("is_retried is not null");
            return (Criteria) this;
        }

        public Criteria andIsRetriedEqualTo(Boolean value) {
            addCriterion("is_retried =", value, "isRetried");
            return (Criteria) this;
        }

        public Criteria andIsRetriedNotEqualTo(Boolean value) {
            addCriterion("is_retried <>", value, "isRetried");
            return (Criteria) this;
        }

        public Criteria andIsRetriedGreaterThan(Boolean value) {
            addCriterion("is_retried >", value, "isRetried");
            return (Criteria) this;
        }

        public Criteria andIsRetriedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_retried >=", value, "isRetried");
            return (Criteria) this;
        }

        public Criteria andIsRetriedLessThan(Boolean value) {
            addCriterion("is_retried <", value, "isRetried");
            return (Criteria) this;
        }

        public Criteria andIsRetriedLessThanOrEqualTo(Boolean value) {
            addCriterion("is_retried <=", value, "isRetried");
            return (Criteria) this;
        }

        public Criteria andIsRetriedIn(List<Boolean> values) {
            addCriterion("is_retried in", values, "isRetried");
            return (Criteria) this;
        }

        public Criteria andIsRetriedNotIn(List<Boolean> values) {
            addCriterion("is_retried not in", values, "isRetried");
            return (Criteria) this;
        }

        public Criteria andIsRetriedBetween(Boolean value1, Boolean value2) {
            addCriterion("is_retried between", value1, value2, "isRetried");
            return (Criteria) this;
        }

        public Criteria andIsRetriedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_retried not between", value1, value2, "isRetried");
            return (Criteria) this;
        }

        public Criteria andRetryIdIsNull() {
            addCriterion("retry_id is null");
            return (Criteria) this;
        }

        public Criteria andRetryIdIsNotNull() {
            addCriterion("retry_id is not null");
            return (Criteria) this;
        }

        public Criteria andRetryIdEqualTo(String value) {
            addCriterion("retry_id =", value, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdNotEqualTo(String value) {
            addCriterion("retry_id <>", value, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdGreaterThan(String value) {
            addCriterion("retry_id >", value, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdGreaterThanOrEqualTo(String value) {
            addCriterion("retry_id >=", value, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdLessThan(String value) {
            addCriterion("retry_id <", value, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdLessThanOrEqualTo(String value) {
            addCriterion("retry_id <=", value, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdLike(String value) {
            addCriterion("retry_id like", value, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdNotLike(String value) {
            addCriterion("retry_id not like", value, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdIn(List<String> values) {
            addCriterion("retry_id in", values, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdNotIn(List<String> values) {
            addCriterion("retry_id not in", values, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdBetween(String value1, String value2) {
            addCriterion("retry_id between", value1, value2, "retryId");
            return (Criteria) this;
        }

        public Criteria andRetryIdNotBetween(String value1, String value2) {
            addCriterion("retry_id not between", value1, value2, "retryId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNull() {
            addCriterion("update_user is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNotNull() {
            addCriterion("update_user is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserEqualTo(String value) {
            addCriterion("update_user =", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotEqualTo(String value) {
            addCriterion("update_user <>", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThan(String value) {
            addCriterion("update_user >", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThanOrEqualTo(String value) {
            addCriterion("update_user >=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThan(String value) {
            addCriterion("update_user <", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThanOrEqualTo(String value) {
            addCriterion("update_user <=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLike(String value) {
            addCriterion("update_user like", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotLike(String value) {
            addCriterion("update_user not like", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIn(List<String> values) {
            addCriterion("update_user in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotIn(List<String> values) {
            addCriterion("update_user not in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserBetween(String value1, String value2) {
            addCriterion("update_user between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotBetween(String value1, String value2) {
            addCriterion("update_user not between", value1, value2, "updateUser");
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