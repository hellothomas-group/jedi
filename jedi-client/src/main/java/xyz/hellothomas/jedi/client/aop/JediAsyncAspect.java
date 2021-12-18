package xyz.hellothomas.jedi.client.aop;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;
import xyz.hellothomas.jedi.client.annotation.JediAsync;
import xyz.hellothomas.jedi.client.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.client.exception.JediClientException;
import xyz.hellothomas.jedi.client.model.JediConfig;
import xyz.hellothomas.jedi.client.persistence.JediPersistentCallable;
import xyz.hellothomas.jedi.client.persistence.PersistenceService;
import xyz.hellothomas.jedi.client.persistence.TaskPersistProperty;
import xyz.hellothomas.jedi.client.util.ExpressionUtil;
import xyz.hellothomas.jedi.core.internals.executor.JediCallable;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolExecutor;
import xyz.hellothomas.jedi.core.trace.AsyncTraceFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static xyz.hellothomas.jedi.core.constants.Constants.EMPTY_STRING;
import static xyz.hellothomas.jedi.core.constants.Constants.JEDI_DEFAULT_TASK_NAME;

@Aspect
@Slf4j
public class JediAsyncAspect implements ApplicationContextAware, InitializingBean, Ordered {
    private ApplicationContext applicationContext;
    private Map<String, JediThreadPoolExecutor> executorMap;
    private JediThreadPoolExecutor uniqueExecutor;
    private int order;
    private AsyncTraceFactory asyncTraceFactory;

    @Pointcut("@annotation(xyz.hellothomas.jedi.client.annotation.JediAsync)")
    public void annotationPointcut() {
        // 仅定义切点
    }

    @Around("annotationPointcut()")
    public Object jediAsyncAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        JediAsync jediAsync = AnnotationUtils.getAnnotation(methodSignature.getMethod(), JediAsync.class);
        JediThreadPoolExecutor jediThreadPoolExecutor = extractJediThreadPoolExecutor(jediAsync);
        String taskName = extractTaskName(joinPoint, jediAsync);
        String taskExtraData = extractTaskExtraData(joinPoint, jediAsync);

        Callable<Object> task = new Callable<Object>() {
            @SneakyThrows
            @Override
            public Object call() throws Exception {
                Object result = joinPoint.proceed();
                if (result instanceof Future) {
                    return ((Future<?>) result).get();
                }

                return null;
            }
        };

