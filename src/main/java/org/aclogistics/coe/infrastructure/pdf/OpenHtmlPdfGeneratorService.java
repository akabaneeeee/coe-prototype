package org.aclogistics.coe.infrastructure.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.port.IPdfRendererService;
import org.springframework.stereotype.Component;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
public class OpenHtmlPdfGeneratorService implements IPdfRendererService {

    @Override
    public byte[] generate(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, "");

            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Error: ", e);
        }

        return new byte[0];
    }
}
