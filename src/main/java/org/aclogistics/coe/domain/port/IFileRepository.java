package org.aclogistics.coe.domain.port;

import java.io.File;
import org.aclogistics.coe.domain.dto.upload.UploadFileResultDto;

/**
 * @author Rosendo Coquilla
 */
public interface IFileRepository {

    UploadFileResultDto upload(File file, String bucket, String key);
    String generatePresignedURL(String key, String bucket);
}
