package com.tunnel.monitor.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class WebSocketConfig {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    void onStart(@Observes StartupEvent ev) {
        log.info("WebSocket 服务已启动");
    }
}
