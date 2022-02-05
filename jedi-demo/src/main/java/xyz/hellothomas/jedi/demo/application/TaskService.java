package xyz.hellothomas.jedi.demo.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.client.annotation.JediAsync;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.core.utils.SleepUtil;

import java.util.Random;

/**
 * @author Thomas
 * @date 2021/11/3 21:41
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class TaskService implements ApplicationContextAware {
    private final Random random = new Random();
    private ApplicationContext applicationContext;

    @JediAsync
    public void runNoPersistTask(String taskExtraData) {
        long startTime = System.currentTimeMillis();
        log.debug("开始执行任务<{}>", taskExtraData);
        SleepUtil.sleepInSecond(10);
        log.debug("完成任务<{}>, 耗时<{}>ms", taskExtraData, System.currentTimeMillis() - startTime);
    }

    @JediAsync(persistent = true, recoverable = true)
    public void runDefaultTask(String taskExtraData) {
        long startTime = System.currentTimeMillis();
        log.debug("开始执行任务<{}>", taskExtraData);
        SleepUtil.sleepInSecond(10);
        log.debug("完成任务<{}>, 耗时<{}>ms", taskExtraData, System.currentTimeMillis() - startTime);
    }

    @JediAsync(executorName = Constants.JEDI_DEFAULT_EXECUTOR_NAME, taskName = "'task1'", taskExtraData =
            "#taskExtraData", persistent = true)
    public void runCustomTask(String taskExtraData) {
        long startTime = System.currentTimeMillis();
        log.debug("开始执行任务<{}>", taskExtraData);
        SleepUtil.sleepInSecond(10);
        log.debug("完成任务<{}>, 耗时<{}>ms", taskExtraData, System.currentTimeMillis() - startTime);
    }

    @JediAsync(persistent = true)
    public void runExceptionTask(String taskExtraData) {
        log.debug("开始执行任务<{}>", taskExtraData);
        SleepUtil.sleepInSecond(10);
        throw new RuntimeException("exceptionTask");
    }

    @JediAsync(executorName = Constants.JEDI_DEFAULT_EXECUTOR_NAME, taskName = "'parentTask'", taskExtraData =
            "#taskExtraData", persistent = true, recoverable = true)
    public void runParentTask(String taskExtraData) {
        long startTime = System.currentTimeMillis();
        log.debug("开始执行任务<{}>", taskExtraData);
        SleepUtil.sleepInSecond(5);
        this.applicationContext.getBean(TaskService.class).runDefaultTask(taskExtraData + "-sub");
        log.debug("完成任务<{}>, 耗时<{}>ms", taskExtraData, System.currentTimeMillis() - startTime);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
