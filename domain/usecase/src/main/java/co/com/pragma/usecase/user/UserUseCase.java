package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.BusinessException;
import co.com.pragma.usecase.user.exceptions.UserByIdentificationNumberNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<User>error(new BusinessException("Ya existe un usuario con el email " + user.getEmail())))
                .switchIfEmpty(
                        userRepository.findByIdentificationNumber(user.getIdentificationNumber())
                                        .flatMap(existing -> Mono.<User>error(new BusinessException("Ya existe un usuario con el numero de identificación " + user.getIdentificationNumber())))
                                        .switchIfEmpty(userRepository.save(user))
                );
    }

    public Mono<User> findUserByIdentificationNumber(String identificationNumber) {
        return userRepository.findByIdentificationNumber(identificationNumber)
                .switchIfEmpty(Mono.error(new UserByIdentificationNumberNotFoundException(identificationNumber)));

    }
}
