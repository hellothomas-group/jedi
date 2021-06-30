package xyz.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author 80234613 唐圆
 * @date 2021/6/30 16:54
 * @descripton
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class TestResponse {
    private int queueSize;

    private BigDecimal poolActivation;

    private int rejectCount;
}
