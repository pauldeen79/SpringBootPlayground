package io.github.pauldeen79.microservicediagramcreator.model;

import io.github.pauldeen79.microservicediagramcreator.model.Service.ServiceTopic;
import io.github.pauldeen79.microservicediagramcreator.util.StringUtils;

public record Topic(String name, String alias) {
    public static Topic fromServiceTopic(ServiceTopic serviceTopic) {
        return new Topic(serviceTopic.name(), StringUtils.toAlias(serviceTopic.name()));
    }
}
