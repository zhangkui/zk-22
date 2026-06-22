package com.tunnel.monitor.repository;

import com.tunnel.monitor.entity.LeakLocation;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class LeakLocationRepository implements PanacheRepository<LeakLocation> {

    public Uni<List<LeakLocation>> findBySectionId(Long sectionId) {
        return list("sectionId", sectionId);
    }

    public Uni<List<LeakLocation>> findByStatus(String status) {
        return list("status", status);
    }

    public Uni<List<LeakLocation>> findBySeverity(String severity) {
        return list("severity", severity);
    }
}
