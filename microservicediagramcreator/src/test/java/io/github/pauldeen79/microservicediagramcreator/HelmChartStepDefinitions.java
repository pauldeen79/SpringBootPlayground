package io.github.pauldeen79.microservicediagramcreator;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.pauldeen79.microservicediagramcreator.model.chart.EnvironmentValues;
import io.github.pauldeen79.microservicediagramcreator.model.chart.Values;
import io.github.pauldeen79.microservicediagramcreator.service.HelmChartParserService;

import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.core.JacksonException;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelmChartStepDefinitions {

    @Autowired
    private HelmChartParserService chartParserService;

    @Autowired
    private YAMLMapper yamlMapper;

    private Path tempChartDir;
    private Optional<String> resolvedServiceName;
    private boolean usesKafkaTopics;
    private Values valuesConfig;
    private List<EnvironmentValues.ParsedConfigMap> environmentConfigMaps;
    private EnvironmentValues.MyCompanyConfig selectedConfig;

    @Before
    public void setUp() throws IOException {
        tempChartDir = Files.createTempDirectory("test-chart-");
    }

    @After
    public void tearDown() throws IOException {
        if (tempChartDir != null && Files.exists(tempChartDir)) {
            try (Stream<Path> walk = Files.walk(tempChartDir)) {
                walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException _) {
                            // intentionally left empty - test clean up failure should not break tests
                        }
                    });
            }
        }
    }

    @Given("a chart directory containing a {string} file with the following content:")
    public void aChartDirectoryContainingAFileWithContent(String fileName, String yamlContent) throws IOException {
        Files.writeString(tempChartDir.resolve(fileName), yamlContent);
    }

    @Given("an empty chart directory with no {string} file")
    public void anEmptyChartDirectoryWithNoFile(String fileName) {
        // Directory starts empty from @Before
    }

    @Given("a {string} file exists with the following content:")
    public void aFileExistsWithTheFollowingContent(String fileName, String yamlContent) throws IOException {
        Files.writeString(tempChartDir.resolve(fileName), yamlContent);
    }

    @When("the service configuration is parsed")
    public void theServiceConfigurationIsParsed() {
        resolvedServiceName = chartParserService.resolveServiceName(tempChartDir);
    }

    @When("the values configuration is parsed")
    public void theValuesConfigurationIsParsed() {
        usesKafkaTopics = chartParserService.usesKafkaTopics(tempChartDir);
    }

    @When("the values file is deserialized")
    public void theValuesFileIsDeserialized() {
        valuesConfig = yamlMapper.readValue(tempChartDir.resolve("values.yaml").toFile(), Values.class);
    }

    @When("the environment ConfigMaps are parsed")
    public void theEnvironmentConfigMapsAreParsed() {
        environmentConfigMaps = chartParserService.parseEnvironmentConfigMaps(tempChartDir);
    }

    @Then("there should be {int} environment ConfigMaps")
    public void thereShouldBeEnvironmentConfigMaps(int expectedCount) {
        assertEquals(expectedCount, environmentConfigMaps.size());
    }

    @When("the data entry {string} from ConfigMap {string} is selected")
    public void theDataEntryFromConfigMapIsSelected(String dataKey, String configMapName) {
        EnvironmentValues.ParsedConfigMap configMap = environmentConfigMaps.stream()
            .filter(candidate -> candidate.name().equals(configMapName))
            .findFirst()
            .orElseThrow(() -> new AssertionError("ConfigMap not found: " + configMapName));
        selectedConfig = configMap.data().get(dataKey);
        assertNotNull(selectedConfig, "ConfigMap data entry not found: " + dataKey);
    }

    @Then("the datasource URL should be {string}")
    public void theDatasourceUrlShouldBe(String expectedUrl) {
        assertEquals(expectedUrl, selectedConfig.spring().datasource().url());
    }

    @Then("the REST client {string} base URL should be {string}")
    public void theRestClientBaseUrlShouldBe(String clientName, String expectedUrl) {
        assertEquals(expectedUrl, selectedConfig.comMycompany().webRestClients().get(clientName).baseUrl());
    }

    @Then("the resource server {string} issuer URI should be {string}")
    public void theResourceServerIssuerUriShouldBe(String serverName, String expectedUri) {
        assertEquals(expectedUri,
            selectedConfig.comMycompany().securityOauth2().resourceServers().get(serverName).jwtIssuerUri());
    }

    @Then("the datasource pool name should be {string}")
    public void theDatasourcePoolNameShouldBe(String expectedName) {
        assertEquals(expectedName, selectedConfig.spring().datasource().hikari().poolName());
    }

    @Then("the OAuth provider issuer URI should be {string}")
    public void theOauthProviderIssuerUriShouldBe(String expectedUri) {
        assertEquals(expectedUri, selectedConfig.spring().securityOauth2().client().providerProductieIssuerUri());
    }

    @Then("parsing environment ConfigMaps should fail")
    public void parsingEnvironmentConfigMapsShouldFail() {
        assertThrows(JacksonException.class,
            () -> chartParserService.parseEnvironmentConfigMaps(tempChartDir));
    }

    @Then("the service name should be {string}")
    public void theServiceNameShouldBe(String expectedName) {
        assertTrue(resolvedServiceName.isPresent(), "Expected a service name to be resolved");
        assertEquals(expectedName, resolvedServiceName.get());
    }

    @Then("no service name should be resolved")
    public void noServiceNameShouldBeResolved() {
        assertTrue(resolvedServiceName.isEmpty(), "Expected no service name to be resolved");
    }

    @Then("the service should use one or more Kafka topics")
    public void theServiceShouldUseOneOrMoreKafkaTopics() {
        assertTrue(usesKafkaTopics, "Expected the service to use Kafka topics");
    }

    @Then("the service should not use Kafka topics")
    public void theServiceShouldNotUseKafkaTopics() {
        assertFalse(usesKafkaTopics, "Expected the service not to use Kafka topics");
    }

    @Then("the enabled default configurations should be:")
    public void theEnabledDefaultConfigurationsShouldBe(List<String> expectedConfigurations) {
        assertEquals(expectedConfigurations, valuesConfig.enableDefaultConfiguration());
    }

    @Then("the service port should be {int}")
    public void theServicePortShouldBe(int expectedPort) {
        assertEquals(expectedPort, valuesConfig.service().port());
    }

    @Then("the volume config map names should be:")
    public void theVolumeConfigMapNamesShouldBe(List<String> expectedNames) {
        assertEquals(expectedNames,
            valuesConfig.volumes().stream().map(volume -> volume.configMap().name()).toList());
    }

    @Then("the service and volumes should be unset")
    public void theServiceAndVolumesShouldBeUnset() {
        assertNull(valuesConfig.service());
        assertNull(valuesConfig.volumes());
    }
}
