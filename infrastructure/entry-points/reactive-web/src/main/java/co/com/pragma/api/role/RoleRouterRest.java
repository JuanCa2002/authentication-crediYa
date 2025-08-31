package co.com.pragma.api.role;

import co.com.pragma.api.config.BasePath;
import co.com.pragma.api.role.config.RolePath;
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
public class RoleRouterRest {

    private final BasePath basePath;
    private final RolePath rolePath;
    private final RoleHandler roleHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/roles",
                    method = {RequestMethod.POST},
                    beanClass = RoleHandler.class,
                    beanMethod = "listenSaveRole"
            )
    })
    public RouterFunction<ServerResponse> roleRouterFunction(RoleHandler roleHandler) {
        return RouterFunctions
                .route()
                .path(basePath.getBasePath(), builder -> builder
                        .POST(rolePath.getRoles(), this.roleHandler::listenSaveRole)
                )
                .build();
    }
}
