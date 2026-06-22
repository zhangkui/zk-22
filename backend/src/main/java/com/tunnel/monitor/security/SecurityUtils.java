package com.tunnel.monitor.security;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Optional;

@ApplicationScoped
public class SecurityUtils {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    JsonWebToken jsonWebToken;

    public Long getCurrentUserId() {
        try {
            if (jsonWebToken.containsClaim("userId")) {
                return jsonWebToken.getClaim("userId");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public String getCurrentUsername() {
        return securityIdentity.getPrincipal().getName();
    }

    public String getCurrentRealName() {
        try {
            if (jsonWebToken.containsClaim("realName")) {
                return jsonWebToken.getClaim("realName");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public String getCurrentRole() {
        try {
            if (jsonWebToken.containsClaim("role")) {
                return jsonWebToken.getClaim("role");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public boolean hasRole(String role) {
        return securityIdentity.hasRole(role);
    }

    public boolean isAuthenticated() {
        return !securityIdentity.isAnonymous();
    }

    public Optional<String> getClaim(String claimName) {
        try {
            if (jsonWebToken.containsClaim(claimName)) {
                return Optional.ofNullable(jsonWebToken.getClaim(claimName));
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}
