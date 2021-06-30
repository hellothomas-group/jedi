package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.admin.api.dto.TestResponse;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @author Thomas
 * @date 2021/2/1 22:36
 * @description
 * @version 1.0
 */
@Api(value = "test", tags = "test")
@RestController
@Slf4j
public class TestController {
    private Random random = new Random();

    @GetMapping("/test")
    public TestResponse get() {
        TestResponse testResponse = new TestResponse();
        testResponse.setQueueSize(random.nextInt(200));
        testResponse.setPoolActivation(new BigDecimal(String.format("0.%d", random.nextInt(101))));
        testResponse.setRejectCount(random.nextInt(10));

        return testResponse;
    }
}
