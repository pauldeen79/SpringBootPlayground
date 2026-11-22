package io.github.pauldeen79.microservicediagramcreator.model.chart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ValuesConfig(
    List<String> enableDefaultConfiguration,
    Service service,
    List<Volume> volumes
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Service(Integer port) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Volume(ConfigMap configMap) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ConfigMap(String name) {
    }
}
