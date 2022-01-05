package xyz.hellothomas.jedi.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import static xyz.hellothomas.jedi.client.constants.Constants.JEDI_DEFAULT_RETRYER_PATH;

/**
 * @author Thomas
 * @date 2021/4/11 10:12
 * @description
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class JediPersistenceProperty {
    /**
     * 开启持久化
     */
    private boolean enable;

    /**
     * 重试
     */
    @NestedConfigurationProperty
    private final Retryer retryer = new Retryer();

    /**
     * 自恢复
     */
    @NestedConfigurationProperty
    private Recover recover = new Recover();

    @Getter
    @Setter
    @ToString
    public static class Retryer {

        /**
         * 开启重试
         */
        private boolean enable;

        /**
         * retry path (default: `/jedi/tasks/retry`).
         */
        private String path = JEDI_DEFAULT_RETRYER_PATH;
    }

    @Getter
    @Setter
    @ToString
    public static class Recover {
        /**
         * 开启重启自恢复（任务处于登记状态）
         */
        private boolean enable;

        /**
         * 需自恢复的数据源名称集合
         */
        private String dataSourceNames;
    }
}
