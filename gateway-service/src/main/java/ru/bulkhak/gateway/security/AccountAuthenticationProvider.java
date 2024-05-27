package ru.bulkhak.gateway.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@RequiredArgsConstructor
public class AccountAuthenticationProvider  implements ReactiveAuthenticationManager {
    private static final String USER_NAME_CLAIM = "preferred_username";
    private static final String ROLES_CLAIM = "roles";
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String USER_ID_CLAIM = "sub";
    private final ReactiveJwtDecoder jwtDecoder;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        BearerTokenAuthenticationToken authenticationToken = (BearerTokenAuthenticationToken) authentication;

        var jwtMono = this.jwtDecoder.decode(authenticationToken.getToken());

        return jwtMono
                .map(jwt -> getUserDetailsFromJwt(jwt))
                .map(userDetails -> (Authentication) new UsernamePasswordAuthenticationToken(
                userDetails,
                authenticationToken.getCredentials(),
                userDetails.getAuthorities()
        ));
    }

    private UserDetailsImpl getUserDetailsFromJwt(Jwt jwt) {
        String userId = jwt.getClaimAsString(USER_ID_CLAIM);
        String userName = jwt.getClaimAsString(USER_NAME_CLAIM);
        Map<String, Object> claim = Optional.ofNullable(jwt.getClaimAsMap(REALM_ACCESS_CLAIM)).orElse(new HashMap<>());
        List<String> rolesList = (List<String>) Optional.ofNullable(claim.get(ROLES_CLAIM)).orElse(new ArrayList<>());

        return new UserDetailsImpl(userId, userName, new HashSet<>(rolesList));
    }
}
