package com.tunnel.monitor.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

@ApplicationScoped
public class JwtSecurity {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @ConfigProperty(name = "jwt.expire.hours", defaultValue = "24")
    long expireHours;

    public String generateToken(Long userId, String username, String realName, String role) {
        return Jwt.issuer(issuer)
                .subject(username)
                .upn(username)
                .groups(new HashSet<>(Arrays.asList(role)))
                .claim("userId", userId)
                .claim("realName", realName)
                .claim("role", role)
                .expiresIn(expireHours * 3600)
                .sign();
    }

    public String generateToken(Long userId, String username, String realName, String role, Map<String, Object> claims) {
        Jwt jwt = Jwt.issuer(issuer)
                .subject(username)
                .upn(username)
                .groups(new HashSet<>(Arrays.asList(role)))
                .claim("userId", userId)
                .claim("realName", realName)
                .claim("role", role)
                .expiresIn(expireHours * 3600);

        if (claims != null) {
            claims.forEach(jwt::claim);
        }

        return jwt.sign();
    }
}
