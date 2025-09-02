package co.com.pragma.usecase.user;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.exception.BusinessException;
import co.com.pragma.usecase.user.constants.UserMessageConstants;
import co.com.pragma.usecase.user.exceptions.UserByIdentificationNumberNotFoundException;
import co.com.pragma.usecase.user.exceptions.UserByUserNameAlreadyExistsBusinessException;
import co.com.pragma.usecase.user.exceptions.UserRoleByIdNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.MessageFormat;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {

    @InjectMocks
    UserUseCase userUseCase;

    @Mock
    RoleRepository roleRepository;

    @Mock
    UserRepository repository;

    private final User user = User.builder()
            .id("1")
            .userName("juan")
            .identificationNumber("123")
            .firstName("Juan")
            .secondName("Camilo")
            .firstLastName("Torres")
            .secondLastName("Beltrán")
            .email("juan@email.com")
            .address("Mi casa")
            .phone("434343")
            .roleId(1)
            .birthDate(LocalDate.of(2003,5, 22))
            .baseSalary(1444.00)
            .build();

    private final Role role = Role.builder()
            .id(1)
            .name("ADMINISTRADOR")
            .description("Rol de administrador")
            .build();

    @Test
    void shouldSaveUser() {
        when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.findByUserName(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(roleRepository.finById(Mockito.anyInt()))
                .thenReturn(Mono.just(role));

        when(repository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.saveUser(user);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals("1") && user.getEmail().equals("juan@email.com"))
                .verifyComplete();
    }

    @Test
    void shouldSaveUser_FindByEmailBusinessException() {
        when(repository.findByUserName(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.just(user));

        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(roleRepository.finById(Mockito.anyInt()))
                .thenReturn(Mono.just(role));

        when(repository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.saveUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals(
                                        MessageFormat.format(UserMessageConstants.USER_BY_EMAIL_ALREADY_EXISTS, user.getEmail())
                                )
                )
                .verify();
    }

    @Test
    void shouldSaveUser_UserByUserNameAlreadyExistsBusinessException() {
        when(repository.findByUserName(Mockito.anyString()))
                .thenReturn(Mono.just(user));

        when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(roleRepository.finById(Mockito.anyInt()))
                .thenReturn(Mono.just(role));

        when(repository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.saveUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof UserByUserNameAlreadyExistsBusinessException &&
                                throwable.getMessage().equals(
                                        UserMessageConstants.USER_BY_USER_NAME_ALREADY_EXISTS
                                )
                )
                .verify();
    }

    @Test
    void shouldSaveUser_UserRoleByIdNotFoundException() {
        when(repository.findByUserName(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(roleRepository.finById(Mockito.anyInt()))
                .thenReturn(Mono.empty());

        when(repository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.saveUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof UserRoleByIdNotFoundException &&
                                throwable.getMessage().equals(
                                       MessageFormat.format(UserMessageConstants.ROLE_BY_ID_NOT_FOUND, role.getId())
                                )
                )
                .verify();
    }


    @Test
    void shouldSaveUser_FindByIdentificationNumberBusinessException() {
        when(repository.findByUserName(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.empty());

        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.just(user));

        when(roleRepository.finById(Mockito.anyInt()))
                .thenReturn(Mono.just(role));

        when(repository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.saveUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals(
                                        MessageFormat.format(UserMessageConstants.USER_BY_IDN_ALREADY_EXISTS, user.getIdentificationNumber())
                                )
                )
                .verify();
    }

    @Test
    void shouldFindByUserName() {
        when(repository.findByUserName(Mockito.anyString()))
                .thenReturn(Mono.just(user));

        when(roleRepository.finById(Mockito.anyInt()))
                .thenReturn(Mono.just(role));

        Mono<User> result = userUseCase.findByUserName(user.getUserName());

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals("1") && user.getUserName().equals("juan"))
                .verifyComplete();
    }

    @Test
    void shouldFindByUserName_UserRoleByIdNotFoundException() {
        when(repository.findByUserName(Mockito.anyString()))
                .thenReturn(Mono.just(user));

        when(roleRepository.finById(Mockito.anyInt()))
                .thenReturn(Mono.empty());

        Mono<User> result = userUseCase.findByUserName(user.getUserName());

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof UserRoleByIdNotFoundException &&
                                throwable.getMessage().equals(
                                        MessageFormat.format(UserMessageConstants.ROLE_BY_ID_NOT_FOUND, role.getId())
                                )
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
                                throwable.getMessage().equals(
                                        MessageFormat.format(UserMessageConstants.USER_BY_IDN_NOT_FOUND, "123")
                                )
                )
                .verify();
    }
}
