package org.aclogistics.coe.infrastructure.watermark;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.model.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.port.IWatermarkService;
import org.aclogistics.coe.infrastructure.watermark.properties.WatermarkProperties;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.springframework.stereotype.Component;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WatermarkService implements IWatermarkService {

    private final WatermarkProperties properties;

    @Override
    public byte[] addWatermark(byte[] pdfBytes, BusinessUnit businessUnit) {
        var config = properties.getUnits().get(businessUnit);

        try (
            PDDocument document = PDDocument.load(pdfBytes);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            InputStream is = WatermarkService.class.getResourceAsStream(config.getLogoPath());
        ) {
            if (Objects.isNull(is)) {
                throw new IllegalArgumentException("Unable to load resource: " + config.getLogoPath());
            }

            PDImageXObject watermarkImage = PDImageXObject.createFromByteArray(document, is.readAllBytes(), "logo");
            for (PDPage page : document.getPages()) {
                float pageWidth = page.getMediaBox().getWidth();

                try (PDPageContentStream contentStream =
                    new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)
                ) {
                    PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
                    gs.setNonStrokingAlphaConstant(0.06f);
                    gs.setAlphaSourceFlag(true);
                    contentStream.setGraphicsStateParameters(gs);

                    float imageWidth = config.getImageWidth();
                    float imageHeight = config.getImageHeight();
                    float x = pageWidth - imageWidth + config.getX();
                    float y = config.getY();

                    contentStream.drawImage(watermarkImage, x, y, imageWidth, imageHeight);
                }
            }

            document.save(os);
            return os.toByteArray();
        } catch (IOException e) {
            log.error("Error: ", e);
        }

        return new byte[0];
    }
}
