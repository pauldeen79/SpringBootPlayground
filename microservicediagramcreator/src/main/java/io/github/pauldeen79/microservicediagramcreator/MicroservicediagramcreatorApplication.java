package io.github.pauldeen79.microservicediagramcreator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import tools.jackson.dataformat.yaml.YAMLMapper;

@SpringBootApplication
public class MicroservicediagramcreatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicediagramcreatorApplication.class, args);
	}

	@Bean
	YAMLMapper yamlMapper() {
        return YAMLMapper.builder().build();
    }
}
