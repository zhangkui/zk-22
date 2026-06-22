package com.tunnel.monitor.service;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.entity.AlarmEvent;
import com.tunnel.monitor.repository.AlarmEventRepository;
import com.tunnel.monitor.util.BusinessException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AlarmEventService {

    @Inject
    AlarmEventRepository repository;

    @Inject
    MockDataService mockDataService;

    public Uni<PageResult<AlarmEvent>> list(PageRequest pageRequest, Long deviceId, String alarmLevel, String status) {
        List<AlarmEvent> list = mockDataService.getMockAlarms();
        if (deviceId != null) {
            list = list.stream().filter(a -> deviceId.equals(a.getDeviceId())).collect(Collectors.toList());
        }
        if (alarmLevel != null && !alarmLevel.isEmpty()) {
            list = list.stream().filter(a -> alarmLevel.equals(a.getAlarmLevel())).collect(Collectors.toList());
        }
        if (status != null && !status.isEmpty()) {
            list = list.stream().filter(a -> status.equals(a.getStatus())).collect(Collectors.toList());
        }
        int total = list.size();
        int start = pageRequest.getPageIndex() * pageRequest.getSize();
        int end = Math.min(start + pageRequest.getSize(), total);
        List<AlarmEvent> records = list.subList(start, end);

        return Uni.createFrom().item(PageResult.of(records, total, pageRequest.getPage(), pageRequest.getSize()));
    }

    public Uni<List<AlarmEvent>> getActiveAlarms() {
        List<AlarmEvent> list = mockDataService.getMockAlarms().stream()
                .filter(a -> "ACTIVE".equals(a.getStatus()) || "ACKNOWLEDGED".equals(a.getStatus()))
                .collect(Collectors.toList());
        return Uni.createFrom().item(list);
    }

    public Uni<AlarmEvent> getById(Long id) {
        AlarmEvent entity = mockDataService.getAlarm(id);
        if (entity == null) {
            throw new BusinessException(404, "告警事件不存在");
        }
        return Uni.createFrom().item(entity);
    }

    public Uni<AlarmEvent> create(AlarmEvent entity) {
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return repository.persist(entity);
    }

    public Uni<AlarmEvent> acknowledge(Long id, Long userId, String userName) {
        AlarmEvent alarm = mockDataService.getAlarm(id);
        if (alarm == null) {
            throw new BusinessException(404, "告警事件不存在");
        }
        alarm.setStatus("ACKNOWLEDGED");
        alarm.setAckTime(LocalDateTime.now());
        alarm.setAckUserId(userId);
        alarm.setAckUserName(userName);
        alarm.setUpdateTime(LocalDateTime.now());
        return Uni.createFrom().item(alarm);
    }

    public Uni<AlarmEvent> resolve(Long id, Long userId, String userName, String resolveMethod) {
        AlarmEvent alarm = mockDataService.getAlarm(id);
        if (alarm == null) {
            throw new BusinessException(404, "告警事件不存在");
        }
        alarm.setStatus("RESOLVED");
        alarm.setResolveTime(LocalDateTime.now());
        alarm.setResolveUserId(userId);
        alarm.setResolveUserName(userName);
        alarm.setResolveMethod(resolveMethod);
        alarm.setUpdateTime(LocalDateTime.now());
        return Uni.createFrom().item(alarm);
    }

    public Uni<Boolean> delete(Long id) {
        return repository.deleteById(id);
    }
}
