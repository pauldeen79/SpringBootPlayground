package io.github.pauldeen79.microservicediagramcreator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.pauldeen79.microservicediagramcreator.model.Database;
import io.github.pauldeen79.microservicediagramcreator.model.Topic;

@Service
public class DiagramService {

    public String generateDiagram(String packageName, List<io.github.pauldeen79.microservicediagramcreator.model.Service> services) {
        var sb = new StringBuilder();

        sb.append("@startuml%n%n");

        sb.append("package \"");
        sb.append(packageName);
        sb.append("\" {%n");

        services
             .forEach(service ->
                 sb.append("[%s] as %s%n"
                     .formatted(service.name(), service.alias())));

        sb.append("}%n%n");

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

        // relations

        sb.append("@enduml");

        return sb.toString();
    }
}
