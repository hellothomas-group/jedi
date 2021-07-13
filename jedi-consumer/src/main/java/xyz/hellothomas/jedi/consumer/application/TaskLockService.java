package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.domain.TaskLock;
import xyz.hellothomas.jedi.consumer.domain.TaskLockExample;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.TaskLockMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 80234613 唐圆
 * @date 2021/7/13 16:05
 * @descripton
 * @version 1.0
 */
@Slf4j
@Service
public class TaskLockService {
    private final TaskLockMapper taskLockMapper;

    public TaskLockService(TaskLockMapper taskLockMapper) {
        this.taskLockMapper = taskLockMapper;
    }

    public int lock(LocalDate taskDate, String taskName) {
        return taskLockMapper.updateLockByUniqueKey(taskDate, taskName, true, LocalDateTime.now());
    }

    public int unlock(LocalDate taskDate, String taskName) {
        return taskLockMapper.updateLockByUniqueKey(taskDate, taskName, false, LocalDateTime.now());
    }

    public void deleteLastDayLock(String taskName) {
        TaskLockExample taskLockExample = new TaskLockExample();
        taskLockExample.createCriteria().andTaskDateEqualTo(LocalDate.now().minusDays(1))
                .andTaskNameEqualTo(taskName);
        taskLockMapper.deleteByExample(taskLockExample);
    }

    public int insertLockedOne(LocalDate taskDate, String taskName) {
        TaskLock taskLock = new TaskLock();
        taskLock.setTaskDate(taskDate);
        taskLock.setTaskName(taskName);
        LocalDateTime currentDateTime = LocalDateTime.now();
        taskLock.setDataChangeCreatedTime(currentDateTime);
        taskLock.setDataChangeLastModifiedTime(currentDateTime);
        taskLock.setVersion(1);
        taskLock.setIsLocked(true);

        try {
            return taskLockMapper.insertSelective(taskLock);
        } catch (Exception e) {
            log.error("insert error：{}", e);
            return 0;
        }
    }
}
