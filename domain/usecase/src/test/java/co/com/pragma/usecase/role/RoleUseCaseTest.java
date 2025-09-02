package co.com.pragma.usecase.role;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.usecase.role.constants.RoleMessageConstants;
import co.com.pragma.usecase.role.exceptions.RoleAlreadyExistsByNameBusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.MessageFormat;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleUseCaseTest {

    @InjectMocks
    RoleUseCase roleUseCase;

    @Mock
    RoleRepository repository;

    private final Role role = Role.builder()
            .id(1)
            .name("ADMINISTRADOR")
            .description("Rol de administrador")
            .build();

    @Test
    void shouldSaveRole() {
        when(repository.findByName(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.save(role))
                .thenReturn(Mono.just(role));

        Mono<Role> result = roleUseCase.saveRole(role);

        StepVerifier.create(result)
                .expectNextMatches(role -> role.getId().equals(1))
                .verifyComplete();
    }

    @Test
    void shouldSaveRole_RoleAlreadyExistsByNameBusinessException() {
        when(repository.findByName(Mockito.anyString()))
                .thenReturn(Mono.just(role));

        when(repository.save(role))
                .thenReturn(Mono.just(role));

        Mono<Role> result = roleUseCase.saveRole(role);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RoleAlreadyExistsByNameBusinessException &&
                                throwable.getMessage().equals(
                                        MessageFormat.format(RoleMessageConstants.ROLE_ALREADY_EXISTS_BY_NAME, role.getName())
                                )
                )
                .verify();
    }
}
