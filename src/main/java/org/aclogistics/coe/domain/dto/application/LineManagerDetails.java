package org.aclogistics.coe.domain.dto.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Rosendo Coquilla
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LineManagerDetails(
    @JsonProperty("line_manager_first_name") String firstName,
    @JsonProperty("line_manager_last_name") String lastName,
    @JsonProperty("line_manager_email") String email
) {

}
