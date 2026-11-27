package io.github.pauldeen79.springbootplayground.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Value
@SuperBuilder
@Jacksonized
public class UserDTO {
    Long id;
    
    @NotBlank
    String name;
    
    @NotBlank
    String emailAddress;
    
    @JsonCreator
    public UserDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("emailAddress") String emailAddress) {
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
    }
}