        if (jediAsync.persistent()) {
            TaskPersistProperty taskPersistProperty = buildTaskPersistProperty(jediAsync,
                    jediThreadPoolExecutor.getPoolName(),
                    taskName);
            PersistenceService persistenceService = this.applicationContext.getBean(PersistenceService.class);
            persistenceService.insertTaskExecution(taskPersistProperty);

            return doSubmit(asyncTraceFactory.getCallable(
                    new JediPersistentCallable<>(
                            new JediCallable<>(jediThreadPoolExecutor, taskName, taskExtraData, task,
                                    taskPersistProperty.getId()),
                            taskPersistProperty, persistenceService)),
                    jediThreadPoolExecutor, methodSignature.getReturnType());
        } else {
            return doSubmit(asyncTraceFactory.getCallable(
                    new JediCallable<>(jediThreadPoolExecutor, taskName, taskExtraData, task)),
                    jediThreadPoolExecutor, methodSignature.getReturnType());
        }
    }

    private TaskPersistProperty buildTaskPersistProperty(JediAsync jediAsync, String executorName, String taskName) {
        TaskPersistProperty taskPersistProperty = new TaskPersistProperty();
        JediConfig jediConfig = this.applicationContext.getBean(JediConfig.class);
        taskPersistProperty.setId(UUID.randomUUID().toString());
        taskPersistProperty.setNamespaceName(jediConfig.getNamespace());
        taskPersistProperty.setAppId(jediConfig.getAppId());
        taskPersistProperty.setExecutorName(executorName);
        taskPersistProperty.setTaskName(taskName);
        taskPersistProperty.setBeanName("");
        taskPersistProperty.setMethodName("");
        taskPersistProperty.setArguments("");
        taskPersistProperty.setProcessStatus(TaskStatusEnum.TODO.getProcessStatus());
        taskPersistProperty.setTraceId(EMPTY_STRING);
        taskPersistProperty.setLastId(EMPTY_STRING);
        taskPersistProperty.setDataSourceName(jediAsync.dataSourceName());

        return taskPersistProperty;
    }

    /**
     * Delegate for actually executing the given task with the chosen executor.
     * @param task the task to execute
     * @param executor the chosen executor
     * @param returnType the declared return type (potentially a {@link Future} variant)
     * @return the execution result (potentially a corresponding {@link Future} handle)
     */
    @Nullable
    private Object doSubmit(Callable<Object> task, JediThreadPoolExecutor executor, Class<?> returnType) {
        if (CompletableFuture.class.isAssignableFrom(returnType)) {
            return CompletableFuture.supplyAsync(new Supplier() {
                @SneakyThrows
                @Override
                public Object get() {
                    return task.call();
                }
            }, executor);
        } else if (ListenableFuture.class.isAssignableFrom(returnType)) {
            ListenableFutureTask future = new ListenableFutureTask<>(task);
            executor.execute(future);
            return future;
        } else if (Future.class.isAssignableFrom(returnType)) {
            return executor.submit(task);
        } else {
            executor.submit(task);
            return null;
        }
    }

    private JediThreadPoolExecutor extractJediThreadPoolExecutor(JediAsync jediAsync) {
        JediThreadPoolExecutor jediThreadPoolExecutor;
        if (StringUtils.isNotBlank(jediAsync.executorName())) {
            jediThreadPoolExecutor = executorMap.get(jediAsync.executorName());
            if (jediThreadPoolExecutor == null) {
                throw new JediClientException(String.format("未配置@JediAsync指定的线程池:%s", jediAsync.executorName()));
            }
        } else {
            // only one executor
            if (uniqueExecutor != null) {
                jediThreadPoolExecutor = uniqueExecutor;
            } else {
                throw new JediClientException(String.format("容器中有 %d 个线程池, 需在@JediAsync中指定", executorMap.size()));
            }
        }
        return jediThreadPoolExecutor;
    }

    private String extractTaskName(ProceedingJoinPoint joinPoint, JediAsync jediAsync) {
        String taskName;
        if (StringUtils.isBlank(jediAsync.taskName())) {
            taskName = JEDI_DEFAULT_TASK_NAME;
        } else {
            try {
                Object taskNameObject = ExpressionUtil.evaluateValue(joinPoint, jediAsync.taskName(),
                        this.applicationContext);
                if (taskNameObject == null) {
                    throw new JediClientException(String.format("@JediAsync taskName:%s, cannot be null",
                            jediAsync.taskName()));
                }
                taskName = StringUtils.isBlank(taskNameObject.toString()) ? JEDI_DEFAULT_TASK_NAME :
                        taskNameObject.toString();
            } catch (JediClientException e1) {
                throw e1;
            } catch (Exception e) {
                throw new JediClientException(String.format("@JediAsync taskName:%s, SpEL Expression exception:%s",
                        jediAsync.taskName(), e));
            }
        }
        return taskName;
    }

    private String extractTaskExtraData(ProceedingJoinPoint joinPoint, JediAsync jediAsync) {
        String taskExtraData;
        if (StringUtils.isBlank(jediAsync.taskExtraData())) {
            taskExtraData = EMPTY;
        } else {
            try {
                Object taskExtraDataObject = ExpressionUtil.evaluateValue(joinPoint, jediAsync.taskExtraData(),
                        this.applicationContext);
                if (taskExtraDataObject == null) {
                    throw new JediClientException(String.format("@JediAsync taskExtraData:%s, cannot be null",
                            jediAsync.taskExtraData()));
                }
                taskExtraData = StringUtils.isBlank(taskExtraDataObject.toString()) ? EMPTY :
                        taskExtraDataObject.toString();
            } catch (JediClientException e1) {
                throw e1;
            } catch (Exception e) {
                throw new JediClientException(String.format("@JediAsync taskExtraData:%s, SpEL Expression exception:%s",
                        jediAsync.taskExtraData(), e));
            }
        }
        return taskExtraData;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.executorMap =
                this.applicationContext.getBeansOfType(JediThreadPoolExecutor.class);
        JediConfig jediConfig = this.applicationContext.getBean(JediConfig.class);
        this.asyncTraceFactory = this.applicationContext.getBean(AsyncTraceFactory.class);
        this.order = jediConfig.getOrder();
        if (executorMap.size() == 1) {
            uniqueExecutor = executorMap.values().stream().findFirst().get();
        } else {
            uniqueExecutor = null;
        }
    }
}
