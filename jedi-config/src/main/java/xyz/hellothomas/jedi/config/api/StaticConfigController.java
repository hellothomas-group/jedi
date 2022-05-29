package xyz.hellothomas.jedi.config.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import xyz.hellothomas.jedi.biz.infrastructure.exception.ServiceException;
import xyz.hellothomas.jedi.config.domain.pojo.CollectorProperty;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import static xyz.hellothomas.jedi.core.enums.CoreErrorCodeEnum.SUCCESS;

/**
 * @author Thomas
 * @date 2021/2/1 22:36
 * @description
 * @version 1.0
 */
@Api(value = "static-config", tags = "static-config")
@RestController
@RequestMapping("/static-config")
@Slf4j
public class StaticConfigController {
    private final RestTemplate restTemplate;

    @Value("${config-service.collector-url}")
    private String collectorUrl;

    public StaticConfigController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Deprecated
    @GetMapping(value = "/consumer/{namespace}/{appId}")
    @ApiOperation("consumer")
    public CollectorProperty consumer(@PathVariable String namespace,
                                      @PathVariable String appId) {
        log.info("namespace:{}, appId:{}", namespace, appId);
        ResponseEntity<ApiResponse<CollectorProperty>> responseEntity = restTemplate.exchange(collectorUrl +
                        "/static-config/collector/{namespace}/{appId}", HttpMethod.GET, null,
                new ParameterizedTypeReference<ApiResponse<CollectorProperty>>() {
                }, namespace, appId);

        ApiResponse<CollectorProperty> apiResponse = responseEntity.getBody();
        if (!SUCCESS.getCode().equals(apiResponse.getCode())) {
            throw new ServiceException(String.format("%s-%s", apiResponse.getCode(), apiResponse.getMessage()));
        }

        return apiResponse.getData();
    }

    @GetMapping(value = "/collector/{namespace}/{appId}")
    @ApiOperation("collector")
    public CollectorProperty collector(@PathVariable String namespace,
                                      @PathVariable String appId) {
        log.info("namespace:{}, appId:{}", namespace, appId);
        ResponseEntity<ApiResponse<CollectorProperty>> responseEntity = restTemplate.exchange(collectorUrl +
                        "/static-config/collector/{namespace}/{appId}", HttpMethod.GET, null,
                new ParameterizedTypeReference<ApiResponse<CollectorProperty>>() {
                }, namespace, appId);

        ApiResponse<CollectorProperty> apiResponse = responseEntity.getBody();
        if (!SUCCESS.getCode().equals(apiResponse.getCode())) {
            throw new ServiceException(String.format("%s-%s", apiResponse.getCode(), apiResponse.getMessage()));
        }

        return apiResponse.getData();
    }
}
