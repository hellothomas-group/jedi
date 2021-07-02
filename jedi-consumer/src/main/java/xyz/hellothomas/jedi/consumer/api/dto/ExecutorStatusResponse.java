package xyz.hellothomas.jedi.consumer.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime recordTime;
}
