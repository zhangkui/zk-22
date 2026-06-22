package com.tunnel.monitor.repository;

import com.tunnel.monitor.entity.User;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Uni<User> findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public Uni<Boolean> existsByUsername(String username) {
        return count("username", username).map(count -> count > 0);
    }
}
