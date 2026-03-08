package org.aclogistics.coe.infrastructure.configuration.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

/**
 * @author Rosendo Coquilla
 */
@Configuration
public class AmazonSESConfiguration {

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
            .credentialsProvider(DefaultCredentialsProvider.builder().build())
            .region(Region.AP_SOUTHEAST_1)
            .build();
    }
}
