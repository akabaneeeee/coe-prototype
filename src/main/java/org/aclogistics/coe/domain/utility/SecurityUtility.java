package org.aclogistics.coe.domain.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.AuthorizedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@UtilityClass
public class SecurityUtility {

    public Jwt getAuthorizedUserJwt() {
        JwtAuthenticationToken authentication =
            (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        return authentication.getToken();
    }

    public AuthorizedUser getAuthorizedUser() {
        Jwt jwt = getAuthorizedUserJwt();
        return new AuthorizedUser(
            jwt.getSubject(),
            jwt.getClaimAsString("name"),
            jwt.getClaimAsString("email"),
            jwt.getTokenValue(),
            jwt.getClaimAsStringList("roles")
        );
    }

    public String getUserFullName() {
        return getAuthorizedUser().fullName();
    }

    public String getUserEmail() {
        return getAuthorizedUser().email();
    }

    public String getUserId() {
        return getAuthorizedUser().id();
    }
}
