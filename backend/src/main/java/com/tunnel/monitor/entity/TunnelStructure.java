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
@Table(name = "tunnel_structure")
@EqualsAndHashCode(callSuper = true)
public class TunnelStructure extends PanacheEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String tunnelCode;

    @Column(nullable = false, length = 100)
    private String tunnelName;

    @Column(length = 200)
    private String location;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalLength;

    @Column(precision = 10, scale = 2)
    private BigDecimal width;

    @Column(precision = 10, scale = 2)
    private BigDecimal height;

    @Column(length = 50)
    private String structureType;

    @Column(length = 50)
    private String constructionMethod;

    private LocalDate buildDate;

    private LocalDate openDate;

    @Column(length = 100)
    private String designUnit;

    @Column(length = 100)
    private String constructionUnit;

    @Column(length = 100)
    private String supervisionUnit;

    @Column(length = 20)
    private String status;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(length = 1000)
    private String remark;
}
