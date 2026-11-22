package io.github.pauldeen79.microservicediagramcreator.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import io.github.pauldeen79.microservicediagramcreator.model.chart.EnvironmentValues;
import io.github.pauldeen79.microservicediagramcreator.model.chart.HelmChart;
import io.github.pauldeen79.microservicediagramcreator.model.chart.Values;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HelmChartParserService {

    private final YAMLMapper yamlMapper;

    public Optional<String> resolveServiceName(Path chartDirectory) {
        Path chartFile = resolveChartFile(chartDirectory);
        if (chartFile == null) {
            return Optional.empty();
        }

        HelmChart config = yamlMapper.readValue(chartFile.toFile(), HelmChart.class);
        if (config == null || config.name() == null || config.name().isBlank()) {
            return Optional.empty();
        }

        if (config.type() == null || !config.type().equals("application")) {
            return Optional.empty();
        }

        return Optional.of(config.name().trim());
    }

    public boolean usesKafkaTopics(Path chartDirectory) {
        Path valuesFile = chartDirectory.resolve("values.yaml");
        if (!Files.exists(valuesFile)) {
            return false;
        }

        Values config = yamlMapper.readValue(valuesFile.toFile(), Values.class);
        if (config == null || config.enableDefaultConfiguration() == null) {
            return false;
        }

        return config.enableDefaultConfiguration().contains("enableKafkaConfig");
    }

    public List<EnvironmentValues.ParsedConfigMap> parseEnvironmentConfigMaps(Path chartDirectory) {
        Path valuesFile = resolveEnvironmentValuesFile(chartDirectory);
        if (valuesFile == null) {
            return List.of();
        }

        EnvironmentValues values = yamlMapper.readValue(valuesFile.toFile(), EnvironmentValues.class);
        if (values == null) {
            throw new IllegalArgumentException("Environment values file is empty: " + valuesFile);
        }
        if (values.configMaps() == null) {
            return List.of();
        }

        List<EnvironmentValues.ParsedConfigMap> configMaps = new ArrayList<>();
        for (EnvironmentValues.ConfigMap configMap : values.configMaps()) {
            if (configMap == null || configMap.name() == null || configMap.name().isBlank()
                    || configMap.data() == null) {
                throw new IllegalArgumentException("ConfigMap must have a name and data in " + valuesFile);
            }

            Map<String, EnvironmentValues.MyCompanyConfig> data = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : configMap.data().entrySet()) {
                if (entry.getValue() == null) {
                    throw new IllegalArgumentException("ConfigMap data is null for " + entry.getKey());
                }
                EnvironmentValues.MyCompanyConfig config = yamlMapper.readValue(
                        entry.getValue(), EnvironmentValues.MyCompanyConfig.class);
                if (config == null) {
                    throw new IllegalArgumentException("ConfigMap data is empty for " + entry.getKey());
                }
                data.put(entry.getKey(), config);
            }
            configMaps.add(new EnvironmentValues.ParsedConfigMap(configMap.name(), data));
        }
        return configMaps;
    }

    private Path resolveEnvironmentValuesFile(Path chartDirectory) {
        Path productionFile = chartDirectory.resolve("values-prd.yaml");
        if (!Files.notExists(productionFile)) {
            return productionFile;
        }

        Path acceptanceFile = chartDirectory.resolve("values-acc.yaml");
        if (!Files.notExists(acceptanceFile)) {
            return acceptanceFile;
        }

        return null;
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
