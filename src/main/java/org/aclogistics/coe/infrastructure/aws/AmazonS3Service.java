package org.aclogistics.coe.infrastructure.aws;

import java.io.File;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.upload.UploadFileResultDto;
import org.aclogistics.coe.domain.port.IFileRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.s3")
public class AmazonS3Service implements IFileRepository {

    @Setter
    private long presignedUrlExpiry;

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Override
    public UploadFileResultDto upload(File file, String bucket, String key) {
        if (StringUtils.isAnyBlank(bucket, key)) {
            throw new IllegalArgumentException("Please provide a valid key and bucket.");
        }

        if (ObjectUtils.isEmpty(file)) {
            throw new IllegalArgumentException("Please provide a valid file.");
        }

        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        s3Client.putObject(request, file.toPath());

        UploadFileResultDto response = new UploadFileResultDto();
        response.setKey(key);
        response.setBucket(bucket);

        return response;
    }

    @Override
    public String generatePresignedURL(String key, String bucket) {
        if (StringUtils.isAnyBlank(key, bucket)) {
            log.info("The provided key or bucket is invalid, returning null...");
            return null;
        }

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(presignedUrlExpiry))
            .getObjectRequest(obj -> obj.key(key).bucket(bucket))
            .build();

        PresignedGetObjectRequest presignedGetObjectRequest;
        try {
            presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
        } catch (Exception genEx) {
            log.info("Something went wrong during generating presigned URL for key {} and bucket {} due to: {}", key, bucket, genEx.getMessage());
            return null;
        }

        return presignedGetObjectRequest.url().toExternalForm();
    }
}
