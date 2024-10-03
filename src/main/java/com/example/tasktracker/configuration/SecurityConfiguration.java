package com.example.tasktracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder(12);}

    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService,
                                                               PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);

        return authenticationManager;

    }

    private ServerHttpSecurity buildDefaultHttpSecurity(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/user").hasAnyRole("MANAGER", "USER")
                        .pathMatchers("/api/v1/user/**").hasAnyRole("MANAGER", "USER")

                        .pathMatchers(HttpMethod.GET, "/api/v1/task/**").hasAnyRole("MANAGER", "USER" )
                        .pathMatchers(HttpMethod.GET, "/api/v1/task").hasAnyRole("MANAGER", "USER" )

                        .pathMatchers(HttpMethod.POST, "/api/v1/task").hasRole("MANAGER" )

                        .pathMatchers(HttpMethod.PUT, "/api/v1/task/*/*").hasAnyRole("MANAGER", "USER" )
                        .pathMatchers(HttpMethod.PUT, "/api/v1/task/**").hasRole("MANAGER" )

                        .pathMatchers(HttpMethod.DELETE, "/api/v1/task/**").hasRole("MANAGER")

                        .anyExchange().authenticated())
                .httpBasic(Customizer.withDefaults());
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ReactiveAuthenticationManager authenticationManager) {

        return buildDefaultHttpSecurity(http)
                .authenticationManager(authenticationManager)
                .build();
    }
}
