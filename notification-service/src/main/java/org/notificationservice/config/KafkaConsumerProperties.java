package org.notificationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.consumer")
public class KafkaConsumerProperties {

    private String bootstrapServers;
    private String groupId;
    private String trustedPackages;
    private String defaultType;

    public KafkaConsumerProperties() {
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getTrustedPackages() {
        return trustedPackages;
    }

    public String getDefaultType() {
        return defaultType;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setTrustedPackages(String trustedPackages) {
        this.trustedPackages = trustedPackages;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }
}