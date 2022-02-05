package xyz.hellothomas.jedi.client.internals;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import xyz.hellothomas.jedi.client.exception.JediClientException;
import xyz.hellothomas.jedi.client.model.JediProperty;
import xyz.hellothomas.jedi.client.model.JediTaskExecution;
import xyz.hellothomas.jedi.client.persistence.PersistenceService;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.core.internals.executor.AsyncAttributes;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;
import xyz.hellothomas.jedi.core.utils.AsyncContextHolder;
import xyz.hellothomas.jedi.core.utils.JsonUtil;
import xyz.hellothomas.jedi.core.utils.NetUtil;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

import static xyz.hellothomas.jedi.client.constants.Constants.JEDI_DEFAULT_RETRYER_PATH;

/**
 * @author 80234613 唐圆
 * @date 2021/12/22 14:36
 * @descripton
 * @version 1.0
 */
@Slf4j
public class DefaultRetryTaskService implements RetryTaskService, ApplicationContextAware, InitializingBean {
    private final PersistenceService persistenceService;
    private final JediProperty jediProperty;
    private ApplicationContext applicationContext;
    private String host = NetUtil.getLocalHost();

    public DefaultRetryTaskService(PersistenceService persistenceService, JediProperty jediProperty) {
        this.persistenceService = persistenceService;
        this.jediProperty = jediProperty;
    }

    @ResponseBody
    public ApiResponse<String> doRetry(@RequestParam("taskId") String taskId,
                                       @RequestParam(value = "dataSourceName", required = false) String dataSourceName,
                                       @RequestParam("operator") String operator) {
        TaskProperty taskProperty;
        try {
            taskProperty = retry(taskId, dataSourceName, operator);
        } catch (Exception e) {
            log.error("任务重试失败", e);
            return ApiResponse.fail(e.getMessage());
        }
        return ApiResponse.success(taskProperty.getId());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    @SneakyThrows
    public TaskProperty retry(String taskId, @Nullable String dataSourceName, String operator) {
        JediTaskExecution jediTaskExecution = persistenceService.queryTaskExecutionById(taskId, dataSourceName);
        if (jediTaskExecution == null) {
            throw new JediClientException(String.format("taskId<%s>不存在", taskId));
        }

        Class beanClazz = Class.forName(jediTaskExecution.getBeanTypeName());

        String[] methodParamTypesString = JsonUtil.deserialize(jediTaskExecution.getMethodParamTypes(), String[].class);
        Class[] methodParamClazzArray = new Class[methodParamTypesString.length];
        for (int i = 0; i < methodParamTypesString.length; i++) {
            methodParamClazzArray[i] = Class.forName(methodParamTypesString[i]);
        }
        Method method = beanClazz.getMethod(jediTaskExecution.getMethodName(), methodParamClazzArray);

        String[] methodArgumentsString = JsonUtil.deserialize(jediTaskExecution.getMethodArguments(), String[].class);
        Object[] methodArguments = new Object[methodArgumentsString.length];
        for (int i = 0; i < methodArgumentsString.length; i++) {
            methodArguments[i] = JsonUtil.deserialize(methodArgumentsString[i], methodParamClazzArray[i]);
        }

        try {
            AsyncAttributes asyncAttributes = addAsyncAttributes(jediTaskExecution, operator);
            method.invoke(this.applicationContext.getBean(jediTaskExecution.getBeanName(), beanClazz), methodArguments);
            return (TaskProperty) asyncAttributes.getAttribute(TaskProperty.class.getName());
        } finally {
            removeAsyncAttributes();
        }
    }

    private AsyncAttributes addAsyncAttributes(JediTaskExecution jediTaskExecution, String operator) {
        // 任务注册
        TaskProperty taskProperty = initTaskProperty(jediTaskExecution, operator);
        AsyncAttributes asyncAttributes = new AsyncAttributes();
        asyncAttributes.setAttribute(TaskProperty.class.getName(), taskProperty);
        AsyncContextHolder.setAsyncAttributes(asyncAttributes);
        return asyncAttributes;
    }

    private void removeAsyncAttributes() {
        AsyncContextHolder.resetAsyncAttributes();
    }

    private TaskProperty initTaskProperty(JediTaskExecution jediTaskExecution, String operator) {
        TaskProperty taskProperty = new TaskProperty();
        BeanUtils.copyProperties(jediTaskExecution, taskProperty);
        taskProperty.setId(UUID.randomUUID().toString());
        taskProperty.setCreateTime(LocalDateTime.now());
        taskProperty.setStartTime(null);
        taskProperty.setEndTime(null);
        taskProperty.setStatus(TaskStatusEnum.REGISTERED.getValue());
        taskProperty.setExitCode(null);
        taskProperty.setExitMessage(null);
        taskProperty.setByRetryer(true);
        taskProperty.setPreviousId(jediTaskExecution.getId());
        taskProperty.setExecutedByParentTaskThread(false);
        taskProperty.setLastUpdatedUser(operator);
        taskProperty.setHost(host);
        taskProperty.setMachineId(host);
        taskProperty.setPersistent(true);
        taskProperty.setInitialized(true);
        return taskProperty;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String path = this.jediProperty.getPersistence().getRetryer().getPath();
        // todo 健壮性
        if (StringUtils.isBlank(path)) {
            path = JEDI_DEFAULT_RETRYER_PATH.substring(1);
        } else if (path.startsWith("/")) {
            path = path.substring(1);
        }

        RequestMappingHandlerMapping requestMappingHandlerMapping =
                this.applicationContext.getBean(RequestMappingHandlerMapping.class);
        Class<?> entry = this.getClass();
        Method method = ReflectionUtils.findMethod(entry, "doRetry", String.class, String.class, String.class);
        PatternsRequestCondition patterns = new PatternsRequestCondition(path);
        RequestMethodsRequestCondition methods = new RequestMethodsRequestCondition(RequestMethod.POST);
        ParamsRequestCondition params = new ParamsRequestCondition("taskId", "dataSourceName", "operator");
        RequestMappingInfo mapping = new RequestMappingInfo(patterns, methods, params, null, null, null, null);
        // spring 5.2.X
//        requestMappingHandlerMapping.registerMapping(mapping, this, method);
        // spring 5.1.x
        Method registerHandlerMethod =
                requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass().getDeclaredMethod(
                        "registerHandlerMethod", Object.class, Method.class, Object.class);
        registerHandlerMethod.setAccessible(true);
        registerHandlerMethod.invoke(requestMappingHandlerMapping, this, method, mapping);
    }
}
