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
import xyz.hellothomas.jedi.config.domain.ConsumerProperty;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

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

    @Value("${config-service.consumer-url}")
    private String consumerUrl;

    public StaticConfigController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/consumer/{namespace}/{appId}")
    @ApiOperation("consumer")
    public ApiResponse<ConsumerProperty> consumer(@PathVariable String namespace,
                                                  @PathVariable String appId) {
        log.info("namespace:{}, appId:{}", namespace, appId);
        ResponseEntity<ApiResponse<ConsumerProperty>> responseEntity = restTemplate.exchange(consumerUrl +
                        "/static-config/consumer/{namespace}/{appId}", HttpMethod.GET, null,
                new ParameterizedTypeReference<ApiResponse<ConsumerProperty>>() {
                }, namespace, appId);

        return responseEntity.getBody();
    }
}
