package xyz.hellothomas.jedi.consumer.api;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.consumer.api.dto.ExecutorStatusResponse;
import xyz.hellothomas.jedi.consumer.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.consumer.api.dto.PageResult;
import xyz.hellothomas.jedi.consumer.application.ExecutorTickerService;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTickerMessage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExecutorStatusController {

    private final ExecutorTickerService executorTickerService;

    public ExecutorStatusController(ExecutorTickerService executorTickerService) {
        this.executorTickerService = executorTickerService;
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/status")
    public PageResult<ExecutorStatusResponse> find(@PathVariable("namespaceName") String namespaceName,
                                                   @PathVariable("appId") String appId,
                                                   @PathVariable("executorName") String executorName,
                                                   @RequestParam("instanceIp") String instanceIp,
                                                   @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd " +
                                                           "HH:mm:ss") LocalDateTime startTime,
                                                   @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd " +
                                                           "HH:mm:ss") LocalDateTime endTime,
                                                   PageHelperRequest pageHelperRequest) {
        PageResult<ExecutorTickerMessage> tickerMessagePageResult =
                executorTickerService.findByExecutorHostAndRecordTime(namespaceName
                        , appId, executorName, instanceIp, startTime, endTime, pageHelperRequest);
        PageResult<ExecutorStatusResponse> executorStatusResponsePageResult =
                transform2PageResult(tickerMessagePageResult);

        return executorStatusResponsePageResult;
    }

    private PageResult<ExecutorStatusResponse> transform2PageResult(PageResult<ExecutorTickerMessage> tickerMessagePageResult) {
        List<ExecutorTickerMessage> executorTickerMessages = tickerMessagePageResult.getContent();
        List<ExecutorStatusResponse> executorStatusResponses = new ArrayList<>(executorTickerMessages.size());
        for (ExecutorTickerMessage tickerMessage : executorTickerMessages) {
            ExecutorStatusResponse executorStatusResponse = new ExecutorStatusResponse();
            executorStatusResponse.setQueueSize(tickerMessage.getQueueSize());
            executorStatusResponse.setRejectCount(tickerMessage.getRejectCount());
            executorStatusResponse.setPoolActivation(new BigDecimal(tickerMessage.getActiveCount()).divide(new BigDecimal(tickerMessage.getMaximumPoolSize())));
            executorStatusResponse.setRecordTime(tickerMessage.getRecordTime());

            executorStatusResponses.add(executorStatusResponse);
        }

        return new PageResult<>(executorStatusResponses, tickerMessagePageResult.getTotal(),
                tickerMessagePageResult.getPageNum(),
                tickerMessagePageResult.getPageSize());
    }
}