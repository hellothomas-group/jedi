package com.hellothomas.jedi.consumer.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExecutorTickerMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ExecutorTickerMessageExample() {
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

        public Criteria andCorePoolSizeIsNull() {
            addCriterion("core_pool_size is null");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeIsNotNull() {
            addCriterion("core_pool_size is not null");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeEqualTo(Integer value) {
            addCriterion("core_pool_size =", value, "corePoolSize");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeNotEqualTo(Integer value) {
            addCriterion("core_pool_size <>", value, "corePoolSize");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeGreaterThan(Integer value) {
            addCriterion("core_pool_size >", value, "corePoolSize");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("core_pool_size >=", value, "corePoolSize");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeLessThan(Integer value) {
            addCriterion("core_pool_size <", value, "corePoolSize");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeLessThanOrEqualTo(Integer value) {
            addCriterion("core_pool_size <=", value, "corePoolSize");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeIn(List<Integer> values) {
            addCriterion("core_pool_size in", values, "corePoolSize");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeNotIn(List<Integer> values) {
            addCriterion("core_pool_size not in", values, "corePoolSize");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeBetween(Integer value1, Integer value2) {
            addCriterion("core_pool_size between", value1, value2, "corePoolSize");
            return (Criteria) this;
        }

        public Criteria andCorePoolSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("core_pool_size not between", value1, value2, "corePoolSize");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeIsNull() {
            addCriterion("maximum_pool_size is null");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeIsNotNull() {
            addCriterion("maximum_pool_size is not null");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeEqualTo(Integer value) {
            addCriterion("maximum_pool_size =", value, "maximumPoolSize");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeNotEqualTo(Integer value) {
            addCriterion("maximum_pool_size <>", value, "maximumPoolSize");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeGreaterThan(Integer value) {
            addCriterion("maximum_pool_size >", value, "maximumPoolSize");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("maximum_pool_size >=", value, "maximumPoolSize");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeLessThan(Integer value) {
            addCriterion("maximum_pool_size <", value, "maximumPoolSize");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeLessThanOrEqualTo(Integer value) {
            addCriterion("maximum_pool_size <=", value, "maximumPoolSize");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeIn(List<Integer> values) {
            addCriterion("maximum_pool_size in", values, "maximumPoolSize");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeNotIn(List<Integer> values) {
            addCriterion("maximum_pool_size not in", values, "maximumPoolSize");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeBetween(Integer value1, Integer value2) {
            addCriterion("maximum_pool_size between", value1, value2, "maximumPoolSize");
            return (Criteria) this;
        }

        public Criteria andMaximumPoolSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("maximum_pool_size not between", value1, value2, "maximumPoolSize");
            return (Criteria) this;
        }

        public Criteria andPoolSizeIsNull() {
            addCriterion("pool_size is null");
            return (Criteria) this;
        }

        public Criteria andPoolSizeIsNotNull() {
            addCriterion("pool_size is not null");
            return (Criteria) this;
        }

        public Criteria andPoolSizeEqualTo(Integer value) {
            addCriterion("pool_size =", value, "poolSize");
            return (Criteria) this;
        }

        public Criteria andPoolSizeNotEqualTo(Integer value) {
            addCriterion("pool_size <>", value, "poolSize");
            return (Criteria) this;
        }

        public Criteria andPoolSizeGreaterThan(Integer value) {
            addCriterion("pool_size >", value, "poolSize");
            return (Criteria) this;
        }

        public Criteria andPoolSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("pool_size >=", value, "poolSize");
            return (Criteria) this;
        }

        public Criteria andPoolSizeLessThan(Integer value) {
            addCriterion("pool_size <", value, "poolSize");
            return (Criteria) this;
        }

        public Criteria andPoolSizeLessThanOrEqualTo(Integer value) {
            addCriterion("pool_size <=", value, "poolSize");
            return (Criteria) this;
        }

        public Criteria andPoolSizeIn(List<Integer> values) {
            addCriterion("pool_size in", values, "poolSize");
            return (Criteria) this;
        }

        public Criteria andPoolSizeNotIn(List<Integer> values) {
            addCriterion("pool_size not in", values, "poolSize");
            return (Criteria) this;
        }

        public Criteria andPoolSizeBetween(Integer value1, Integer value2) {
            addCriterion("pool_size between", value1, value2, "poolSize");
            return (Criteria) this;
        }

        public Criteria andPoolSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("pool_size not between", value1, value2, "poolSize");
            return (Criteria) this;
        }

        public Criteria andActiveCountIsNull() {
            addCriterion("active_count is null");
            return (Criteria) this;
        }

        public Criteria andActiveCountIsNotNull() {
            addCriterion("active_count is not null");
            return (Criteria) this;
        }

        public Criteria andActiveCountEqualTo(Integer value) {
            addCriterion("active_count =", value, "activeCount");
            return (Criteria) this;
        }

        public Criteria andActiveCountNotEqualTo(Integer value) {
            addCriterion("active_count <>", value, "activeCount");
            return (Criteria) this;
        }

        public Criteria andActiveCountGreaterThan(Integer value) {
            addCriterion("active_count >", value, "activeCount");
            return (Criteria) this;
        }

        public Criteria andActiveCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("active_count >=", value, "activeCount");
            return (Criteria) this;
        }

        public Criteria andActiveCountLessThan(Integer value) {
            addCriterion("active_count <", value, "activeCount");
            return (Criteria) this;
        }

        public Criteria andActiveCountLessThanOrEqualTo(Integer value) {
            addCriterion("active_count <=", value, "activeCount");
            return (Criteria) this;
        }

        public Criteria andActiveCountIn(List<Integer> values) {
            addCriterion("active_count in", values, "activeCount");
            return (Criteria) this;
        }

        public Criteria andActiveCountNotIn(List<Integer> values) {
            addCriterion("active_count not in", values, "activeCount");
            return (Criteria) this;
        }

        public Criteria andActiveCountBetween(Integer value1, Integer value2) {
            addCriterion("active_count between", value1, value2, "activeCount");
            return (Criteria) this;
        }

        public Criteria andActiveCountNotBetween(Integer value1, Integer value2) {
            addCriterion("active_count not between", value1, value2, "activeCount");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeIsNull() {
            addCriterion("largest_pool_size is null");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeIsNotNull() {
            addCriterion("largest_pool_size is not null");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeEqualTo(Integer value) {
            addCriterion("largest_pool_size =", value, "largestPoolSize");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeNotEqualTo(Integer value) {
            addCriterion("largest_pool_size <>", value, "largestPoolSize");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeGreaterThan(Integer value) {
            addCriterion("largest_pool_size >", value, "largestPoolSize");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("largest_pool_size >=", value, "largestPoolSize");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeLessThan(Integer value) {
            addCriterion("largest_pool_size <", value, "largestPoolSize");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeLessThanOrEqualTo(Integer value) {
            addCriterion("largest_pool_size <=", value, "largestPoolSize");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeIn(List<Integer> values) {
            addCriterion("largest_pool_size in", values, "largestPoolSize");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeNotIn(List<Integer> values) {
            addCriterion("largest_pool_size not in", values, "largestPoolSize");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeBetween(Integer value1, Integer value2) {
            addCriterion("largest_pool_size between", value1, value2, "largestPoolSize");
            return (Criteria) this;
        }

        public Criteria andLargestPoolSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("largest_pool_size not between", value1, value2, "largestPoolSize");
            return (Criteria) this;
        }

        public Criteria andQueueTypeIsNull() {
            addCriterion("queue_type is null");
            return (Criteria) this;
        }

        public Criteria andQueueTypeIsNotNull() {
            addCriterion("queue_type is not null");
            return (Criteria) this;
        }

        public Criteria andQueueTypeEqualTo(String value) {
            addCriterion("queue_type =", value, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeNotEqualTo(String value) {
            addCriterion("queue_type <>", value, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeGreaterThan(String value) {
            addCriterion("queue_type >", value, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeGreaterThanOrEqualTo(String value) {
            addCriterion("queue_type >=", value, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeLessThan(String value) {
            addCriterion("queue_type <", value, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeLessThanOrEqualTo(String value) {
            addCriterion("queue_type <=", value, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeLike(String value) {
            addCriterion("queue_type like", value, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeNotLike(String value) {
            addCriterion("queue_type not like", value, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeIn(List<String> values) {
            addCriterion("queue_type in", values, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeNotIn(List<String> values) {
            addCriterion("queue_type not in", values, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeBetween(String value1, String value2) {
            addCriterion("queue_type between", value1, value2, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueTypeNotBetween(String value1, String value2) {
            addCriterion("queue_type not between", value1, value2, "queueType");
            return (Criteria) this;
        }

        public Criteria andQueueSizeIsNull() {
            addCriterion("queue_size is null");
            return (Criteria) this;
        }

        public Criteria andQueueSizeIsNotNull() {
            addCriterion("queue_size is not null");
            return (Criteria) this;
        }

        public Criteria andQueueSizeEqualTo(Integer value) {
            addCriterion("queue_size =", value, "queueSize");
            return (Criteria) this;
        }

        public Criteria andQueueSizeNotEqualTo(Integer value) {
            addCriterion("queue_size <>", value, "queueSize");
            return (Criteria) this;
        }

        public Criteria andQueueSizeGreaterThan(Integer value) {
            addCriterion("queue_size >", value, "queueSize");
            return (Criteria) this;
        }

        public Criteria andQueueSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("queue_size >=", value, "queueSize");
            return (Criteria) this;
        }

        public Criteria andQueueSizeLessThan(Integer value) {
            addCriterion("queue_size <", value, "queueSize");
            return (Criteria) this;
        }

        public Criteria andQueueSizeLessThanOrEqualTo(Integer value) {
            addCriterion("queue_size <=", value, "queueSize");
            return (Criteria) this;
        }

        public Criteria andQueueSizeIn(List<Integer> values) {
            addCriterion("queue_size in", values, "queueSize");
            return (Criteria) this;
        }

        public Criteria andQueueSizeNotIn(List<Integer> values) {
            addCriterion("queue_size not in", values, "queueSize");
            return (Criteria) this;
        }

        public Criteria andQueueSizeBetween(Integer value1, Integer value2) {
            addCriterion("queue_size between", value1, value2, "queueSize");
            return (Criteria) this;
        }

        public Criteria andQueueSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("queue_size not between", value1, value2, "queueSize");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityIsNull() {
            addCriterion("queue_remaining_capacity is null");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityIsNotNull() {
            addCriterion("queue_remaining_capacity is not null");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityEqualTo(Integer value) {
            addCriterion("queue_remaining_capacity =", value, "queueRemainingCapacity");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityNotEqualTo(Integer value) {
            addCriterion("queue_remaining_capacity <>", value, "queueRemainingCapacity");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityGreaterThan(Integer value) {
            addCriterion("queue_remaining_capacity >", value, "queueRemainingCapacity");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityGreaterThanOrEqualTo(Integer value) {
            addCriterion("queue_remaining_capacity >=", value, "queueRemainingCapacity");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityLessThan(Integer value) {
            addCriterion("queue_remaining_capacity <", value, "queueRemainingCapacity");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityLessThanOrEqualTo(Integer value) {
            addCriterion("queue_remaining_capacity <=", value, "queueRemainingCapacity");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityIn(List<Integer> values) {
            addCriterion("queue_remaining_capacity in", values, "queueRemainingCapacity");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityNotIn(List<Integer> values) {
            addCriterion("queue_remaining_capacity not in", values, "queueRemainingCapacity");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityBetween(Integer value1, Integer value2) {
            addCriterion("queue_remaining_capacity between", value1, value2, "queueRemainingCapacity");
            return (Criteria) this;
        }

        public Criteria andQueueRemainingCapacityNotBetween(Integer value1, Integer value2) {
            addCriterion("queue_remaining_capacity not between", value1, value2, "queueRemainingCapacity");
            return (Criteria) this;
        }

        public Criteria andTaskCountIsNull() {
            addCriterion("task_count is null");
            return (Criteria) this;
        }

        public Criteria andTaskCountIsNotNull() {
            addCriterion("task_count is not null");
            return (Criteria) this;
        }

        public Criteria andTaskCountEqualTo(Long value) {
            addCriterion("task_count =", value, "taskCount");
            return (Criteria) this;
        }

        public Criteria andTaskCountNotEqualTo(Long value) {
            addCriterion("task_count <>", value, "taskCount");
            return (Criteria) this;
        }

        public Criteria andTaskCountGreaterThan(Long value) {
            addCriterion("task_count >", value, "taskCount");
            return (Criteria) this;
        }

        public Criteria andTaskCountGreaterThanOrEqualTo(Long value) {
            addCriterion("task_count >=", value, "taskCount");
            return (Criteria) this;
        }

        public Criteria andTaskCountLessThan(Long value) {
            addCriterion("task_count <", value, "taskCount");
            return (Criteria) this;
        }

        public Criteria andTaskCountLessThanOrEqualTo(Long value) {
            addCriterion("task_count <=", value, "taskCount");
            return (Criteria) this;
        }

        public Criteria andTaskCountIn(List<Long> values) {
            addCriterion("task_count in", values, "taskCount");
            return (Criteria) this;
        }

        public Criteria andTaskCountNotIn(List<Long> values) {
            addCriterion("task_count not in", values, "taskCount");
            return (Criteria) this;
        }

        public Criteria andTaskCountBetween(Long value1, Long value2) {
            addCriterion("task_count between", value1, value2, "taskCount");
            return (Criteria) this;
        }

        public Criteria andTaskCountNotBetween(Long value1, Long value2) {
            addCriterion("task_count not between", value1, value2, "taskCount");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountIsNull() {
            addCriterion("completed_task_count is null");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountIsNotNull() {
            addCriterion("completed_task_count is not null");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountEqualTo(Long value) {
            addCriterion("completed_task_count =", value, "completedTaskCount");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountNotEqualTo(Long value) {
            addCriterion("completed_task_count <>", value, "completedTaskCount");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountGreaterThan(Long value) {
            addCriterion("completed_task_count >", value, "completedTaskCount");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountGreaterThanOrEqualTo(Long value) {
            addCriterion("completed_task_count >=", value, "completedTaskCount");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountLessThan(Long value) {
            addCriterion("completed_task_count <", value, "completedTaskCount");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountLessThanOrEqualTo(Long value) {
            addCriterion("completed_task_count <=", value, "completedTaskCount");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountIn(List<Long> values) {
            addCriterion("completed_task_count in", values, "completedTaskCount");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountNotIn(List<Long> values) {
            addCriterion("completed_task_count not in", values, "completedTaskCount");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountBetween(Long value1, Long value2) {
            addCriterion("completed_task_count between", value1, value2, "completedTaskCount");
            return (Criteria) this;
        }

        public Criteria andCompletedTaskCountNotBetween(Long value1, Long value2) {
            addCriterion("completed_task_count not between", value1, value2, "completedTaskCount");
            return (Criteria) this;
        }

        public Criteria andRejectCountIsNull() {
            addCriterion("reject_count is null");
            return (Criteria) this;
        }

        public Criteria andRejectCountIsNotNull() {
            addCriterion("reject_count is not null");
            return (Criteria) this;
        }

        public Criteria andRejectCountEqualTo(Long value) {
            addCriterion("reject_count =", value, "rejectCount");
            return (Criteria) this;
        }

        public Criteria andRejectCountNotEqualTo(Long value) {
            addCriterion("reject_count <>", value, "rejectCount");
            return (Criteria) this;
        }

        public Criteria andRejectCountGreaterThan(Long value) {
            addCriterion("reject_count >", value, "rejectCount");
            return (Criteria) this;
        }

        public Criteria andRejectCountGreaterThanOrEqualTo(Long value) {
            addCriterion("reject_count >=", value, "rejectCount");
            return (Criteria) this;
        }

        public Criteria andRejectCountLessThan(Long value) {
            addCriterion("reject_count <", value, "rejectCount");
            return (Criteria) this;
        }

        public Criteria andRejectCountLessThanOrEqualTo(Long value) {
            addCriterion("reject_count <=", value, "rejectCount");
            return (Criteria) this;
        }

        public Criteria andRejectCountIn(List<Long> values) {
            addCriterion("reject_count in", values, "rejectCount");
            return (Criteria) this;
        }

        public Criteria andRejectCountNotIn(List<Long> values) {
            addCriterion("reject_count not in", values, "rejectCount");
            return (Criteria) this;
        }

        public Criteria andRejectCountBetween(Long value1, Long value2) {
            addCriterion("reject_count between", value1, value2, "rejectCount");
            return (Criteria) this;
        }

        public Criteria andRejectCountNotBetween(Long value1, Long value2) {
            addCriterion("reject_count not between", value1, value2, "rejectCount");
            return (Criteria) this;
        }

        public Criteria andIsShutdownIsNull() {
            addCriterion("is_shutdown is null");
            return (Criteria) this;
        }

        public Criteria andIsShutdownIsNotNull() {
            addCriterion("is_shutdown is not null");
            return (Criteria) this;
        }

        public Criteria andIsShutdownEqualTo(Byte value) {
            addCriterion("is_shutdown =", value, "isShutdown");
            return (Criteria) this;
        }

        public Criteria andIsShutdownNotEqualTo(Byte value) {
            addCriterion("is_shutdown <>", value, "isShutdown");
            return (Criteria) this;
        }

        public Criteria andIsShutdownGreaterThan(Byte value) {
            addCriterion("is_shutdown >", value, "isShutdown");
            return (Criteria) this;
        }

        public Criteria andIsShutdownGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_shutdown >=", value, "isShutdown");
            return (Criteria) this;
        }

        public Criteria andIsShutdownLessThan(Byte value) {
            addCriterion("is_shutdown <", value, "isShutdown");
            return (Criteria) this;
        }

        public Criteria andIsShutdownLessThanOrEqualTo(Byte value) {
            addCriterion("is_shutdown <=", value, "isShutdown");
            return (Criteria) this;
        }

        public Criteria andIsShutdownIn(List<Byte> values) {
            addCriterion("is_shutdown in", values, "isShutdown");
            return (Criteria) this;
        }

        public Criteria andIsShutdownNotIn(List<Byte> values) {
            addCriterion("is_shutdown not in", values, "isShutdown");
            return (Criteria) this;
        }

        public Criteria andIsShutdownBetween(Byte value1, Byte value2) {
            addCriterion("is_shutdown between", value1, value2, "isShutdown");
            return (Criteria) this;
        }

        public Criteria andIsShutdownNotBetween(Byte value1, Byte value2) {
            addCriterion("is_shutdown not between", value1, value2, "isShutdown");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedIsNull() {
            addCriterion("is_terminated is null");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedIsNotNull() {
            addCriterion("is_terminated is not null");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedEqualTo(Byte value) {
            addCriterion("is_terminated =", value, "isTerminated");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedNotEqualTo(Byte value) {
            addCriterion("is_terminated <>", value, "isTerminated");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedGreaterThan(Byte value) {
            addCriterion("is_terminated >", value, "isTerminated");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_terminated >=", value, "isTerminated");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedLessThan(Byte value) {
            addCriterion("is_terminated <", value, "isTerminated");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedLessThanOrEqualTo(Byte value) {
            addCriterion("is_terminated <=", value, "isTerminated");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedIn(List<Byte> values) {
            addCriterion("is_terminated in", values, "isTerminated");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedNotIn(List<Byte> values) {
            addCriterion("is_terminated not in", values, "isTerminated");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedBetween(Byte value1, Byte value2) {
            addCriterion("is_terminated between", value1, value2, "isTerminated");
            return (Criteria) this;
        }

        public Criteria andIsTerminatedNotBetween(Byte value1, Byte value2) {
            addCriterion("is_terminated not between", value1, value2, "isTerminated");
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