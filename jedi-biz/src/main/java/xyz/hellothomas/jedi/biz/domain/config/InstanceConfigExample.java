package xyz.hellothomas.jedi.biz.domain.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InstanceConfigExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public InstanceConfigExample() {
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

        public Criteria andInstanceIdIsNull() {
            addCriterion("instance_id is null");
            return (Criteria) this;
        }

        public Criteria andInstanceIdIsNotNull() {
            addCriterion("instance_id is not null");
            return (Criteria) this;
        }

        public Criteria andInstanceIdEqualTo(Long value) {
            addCriterion("instance_id =", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdNotEqualTo(Long value) {
            addCriterion("instance_id <>", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdGreaterThan(Long value) {
            addCriterion("instance_id >", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdGreaterThanOrEqualTo(Long value) {
            addCriterion("instance_id >=", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdLessThan(Long value) {
            addCriterion("instance_id <", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdLessThanOrEqualTo(Long value) {
            addCriterion("instance_id <=", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdIn(List<Long> values) {
            addCriterion("instance_id in", values, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdNotIn(List<Long> values) {
            addCriterion("instance_id not in", values, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdBetween(Long value1, Long value2) {
            addCriterion("instance_id between", value1, value2, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdNotBetween(Long value1, Long value2) {
            addCriterion("instance_id not between", value1, value2, "instanceId");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameIsNull() {
            addCriterion("config_namespace_name is null");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameIsNotNull() {
            addCriterion("config_namespace_name is not null");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameEqualTo(String value) {
            addCriterion("config_namespace_name =", value, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameNotEqualTo(String value) {
            addCriterion("config_namespace_name <>", value, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameGreaterThan(String value) {
            addCriterion("config_namespace_name >", value, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameGreaterThanOrEqualTo(String value) {
            addCriterion("config_namespace_name >=", value, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameLessThan(String value) {
            addCriterion("config_namespace_name <", value, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameLessThanOrEqualTo(String value) {
            addCriterion("config_namespace_name <=", value, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameLike(String value) {
            addCriterion("config_namespace_name like", value, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameNotLike(String value) {
            addCriterion("config_namespace_name not like", value, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameIn(List<String> values) {
            addCriterion("config_namespace_name in", values, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameNotIn(List<String> values) {
            addCriterion("config_namespace_name not in", values, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameBetween(String value1, String value2) {
            addCriterion("config_namespace_name between", value1, value2, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigNamespaceNameNotBetween(String value1, String value2) {
            addCriterion("config_namespace_name not between", value1, value2, "configNamespaceName");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdIsNull() {
            addCriterion("config_app_id is null");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdIsNotNull() {
            addCriterion("config_app_id is not null");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdEqualTo(String value) {
            addCriterion("config_app_id =", value, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdNotEqualTo(String value) {
            addCriterion("config_app_id <>", value, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdGreaterThan(String value) {
            addCriterion("config_app_id >", value, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("config_app_id >=", value, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdLessThan(String value) {
            addCriterion("config_app_id <", value, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdLessThanOrEqualTo(String value) {
            addCriterion("config_app_id <=", value, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdLike(String value) {
            addCriterion("config_app_id like", value, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdNotLike(String value) {
            addCriterion("config_app_id not like", value, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdIn(List<String> values) {
            addCriterion("config_app_id in", values, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdNotIn(List<String> values) {
            addCriterion("config_app_id not in", values, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdBetween(String value1, String value2) {
            addCriterion("config_app_id between", value1, value2, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigAppIdNotBetween(String value1, String value2) {
            addCriterion("config_app_id not between", value1, value2, "configAppId");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameIsNull() {
            addCriterion("config_executor_name is null");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameIsNotNull() {
            addCriterion("config_executor_name is not null");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameEqualTo(String value) {
            addCriterion("config_executor_name =", value, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameNotEqualTo(String value) {
            addCriterion("config_executor_name <>", value, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameGreaterThan(String value) {
            addCriterion("config_executor_name >", value, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameGreaterThanOrEqualTo(String value) {
            addCriterion("config_executor_name >=", value, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameLessThan(String value) {
            addCriterion("config_executor_name <", value, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameLessThanOrEqualTo(String value) {
            addCriterion("config_executor_name <=", value, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameLike(String value) {
            addCriterion("config_executor_name like", value, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameNotLike(String value) {
            addCriterion("config_executor_name not like", value, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameIn(List<String> values) {
            addCriterion("config_executor_name in", values, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameNotIn(List<String> values) {
            addCriterion("config_executor_name not in", values, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameBetween(String value1, String value2) {
            addCriterion("config_executor_name between", value1, value2, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andConfigExecutorNameNotBetween(String value1, String value2) {
            addCriterion("config_executor_name not between", value1, value2, "configExecutorName");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyIsNull() {
            addCriterion("release_key is null");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyIsNotNull() {
            addCriterion("release_key is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyEqualTo(String value) {
            addCriterion("release_key =", value, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyNotEqualTo(String value) {
            addCriterion("release_key <>", value, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyGreaterThan(String value) {
            addCriterion("release_key >", value, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyGreaterThanOrEqualTo(String value) {
            addCriterion("release_key >=", value, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyLessThan(String value) {
            addCriterion("release_key <", value, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyLessThanOrEqualTo(String value) {
            addCriterion("release_key <=", value, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyLike(String value) {
            addCriterion("release_key like", value, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyNotLike(String value) {
            addCriterion("release_key not like", value, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyIn(List<String> values) {
            addCriterion("release_key in", values, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyNotIn(List<String> values) {
            addCriterion("release_key not in", values, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyBetween(String value1, String value2) {
            addCriterion("release_key between", value1, value2, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseKeyNotBetween(String value1, String value2) {
            addCriterion("release_key not between", value1, value2, "releaseKey");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeIsNull() {
            addCriterion("release_delivery_time is null");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeIsNotNull() {
            addCriterion("release_delivery_time is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeEqualTo(LocalDateTime value) {
            addCriterion("release_delivery_time =", value, "releaseDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeNotEqualTo(LocalDateTime value) {
            addCriterion("release_delivery_time <>", value, "releaseDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeGreaterThan(LocalDateTime value) {
            addCriterion("release_delivery_time >", value, "releaseDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("release_delivery_time >=", value, "releaseDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeLessThan(LocalDateTime value) {
            addCriterion("release_delivery_time <", value, "releaseDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("release_delivery_time <=", value, "releaseDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeIn(List<LocalDateTime> values) {
            addCriterion("release_delivery_time in", values, "releaseDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeNotIn(List<LocalDateTime> values) {
            addCriterion("release_delivery_time not in", values, "releaseDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("release_delivery_time between", value1, value2, "releaseDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andReleaseDeliveryTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("release_delivery_time not between", value1, value2, "releaseDeliveryTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeIsNull() {
            addCriterion("data_change_created_time is null");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeIsNotNull() {
            addCriterion("data_change_created_time is not null");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeEqualTo(LocalDateTime value) {
            addCriterion("data_change_created_time =", value, "dataChangeCreatedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeNotEqualTo(LocalDateTime value) {
            addCriterion("data_change_created_time <>", value, "dataChangeCreatedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeGreaterThan(LocalDateTime value) {
            addCriterion("data_change_created_time >", value, "dataChangeCreatedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("data_change_created_time >=", value, "dataChangeCreatedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeLessThan(LocalDateTime value) {
            addCriterion("data_change_created_time <", value, "dataChangeCreatedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("data_change_created_time <=", value, "dataChangeCreatedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeIn(List<LocalDateTime> values) {
            addCriterion("data_change_created_time in", values, "dataChangeCreatedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeNotIn(List<LocalDateTime> values) {
            addCriterion("data_change_created_time not in", values, "dataChangeCreatedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("data_change_created_time between", value1, value2, "dataChangeCreatedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("data_change_created_time not between", value1, value2, "dataChangeCreatedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeIsNull() {
            addCriterion("data_change_last_modified_time is null");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeIsNotNull() {
            addCriterion("data_change_last_modified_time is not null");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeEqualTo(LocalDateTime value) {
            addCriterion("data_change_last_modified_time =", value, "dataChangeLastModifiedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeNotEqualTo(LocalDateTime value) {
            addCriterion("data_change_last_modified_time <>", value, "dataChangeLastModifiedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeGreaterThan(LocalDateTime value) {
            addCriterion("data_change_last_modified_time >", value, "dataChangeLastModifiedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("data_change_last_modified_time >=", value, "dataChangeLastModifiedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeLessThan(LocalDateTime value) {
            addCriterion("data_change_last_modified_time <", value, "dataChangeLastModifiedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("data_change_last_modified_time <=", value, "dataChangeLastModifiedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeIn(List<LocalDateTime> values) {
            addCriterion("data_change_last_modified_time in", values, "dataChangeLastModifiedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeNotIn(List<LocalDateTime> values) {
            addCriterion("data_change_last_modified_time not in", values, "dataChangeLastModifiedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("data_change_last_modified_time between", value1, value2, "dataChangeLastModifiedTime");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("data_change_last_modified_time not between", value1, value2, "dataChangeLastModifiedTime");
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