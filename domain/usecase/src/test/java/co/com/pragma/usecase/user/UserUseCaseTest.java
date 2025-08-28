package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.BusinessException;
import co.com.pragma.usecase.user.exceptions.UserByIdentificationNumberNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {

    @InjectMocks
    UserUseCase userUseCase;

    @Mock
    UserRepository repository;

    private final User user = User.builder()
            .id("1")
            .identificationNumber("123")
            .firstName("Juan")
            .secondName("Camilo")
            .firstLastName("Torres")
            .secondLastName("Beltrán")
            .email("juan@email.com")
            .address("Mi casa")
            .phone("434343")
            .birthDate(LocalDate.of(2003,5, 22))
            .baseSalary(1444.00)
            .build();

    @Test
    void shouldSaveUser() {
        when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.saveUser(user);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals("1") && user.getEmail().equals("juan@email.com"))
                .verifyComplete();
    }

    @Test
    void shouldSaveUser_FindByEmailBusinessException() {
        when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.just(user));

        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.saveUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("Ya existe un usuario con el email juan@email.com")
                )
                .verify();
    }

    @Test
    void shouldSaveUser_FindByIdentificationNumberBusinessException() {
        when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.just(user));

        when(repository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.saveUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("Ya existe un usuario con el numero de identificación 123")
                )
                .verify();
    }

    @Test
    void shouldFindUserByIdentificationNumber() {
        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.findUserByIdentificationNumber("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals("1") && user.getEmail().equals("juan@email.com"))
                .verifyComplete();
    }

    @Test
    void shouldFindUserByIdentificationNumber_UserByIdentificationNumberNotFoundException() {
        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.empty());

        Mono<User> result = userUseCase.findUserByIdentificationNumber("123");

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof UserByIdentificationNumberNotFoundException &&
                                throwable.getMessage().equals("No se encontro un usuario con el numero de identificación 123")
                )
                .verify();
    }
}
