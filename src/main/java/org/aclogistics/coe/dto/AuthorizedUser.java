package org.aclogistics.coe.dto;

import java.util.List;

/**
 * @author Rosendo Coquilla
 */
public record AuthorizedUser(String id, String fullName, String email, String bearerToken, List<String> roles) {

}
