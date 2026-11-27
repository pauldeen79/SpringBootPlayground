Feature: Read service and volume settings from values configuration
  As a deployment analysis system
  I want to read service and volume settings from values.yaml
  So that they are available alongside the existing configuration

  Scenario: Service and volume settings are read alongside default configuration
    Given a "values.yaml" file exists with the following content:
      """
      enableDefaultConfiguration:
        - enableKafkaConfig
      service:
        port: 8080
        type: ClusterIP
      volumes:
        - configMap:
            name: my-application-config
            optional: true
          mountPath: /config
        - configMap:
            name: another-config
      """
    When the values file is deserialized
    Then the enabled default configurations should be:
      | enableKafkaConfig |
    And the service port should be 8080
    And the volume config map names should be:
      | my-application-config |
      | another-config        |

  Scenario: Service and volume settings are absent
    Given a "values.yaml" file exists with the following content:
      """
      enableDefaultConfiguration:
        - enableKafkaConfig
      """
    When the values file is deserialized
    Then the enabled default configurations should be:
      | enableKafkaConfig |
    And the service and volumes should be unset
