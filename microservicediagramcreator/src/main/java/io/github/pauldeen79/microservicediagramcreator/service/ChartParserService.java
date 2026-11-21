package io.github.pauldeen79.microservicediagramcreator.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import io.github.pauldeen79.microservicediagramcreator.model.chart.ChartConfig;
import io.github.pauldeen79.microservicediagramcreator.model.chart.ValuesConfig;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChartParserService {

    private final YAMLMapper yamlMapper;

    public Optional<String> resolveServiceName(Path chartDirectory) {
        Path chartFile = resolveChartFile(chartDirectory);
        if (chartFile == null) {
            return Optional.empty();
        }

        ChartConfig config = yamlMapper.readValue(chartFile.toFile(), ChartConfig.class);
        if (config == null || config.name() == null || config.name().isBlank()) {
            return Optional.empty();
        }

        return Optional.of(config.name().trim());
    }

    public boolean usesKafkaTopics(Path chartDirectory) {
        Path valuesFile = chartDirectory.resolve("values.yaml");
        if (!Files.exists(valuesFile)) {
            return false;
        }

        ValuesConfig config = yamlMapper.readValue(valuesFile.toFile(), ValuesConfig.class);
        if (config == null || config.enableDefaultConfiguration() == null) {
            return false;
        }

        return config.enableDefaultConfiguration().contains("enableKafkaConfig");
    }

    private Path resolveChartFile(Path chartDirectory) {
        Path yamlPath = chartDirectory.resolve("chart.yaml");
        if (Files.exists(yamlPath)) {
            return yamlPath;
        }

        Path ymlPath = chartDirectory.resolve("chart.yml");
        if (Files.exists(ymlPath)) {
            return ymlPath;
        }

        return null;
    }
}
