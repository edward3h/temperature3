package org.ethelred.temperature3;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("kumojs")
public class KumoJsPoller implements Poller {
    private final Logger LOGGER = LoggerFactory.getLogger(KumoJsPoller.class);
    private final KumoJsAPI kumoJsAPI;
    private final ReadingsDao readingsDao;

    public KumoJsPoller(KumoJsAPI kumoJsAPI, ReadingsDao readingsDao) {

        this.kumoJsAPI = kumoJsAPI;
        this.readingsDao = readingsDao;
    }

    @Override
    public void poll() {
        var rooms = kumoJsAPI.getRoomList();
        for (var room : rooms) {
            Thread.ofVirtual().start(() -> {
                var roomName = URLEncoder.encode(room, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
                var status = kumoJsAPI.getRoomStatus(roomName);
                LOGGER.info("Room {} status {}", room, status);
            });
        }
    }
}
