package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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

    @NotBlank
    @NotNull
    @Length(max = 100, min = 1)
    @Schema(description = "Second name of the user")
    private String secondName;

    @NotNull
    @NotBlank
    @Length(max = 100, min = 1)
    @Schema(description = "First last name of the user")
    private String firstLastName;

    @NotBlank
    @NotNull
    @Length(max = 100, min = 1)
    @Schema(description = "Second last name of the user")
    private String secondLastName;

    @NotNull
    @NotBlank
    @Length(max = 50, min = 1)
    @Schema(description = "Identification Number of the user")
    private String identificationNumber;

    @NotNull
    @NotBlank
    @Email
    @Length(max = 255, min = 1)
    @Schema(description = "Email of the user")
    private String email;

    @Length(min = 1, max = 255)
    @Schema(description = "Address of the user")
    private String address;

    @Length(min = 1, max = 50)
    @Schema(description = "Phone number of the user")
    private String phone;

    @Length(min = 1, max = 10)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "debe estar en yyyy-MM-dd formato")
    @Schema(description = "Birth Date of the user")
    private String birthDate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "debe ser mayor o igual a 0")
    @DecimalMax(value = "15000000.0", inclusive = true, message = "no puede superar 15.000.000")
    @Digits(integer = 8, fraction = 2, message = "debe tener hasta 8 dígitos enteros y 2 decimales")
    @Schema(description = "Base salary of the user")
    private Double baseSalary;

}
