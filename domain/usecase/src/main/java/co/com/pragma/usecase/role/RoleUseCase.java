package co.com.pragma.usecase.role;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.usecase.role.exceptions.RoleAlreadyExistsByNameBusinessException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleUseCase {

    private final RoleRepository roleRepository;

    public Mono<Role> saveRole(Role role){
        return roleRepository.findByName(role.getName().toUpperCase())
                .flatMap(exists ->  Mono.<Role>error(new RoleAlreadyExistsByNameBusinessException(role.getName())))
                .switchIfEmpty(roleRepository.save(role));
    }
}
