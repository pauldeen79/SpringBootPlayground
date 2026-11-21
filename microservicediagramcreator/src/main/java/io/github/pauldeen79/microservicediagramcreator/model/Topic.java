package io.github.pauldeen79.microservicediagramcreator.model;

public record Topic(String name, String alias) {
    public static Topic fromServiceTopic(io.github.pauldeen79.microservicediagramcreator.model.ServiceTopic serviceTopic) {
        return new Topic(serviceTopic.name(), serviceTopic.name()
                .replace("-", "_")
                .replace(" ", "_"));
    }
}
