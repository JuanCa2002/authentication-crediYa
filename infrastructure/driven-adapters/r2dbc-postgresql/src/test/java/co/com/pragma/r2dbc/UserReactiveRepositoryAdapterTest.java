package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    TransactionalOperator txOperator;

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

    private final UserEntity userEntity = UserEntity.builder()
            .id(1L)
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
        when(mapper.map(user, UserEntity.class))
                .thenReturn(userEntity);

        when(mapper.map(userEntity, User.class))
                .thenReturn(user);

        when(repository.save(Mockito.any(UserEntity.class)))
                .thenReturn(Mono.just(userEntity));

        when(txOperator.transactional(ArgumentMatchers.<Mono<User>>any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void shouldFindByEmail() {

        when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.just(userEntity));

        when(mapper.map(Mockito.any(UserEntity.class), Mockito.eq(User.class)))
                .thenReturn(user);

        when(txOperator.transactional(ArgumentMatchers.<Mono<User>>any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mono<User> result = repositoryAdapter.findByEmail("juan@email.com");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals("1")
                        && user.getEmail().equals("juan@email.com"))
                .verifyComplete();
    }

    @Test
    void shouldFindByIdentificationNumber() {

        when(repository.findByIdentificationNumber(Mockito.anyString()))
                .thenReturn(Mono.just(userEntity));

        when(mapper.map(Mockito.any(UserEntity.class), Mockito.eq(User.class)))
                .thenReturn(user);

        when(txOperator.transactional(ArgumentMatchers.<Mono<User>>any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mono<User> result = repositoryAdapter.findByIdentificationNumber("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals("1")
                        && user.getIdentificationNumber().equals("123"))
                .verifyComplete();
    }


}
