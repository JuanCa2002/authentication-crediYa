package co.com.pragma.api.config;

import co.com.pragma.api.auth.JWTAuthenticationManager;
import co.com.pragma.api.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    public SecurityConfig(JWTAuthenticationManager authenticationManager) {
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JWTAuthenticationManager jwtAuthenticationManager,
                                                         GlobalExceptionHandler globalExceptionHandler) {

        AuthenticationWebFilter jwtFilter =
                new AuthenticationWebFilter(jwtAuthenticationManager);

        jwtFilter.setServerAuthenticationConverter(jwtAuthenticationManager.authenticationConverter());

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/api/v1/autenticacion/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/configuration/**",
                                "/actuator/health").permitAll()
                        .pathMatchers(HttpMethod.POST,"/api/v1/usuarios/**").hasAnyAuthority("ADMINISTRADOR", "ASESOR")
                        .pathMatchers(HttpMethod.GET,"/api/v1/usuarios/**").hasAnyAuthority("ADMINISTRADOR", "ASESOR", "CLIENTE")
                        .pathMatchers("/api/v1/roles/**").hasAnyAuthority("ADMINISTRADOR")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(globalExceptionHandler)
                        .accessDeniedHandler(globalExceptionHandler)
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}