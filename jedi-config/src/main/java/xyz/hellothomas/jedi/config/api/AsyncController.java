package xyz.hellothomas.jedi.config.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import xyz.hellothomas.jedi.core.utils.SleepUtil;

import java.util.Map;

/**
 * @author 80234613 唐圆
 * @date 2021/1/29 15:56
 * @descripton
 * @version 1.0
 */
@Api(value = "async", tags = "async")
@RestController
@RequestMapping("/async")
@Slf4j
public class AsyncController {
    final Map<Integer, DeferredResult<String>> deferredResultMap = new ConcurrentReferenceHashMap();

    @ApiOperation("longPooling")
    @GetMapping("/longPolling")
    public DeferredResult<String> longPolling() {
        log.warn("进入longPolling方法");
        DeferredResult<String> deferredResult = new DeferredResult(5000L);
        deferredResultMap.put(deferredResult.hashCode(), deferredResult);
        deferredResult.onCompletion(() -> {
            deferredResultMap.remove(deferredResult.hashCode());
            log.info("{}完成,还剩{}个deferredResult未响应", deferredResult.hashCode(), deferredResultMap.size());
            log.info("{}的result:{}, setOrExpire:{}", deferredResult.hashCode(), deferredResult.getResult(),
                    deferredResult.isSetOrExpired());
        });
        deferredResult.onTimeout(() -> {
            deferredResultMap.remove(deferredResult.hashCode());
            log.info("{}超时,还剩{}个deferredResult未响应", deferredResult.hashCode(), deferredResultMap.size());
            SleepUtil.sleep(8000);
            log.warn("退出超时方法");
        });
        log.warn("退出longPolling方法");
        return deferredResult;
    }

    @ApiOperation("returnLongPollingValue")
    @GetMapping("/returnLongPollingValue")
    public void returnLongPollingValue() {
        log.info("进入returnLongPollingValue");
        for (Map.Entry<Integer, DeferredResult<String>> entry : deferredResultMap.entrySet()) {
            boolean result = entry.getValue().setResult("kl1");
            log.info("{} setResult {}, success: {}", entry.getKey(), entry.getValue().getResult(), result);
            SleepUtil.sleep(1000);
            result = entry.getValue().setResult("kl2");
            log.info("{} setResult {}, success: {}", entry.getKey(), entry.getValue().getResult(), result);
        }
        log.info("退出returnLongPollingValue");
    }
}

