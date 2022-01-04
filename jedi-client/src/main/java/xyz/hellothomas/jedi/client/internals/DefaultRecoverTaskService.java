package xyz.hellothomas.jedi.client.internals;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import xyz.hellothomas.jedi.client.model.JediConfig;
import xyz.hellothomas.jedi.client.model.JediProperty;
import xyz.hellothomas.jedi.client.model.JediTaskExecution;
import xyz.hellothomas.jedi.client.persistence.PersistenceService;
import xyz.hellothomas.jedi.core.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.core.internals.executor.AsyncAttributes;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolExecutor;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;
import xyz.hellothomas.jedi.core.utils.AsyncContextHolder;
import xyz.hellothomas.jedi.core.utils.JsonUtil;
import xyz.hellothomas.jedi.core.utils.NetUtil;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static xyz.hellothomas.jedi.core.constants.Constants.EMPTY_STRING;

/**
 * @author Thomas
 * @date 2022/1/2 20:56
 * @description
 * @version 1.0
 */
@Slf4j
public class DefaultRecoverTaskService implements RecoverTaskService, ApplicationListener<ApplicationStartedEvent> {
    private static final Splitter DATA_SOURCE_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
    private static final int DEFAULT_PAGE_SIZE = 1000;
    private final PersistenceService persistenceService;
    private final JediProperty jediProperty;
    private String host;
    private ApplicationContext applicationContext;
    private LocalDateTime appInitTime;

    public DefaultRecoverTaskService(PersistenceService persistenceService, JediProperty jediProperty) {
        this.persistenceService = persistenceService;
        this.jediProperty = jediProperty;
        this.host = NetUtil.getLocalHost();
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("tasks start recover");
        this.applicationContext = event.getApplicationContext();
        this.appInitTime = this.applicationContext.getBean(JediConfig.class).getAppInitTime();

        Map<String, JediThreadPoolExecutor> jediThreadPoolExecutorMap =
                event.getApplicationContext().getBeansOfType(JediThreadPoolExecutor.class);
        String[] executorNames = jediThreadPoolExecutorMap.keySet().toArray(new String[0]);
        // 选第一个线程池
//        JediThreadPoolExecutor jediThreadPoolExecutor = jediThreadPoolExecutorMap.get(executorNames[0]);
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();

        List<String> dataSourceNameList;
        String dataSourceNames = jediProperty.getPersistence().getRecover().getDataSourceNames();
        if (StringUtils.isBlank(dataSourceNames)) {
            dataSourceNameList = Lists.newArrayList(EMPTY_STRING);
        } else {
            dataSourceNameList = DATA_SOURCE_SPLITTER.splitToList(dataSourceNames);
        }
        // todo taskName
        dataSourceNameList.stream().forEach(i -> executor.submit(() -> recover(i)));
        log.info("tasks end recover");
    }

    @Override
    public void recover(String dataSourceName) {
        log.info("dataSourceName: <{}>, tasks start recover", dataSourceName);
        long total = persistenceService.queryCountByStatusAndRecoverable(host, TaskStatusEnum.REGISTERED.getValue(),
                true, this.appInitTime, dataSourceName);
        if (total <= 0) {
            log.info("dataSourceName: <{}>, no registered tasks, tasks end recover", dataSourceName);
            return;
        }

        String host = NetUtil.getLocalHost();
        int count = total % DEFAULT_PAGE_SIZE == 0 ? (int) (total / DEFAULT_PAGE_SIZE) :
                (int) (total / DEFAULT_PAGE_SIZE) + 1;
        int num = 0;
        while (count > 0) {
            num++;
            List<JediTaskExecution> jediTaskExecutionList =
                    persistenceService.queryPageByStatusAndRecoverable(host, TaskStatusEnum.REGISTERED.getValue(),
                            true, this.appInitTime, num, DEFAULT_PAGE_SIZE, dataSourceName);
            // 某个任务提交失败，整个任务失败
            jediTaskExecutionList.stream().forEach(i -> doRecover(i));
            count--;
        }
        log.info("dataSourceName: {}, tasks end recover", dataSourceName);
    }

    @SneakyThrows
    public void doRecover(JediTaskExecution jediTaskExecution) {
        log.debug("taskId: {} is recovering", jediTaskExecution.getId());
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
            log.debug("taskId: {} is recovered", jediTaskExecution.getId());
        } finally {
            removeAsyncAttributes();
        }
    }

    private AsyncAttributes addAsyncAttributes(JediTaskExecution jediTaskExecution) {
        // 任务注册
        TaskProperty taskProperty = initTaskProperty(jediTaskExecution);
        AsyncAttributes asyncAttributes = new AsyncAttributes();
        asyncAttributes.setAttribute(TaskProperty.class.getName(), taskProperty);
        AsyncContextHolder.setAsyncAttributes(asyncAttributes);
        return asyncAttributes;
    }

    private void removeAsyncAttributes() {
        AsyncContextHolder.resetAsyncAttributes();
    }

    private TaskProperty initTaskProperty(JediTaskExecution jediTaskExecution) {
        TaskProperty taskProperty = new TaskProperty();
        BeanUtils.copyProperties(jediTaskExecution, taskProperty);
        taskProperty.setStatus(TaskStatusEnum.REGISTERED.getValue());
        taskProperty.setRecovered(true);
        taskProperty.setPersistent(true);
        return taskProperty;
    }
}
