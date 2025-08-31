package co.com.pragma.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {

    @NotNull
    @NotBlank
    @Length(min = 1, max = 100)
    @Schema(description = "User Name of the user to log in")
    private String userName;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 8)
    @Schema(description = "Password of the user to log in")
    private String password;
}
