package co.com.pragma.api;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.dto.UserResponseDTO;
import co.com.pragma.api.dto.errors.ErrorResponse;
import co.com.pragma.api.exception.FieldValidationException;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Users", description = "User management APIs")
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
                responseCode = "500",
                description = "Unexpected server error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserUseCase userUseCase;
    private final UserMapper mapper;
    private final Validator validator;

    @Operation(
            operationId = "saveUser",
            summary = "Save a user",
            description = "Creates a new user",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateUserDTO.class)
                    )
            ),responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User created",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = UserResponseDTO.class)
                        )
                )
        }
    )
    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .flatMap(dto -> {
                    Errors errors = new BeanPropertyBindingResult(dto, CreateUserDTO.class.getName());
                    validator.validate(dto, errors);

                    if (errors.hasErrors()) {
                        List<String> messageErrors = errors.getFieldErrors().stream()
                                .map(fieldError ->  fieldError.getField() + " " + fieldError.getDefaultMessage())
                                .toList();
                        return Mono.error(new FieldValidationException(messageErrors));
                    }
                    return Mono.just(dto);
                })
                .map(mapper::toDomain)
                .flatMap(userUseCase::saveUser)
                .map(mapper::toResponse)
                .flatMap(savedUserResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUserResponse));
    }

}
