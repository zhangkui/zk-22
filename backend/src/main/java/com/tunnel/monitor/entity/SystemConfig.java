package com.tunnel.monitor.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "system_config")
@EqualsAndHashCode(callSuper = true)
public class SystemConfig extends PanacheEntity {

    @Column(unique = true, nullable = false, length = 100)
    private String configKey;

    @Column(length = 500)
    private String configValue;

    @Column(length = 100)
    private String configName;

    @Column(length = 50)
    private String configType;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false)
    private Boolean isSystem = false;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(length = 1000)
    private String remark;
}
