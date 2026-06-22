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
@Table(name = "inspection_task")
@EqualsAndHashCode(callSuper = true)
public class InspectionTask extends PanacheEntity {

    @Column(name = "tunnel_id", nullable = false)
    private Long tunnelId;

    @Column(length = 50)
    private String taskCode;

    @Column(length = 100)
    private String taskName;

    @Column(length = 50)
    private String taskType;

    @Column(length = 20)
    private String priority;

    @Column(length = 20)
    private String status;

    @Column(name = "plan_start_time")
    private LocalDateTime planStartTime;

    @Column(name = "plan_end_time")
    private LocalDateTime planEndTime;

    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    @Column(name = "inspector_id")
    private Long inspectorId;

    @Column(length = 50)
    private String inspectorName;

    @Column(length = 100)
    private String inspectionScope;

    @Column(length = 1000)
    private String inspectionContent;

    @Column(length = 1000)
    private String inspectionResult;

    @Column(length = 1000)
    private String problemDescription;

    @Column(length = 1000)
    private String suggestion;

    @Column(name = "create_user_id")
    private Long createUserId;

    @Column(length = 50)
    private String createUserName;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(length = 1000)
    private String remark;
}
