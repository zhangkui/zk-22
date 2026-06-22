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
@Table(name = "risk_heatmap")
@EqualsAndHashCode(callSuper = true)
public class RiskHeatmap extends PanacheEntity {

    @Column(name = "tunnel_id", nullable = false)
    private Long tunnelId;

    @Column(name = "section_id")
    private Long sectionId;

    @Column(length = 50)
    private String heatmapCode;

    @Column(length = 100)
    private String heatmapName;

    @Column(length = 50)
    private String riskType;

    @Column(precision = 10, scale = 2)
    private BigDecimal startMileage;

    @Column(precision = 10, scale = 2)
    private BigDecimal endMileage;

    @Column(precision = 10, scale = 2)
    private BigDecimal positionX;

    @Column(precision = 10, scale = 2)
    private BigDecimal positionY;

    @Column(precision = 10, scale = 2)
    private BigDecimal positionZ;

    @Column(precision = 5, scale = 2)
    private BigDecimal riskValue;

    @Column(length = 20)
    private String riskLevel;

    @Column(length = 20)
    private String colorCode;

    @Column(name = "statistic_time")
    private LocalDateTime statisticTime;

    @Column(precision = 5, scale = 2)
    private BigDecimal occurrenceProbability;

    @Column(precision = 10, scale = 2)
    private BigDecimal consequenceSeverity;

    @Column(length = 1000)
    private String riskDescription;

    @Column(length = 1000)
    private String mitigationMeasures;

    @Column(length = 20)
    private String status;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(length = 1000)
    private String remark;
}
