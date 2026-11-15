package io.github.pauldeen79.springbootplayground;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@CucumberContextConfiguration
@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class CucumberSpringConfiguration {
}
