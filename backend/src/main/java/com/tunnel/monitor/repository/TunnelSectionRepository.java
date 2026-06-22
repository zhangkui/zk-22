package com.tunnel.monitor.repository;

import com.tunnel.monitor.entity.TunnelSection;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TunnelSectionRepository implements PanacheRepository<TunnelSection> {

    public Uni<List<TunnelSection>> findByTunnelId(Long tunnelId) {
        return list("tunnelId", tunnelId);
    }

    public Uni<TunnelSection> findBySectionCode(String sectionCode) {
        return find("sectionCode", sectionCode).firstResult();
    }
}
