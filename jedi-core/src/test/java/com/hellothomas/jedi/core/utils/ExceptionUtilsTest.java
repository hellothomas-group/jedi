package com.hellothomas.jedi.core.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionUtilsTest.class);

    @Test
    public void testGetStackTrace() {
        try {
            String string = "".substring(2);
        } catch (Exception e) {
            LOGGER.error("getLocalHostError: {}", ExceptionUtils.getStackTrace(e));
        }
    }
}