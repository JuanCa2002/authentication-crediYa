package co.com.pragma.usecase.user;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Mono<User> saveUser(User user) {
        return validateEmail(user)
                .then(validateIdentification(user))
                .then(validateUserName(user))
                .then(validateRole(user))
                .then(userRepository.save(user));
    }

    private Mono<Void> validateEmail(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<Void>error(new UserByEmailAlreadyExistsBusinessException(user.getEmail())))
                .then();
    }

    private Mono<Void> validateIdentification(User user) {
        return userRepository.findByIdentificationNumber(user.getIdentificationNumber())
                .flatMap(existing -> Mono.<Void>error(new UserByIdentificationNumberAlreadyExistsBusinessException(user.getIdentificationNumber())))
                .then();
    }

    private Mono<Void> validateUserName(User user) {
        return userRepository.findByUserName(user.getUserName().toLowerCase())
                .flatMap(existing -> Mono.<Void>error(new UserByUserNameAlreadyExistsBusinessException()))
                .then();
    }

    private Mono<Void> validateRole(User user) {
        return roleRepository.finById(user.getRoleId())
                .switchIfEmpty(Mono.error(new UserRoleByIdNotFoundException(user.getRoleId())))
                .then();
    }

    public Mono<User> findUserByIdentificationNumber(String identificationNumber) {
        return userRepository.findByIdentificationNumber(identificationNumber)
                .switchIfEmpty(Mono.error(new UserByIdentificationNumberNotFoundException(identificationNumber)));

    }
}
