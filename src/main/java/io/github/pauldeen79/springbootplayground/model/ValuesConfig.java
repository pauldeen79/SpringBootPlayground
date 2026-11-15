package io.github.pauldeen79.springbootplayground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ValuesConfig(List<String> enableDefaultConfiguration) {
}
