package co.com.pragma.api.auth;

import co.com.pragma.api.auth.config.AuthPath;
import co.com.pragma.api.config.BasePath;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class AuthRouterRest {

    private final BasePath basePath;
    private final AuthPath authPath;
    private final AuthHandler authHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/autenticacion/login",
                    method = {RequestMethod.POST},
                    beanClass = AuthHandler.class,
                    beanMethod = "listenAuthLogin"
            )
    })
    public RouterFunction<ServerResponse> authRouterFunction(AuthHandler authHandler) {
        return RouterFunctions
                .route()
                .path(basePath.getBasePath(), builder -> builder
                        .POST(authPath.getAuth()+"/login", this.authHandler::listenAuthLogin)
                )
                .build();
    }
}
