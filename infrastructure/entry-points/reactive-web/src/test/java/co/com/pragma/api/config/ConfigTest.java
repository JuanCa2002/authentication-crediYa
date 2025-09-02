package co.com.pragma.api.config;

import co.com.pragma.api.SecurityTestConfig;
import co.com.pragma.api.user.UserHandler;
import co.com.pragma.api.user.UserRouterRest;
import co.com.pragma.api.user.config.UserPath;
import co.com.pragma.api.user.dto.UserResponseDTO;
import co.com.pragma.api.user.mapper.UserMapper;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class, UserPath.class, BasePath.class})
@TestPropertySource(properties = "routes.base-path=/api/v1")
@TestPropertySource(properties = "routes.paths.users=/usuarios")
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class, SecurityTestConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private Validator validator;

    private final String identificationNumber = "123";

    private final User domain = User.builder()
            .identificationNumber("123")
            .firstName("Juan")
            .secondName("Camilo")
            .firstLastName("Torres")
            .secondLastName("Beltrán")
            .email("juan@email.com")
            .address("Mi casa")
            .phone("434343")
            .birthDate(LocalDate.of(2002, 5, 22))
            .baseSalary(1444.00)
            .build();

    private final UserResponseDTO response = UserResponseDTO.builder()
            .identificationNumber("123")
            .firstName("Juan")
            .secondName("Camilo")
            .firstLastName("Torres")
            .secondLastName("Beltrán")
            .email("juan@email.com")
            .address("Mi casa")
            .phone("434343")
            .baseSalary(1444.00)
            .build();

    @BeforeEach
    void setUp() {
        when(userMapper.toResponse(Mockito.any(User.class))).thenReturn(response);
        when(userUseCase.findUserByIdentificationNumber(identificationNumber)).thenReturn(Mono.just(domain));
    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.get()
                .uri("/api/v1/usuarios/"+ identificationNumber)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}