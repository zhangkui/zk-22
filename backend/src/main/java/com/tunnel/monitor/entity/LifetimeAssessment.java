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
@Table(name = "lifetime_assessment")
@EqualsAndHashCode(callSuper = true)
public class LifetimeAssessment extends PanacheEntity {

    @Column(name = "section_id", nullable = false)
    private Long sectionId;

    @Column(length = 50)
    private String assessmentCode;

    @Column(length = 100)
    private String assessmentName;

    @Column(length = 50)
    private String assessmentType;

    private LocalDate assessmentDate;

    @Column(precision = 5, scale = 2)
    private BigDecimal designLife;

    @Column(precision = 5, scale = 2)
    private BigDecimal usedLife;

    @Column(precision = 5, scale = 2)
    private BigDecimal remainingLife;

    @Column(precision = 5, scale = 2)
    private BigDecimal predictedRemainingLife;

    @Column(length = 20)
    private String healthLevel;

    @Column(precision = 5, scale = 2)
    private BigDecimal healthScore;

    @Column(precision = 5, scale = 2)
    private BigDecimal deteriorationRate;

    @Column(length = 50)
    private String mainDeteriorationType;

    @Column(length = 1000)
    private String deteriorationDescription;

    @Column(length = 1000)
    private String riskFactors;

    @Column(length = 1000)
    private String maintenanceSuggestions;

    @Column(length = 1000)
    private String assessmentConclusion;

    @Column(name = "assessor_id")
    private Long assessorId;

    @Column(length = 50)
    private String assessorName;

    @Column(length = 20)
    private String status;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(length = 1000)
    private String remark;
}
