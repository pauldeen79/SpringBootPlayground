package io.github.pauldeen79.microservicediagramcreator.model.chart;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EnvironmentValues(
        String namespace,
        String dns,
        Integer replicaCount,
        List<Secret> secrets,
        List<ConfigMap> configMaps) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Secret(
            String name,
            Map<String, String> data) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ConfigMap(
            String name,
            Map<String, String> data) {
    }

    public record ParsedConfigMap(
            String name,
            Map<String, MyCompanyConfig> data) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MyCompanyConfig(
            @JsonProperty("com.mycompany") ComMycompany comMycompany,
            Spring spring) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ComMycompany(
            @JsonProperty("security.oauth2") SecurityOauth2 securityOauth2,
            @JsonProperty("web.rest-clients") Map<String, RestClient> webRestClients) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SecurityOauth2(
            @JsonProperty("resourceservers") Map<String, ResourceServer> resourceServers) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResourceServer(
            @JsonProperty("jwt.issuer-uri") String jwtIssuerUri) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RestClient(
            @JsonProperty("base-url") String baseUrl,
            @JsonProperty("auth.oauth-context") String authOauthContext) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Spring(
            Datasource datasource,
            @JsonProperty("security.oauth2") SpringSecurityOauth2 securityOauth2) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Datasource(
            String url,
            @JsonProperty("driver-class-name") String driverClassName,
            Hikari hikari) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Hikari(
            @JsonProperty("pool-name") String poolName,
            @JsonProperty("minimum-idle") Integer minimumIdle,
            @JsonProperty("maximum-pool-size") Integer maximumPoolSize) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SpringSecurityOauth2(
            Client client) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Client(
            @JsonProperty("provider.productie.issuer-uri") String providerProductieIssuerUri,
            @JsonProperty("registration.productie.authorization-grant-type") String registrationProductieAuthorizationGrantType) {
    }
}
