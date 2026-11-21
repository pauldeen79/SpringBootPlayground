package io.github.pauldeen79.microservicediagramcreator.model;

import java.util.List;

public record Service(
    String name,
    String alias,
    List<ServiceTopic> topics,
    List<ServiceDatabase> databases,
    List<ServiceRestClient> restClients) {

}
