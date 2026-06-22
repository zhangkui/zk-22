package com.tunnel.monitor.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "leak_location")
@EqualsAndHashCode(callSuper = true)
public class LeakLocation extends PanacheEntity {

    @Column(name = "section_id", nullable = false)
    private Long sectionId;

    @Column(length = 50)
    private String leakCode;

    @Column(precision = 10, scale = 2)
    private BigDecimal mileage;

    @Column(precision = 10, scale = 2)
    private BigDecimal positionX;

    @Column(precision = 10, scale = 2)
    private BigDecimal positionY;

    @Column(length = 50)
    private String leakType;

    @Column(length = 50)
    private String severity;

    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    @Column(precision = 10, scale = 2)
    private BigDecimal flowRate;

    @Column(length = 20)
    private String status;

    @Column(length = 50)
    private String detectionMethod;

    @Column(name = "detect_time")
    private LocalDateTime detectTime;

    @Column(name = "repair_time")
    private LocalDateTime repairTime;

    @Column(length = 100)
    private String repairMethod;

    @Column(length = 20)
    private String repairStatus;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(length = 1000)
    private String remark;
}
