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
@Table(name = "alarm_event")
@EqualsAndHashCode(callSuper = true)
public class AlarmEvent extends PanacheEntity {

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "leak_id")
    private Long leakId;

    @Column(length = 50)
    private String alarmCode;

    @Column(length = 50)
    private String alarmType;

    @Column(length = 20)
    private String alarmLevel;

    @Column(length = 200)
    private String alarmTitle;

    @Column(length = 1000)
    private String alarmContent;

    @Column(precision = 10, scale = 2)
    private BigDecimal alarmValue;

    @Column(precision = 10, scale = 2)
    private BigDecimal thresholdValue;

    @Column(length = 20)
    private String status;

    @Column(name = "alarm_time")
    private LocalDateTime alarmTime;

    @Column(name = "ack_time")
    private LocalDateTime ackTime;

    @Column(name = "ack_user_id")
    private Long ackUserId;

    @Column(length = 50)
    private String ackUserName;

    @Column(name = "resolve_time")
    private LocalDateTime resolveTime;

    @Column(name = "resolve_user_id")
    private Long resolveUserId;

    @Column(length = 50)
    private String resolveUserName;

    @Column(length = 1000)
    private String resolveMethod;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(length = 1000)
    private String remark;
}
