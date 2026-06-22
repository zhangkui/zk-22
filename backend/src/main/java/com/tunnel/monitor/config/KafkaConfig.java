package com.tunnel.monitor.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KafkaConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Inject
    @Channel("alarm-events")
    Emitter<Object> alarmEmitter;

    @Inject
    @Channel("device-data")
    Emitter<Object> deviceDataEmitter;

    public void sendAlarmEvent(Object event) {
        try {
            alarmEmitter.send(event);
            log.debug("发送告警事件到Kafka: {}", event);
        } catch (Exception e) {
            log.error("发送告警事件失败: {}", e.getMessage());
        }
    }

    public void sendDeviceData(Object data) {
        try {
            deviceDataEmitter.send(data);
            log.debug("发送设备数据到Kafka: {}", data);
        } catch (Exception e) {
            log.error("发送设备数据失败: {}", e.getMessage());
        }
    }
}
