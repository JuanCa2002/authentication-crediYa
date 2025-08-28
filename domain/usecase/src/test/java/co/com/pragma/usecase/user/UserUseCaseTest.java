package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
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
}
