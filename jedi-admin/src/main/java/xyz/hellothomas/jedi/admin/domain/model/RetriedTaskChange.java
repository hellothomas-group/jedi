package xyz.hellothomas.jedi.admin.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Thomas
 * @date 2022/1/3 15:28
 * @description
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class RetriedTaskChange {
    private String taskId;

    private String newTaskId;

    private String operator;
}
