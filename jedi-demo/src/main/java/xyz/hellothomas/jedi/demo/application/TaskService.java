package xyz.hellothomas.jedi.demo.application;

import lombok.extern.slf4j.Slf4j;
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
public class TaskService {
    private final Random random = new Random();

    @JediAsync
    public void runDefaultTask(String taskExtraData) {
        long startTime = System.currentTimeMillis();
        log.debug("开始执行任务<{}>", taskExtraData);
        SleepUtil.sleepInSecond(10 + random.nextInt(2));
        log.debug("完成任务<{}>, 耗时<{}>ms", taskExtraData, System.currentTimeMillis() - startTime);
    }

    @JediAsync(executorName = Constants.JEDI_DEFAULT_EXECUTOR_NAME, taskName = "'Task1'", taskExtraData = "#taskExtraData")
    public void runCustomTask(String taskExtraData) {
        long startTime = System.currentTimeMillis();
        log.debug("开始执行任务<{}>", taskExtraData);
        SleepUtil.sleepInSecond(10 + random.nextInt(2));
        log.debug("完成任务<{}>, 耗时<{}>ms", taskExtraData, System.currentTimeMillis() - startTime);
    }
}
