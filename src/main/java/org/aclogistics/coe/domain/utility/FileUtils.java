package org.aclogistics.coe.domain.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.exception.FileProcessingException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@UtilityClass
public class FileUtils {

    public void initializeTempDirectory(String... tempDir) {
        try {
            for (String directory : tempDir) {
                Path path = Paths.get(directory);

                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
            }
        } catch (InvalidPathException | IOException e) {
            log.warn("An error occurred while creating temporary directory [{}] {}", tempDir, e.getMessage());
        }
    }

    public void deleteFileIfExists(File fileToDelete) {
        if (fileToDelete != null) {
            String fileName = fileToDelete.getName();

            try {
                log.info("Deleting {} from the temporary directory...", fileName);
                Files.deleteIfExists(fileToDelete.toPath());
                log.info("{} successfully deleted!", fileName);
            } catch (IOException ioEx) {
                log.warn("Unable to delete this file {}!", fileName);
            }
        }
    }
}
