package org.aclogistics.coe.configuration.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
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
            InputStream is = WatermarkService.class.getResourceAsStream("/static/img/u-logo.png");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
        ) {
            PDImageXObject watermarkImage = PDImageXObject.createFromByteArray(document, is.readAllBytes(), "u-logo");
            for (PDPage page : document.getPages()) {
                float pageWidth = page.getMediaBox().getWidth();

                try (PDPageContentStream contentStream =
                    new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)
                ) {

                    PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
                    gs.setNonStrokingAlphaConstant(0.06f);
                    gs.setAlphaSourceFlag(true);
                    contentStream.setGraphicsStateParameters(gs);

                    float imageWidth = 300;
                    float imageHeight = 180;
                    float x = pageWidth - imageWidth + 55;
                    float y = -5;

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
