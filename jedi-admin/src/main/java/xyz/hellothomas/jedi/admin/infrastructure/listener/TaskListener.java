package xyz.hellothomas.jedi.admin.infrastructure.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.hellothomas.jedi.admin.domain.model.RetriedTaskChange;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import static xyz.hellothomas.jedi.core.enums.CoreErrorCodeEnum.SUCCESS;

@Slf4j
@Component
public class TaskListener {
    @Value("${admin-service.collector-url}")
    private String collectorUrl;

    private final RestTemplate restTemplate;

    public TaskListener(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    @EventListener
    public void onSyncEvent(TaskEvent event) {
        try {
            RetriedTaskChange retriedTaskChange = (RetriedTaskChange) event.getSource();
            ResponseEntity<ApiResponse<String>> responseEntity = restTemplate.exchange(collectorUrl +
                            "/tasks/{taskId}/retried?newTaskId={newTaskId}&operator={operator}", HttpMethod.POST,
                    null, new ParameterizedTypeReference<ApiResponse<String>>() {
                    }, retriedTaskChange.getTaskId(), retriedTaskChange.getNewTaskId(),
                    retriedTaskChange.getOperator());

            ApiResponse<String> apiResponse = responseEntity.getBody();
            if (!SUCCESS.getCode().equals(apiResponse.getCode())) {
                log.error("Retry sync failed. retriedTaskChange = {}, errorMessage:{}",
                        retriedTaskChange, String.format("%s-%s", apiResponse.getCode(), apiResponse.getMessage()));
            } else {
                log.info("Retry sync success. retriedTaskChange = {})", retriedTaskChange);
            }
        } catch (Exception e) {
            log.error("Retry sync failed. retriedTaskChange = {}", event.getSource(), e);
        }
    }
}
