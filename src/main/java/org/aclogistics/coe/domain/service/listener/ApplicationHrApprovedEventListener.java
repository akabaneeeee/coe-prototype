package org.aclogistics.coe.domain.service.listener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.TemplateDetails;
import org.aclogistics.coe.domain.dto.transition.TransitionApplicationDto;
import org.aclogistics.coe.domain.dto.upload.UploadFileResultDto;
import org.aclogistics.coe.domain.model.enumeration.Event;
import org.aclogistics.coe.domain.model.event.ApplicationHrApprovedEvent;
import org.aclogistics.coe.domain.model.CertificateApplication;
import org.aclogistics.coe.domain.model.GeneratedCertificate;
import org.aclogistics.coe.domain.port.ICertificateApplicationRepository;
import org.aclogistics.coe.domain.port.IFileRepository;
import org.aclogistics.coe.domain.port.IHtmlTemplateRenderer;
import org.aclogistics.coe.domain.port.IPdfRendererService;
import org.aclogistics.coe.domain.port.IWatermarkService;
import org.aclogistics.coe.domain.service.application.IEmploymentCertificateService;
import org.aclogistics.coe.domain.utility.DateTimeHelper;
import org.aclogistics.coe.domain.utility.FileUtils;
import org.aclogistics.coe.domain.utility.NumberUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationHrApprovedEventListener {

    @Setter
    @Value("${app.generated-coe.temporary-directory}")
    private String temporaryDirectory;

    @Setter
    @Value("${signature}")
    private String signature;

    private final IHtmlTemplateRenderer htmlTemplateRenderer;
    private final IPdfRendererService pdfGeneratorService;
    private final IWatermarkService watermarkService;
    private final IFileRepository  fileRepository;
    private final ICertificateApplicationRepository  certificateApplicationRepository;
    private final IEmploymentCertificateService  employmentCertificateService;

    @Async
    @EventListener
    public void onApplicationEvent(ApplicationHrApprovedEvent event) {
        if (Objects.isNull(event)) {
            log.info("The received ApplicationHrApprovedEvent is null, skipping...");
            return;
        }

        var now = DateTimeHelper.getTodayInPHTDefaultFormat();
        CertificateApplication application = event.getApplication();

        String htmlContent = htmlTemplateRenderer.render(
            new TemplateDetails(
                constructTemplateVariables(application, now),
                "coe/%s.html".formatted(application.getBusinessUnit().name().toLowerCase())
            )
        );

        byte[] originalPdfBytes = pdfGeneratorService.generate(htmlContent);
        byte[] watermarkedPdfBytes = watermarkService.addWatermark(originalPdfBytes, application.getBusinessUnit());

        Short coeVersion = application.getNextCertificateVersion();
        var coeFileName = "%s_V%s.pdf".formatted(application.getReferenceNumber(), coeVersion);
        Path generatedCoePath = Path.of(this.constructCoeFileName(application.getReferenceNumber(), coeFileName));
        try {
            Files.write(generatedCoePath, watermarkedPdfBytes);

            UploadFileResultDto result = fileRepository.upload(
                generatedCoePath.toFile(), "eit-service",
                this.buildS3Key("dev/coe-generator", coeFileName)
            );

            GeneratedCertificate generatedCertificate = GeneratedCertificate.builder()
                .version(coeVersion)
                .fileName(coeFileName)
                .s3Key(result.getKey())
                .s3Bucket(result.getBucket())
                .generatedBy("system")
                .generatedDt(now)
                .build();

            application.addGeneratedCertificate(generatedCertificate);

            certificateApplicationRepository.save(application);

            TransitionApplicationDto transitionApplication =  new TransitionApplicationDto();
            transitionApplication.setReferenceNumber(application.getReferenceNumber());
            transitionApplication.setEvent(Event.CERTIFICATE_GENERATED);

            employmentCertificateService.transitionToNextStatus(transitionApplication);
        } catch (IOException e) {
            log.error("Error: ", e);
        } finally {
            FileUtils.deleteFileIfExists(generatedCoePath.toFile());
        }
    }

    private Map<String, Object> constructTemplateVariables(CertificateApplication application, LocalDateTime now) {
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("date_created", DateTimeHelper.convertToFullDateFormat(now.toLocalDate()));
        templateVariables.put("addressee", application.getAddressee());
        templateVariables.put("place_of_addressee", application.getPlaceOfAddressee());
        templateVariables.put("full_name", application.constructFullName().toUpperCase());
        templateVariables.put("business_unit", application.getBusinessUnit().getActualName());
        templateVariables.put("hire_date", DateTimeHelper.convertToFullDateFormat(application.getHiredDate()));
        templateVariables.put("employee_status", application.getEmploymentStatus());
        templateVariables.put("position", application.getPosition());
        templateVariables.put("department", application.getDepartment().getActualName());
        templateVariables.put("with_compensation", application.isWithCompensation());

        if (application.isWithCompensation()) {
            templateVariables.put("annual_amount", NumberUtility.format(application.getAnnualCompensation()));
        }

        templateVariables.put("last_name", application.getLastName().toUpperCase());
        templateVariables.put("purpose", application.getPurpose().getFriendlyName());
        templateVariables.put("day", now.getDayOfMonth());
        templateVariables.put("month", DateTimeHelper.convertToMonthCOEFormat(now.toLocalDate()));
        templateVariables.put("year", now.getYear());
        templateVariables.put("signature", signature);
        templateVariables.put("signatory", "Juan Dela Cruz");
        templateVariables.put("signatory_designation", "Chief Executive Officer");

        return templateVariables;
    }

    private String constructCoeFileName(String referenceNumber, String fileName) {
        return "%s/%s".formatted(temporaryDirectory, fileName);
    }

    protected String buildS3Key(String s3BucketDirectory, String fileName) {
        return String.format(
            "%s/%s/%s_%s",
            s3BucketDirectory, DateTimeHelper.getTodayInPHTDefaultFormat().toLocalDate(),
            DateTimeHelper.getTodayPHTFileNameFormat(), fileName
        );
    }
}
