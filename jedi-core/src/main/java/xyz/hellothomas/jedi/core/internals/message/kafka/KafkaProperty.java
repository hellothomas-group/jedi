package xyz.hellothomas.jedi.core.internals.message.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hellothomas.jedi.core.enums.MessageType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

/**
 * @author Thomas
 * @date 2021/1/24 22:25
 * @description
 * @version 1.0
 */
public class KafkaProperty {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProperty.class);
    private String defaultTopic = null;

    private Integer partition = null;

    private Map<String, Object> producerConfig = new HashMap();

    private EnumMap<MessageType, String> topics = new EnumMap<>(MessageType.class);

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public void addProducerConfigValue(String key, Object value) {
        this.producerConfig.put(key, value);
    }

    public Map<String, Object> getProducerConfig() {
        return producerConfig;
    }

    public void setProducerConfig(Map<String, Object> producerConfig) {
        this.producerConfig = producerConfig;
    }

    public Map<MessageType, String> getTopics() {
        return topics;
    }

    public void addTopic(MessageType messageType, String topic) {
        this.topics.put(messageType, topic);
    }

    public boolean checkPrerequisites() {

        if (producerConfig.get(BOOTSTRAP_SERVERS_CONFIG) == null) {
            LOGGER.error("No \"" + BOOTSTRAP_SERVERS_CONFIG + "\" set.");
            return false;
        }

        return true;
    }
}
