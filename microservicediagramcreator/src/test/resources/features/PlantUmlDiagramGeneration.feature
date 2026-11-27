Feature: Generate PlantUML Diagram
  As a deployment analysis system
  I want to generate a PlantUML diagram from the parsed services
  So that the microservice landscape can be visualized

  Scenario: Diagram for a single service without databases or topics
    Given a diagram package named "my-landscape"
    And a service named "Order Service" with alias "order_service"
    When the PlantUML diagram is generated
    Then the diagram should be:
      """
      @startuml

      package "my-landscape" {
      [Order Service] as order_service
      }

      @enduml
      """

  Scenario: Diagram for multiple services with databases and topics
    Given a diagram package named "my-landscape"
    And a service named "Order Service" with alias "order_service"
    And a service named "Invoice Service" with alias "invoice_service"
    And service "order_service" uses a "POSTGRESQL" database named "order-db"
    And service "invoice_service" uses a "SYBASE" database named "invoice-db"
    And service "order_service" publishes to topic "order-events"
    When the PlantUML diagram is generated
    Then the diagram should be:
      """
      @startuml

      package "my-landscape" {
      [Order Service] as order_service
      [Invoice Service] as invoice_service
      }

      database "order-db" as order_db
      database "invoice-db" as invoice_db
      queue "order-events" as order_events
      order_service --> order_db : JPA
      invoice_service --> invoice_db : JDBC
      order_service --> order_events : Publish
      @enduml
      """

  Scenario: A database shared by multiple services is rendered only once
    Given a diagram package named "my-landscape"
    And a service named "Order Service" with alias "order_service"
    And a service named "Invoice Service" with alias "invoice_service"
    And service "order_service" uses a "POSTGRESQL" database named "shared-db"
    And service "invoice_service" uses a "POSTGRESQL" database named "shared-db"
    When the PlantUML diagram is generated
    Then the diagram should be:
      """
      @startuml

      package "my-landscape" {
      [Order Service] as order_service
      [Invoice Service] as invoice_service
      }

      database "shared-db" as shared_db
      order_service --> shared_db : JPA
      invoice_service --> shared_db : JPA
      @enduml
      """

  Scenario: A topic shared by multiple services is rendered only once
    Given a diagram package named "my-landscape"
    And a service named "Order Service" with alias "order_service"
    And a service named "Invoice Service" with alias "invoice_service"
    And service "order_service" publishes to topic "shared-events"
    And service "invoice_service" subscribes to topic "shared-events"
    When the PlantUML diagram is generated
    Then the diagram should be:
      """
      @startuml

      package "my-landscape" {
      [Order Service] as order_service
      [Invoice Service] as invoice_service
      }

      queue "shared-events" as shared_events
      order_service --> shared_events : Publish
      invoice_service --> shared_events : Subscribe
      @enduml
      """

  Scenario: A service calling another service through a REST client
    Given a diagram package named "my-landscape"
    And a service named "Order Service" with alias "order_service"
    And a service named "Invoice Service" with alias "invoice_service"
    And service "order_service" has a REST client for "Invoice Service"
    When the PlantUML diagram is generated
    Then the diagram should be:
      """
      @startuml

      package "my-landscape" {
      [Order Service] as order_service
      [Invoice Service] as invoice_service
      }

      order_service --> invoice_service : REST
      @enduml
      """

  Scenario: Diagram for a package without services
    Given a diagram package named "empty-landscape"
    When the PlantUML diagram is generated
    Then the diagram should be:
      """
      @startuml

      package "empty-landscape" {
      }

      @enduml
      """
