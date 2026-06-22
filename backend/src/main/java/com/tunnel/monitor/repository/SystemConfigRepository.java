package com.tunnel.monitor.repository;

import com.tunnel.monitor.entity.SystemConfig;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SystemConfigRepository implements PanacheRepository<SystemConfig> {

    public Uni<SystemConfig> findByConfigKey(String configKey) {
        return find("configKey", configKey).firstResult();
    }

    public Uni<Boolean> existsByConfigKey(String configKey) {
        return count("configKey", configKey).map(count -> count > 0);
    }
}
