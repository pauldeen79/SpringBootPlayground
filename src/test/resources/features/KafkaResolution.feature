Feature: Determine Kafka Topic Usage from Values Configuration
  As a deployment analysis system
  I want to check if enableKafkaConfig is listed under enableDefaultConfiguration in values.yaml
  So that I can identify whether the service uses one or more Kafka topics

  Scenario: Service uses Kafka topics when enableKafkaConfig is enabled
    Given a "values.yaml" file exists with the following content:
      """
      ---
      enableDefaultConfiguration:
        - enableKafkaConfig
        - enableSybaseConfig
      """
    When the values configuration is parsed
    Then the service should use one or more Kafka topics

  Scenario: Service does not use Kafka topics when enableKafkaConfig is omitted
    Given a "values.yaml" file exists with the following content:
      """
      ---
      enableDefaultConfiguration:
        - enableSybaseConfig
      """
    When the values configuration is parsed
    Then the service should not use Kafka topics
