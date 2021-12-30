package xyz.hellothomas.jedi.client.internals;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import xyz.hellothomas.jedi.client.model.JediTaskExecution;
import xyz.hellothomas.jedi.client.persistence.PersistenceService;
import xyz.hellothomas.jedi.core.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.core.internals.executor.AsyncAttributes;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;
import xyz.hellothomas.jedi.core.utils.AsyncContextHolder;
import xyz.hellothomas.jedi.core.utils.JsonUtil;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author 80234613 唐圆
 * @date 2021/12/22 14:36
 * @descripton
 * @version 1.0
 */
@Slf4j
public class DefaultRetryTaskService implements RetryTaskService, ApplicationContextAware, InitializingBean {
    private final PersistenceService persistenceService;
    private ApplicationContext applicationContext;

    public DefaultRetryTaskService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @ResponseBody
    public void doRetry(@RequestParam("taskId") String taskId,
                        @RequestParam(value = "dataSourceName", required = false) String dataSourceName,
                        @RequestParam("operator") String operator) {
        retry(taskId, dataSourceName, operator);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    @SneakyThrows
    public void retry(String taskId, @Nullable String dataSourceName, String operator) {
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
            addAsyncAttributes(jediTaskExecution);
            method.invoke(this.applicationContext.getBean(jediTaskExecution.getBeanName(), beanClazz), methodArguments);
        } finally {
            removeAsyncAttributes();
        }
    }

    private void addAsyncAttributes(JediTaskExecution jediTaskExecution) {
        // 任务注册
        TaskProperty taskProperty = initTaskProperty(jediTaskExecution);
        AsyncAttributes asyncAttributes = new AsyncAttributes();
        asyncAttributes.setAttribute(TaskProperty.class.getName(), taskProperty);
        AsyncContextHolder.setAsyncAttributes(asyncAttributes);
    }

    private void removeAsyncAttributes() {
        AsyncContextHolder.resetAsyncAttributes();
    }

    private TaskProperty initTaskProperty(JediTaskExecution jediTaskExecution) {
        TaskProperty taskProperty = new TaskProperty();
        BeanUtils.copyProperties(jediTaskExecution, taskProperty);
        taskProperty.setId(UUID.randomUUID().toString());
        taskProperty.setCreateTime(LocalDateTime.now());
        taskProperty.setStartTime(null);
        taskProperty.setEndTime(null);
        taskProperty.setStatus(TaskStatusEnum.REGISTERED.getValue());
        taskProperty.setExitCode(null);
        taskProperty.setExitMessage(null);
        taskProperty.setPreviousId(jediTaskExecution.getId());
        taskProperty.setPersistent(true);
        taskProperty.setDataSourceName(jediTaskExecution.getDataSourceName());
        return taskProperty;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RequestMappingHandlerMapping requestMappingHandlerMapping =
                this.applicationContext.getBean(RequestMappingHandlerMapping.class);
        Class<?> entry = this.getClass();
        Method methodName = ReflectionUtils.findMethod(entry, "doRetry", String.class, String.class, String.class);
        PatternsRequestCondition patterns = new PatternsRequestCondition("jedi/tasks/retry");
        RequestMethodsRequestCondition method = new RequestMethodsRequestCondition(RequestMethod.POST);
        ParamsRequestCondition params = new ParamsRequestCondition("taskId", "dataSourceName", "operator");
        RequestMappingInfo mappingInfo = new RequestMappingInfo(patterns, method, params, null, null, null, null);
        requestMappingHandlerMapping.registerMapping(mappingInfo, this, methodName);
    }
}
