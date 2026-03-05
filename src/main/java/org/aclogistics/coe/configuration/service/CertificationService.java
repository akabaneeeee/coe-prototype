package org.aclogistics.coe.configuration.service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CertificationService {

    private final HtmlTemplateRenderer htmlTemplateRenderer;
    private final PdfGeneratorService pdfGeneratorService;
    private final WatermarkService watermarkService;

    @PostConstruct
    public void generateCertificate() {
        String htmlContent = htmlTemplateRenderer.render();
        byte[] pdfBytes = pdfGeneratorService.generate(htmlContent);

        try {
            Path outputPath = Path.of("src/main/resources/generated-coe.pdf");
            Files.createDirectories(outputPath.getParent());

            byte[] watermarkedPdf = watermarkService.addWatermark(pdfBytes);
            Files.write(outputPath, watermarkedPdf);
        } catch (IOException ex) {
            log.error("Error: ", ex);
        }
    }
}
