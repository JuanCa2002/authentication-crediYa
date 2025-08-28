package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.BusinessException;
import co.com.pragma.usecase.user.exceptions.UserByIdentificationNumberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        log.info("[UserUseCase] Saving user with email={} and identification number={}",
                user.getEmail(), user.getIdentificationNumber());

        return userRepository.findByEmail(user.getEmail())
                .flatMap(existing -> {
                    log.warn("[UserUseCase] User with email={} already exists", user.getEmail());
                    return Mono.<User>error(new BusinessException("Ya existe un usuario con el email " + user.getEmail()));
                })
                .switchIfEmpty(
                        userRepository.findByIdentificationNumber(user.getIdentificationNumber())
                                        .flatMap(existing -> {
                                            log.warn("[UserUseCase] User with identification number={} already exists", user.getIdentificationNumber());
                                            return Mono.<User>error(new BusinessException("Ya existe un usuario con el numero de identificación " + user.getIdentificationNumber()));
                                        })
                                        .switchIfEmpty(userRepository.save(user))
                                        .doOnSuccess(saved -> log.info("[UserUseCase] User saved successfully with id={}", saved.getId()))
                );
    }

    public Mono<User> findUserByIdentificationNumber(String identificationNumber) {
        log.info("[UserUseCase] Searching user with identification number={}", identificationNumber);

        return userRepository.findByIdentificationNumber(identificationNumber)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("[UserUseCase] User not found with identification number={}", identificationNumber);
                    return Mono.error(new UserByIdentificationNumberNotFoundException(identificationNumber));
                }));

    }
}
