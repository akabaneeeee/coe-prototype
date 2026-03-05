package org.aclogistics.coe.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.springframework.stereotype.Service;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WatermarkService {

    public byte[] addWatermark(byte[] pdfBytes) {
        try (
            PDDocument document = PDDocument.load(pdfBytes);
            InputStream is = WatermarkService.class.getResourceAsStream("/static/img/lgc-logo.png");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
        ) {
            PDImageXObject watermarkImage = PDImageXObject.createFromByteArray(document, is.readAllBytes(), "lgc-logo");
            for (PDPage page : document.getPages()) {
                float pageWidth = page.getMediaBox().getWidth();

                try (PDPageContentStream contentStream =
                    new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)
                ) {
                    PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
                    gs.setNonStrokingAlphaConstant(0.06f);
                    gs.setAlphaSourceFlag(true);
                    contentStream.setGraphicsStateParameters(gs);

                    /**
                     * U-FREIGHT: width = 300, height = 180, x = pageWidth - width + 55, y = -5
                     * AIR21: width = 300, height = 180, x = pageWidth - width + 10, y = -10
                     * ACL: width = 500, height = 180, x = pageWidth - width + 30, y = -35 | THE IMAGE IS TOO LARGE, REQUEST TO MAKE THIS MORE COMPACT TO REDUCE SIZE
                     * AMOVE: width = 360, height = 180, x = pageWidth - width + 10, y = -20
                     * CHI: width = 500, height = 120, x = pageWidth - width + 10, y = -15 | THE IMAGE IS TOO LARGE, REQUEST TO MAKE THIS MORE COMPACT TO REDUCE SIZE
                     * LGC: width = 360, height = 180, x = pageWidth - width + 15, y = -20 | THE IMAGE IS TOO LARGE, REQUEST TO MAKE THIS MORE COMPACT TO REDUCE SIZE
                     */
                    float imageWidth = 360;
                    float imageHeight = 180;
                    float x = pageWidth - imageWidth + 15;
                    float y = -20;

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
