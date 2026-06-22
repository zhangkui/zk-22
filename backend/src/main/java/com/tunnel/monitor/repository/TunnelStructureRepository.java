package com.tunnel.monitor.repository;

import com.tunnel.monitor.entity.TunnelStructure;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TunnelStructureRepository implements PanacheRepository<TunnelStructure> {

    public Uni<TunnelStructure> findByTunnelCode(String tunnelCode) {
        return find("tunnelCode", tunnelCode).firstResult();
    }
}
