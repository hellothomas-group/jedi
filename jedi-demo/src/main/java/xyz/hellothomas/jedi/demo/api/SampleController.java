package xyz.hellothomas.jedi.demo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.demo.application.TaskService;

/**
 * @author Thomas
 * @date 2020/12/27 21:28
 * @description
 * @version 1.0
 */
@Api(value = "sample", tags = "sample")
@RestController
public class SampleController {
    private final TaskService taskService;

    public SampleController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ApiOperation("hello")
    @GetMapping("/hello")
    public String sayHello() {
        return "hello thomas";
    }

    @ApiOperation("submit-default-task")
    @GetMapping("/submit-default-task")
    public String submitDefaultTask(@RequestParam int counts) {
        long currentTime = System.currentTimeMillis();
        for (int i = 1; i <= counts; i++) {
            taskService.runDefaultTask(currentTime + "-" + i);
        }
        return "提交成功";
    }

    @ApiOperation("submit-no-persist-task")
    @GetMapping("/submit-no-persist-task")
    public String submitNoPersistTask(@RequestParam int counts) {
        long currentTime = System.currentTimeMillis();
        for (int i = 1; i <= counts; i++) {
            taskService.runNoPersistTask(currentTime + "-" + i);
        }
        return "提交成功";
    }

    @ApiOperation("submit-custom-task")
    @GetMapping("/submit-custom-task")
    public String submitCustomTask(@RequestParam int counts) {
        long currentTime = System.currentTimeMillis();
        for (int i = 1; i <= counts; i++) {
            taskService.runCustomTask(currentTime + "-" + i);
        }
        return "提交成功";
    }

    @ApiOperation("submit-exception-task")
    @GetMapping("/submit-exception-task")
    public String submitExceptionTask(@RequestParam int counts) {
        long currentTime = System.currentTimeMillis();
        for (int i = 1; i <= counts; i++) {
            taskService.runExceptionTask(currentTime + "-" + i);
        }
        return "提交成功";
    }

    @ApiOperation("submit-parent-task")
    @GetMapping("/submit-parent-task")
    public String submitParentTask(@RequestParam int counts) {
        long currentTime = System.currentTimeMillis();
        for (int i = 1; i <= counts; i++) {
            taskService.runParentTask(currentTime + "-" + i);
        }
        return "提交成功";
    }
}
