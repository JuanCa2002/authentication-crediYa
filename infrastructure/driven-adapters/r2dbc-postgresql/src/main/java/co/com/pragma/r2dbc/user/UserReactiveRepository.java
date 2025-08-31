package co.com.pragma.r2dbc.user;

import co.com.pragma.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<UserEntity> findByEmail(String email);
    Mono<UserEntity> findByIdentificationNumber(String identificationNumber);
    Mono<UserEntity> findByUserNameIgnoreCase(String userName);
    Mono<UserEntity> findByUserName(String userName);

}
