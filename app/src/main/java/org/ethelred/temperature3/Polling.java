package org.ethelred.temperature3;

import io.avaje.inject.QualifiedMap;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class Polling {
    private final Configuration configuration;
    private final ScheduledExecutorService executorService;
    private final Map<String, Poller> pollers;

    @Inject
    public Polling(
            Configuration configuration,
            ScheduledExecutorService executorService,
            @QualifiedMap Map<String, Poller> pollers) {
        this.configuration = configuration;
        this.executorService = executorService;
        this.pollers = pollers;
    }

    public void start() {
        pollers.forEach((name, poller) -> {
            var duration = configuration.getPollers().get(name).getPollEvery().toJavaDuration();
            executorService.scheduleAtFixedRate(
                    () -> Thread.ofVirtual().start(poller::poll), 0, duration.toMillis(), TimeUnit.MILLISECONDS);
        });
    }
}
