package io.github.pauldeen79.microservicediagramcreator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.pauldeen79.microservicediagramcreator.model.Database;
import io.github.pauldeen79.microservicediagramcreator.model.DatabaseType;
import io.github.pauldeen79.microservicediagramcreator.model.Topic;
import io.github.pauldeen79.microservicediagramcreator.util.StringUtils;

@Service
public class PlantUmlDiagramService {

    public String generateDiagram(String packageName, List<io.github.pauldeen79.microservicediagramcreator.model.Service> services) {
        var sb = new StringBuilder();

        sb.append("@startuml%n%n".formatted());

        sb.append("package \"%s\" {%n".formatted(packageName));

        services
             .forEach(service ->
                 sb.append("[%s] as %s%n"
                     .formatted(service.name(), service.alias())));

        sb.append("}%n%n".formatted());

        var databases = services.stream()
                .flatMap(service -> service.databases().stream())
                .map(Database::fromServiceDatabase)
                .distinct()
                .toList();

        databases.forEach(database -> sb.append("database \"%s\" as %s%n"
                .formatted(database.name(), database.alias())));

        var queues = services.stream()
                .flatMap(service -> service.topics().stream()
                .map(Topic::fromServiceTopic))
                .distinct()
                .toList();

        queues.forEach(queue -> sb.append("queue \"%s\" as %s%n"
                .formatted(queue.name(), queue.alias())));

        services.forEach(service -> service.databases().forEach(database ->
                sb.append("%s --> %s : %s%n".formatted(
                        service.alias(),
                        Database.fromServiceDatabase(database).alias(),
                        databaseLabel(database.type())))));

        services.forEach(service -> service.topics().forEach(topic -> {
            var topicAlias = Topic.fromServiceTopic(topic).alias();
            if (topic.send()) {
                sb.append("%s --> %s : Publish%n".formatted(service.alias(), topicAlias));
            }
            if (topic.receive()) {
                sb.append("%s --> %s : Subscribe%n".formatted(service.alias(), topicAlias));
            }
        }));

        services.forEach(service -> service.restClients().forEach(restClient ->
                sb.append("%s --> %s : REST%n".formatted(
                        service.alias(),
                        resolveServiceAlias(restClient.name(), services)))));

        sb.append("@enduml");

        return sb.toString();
    }

    private static String databaseLabel(DatabaseType type) {
        return switch (type) {
            case POSTGRESQL -> "JPA";
            case SYBASE -> "JDBC";
        };
    }

    private static String resolveServiceAlias(String name, List<io.github.pauldeen79.microservicediagramcreator.model.Service> services) {
        return services.stream()
                .filter(service -> service.name().equals(name) || service.alias().equals(name))
                .findFirst()
                .map(io.github.pauldeen79.microservicediagramcreator.model.Service::alias)
                .orElseGet(() -> StringUtils.toAlias(name));
    }
}
