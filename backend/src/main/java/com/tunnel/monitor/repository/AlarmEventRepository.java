package com.tunnel.monitor.repository;

import com.tunnel.monitor.entity.AlarmEvent;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class AlarmEventRepository implements PanacheRepository<AlarmEvent> {

    public Uni<List<AlarmEvent>> findByDeviceId(Long deviceId) {
        return list("deviceId", deviceId);
    }

    public Uni<List<AlarmEvent>> findByStatus(String status) {
        return list("status", status);
    }

    public Uni<List<AlarmEvent>> findByAlarmLevel(String alarmLevel) {
        return list("alarmLevel", alarmLevel);
    }
}
