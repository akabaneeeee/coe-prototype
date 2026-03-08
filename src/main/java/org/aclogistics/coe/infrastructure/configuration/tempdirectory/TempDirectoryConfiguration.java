package org.aclogistics.coe.infrastructure.configuration.tempdirectory;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.utility.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TempDirectoryConfiguration {

    @Setter
    @Value("${app.generated-coe.temporary-directory}")
    private String temporaryDirectory;

    @PostConstruct
    public void initialize() {
        log.info("Initializing temporary directories...");

        FileUtils.initializeTempDirectory(temporaryDirectory);

        log.info("Successfully initialized directories!");
    }
}
