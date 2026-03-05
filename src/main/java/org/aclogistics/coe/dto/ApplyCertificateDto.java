package org.aclogistics.coe.dto;

import java.io.Serial;
import lombok.Data;
import org.aclogistics.coe.utility.SecurityUtility;

/**
 * @author Rosendo Coquilla
 */
@Data
public class ApplyCertificateDto implements Dto {

    @Serial
    private static final long serialVersionUID = 1093712730365435926L;

    private String requesterEmail = SecurityUtility.getUserEmail();
    private String requestedBy = SecurityUtility.getUserFullName();
}
