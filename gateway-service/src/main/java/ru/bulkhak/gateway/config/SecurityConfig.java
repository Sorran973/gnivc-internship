package ru.bulkhak.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import ru.bulkhak.gateway.security.AccountAuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AccountAuthenticationProvider provider;

    @Bean
    protected SecurityWebFilterChain configure(ServerHttpSecurity http) {
            http.httpBasic(Customizer.withDefaults())
                    .cors(Customizer.withDefaults())
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .authorizeExchange(requests -> {
                        requests.pathMatchers("/openid-connect/**").permitAll();
                        requests.pathMatchers("/demo").authenticated();
                        requests.pathMatchers("/user").hasAnyAuthority("ROLE_USER");
                        requests.anyExchange().authenticated();
                    });

            http.oauth2ResourceServer(resourceServerConfigurer ->
                            resourceServerConfigurer.authenticationManagerResolver(context -> Mono.just(provider)));

        return http.build();
    }
}
