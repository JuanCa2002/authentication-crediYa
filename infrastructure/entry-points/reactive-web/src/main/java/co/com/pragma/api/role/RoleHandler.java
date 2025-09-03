package co.com.pragma.api.role;

import co.com.pragma.api.dto.errors.ErrorResponse;
import co.com.pragma.api.exception.FieldValidationException;
import co.com.pragma.api.role.dto.CreateRoleDTO;
import co.com.pragma.api.role.dto.RoleResponseDTO;
import co.com.pragma.api.role.mapper.RoleMapper;
import co.com.pragma.usecase.role.RoleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Roles", description = "Role management APIs")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "Validation data error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected server error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleHandler {

    private final RoleUseCase roleUseCase;
    private final RoleMapper mapper;
    private final Validator validator;

    @Operation(
            operationId = "saveRole",
            summary = "Save a role",
            description = "Creates a new role",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateRoleDTO.class)
                    )
            ),responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Role created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RoleResponseDTO.class)
                    )
            )
    }
    )
    public Mono<ServerResponse> listenSaveRole(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateRoleDTO.class)
                .doOnNext(dto -> log.info("[RoleHandler] Receiving request to create a new role: {}", dto))
                .flatMap(dto -> {
                    Errors errors = new BeanPropertyBindingResult(dto, CreateRoleDTO.class.getName());
                    validator.validate(dto, errors);

                    if (errors.hasErrors()) {
                        List<String> messageErrors = errors.getFieldErrors().stream()
                                .map(fieldError ->  fieldError.getField() + " " + fieldError.getDefaultMessage())
                                .toList();
                        log.warn("[RoleHandler] Validation Errors While Creating Role: {}", messageErrors);
                        return Mono.error(new FieldValidationException(messageErrors));
                    }
                    return Mono.just(dto);
                })
                .map(mapper::toDomain)
                .flatMap(roleUseCase::saveRole)
                .doOnNext(role -> log.info("[RoleHandler] Role saved successfully: {}", role.getName()))
                .map(mapper::toResponse)
                .flatMap(savedRoleResponse -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedRoleResponse))
                .doOnError(e -> log.error("[RoleHandler] Error processing role creation request ", e));
    }
}
