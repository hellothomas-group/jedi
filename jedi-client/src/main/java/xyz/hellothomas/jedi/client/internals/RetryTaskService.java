package xyz.hellothomas.jedi.client.internals;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
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
public class RetryTaskService implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private PersistenceService persistenceService;

    public RetryTaskService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    public void retry(String taskId, @Nullable String dataSourceName) {
        JediTaskExecution jediTaskExecution = persistenceService.queryTaskExecutionById(taskId, dataSourceName);
        if (jediTaskExecution == null) {
            throw new RuntimeException(String.format("taskId<%s>不存在", taskId));
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

        addAsyncAttributes(jediTaskExecution);
        method.invoke(this.applicationContext.getBean(jediTaskExecution.getBeanName(), beanClazz), methodArguments);
        removeAsyncAttributes();
    }

    public void addAsyncAttributes(JediTaskExecution jediTaskExecution) {
        // 任务注册
        TaskProperty taskProperty = initTaskProperty(jediTaskExecution);
        AsyncAttributes asyncAttributes = new AsyncAttributes();
        asyncAttributes.setAttribute(TaskProperty.class.getName(), taskProperty);
        AsyncContextHolder.setAsyncAttributes(asyncAttributes);
    }

    public void removeAsyncAttributes() {
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
}
