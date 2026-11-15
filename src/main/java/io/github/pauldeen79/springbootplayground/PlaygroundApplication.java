package io.github.pauldeen79.springbootplayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import tools.jackson.dataformat.yaml.YAMLMapper;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "io.github.pauldeen79.springbootplayground.repository")
@EntityScan(basePackages = "io.github.pauldeen79.springbootplayground.model.entity")
public class PlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaygroundApplication.class, args);
	}

	@Bean
	YAMLMapper yamlMapper() {
        return YAMLMapper.builder().build();
    }
}
