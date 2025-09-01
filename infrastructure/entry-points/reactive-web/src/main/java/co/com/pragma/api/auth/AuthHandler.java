package co.com.pragma.api.auth;

import co.com.pragma.api.auth.dto.LoginDTO;
import co.com.pragma.api.auth.dto.LoginResponseDTO;
import co.com.pragma.api.auth.exceptions.InvalidLoginException;
import co.com.pragma.api.dto.errors.ErrorResponse;
import co.com.pragma.api.exception.FieldValidationException;
import co.com.pragma.api.helper.JWTUtil;
import co.com.pragma.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Authentication", description = "Authentication management APIs")
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
                responseCode = "404",
                description = "Not Found Error",
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
public class AuthHandler {

    private final UserUseCase userUseCase;
    private final Validator validator;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            operationId = "login",
            summary = "Login with credentials",
            description = "Login a created user",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginDTO.class)
                    )
            ),responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login Successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class)
                    )
            )
    }
    )
    public Mono<ServerResponse> listenAuthLogin(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginDTO.class)
                .doOnNext(dto -> log.info("[AuthHandler] Receiving request to login"))
                .flatMap(dto -> {
                    Errors errors = new BeanPropertyBindingResult(dto, LoginDTO.class.getName());
                    validator.validate(dto, errors);

                    if (errors.hasErrors()) {
                        List<String> messageErrors = errors.getFieldErrors().stream()
                                .map(fieldError ->  fieldError.getField() + " " + fieldError.getDefaultMessage())
                                .toList();
                        log.warn("[AuthHandler] Validation Errors While Login Process: {}", messageErrors);
                        return Mono.error(new FieldValidationException(messageErrors));
                    }
                    return Mono.just(dto);
                })
                .doOnNext(currentUser ->  log.info("[AuthHandler] searching for user with userName: {}", currentUser.getUserName()))
                .flatMap(dto ->
                        userUseCase.findByUserName(dto.getUserName())
                                .flatMap(user -> {
                                    log.info("[AuthHandler] found user");
                                    if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                                        LoginResponseDTO response = LoginResponseDTO.builder()
                                                .token(jwtUtil.generateToken(user.getUserName(), user.getRole().getName()))
                                                .build();

                                        log.info("[AuthHandler] Login success");
                                        return ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(response);
                                    } else {
                                        log.error("[AuthHandler] Invalid username or password");
                                        return Mono.error(new InvalidLoginException());
                                    }
                                })
                                .switchIfEmpty(Mono.error(new InvalidLoginException()))
                )
                .doOnError(e -> log.error("[AuthHandler] Error processing login creation request ", e));

    }
}
