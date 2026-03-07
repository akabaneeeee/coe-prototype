package org.aclogistics.coe.domain.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.ApplyCertificateDto;
import org.aclogistics.coe.domain.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.port.IHtmlTemplateRenderer;
import org.aclogistics.coe.domain.port.IPdfRendererService;
import org.aclogistics.coe.domain.port.IWatermarkService;
import org.springframework.stereotype.Service;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmploymentCertificateService implements IEmploymentCertificateService {

    private final IHtmlTemplateRenderer htmlTemplateRenderer;
    private final IPdfRendererService pdfGeneratorService;
    private final IWatermarkService watermarkService;

    @Override
    public void apply(ApplyCertificateDto dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("Please provide a valid certificate application form");
        }

        log.info("Applying certificate application form: {}", dto);
    }

    @Override
    public void generateCertificate() {
        String htmlContent = htmlTemplateRenderer.render();
        byte[] originalPdfBytes = pdfGeneratorService.generate(htmlContent);

        try {
            Path outputPath = Path.of("src/main/resources/generated-coe.pdf");
            Files.createDirectories(outputPath.getParent());

            byte[] watermarkedPdfBytes = watermarkService.addWatermark(originalPdfBytes, BusinessUnit.AMC);
            Files.write(outputPath, watermarkedPdfBytes);
        } catch (IOException ex) {
            log.error("Error: ", ex);
        }
    }
}
