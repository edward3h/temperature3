package org.ethelred.temperature3;

import io.avaje.inject.BeanScope;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpFeature;
import io.helidon.webserver.http.HttpRouting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Starting");
        var bs = BeanScope.builder().build();

        var polling = bs.get(Polling.class);
        polling.start();
        var routeBuilder = HttpRouting.builder();
        var routes = bs.list(HttpFeature.class);
        routes.forEach(routeBuilder::addFeature);
        WebServer.builder().addRouting(routeBuilder).build().start();
    }
}
