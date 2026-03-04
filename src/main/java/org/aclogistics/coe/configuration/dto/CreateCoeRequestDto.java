package org.aclogistics.coe.configuration.dto;

import java.io.Serial;
import lombok.Data;
import org.aclogistics.coe.configuration.utility.SecurityUtility;

/**
 * @author Rosendo Coquilla
 */
@Data
public class CreateCoeRequestDto implements Dto {

    @Serial
    private static final long serialVersionUID = 1093712730365435926L;

    private String requesterEmail = SecurityUtility.getUserEmail();
    private String requestedBy = SecurityUtility.getUserFullName();
}
