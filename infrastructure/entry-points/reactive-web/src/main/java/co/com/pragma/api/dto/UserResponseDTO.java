package co.com.pragma.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private String id;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private String email;
}
