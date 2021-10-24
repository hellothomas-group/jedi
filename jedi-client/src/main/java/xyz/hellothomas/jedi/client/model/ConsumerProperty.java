package xyz.hellothomas.jedi.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author Thomas
 * @date 2021/4/14 22:55
 * @description
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class ConsumerProperty {
    private String type;

    private Map<String, Object> configDetails;
}
