package xyz.hellothomas.jedi.consumer.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author Thomas
 */
@Getter
@Setter
@ToString
public class ExecutorStatusResponse {
    private int queueSize;

    private BigDecimal poolActivation;

    private long rejectCount;
}
