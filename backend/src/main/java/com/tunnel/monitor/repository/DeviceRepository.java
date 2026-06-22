package com.tunnel.monitor.repository;

import com.tunnel.monitor.entity.Device;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class DeviceRepository implements PanacheRepository<Device> {

    public Uni<List<Device>> findBySectionId(Long sectionId) {
        return list("sectionId", sectionId);
    }

    public Uni<Device> findByDeviceCode(String deviceCode) {
        return find("deviceCode", deviceCode).firstResult();
    }

    public Uni<List<Device>> findByStatus(String status) {
        return list("status", status);
    }
}
