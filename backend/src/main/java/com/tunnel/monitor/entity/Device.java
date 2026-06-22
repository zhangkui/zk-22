package com.tunnel.monitor.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "device")
@EqualsAndHashCode(callSuper = true)
public class Device extends PanacheEntity {

    @Column(name = "section_id", nullable = false)
    private Long sectionId;

    @Column(unique = true, length = 50)
    private String deviceCode;

    @Column(length = 100)
    private String deviceName;

    @Column(length = 50)
    private String deviceType;

    @Column(length = 50)
    private String manufacturer;

    @Column(length = 50)
    private String model;

    @Column(length = 50)
    private String serialNumber;

    private LocalDate installDate;

    private LocalDate lastMaintenanceDate;

    private LocalDate nextMaintenanceDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal mileage;

    @Column(precision = 10, scale = 2)
    private BigDecimal installPositionX;

    @Column(precision = 10, scale = 2)
    private BigDecimal installPositionY;

    @Column(precision = 10, scale = 2)
    private BigDecimal installPositionZ;

    @Column(length = 20)
    private String status;

    @Column(precision = 5, scale = 2)
    private BigDecimal batteryLevel;

    @Column(length = 50)
    private String communicationMode;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(length = 1000)
    private String remark;
}
