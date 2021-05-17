package xyz.hellothomas.jedi.core.dto.config;

import java.util.Map;

public class JediConfig {

    private String namespaceName;

    private String appId;

    private String executorName;

    private Map<String, String> configurations;

    private String releaseKey;

    public JediConfig() {
    }

    public JediConfig(String namespaceName,
                      String appId,
                      String executorName,
                      String releaseKey) {
        this.namespaceName = namespaceName;
        this.appId = appId;
        this.executorName = executorName;
        this.releaseKey = releaseKey;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public Map<String, String> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Map<String, String> configurations) {
        this.configurations = configurations;
    }

    public String getReleaseKey() {
        return releaseKey;
    }

    public void setReleaseKey(String releaseKey) {
        this.releaseKey = releaseKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JediConfig{");
        sb.append("namespaceName='").append(namespaceName).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", executorName='").append(executorName).append('\'');
        sb.append(", configurations=").append(configurations);
        sb.append(", releaseKey='").append(releaseKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
