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
@Table(name = "tunnel_section")
@EqualsAndHashCode(callSuper = true)
public class TunnelSection extends PanacheEntity {

    @Column(name = "tunnel_id", nullable = false)
    private Long tunnelId;

    @Column(length = 50)
    private String sectionCode;

    @Column(length = 100)
    private String sectionName;

    @Column(precision = 10, scale = 2)
    private BigDecimal startMileage;

    @Column(precision = 10, scale = 2)
    private BigDecimal endMileage;

    @Column(precision = 10, scale = 2)
    private BigDecimal length;

    @Column(length = 50)
    private String sectionType;

    @Column(precision = 10, scale = 2)
    private BigDecimal designThickness;

    @Column(length = 50)
    private String concreteGrade;

    @Column(length = 50)
    private String reinforcementGrade;

    @Column(precision = 5, scale = 2)
    private BigDecimal slope;

    @Column(precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 2)
    private BigDecimal elevation;

    @Column(length = 20)
    private String status;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(length = 1000)
    private String remark;
}
