package xyz.hellothomas.jedi.admin.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRoleExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserRoleExample() {
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

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNull() {
            addCriterion("role_id is null");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNotNull() {
            addCriterion("role_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoleIdEqualTo(Long value) {
            addCriterion("role_id =", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotEqualTo(Long value) {
            addCriterion("role_id <>", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThan(Long value) {
            addCriterion("role_id >", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThanOrEqualTo(Long value) {
            addCriterion("role_id >=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThan(Long value) {
            addCriterion("role_id <", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThanOrEqualTo(Long value) {
            addCriterion("role_id <=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIn(List<Long> values) {
            addCriterion("role_id in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotIn(List<Long> values) {
            addCriterion("role_id not in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdBetween(Long value1, Long value2) {
            addCriterion("role_id between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotBetween(Long value1, Long value2) {
            addCriterion("role_id not between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNull() {
            addCriterion("is_deleted is null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNotNull() {
            addCriterion("is_deleted is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedEqualTo(Boolean value) {
            addCriterion("is_deleted =", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotEqualTo(Boolean value) {
            addCriterion("is_deleted <>", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThan(Boolean value) {
            addCriterion("is_deleted >", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted >=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThan(Boolean value) {
            addCriterion("is_deleted <", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted <=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIn(List<Boolean> values) {
            addCriterion("is_deleted in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotIn(List<Boolean> values) {
            addCriterion("is_deleted not in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted not between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByIsNull() {
            addCriterion("data_change_created_by is null");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByIsNotNull() {
            addCriterion("data_change_created_by is not null");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByEqualTo(String value) {
            addCriterion("data_change_created_by =", value, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByNotEqualTo(String value) {
            addCriterion("data_change_created_by <>", value, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByGreaterThan(String value) {
            addCriterion("data_change_created_by >", value, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByGreaterThanOrEqualTo(String value) {
            addCriterion("data_change_created_by >=", value, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByLessThan(String value) {
            addCriterion("data_change_created_by <", value, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByLessThanOrEqualTo(String value) {
            addCriterion("data_change_created_by <=", value, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByLike(String value) {
            addCriterion("data_change_created_by like", value, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByNotLike(String value) {
            addCriterion("data_change_created_by not like", value, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByIn(List<String> values) {
            addCriterion("data_change_created_by in", values, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByNotIn(List<String> values) {
            addCriterion("data_change_created_by not in", values, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByBetween(String value1, String value2) {
            addCriterion("data_change_created_by between", value1, value2, "dataChangeCreatedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeCreatedByNotBetween(String value1, String value2) {
            addCriterion("data_change_created_by not between", value1, value2, "dataChangeCreatedBy");
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

        public Criteria andDataChangeLastModifiedByIsNull() {
            addCriterion("data_change_last_modified_by is null");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByIsNotNull() {
            addCriterion("data_change_last_modified_by is not null");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByEqualTo(String value) {
            addCriterion("data_change_last_modified_by =", value, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByNotEqualTo(String value) {
            addCriterion("data_change_last_modified_by <>", value, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByGreaterThan(String value) {
            addCriterion("data_change_last_modified_by >", value, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByGreaterThanOrEqualTo(String value) {
            addCriterion("data_change_last_modified_by >=", value, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByLessThan(String value) {
            addCriterion("data_change_last_modified_by <", value, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByLessThanOrEqualTo(String value) {
            addCriterion("data_change_last_modified_by <=", value, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByLike(String value) {
            addCriterion("data_change_last_modified_by like", value, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByNotLike(String value) {
            addCriterion("data_change_last_modified_by not like", value, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByIn(List<String> values) {
            addCriterion("data_change_last_modified_by in", values, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByNotIn(List<String> values) {
            addCriterion("data_change_last_modified_by not in", values, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByBetween(String value1, String value2) {
            addCriterion("data_change_last_modified_by between", value1, value2, "dataChangeLastModifiedBy");
            return (Criteria) this;
        }

        public Criteria andDataChangeLastModifiedByNotBetween(String value1, String value2) {
            addCriterion("data_change_last_modified_by not between", value1, value2, "dataChangeLastModifiedBy");
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