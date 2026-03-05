package org.aclogistics.coe.service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.enumeration.BusinessUnit;
import org.aclogistics.coe.service.watermark.WatermarkService;
import org.springframework.stereotype.Service;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmploymentCertificateService {

    private final HtmlTemplateRenderer htmlTemplateRenderer;
    private final PdfGeneratorService pdfGeneratorService;
    private final WatermarkService watermarkService;

    @PostConstruct
    public void generateCertificate() {
        String htmlContent = htmlTemplateRenderer.render();
        byte[] originalPdfBytes = pdfGeneratorService.generate(htmlContent);

        try {
            Path outputPath = Path.of("src/main/resources/generated-coe.pdf");
            Files.createDirectories(outputPath.getParent());

            byte[] watermarkedPdfBytes = watermarkService.addWatermark(originalPdfBytes, BusinessUnit.LGC);
            Files.write(outputPath, watermarkedPdfBytes);
        } catch (IOException ex) {
            log.error("Error: ", ex);
        }
    }
}
