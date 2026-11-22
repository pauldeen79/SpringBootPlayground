package io.github.pauldeen79.microservicediagramcreator.model.chart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HelmChart(String name, String type) {
}
