Feature: Resolve Service Name from Chart Configuration
  As a deployment analysis system
  I want to determine the service name from the chart.yaml file
  So that the service is deployed with the correct identifier

  Scenario: Service name is taken from the name attribute in chart.yaml
    Given a "chart.yaml" file exists with the following content:
      """
      apiVersion: v2
      name: my-custom-service
      version: 1.0.0
      type: application
      """
    When the service configuration is parsed
    Then the service name should be "my-custom-service"

  Scenario: Directory does not contain a chart.yaml file
    Given an empty chart directory with no "chart.yaml" file
    When the service configuration is parsed
    Then no service name should be resolved

  Scenario: chart.yaml exists but does not contain a name attribute
    Given a chart directory containing a "chart.yaml" file with the following content:
      """
      apiVersion: v2
      version: 1.0.0
      type: application
      """
    When the service configuration is parsed
    Then no service name should be resolved

  Scenario: chart.yaml exists but the type attribute is not "application"
    Given a chart directory containing a "chart.yaml" file with the following content:
      """
      apiVersion: v2
      name: my-custom-service
      version: 1.0.0
      type: library
      """
    When the service configuration is parsed
    Then no service name should be resolved