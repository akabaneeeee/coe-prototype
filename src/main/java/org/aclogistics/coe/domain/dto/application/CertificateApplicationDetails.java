package org.aclogistics.coe.domain.dto.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.aclogistics.coe.domain.dto.Dto;

/**
 * @author Rosendo Coquilla
 */
@Data
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_ABSENT)
@JsonNaming(SnakeCaseStrategy.class)
public class CertificateApplicationDetails implements Dto {

    @Serial
    private static final long serialVersionUID = 72076732104673424L;

    private String referenceNumber;
    private String firstName;
    private String lastName;
    private String middleInitial;
    private String hiredDate;
    private String position;
    private String department;
    private String employmentStatus;
    private Boolean withCompensation;
    private String annualCompensation;
    private String purpose;
    private Map<String, String> additionalInfo;
    private String addressee;
    private String placeOfAddressee;
    private String requestedBy;
    private String requestedDt;
    private String modifiedBy;
    private String modifiedDt;
    private GeneratedCertificateDetails latestCertificate;

    @Builder.Default
    private List<CertificateApplicationMilestoneDetails> milestones = new ArrayList<>();

    @Builder.Default
    private List<GeneratedCertificateDetails> pastCertificates = new ArrayList<>();
}
