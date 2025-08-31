package co.com.pragma.r2dbc.role;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.r2dbc.entity.RoleEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Integer,
        RoleReactiveRepository
> implements RoleRepository {

    private final TransactionalOperator txOperator;

    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper,
                                         TransactionalOperator txOperator) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
        this.txOperator = txOperator;
    }

    @Override
    public Mono<Role> save(Role role) {
        return super.save(role)
                .as(txOperator::transactional);
    }


    @Override
    public Mono<Role> findByName(String name) {
        return repository.findByName(name)
                .map(entity -> mapper.map(entity, Role.class))
                .as(txOperator::transactional);
    }

    @Override
    public Mono<Role> finById(Integer id) {
        return super.findById(id)
                .as(txOperator::transactional);
    }
}
