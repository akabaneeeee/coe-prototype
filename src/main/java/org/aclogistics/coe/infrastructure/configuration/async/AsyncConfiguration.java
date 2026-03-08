package org.aclogistics.coe.infrastructure.configuration.async;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Rosendo Coquilla
 */
@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    @Override
    public Executor  getAsyncExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
