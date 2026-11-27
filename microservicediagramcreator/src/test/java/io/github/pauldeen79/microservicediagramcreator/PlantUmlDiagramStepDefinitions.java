package io.github.pauldeen79.microservicediagramcreator;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.pauldeen79.microservicediagramcreator.model.DatabaseType;
import io.github.pauldeen79.microservicediagramcreator.model.Service;
import io.github.pauldeen79.microservicediagramcreator.service.PlantUmlDiagramService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUmlDiagramStepDefinitions {

    @Autowired
    private PlantUmlDiagramService plantUmlDiagramService;

    private String packageName;
    private final List<Service> services = new ArrayList<>();
    private String diagram;

    @Given("a diagram package named {string}")
    public void aDiagramPackageNamed(String name) {
        packageName = name;
    }

    @Given("a service named {string} with alias {string}")
    public void aServiceNamedWithAlias(String name, String alias) {
        services.add(new Service(name, alias, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }

    @Given("service {string} uses a {string} database named {string}")
    public void serviceUsesADatabaseNamed(String serviceAlias, String databaseType, String databaseName) {
        findService(serviceAlias).databases()
            .add(new Service.ServiceDatabase(databaseName, DatabaseType.valueOf(databaseType), true, true));
    }

    @Given("service {string} publishes to topic {string}")
    public void servicePublishesToTopic(String serviceAlias, String topicName) {
        findService(serviceAlias).topics()
            .add(new Service.ServiceTopic(topicName, false, true));
    }

    @Given("service {string} subscribes to topic {string}")
    public void serviceSubscribesToTopic(String serviceAlias, String topicName) {
        findService(serviceAlias).topics()
            .add(new Service.ServiceTopic(topicName, true, false));
    }

    @Given("service {string} has a REST client for {string}")
    public void serviceHasARestClientFor(String serviceAlias, String targetServiceName) {
        findService(serviceAlias).restClients()
            .add(new Service.ServiceRestClient(targetServiceName));
    }

    @When("the PlantUML diagram is generated")
    public void thePlantUmlDiagramIsGenerated() {
        diagram = plantUmlDiagramService.generateDiagram(packageName, services);
    }

    @Then("the diagram should be:")
    public void theDiagramShouldBe(String expectedDiagram) {
        // %n produces the platform line separator, docstrings use \n
        assertEquals(normalizeLineEndings(expectedDiagram), normalizeLineEndings(diagram));
    }

    private Service findService(String alias) {
        return services.stream()
            .filter(service -> service.alias().equals(alias))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Service not found: " + alias));
    }

    private static String normalizeLineEndings(String value) {
        return value.replace("\r\n", "\n");
    }
}
