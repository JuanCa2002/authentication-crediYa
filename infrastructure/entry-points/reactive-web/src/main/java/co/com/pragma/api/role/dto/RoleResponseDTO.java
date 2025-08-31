package co.com.pragma.api.role.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {

    @Schema(description = "Unique identifier of the role")
    private Integer id;

    @Schema(description = "Name of the role")
    private String name;

    @Schema(description = "Description of the role")
    private String description;
}
