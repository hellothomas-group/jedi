package xyz.hellothomas.jedi.admin.infrastructure.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.utils.JsonUtil;

import static xyz.hellothomas.jedi.core.enums.CoreErrorCodeEnum.SUCCESS;

@Slf4j
@Component
public class SyncListener {
    @Value("${admin-service.collector-url}")
    private String collectorUrl;

    private final RestTemplate restTemplate;

    public SyncListener(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    @EventListener
    public void onSyncEvent(SyncEvent event) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity(JsonUtil.serialize(event.getSource()));
            ResponseEntity<ApiResponse<String>> responseEntity = restTemplate.exchange(collectorUrl +
                            "/sync/syncType/{syncType}/syncOperation/{syncOperation}", HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<ApiResponse<String>>() {
                    }, event.getSyncTypeEnum(), event.getSyncOperationEnum());

            ApiResponse<String> apiResponse = responseEntity.getBody();
            if (!SUCCESS.getCode().equals(apiResponse.getCode())) {
                log.error("Sync failed. syncType = {}, syncOperation = {}, object = {}, errorMessage:{}",
                        event.getSyncTypeEnum(), event.getSyncOperationEnum(), event.getSource(),
                        String.format("%s-%s", apiResponse.getCode(), apiResponse.getMessage()));
            } else {
                log.info("Sync success. syncType = {}, syncOperation = {}, object = {}", event.getSyncTypeEnum(),
                        event.getSyncOperationEnum(), event.getSource());
            }
        } catch (Exception e) {
            log.error("Sync failed. syncType = {}, syncOperation = {}, object = {}", event.getSyncTypeEnum(),
                    event.getSyncOperationEnum(), event.getSource(), e);
        }
    }
}
