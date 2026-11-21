package io.github.pauldeen79.microservicediagramcreator.model;

public record ServiceDatabase(
    String name,
    DatabaseType type,
    boolean read,
    boolean write) {

}
