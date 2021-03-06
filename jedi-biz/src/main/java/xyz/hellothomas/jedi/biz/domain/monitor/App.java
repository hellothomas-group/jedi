package xyz.hellothomas.jedi.biz.domain.monitor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;

/**
 * @author
 */
@Getter
@Setter
@ToString
public class App extends BaseEntity {

    /**
     * namespace名字，注意，需要全局唯一
     */
    private String namespaceName;

    /**
     * appId
     */
    private String appId;

    /**
     * app描述
     */
    private String appDescription;

    /**
     * ownerName
     */
    private String ownerName;
}