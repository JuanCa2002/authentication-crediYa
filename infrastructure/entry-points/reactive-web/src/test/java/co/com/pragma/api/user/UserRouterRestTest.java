package co.com.pragma.api.user;

import co.com.pragma.api.SecurityTestConfig;
import co.com.pragma.api.config.BasePath;
import co.com.pragma.api.user.config.UserPath;
import co.com.pragma.api.user.dto.CreateUserDTO;
import co.com.pragma.api.user.dto.UserResponseDTO;
import co.com.pragma.api.user.mapper.UserMapper;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
@EnableConfigurationProperties({UserPath.class, BasePath.class})
@TestPropertySource(properties = "routes.base-path=/api/v1")
@TestPropertySource(properties = "routes.paths.users=/usuarios")
@Import(SecurityTestConfig.class)
@WebFluxTest
class UserRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private Validator validator;

    private final String users = "/usuarios";
    private final String mainPath = "/api/v1";

    private final CreateUserDTO request = CreateUserDTO.builder()
            .identificationNumber("123")
            .firstName("Juan")
            .secondName("Camilo")
            .firstLastName("Torres")
            .secondLastName("Beltrán")
            .email("juan@email.com")
            .address("Mi casa")
            .phone("434343")
            .birthDate("2002-07-22")
            .baseSalary(1444.00)
            .build();

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

    @Autowired
    private UserPath userPath;

    @Autowired
    private BasePath basePath;

    @Test
    void shouldLoadUserPathProperties() {
        assertEquals("/usuarios", userPath.getUsers());
        assertEquals("/api/v1", basePath.getBasePath());
        assertEquals("/usuarios/{identificationNumber}", userPath.getUsers() + "/{identificationNumber}");
    }

    @Test
    void shouldPostSaveUser() {
        when(validator.supports(CreateUserDTO.class)).thenReturn(true);
        Mockito.doAnswer(invocation -> null)
                .when(validator).validate(Mockito.any(), Mockito.any());

        when(userMapper.toDomain(Mockito.any(CreateUserDTO.class))).thenReturn(domain);
        when(userUseCase.saveUser(Mockito.any(User.class))).thenReturn(Mono.just(domain));
        when(userMapper.toResponse(Mockito.any(User.class))).thenReturn(response);

        webTestClient.post()
                .uri(mainPath + users)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDTO.class)
                .value(saved -> Assertions.assertThat(saved.getEmail()).isEqualTo(response.getEmail()));
    }

    @Test
    void shouldGetUserByIdentificationNumber() {
        String identificationNumber = "123";

        when(userUseCase.findUserByIdentificationNumber(identificationNumber)).thenReturn(Mono.just(domain));
        when(userMapper.toResponse(Mockito.any(User.class))).thenReturn(response);

        webTestClient.get()
                .uri(mainPath + users + "/" + identificationNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .value(response -> Assertions.assertThat(response.getEmail()).isEqualTo(this.response.getEmail()));
    }
}
