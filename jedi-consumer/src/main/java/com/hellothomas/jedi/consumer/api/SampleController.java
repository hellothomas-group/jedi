package com.hellothomas.jedi.consumer.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className SampleController
 * @author Thomas
 * @date 2020/12/27 21:28
 * @description
 * @version 1.0
 */
@Api(value = "sample", tags = "sample")
@RestController
public class SampleController {

    @ApiOperation("hello")
    @GetMapping("/hello")
    public String sayHello() {
        return "hello thomas";
    }
}
