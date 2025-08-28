package co.com.pragma.api;

import co.com.pragma.api.config.BasePath;
import co.com.pragma.api.config.UserPath;
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
public class RouterRest {

    private final BasePath basePath;
    private final UserPath userPath;
    private final UserHandler userHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/usuarios",
                    method = {RequestMethod.POST},
                    beanClass = UserHandler.class,
                    beanMethod = "listenSaveUser"
            ),
            @RouterOperation(
                    path = "/usuarios/{identificationNumber}",
                    method = {RequestMethod.GET},
                    beanClass = UserHandler.class,
                    beanMethod = "listenGetUserByIdentificationNumber"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return RouterFunctions
                .route()
                .path(basePath.getBasePath(), builder -> builder
                        .POST(userPath.getUsers(), this.userHandler::listenSaveUser)
                        .GET(userPath.getUsers()+"/{identificationNumber}", this.userHandler::listenGetUserByIdentificationNumber )
                )
                .build();
    }
}
