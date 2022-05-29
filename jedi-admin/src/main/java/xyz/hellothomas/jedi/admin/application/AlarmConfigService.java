package xyz.hellothomas.jedi.admin.application;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xyz.hellothomas.jedi.admin.api.dto.AlarmConfigResponse;
import xyz.hellothomas.jedi.admin.domain.Audit;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.biz.domain.monitor.AlarmConfig;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import xyz.hellothomas.jedi.biz.infrastructure.exception.ServiceException;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import static xyz.hellothomas.jedi.core.enums.CoreErrorCodeEnum.SUCCESS;

@Service
public class AlarmConfigService {

    @Value("${admin-service.collector-url}")
    private String collectorUrl;

    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final AuditService auditService;

    public AlarmConfigService(
            final RestTemplate restTemplate,
            final @Lazy ExecutorService executorService,
            final AuditService auditService) {
        this.restTemplate = restTemplate;
        this.executorService = executorService;
        this.auditService = auditService;
    }

    public AlarmConfigResponse save(String namespaceName, String appId, String executorName, String configuration,
                                    String operator) {
        Executor executor = executorService.findOne(namespaceName, appId, executorName);
        if (executor == null) {
            throw new NotFoundException(
                    String.format("executor not found for %s %s %s", namespaceName, appId, executorName));
        }

        ResponseEntity<ApiResponse<AlarmConfigResponse>> responseEntity = restTemplate.exchange(collectorUrl +
                        "/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs" +
                        "?configuration={configuration}&operator={operator}", HttpMethod.POST, null,
                new ParameterizedTypeReference<ApiResponse<AlarmConfigResponse>>() {
                }, namespaceName, appId, executorName, configuration, operator);

        ApiResponse<AlarmConfigResponse> apiResponse = responseEntity.getBody();
        if (!SUCCESS.getCode().equals(apiResponse.getCode())) {
            throw new ServiceException(String.format("%s-%s", apiResponse.getCode(), apiResponse.getMessage()));
        }

        auditService.audit(AlarmConfig.class.getSimpleName(), apiResponse.getData().getId(), Audit.OP.INSERT,
                apiResponse.getData().getCreateUser());

        return apiResponse.getData();
    }

    public AlarmConfigResponse update(String namespaceName, String appId, String executorName, String configuration,
                                      String operator) {
        ResponseEntity<ApiResponse<AlarmConfigResponse>> responseEntity = restTemplate.exchange(collectorUrl +
                        "/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs" +
                        "?configuration={configuration}&operator={operator}", HttpMethod.PUT, null,
                new ParameterizedTypeReference<ApiResponse<AlarmConfigResponse>>() {
                }, namespaceName, appId, executorName, configuration, operator);

        ApiResponse<AlarmConfigResponse> apiResponse = responseEntity.getBody();
        if (!SUCCESS.getCode().equals(apiResponse.getCode())) {
            throw new ServiceException(String.format("%s-%s", apiResponse.getCode(), apiResponse.getMessage()));
        }

        auditService.audit(AlarmConfig.class.getSimpleName(), apiResponse.getData().getId(), Audit.OP.UPDATE,
                apiResponse.getData().getCreateUser());

        return apiResponse.getData();
    }

    public void delete(long alarmConfigId, String operator) {
        restTemplate.delete(collectorUrl + "/alarm-configs/{alarmConfigId}?operator={operator}", alarmConfigId,
                operator);

        auditService.audit(AlarmConfig.class.getSimpleName(), alarmConfigId, Audit.OP.DELETE, operator);
    }

    public AlarmConfigResponse findOne(String namespaceName, String appId, String executorName) {
        return restTemplate.getForObject(collectorUrl + "/namespaces/{namespaceName}/apps/{appId}/executors" +
                "/{executorName}/alarm-configs", AlarmConfigResponse.class, namespaceName, appId, executorName);
    }
}
