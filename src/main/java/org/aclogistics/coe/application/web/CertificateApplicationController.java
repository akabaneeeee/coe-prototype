package org.aclogistics.coe.application.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.application.ApplyCertificateDto;
import org.aclogistics.coe.domain.service.IEmploymentCertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
