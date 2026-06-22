package com.tunnel.monitor.repository;

import com.tunnel.monitor.entity.InspectionTask;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class InspectionTaskRepository implements PanacheRepository<InspectionTask> {

    public Uni<List<InspectionTask>> findByTunnelId(Long tunnelId) {
        return list("tunnelId", tunnelId);
    }

    public Uni<List<InspectionTask>> findByStatus(String status) {
        return list("status", status);
    }

    public Uni<List<InspectionTask>> findByInspectorId(Long inspectorId) {
        return list("inspectorId", inspectorId);
    }
}
