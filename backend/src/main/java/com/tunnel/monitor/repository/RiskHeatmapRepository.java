package com.tunnel.monitor.repository;

import com.tunnel.monitor.entity.RiskHeatmap;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RiskHeatmapRepository implements PanacheRepository<RiskHeatmap> {

    public Uni<List<RiskHeatmap>> findByTunnelId(Long tunnelId) {
        return list("tunnelId", tunnelId);
    }

    public Uni<List<RiskHeatmap>> findBySectionId(Long sectionId) {
        return list("sectionId", sectionId);
    }

    public Uni<List<RiskHeatmap>> findByRiskLevel(String riskLevel) {
        return list("riskLevel", riskLevel);
    }
}
