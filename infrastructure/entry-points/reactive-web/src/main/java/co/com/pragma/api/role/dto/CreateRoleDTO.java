package co.com.pragma.api.role.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoleDTO {

    @NotNull
    @NotBlank
    @Length(min = 1, max = 100)
    @Pattern(regexp = "^[A-Z_]+$", message = "solo permite letras mayúsculas y guiones bajos")
    @Schema(description = "Name of the role")
    private String name;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 255)
    @Schema(description = "Description of the role")
    private String description;
}
