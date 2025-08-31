package co.com.pragma.model.role.gateways;

import co.com.pragma.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {

    Mono<Role> save(Role role);
    Mono<Role> findByName(String name);
    Mono<Role> finById(Integer id);
}
