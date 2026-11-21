package io.github.pauldeen79.microservicediagramcreator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChartConfig(String name) {
}
