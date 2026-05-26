package org.pauldeen.playground.model.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class UserDTO {
    Long id;
    String name;
    String emailAddress;
}
