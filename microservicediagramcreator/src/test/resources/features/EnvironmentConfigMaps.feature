Feature: Read environment ConfigMap data
  As a deployment analysis system
  I want to parse the ConfigMaps in the production or acceptance values file
  So that their configuration is available for analysis

  Scenario: Parse all ConfigMaps and their embedded YAML data
    Given a "values-prd.yaml" file exists with the following content:
      """
      namespace: production
      configMaps:
        - name: application-config
          data:
            application.yaml: |
              com.mycompany:
                security.oauth2:
                  resourceservers:
                    one:
                      jwt.issuer-uri: https://auth.example.com
                web.rest-clients:
                  first:
                    base-url: http://first-service:8080
                    auth.oauth-context: production
              spring:
                datasource:
                  url: jdbc:postgresql://db:5432/application
                  hikari:
                    pool-name: application-pool
                security.oauth2:
                  client:
                    provider.productie.issuer-uri: https://auth.example.com/production
            other.yaml: |
              spring:
                datasource:
                  url: jdbc:postgresql://db:5432/other
        - name: second-config
          data:
            service.yaml: |
              spring:
                datasource:
                  url: jdbc:postgresql://db:5432/second
      """
    When the environment ConfigMaps are parsed
    Then there should be 2 environment ConfigMaps
    When the data entry "application.yaml" from ConfigMap "application-config" is selected
    Then the datasource URL should be "jdbc:postgresql://db:5432/application"
    And the datasource pool name should be "application-pool"
    And the OAuth provider issuer URI should be "https://auth.example.com/production"
    And the REST client "first" base URL should be "http://first-service:8080"
    And the resource server "one" issuer URI should be "https://auth.example.com"
    When the data entry "other.yaml" from ConfigMap "application-config" is selected
    Then the datasource URL should be "jdbc:postgresql://db:5432/other"
    When the data entry "service.yaml" from ConfigMap "second-config" is selected
    Then the datasource URL should be "jdbc:postgresql://db:5432/second"

  Scenario: Production values take precedence over acceptance values
    Given a "values-prd.yaml" file exists with the following content:
      """
      configMaps:
        - name: production-config
          data:
            application.yaml: |
              spring:
                datasource:
                  url: jdbc:postgresql://db:5432/production
      """
    And a "values-acc.yaml" file exists with the following content:
      """
      configMaps:
        - name: acceptance-config
          data:
            application.yaml: |
              spring:
                datasource:
                  url: jdbc:postgresql://db:5432/acceptance
      """
    When the environment ConfigMaps are parsed
    Then there should be 1 environment ConfigMaps
    When the data entry "application.yaml" from ConfigMap "production-config" is selected
    Then the datasource URL should be "jdbc:postgresql://db:5432/production"

  Scenario: Use acceptance values when production values are absent
    Given a "values-acc.yaml" file exists with the following content:
      """
      configMaps:
        - name: acceptance-config
          data:
            application.yaml: |
              spring:
                datasource:
                  url: jdbc:postgresql://db:5432/acceptance
      """
    When the environment ConfigMaps are parsed
    Then there should be 1 environment ConfigMaps
    When the data entry "application.yaml" from ConfigMap "acceptance-config" is selected
    Then the datasource URL should be "jdbc:postgresql://db:5432/acceptance"

  Scenario: Neither environment values file exists
    Given an empty chart directory with no "values-prd.yaml" file
    When the environment ConfigMaps are parsed
    Then there should be 0 environment ConfigMaps

  Scenario: Environment values contain no ConfigMaps
    Given a "values-prd.yaml" file exists with the following content:
      """
      namespace: production
      """
    When the environment ConfigMaps are parsed
    Then there should be 0 environment ConfigMaps

  Scenario: Embedded ConfigMap YAML is invalid
    Given a "values-prd.yaml" file exists with the following content:
      """
      configMaps:
        - name: broken-config
          data:
            application.yaml: |
              spring: [
      """
    And a "values-acc.yaml" file exists with the following content:
      """
      namespace: acceptance
      """
    Then parsing environment ConfigMaps should fail
