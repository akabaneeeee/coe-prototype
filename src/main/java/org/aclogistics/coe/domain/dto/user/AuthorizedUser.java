package org.aclogistics.coe.domain.dto.user;

import java.util.List;

/**
 * @author Rosendo Coquilla
 */
public record AuthorizedUser(String id, String fullName, String email, String bearerToken, List<String> roles) {

}
