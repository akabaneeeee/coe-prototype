package org.aclogistics.coe.application.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.application.ApplyCertificateDto;
import org.aclogistics.coe.domain.dto.application.CertificateApplicationDetails;
import org.aclogistics.coe.domain.dto.application.UpdateApplicationDetails;
import org.aclogistics.coe.domain.dto.application.filtered.GetFilteredApplicationDto;
import org.aclogistics.coe.domain.dto.application.filtered.PaginatedApplicationDto;
import org.aclogistics.coe.domain.dto.transition.TransitionApplicationDto;
import org.aclogistics.coe.domain.service.application.IEmploymentCertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ecgen/applications")
public class CertificateApplicationController {

    private final IEmploymentCertificateService employmentCertificateService;

    @PostMapping
    public ResponseEntity<Void> applyForCertificate(
        @RequestBody @Valid final ApplyCertificateDto request
    ) {
        log.info("REST request received to apply for new employment certificate");
        employmentCertificateService.apply(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{reference-number}/transition")
    public ResponseEntity<Void> transitionApplication(
        @PathVariable("reference-number") final String referenceNumber,
        @RequestBody @Valid final TransitionApplicationDto request
    ) {
        log.info("REST request received to transition application: {}", referenceNumber);
        request.setReferenceNumber(referenceNumber);
        employmentCertificateService.transitionToNextStatus(request);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/filtered")
    public ResponseEntity<PaginatedApplicationDto> filteredApplication(
        @RequestBody @Valid final GetFilteredApplicationDto  request
    ) {
        return ResponseEntity.ok().body(employmentCertificateService.getPaginatedApplications(request));
    }

    @GetMapping("/{reference-number}")
    public ResponseEntity<CertificateApplicationDetails> getApplicationDetails(
        @PathVariable("reference-number") final String referenceNumber
    ) {
        log.info("REST request received to get certificate application details: {}",  referenceNumber);
        return ResponseEntity.ok().body(employmentCertificateService.getApplicationDetails(referenceNumber));
    }

    @PatchMapping("/{reference-number}")
    public ResponseEntity<CertificateApplicationDetails> updateApplicationDetails(
        @PathVariable("reference-number") final String referenceNumber,
        @RequestBody @Valid final UpdateApplicationDetails request
    ) {
        log.info("REST request received to update application details: {}",  referenceNumber);
        request.setReferenceNumber(referenceNumber);
        employmentCertificateService.updateApplication(request);

        return ResponseEntity.accepted().build();
    }
}
