package com.tunnel.monitor.repository;

import com.tunnel.monitor.entity.LifetimeAssessment;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class LifetimeAssessmentRepository implements PanacheRepository<LifetimeAssessment> {

    public Uni<List<LifetimeAssessment>> findBySectionId(Long sectionId) {
        return list("sectionId", sectionId);
    }

    public Uni<List<LifetimeAssessment>> findByHealthLevel(String healthLevel) {
        return list("healthLevel", healthLevel);
    }

    public Uni<LifetimeAssessment> findLatestBySectionId(Long sectionId) {
        return find("sectionId order by assessmentDate desc", sectionId).firstResult();
    }
}
