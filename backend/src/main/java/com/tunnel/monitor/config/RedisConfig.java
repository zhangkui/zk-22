package com.tunnel.monitor.config;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;

@ApplicationScoped
public class RedisConfig {

    @Inject
    RedisDataSource redisDataSource;

    public <T> ValueCommands<String, T> getValueCommands(Class<T> clazz) {
        return redisDataSource.value(clazz);
    }

    public void set(String key, Object value) {
        ValueCommands<String, Object> commands = redisDataSource.value(Object.class);
        commands.set(key, value);
    }

    public void set(String key, Object value, Duration duration) {
        ValueCommands<String, Object> commands = redisDataSource.value(Object.class);
        commands.setex(key, duration.getSeconds(), value);
    }

    public Object get(String key) {
        ValueCommands<String, Object> commands = redisDataSource.value(Object.class);
        return commands.get(key);
    }

    public void delete(String key) {
        redisDataSource.key().del(key);
    }

    public boolean exists(String key) {
        return redisDataSource.key().exists(key) > 0;
    }

    public void expire(String key, Duration duration) {
        redisDataSource.key().expire(key, duration.getSeconds());
    }

    public Long increment(String key) {
        return redisDataSource.value(Long.class).incr(key);
    }

    public Long decrement(String key) {
        return redisDataSource.value(Long.class).decr(key);
    }
}
