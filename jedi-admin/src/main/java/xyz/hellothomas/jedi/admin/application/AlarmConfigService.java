package xyz.hellothomas.jedi.admin.application;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xyz.hellothomas.jedi.admin.api.dto.AlarmConfigResponse;
import xyz.hellothomas.jedi.admin.domain.AlarmConfig;
import xyz.hellothomas.jedi.admin.domain.Audit;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;

@Service
public class AlarmConfigService {

    @Value("${admin-service.consumer-url}")
    private String consumerUrl;

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

        ResponseEntity<AlarmConfigResponse> responseEntity = restTemplate.postForEntity(consumerUrl + "/namespaces" +
                        "/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs?configuration" +
                        "={configuration}&operator={operator}", null, AlarmConfigResponse.class,
                namespaceName, appId, executorName, configuration, operator);

        auditService.audit(AlarmConfig.class.getSimpleName(), responseEntity.getBody().getId(), Audit.OP.INSERT,
                responseEntity.getBody().getDataChangeCreatedBy());

        return responseEntity.getBody();
    }

    public AlarmConfigResponse update(String namespaceName, String appId, String executorName, String configuration,
                                      String operator) {
        ResponseEntity<AlarmConfigResponse> responseEntity = restTemplate.exchange(consumerUrl + "/namespaces" +
                        "/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs?configuration" +
                        "={configuration}&operator={operator}", HttpMethod.PUT, null, AlarmConfigResponse.class,
                namespaceName, appId, executorName, configuration, operator);

        auditService.audit(AlarmConfig.class.getSimpleName(), responseEntity.getBody().getId(), Audit.OP.UPDATE,
                responseEntity.getBody().getDataChangeLastModifiedBy());

        return responseEntity.getBody();
    }

    public void delete(long alarmConfigId, String operator) {
        restTemplate.delete(consumerUrl + "/alarm-configs/{alarmConfigId}?operator={operator}", alarmConfigId,
                operator);

        auditService.audit(AlarmConfig.class.getSimpleName(), alarmConfigId, Audit.OP.DELETE, operator);
    }
}
