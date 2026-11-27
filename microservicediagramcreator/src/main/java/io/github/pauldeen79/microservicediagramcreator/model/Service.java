package io.github.pauldeen79.microservicediagramcreator.model;

import java.util.List;

public record Service(
    String name,
    String alias,
    List<ServiceTopic> topics,
    List<ServiceDatabase> databases,
    List<ServiceRestClient> restClients) {

    public record ServiceDatabase(
        String name,
        DatabaseType type,
        boolean read,
        boolean write) {
    }

    public record ServiceRestClient(String name) {
    }

    public record ServiceTopic(String name, boolean receive, boolean send) {
    }
}
