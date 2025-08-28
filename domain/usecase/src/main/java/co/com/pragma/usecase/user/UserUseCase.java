package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.UserByEmailAlreadyExistsBusinessException;
import co.com.pragma.usecase.user.exceptions.UserByIdentificationNumberAlreadyExistsBusinessException;
import co.com.pragma.usecase.user.exceptions.UserByIdentificationNumberNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<User>error(new UserByEmailAlreadyExistsBusinessException(user.getEmail())))
                .switchIfEmpty(
                        userRepository.findByIdentificationNumber(user.getIdentificationNumber())
                                        .flatMap(existing -> Mono.<User>error(new UserByIdentificationNumberAlreadyExistsBusinessException(user.getIdentificationNumber())))
                                        .switchIfEmpty(userRepository.save(user))
                );
    }

    public Mono<User> findUserByIdentificationNumber(String identificationNumber) {
        return userRepository.findByIdentificationNumber(identificationNumber)
                .switchIfEmpty(Mono.error(new UserByIdentificationNumberNotFoundException(identificationNumber)));

    }
}
