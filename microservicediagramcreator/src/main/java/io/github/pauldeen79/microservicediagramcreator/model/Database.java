package io.github.pauldeen79.microservicediagramcreator.model;

import io.github.pauldeen79.microservicediagramcreator.model.Service.ServiceDatabase;
import io.github.pauldeen79.microservicediagramcreator.util.StringUtils;

public record Database(
    String name,
    String alias) {

    public static Database fromServiceDatabase(ServiceDatabase serviceDatabase) {
        return new Database(serviceDatabase.name(), StringUtils.toAlias(serviceDatabase.name()));
    }
}
