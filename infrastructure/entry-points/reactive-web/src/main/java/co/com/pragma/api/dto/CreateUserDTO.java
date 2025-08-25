package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateUserDTO {

    @NotNull
    @NotBlank
    @Length(max = 100, min = 1)
    @Schema(description = "Fist name of the user")
    private String firstName;

    @Length(max = 100, min = 1)
    @Schema(description = "Second name of the user")
    private String secondName;

    @NotNull
    @NotBlank
    @Length(max = 100, min = 1)
    @Schema(description = "First last name of the user")
    private String firstLastName;

    @Length(max = 100, min = 1)
    @Schema(description = "Second last name of the user")
    private String secondLastName;

    @NotNull
    @NotBlank
    @Email
    @Length(max = 255, min = 1)
    @Schema(description = "Email of the user")
    private String email;

}
