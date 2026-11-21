package io.github.pauldeen79.microservicediagramcreator.model;

public record Database(
    String name,
    String alias) {

    public static Database fromServiceDatabase(io.github.pauldeen79.microservicediagramcreator.model.ServiceDatabase serviceDatabase) {
        return new Database(serviceDatabase.name(), serviceDatabase.name()
                .replace("-", "_")
                .replace(" ", "_"));
    }
}
