package com.tunnel.monitor.service;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.entity.Device;
import com.tunnel.monitor.repository.DeviceRepository;
import com.tunnel.monitor.util.BusinessException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DeviceService {

    @Inject
    DeviceRepository repository;

    @Inject
    MockDataService mockDataService;

    public Uni<PageResult<Device>> list(PageRequest pageRequest, Long sectionId, String status) {
        List<Device> list = mockDataService.getMockDevices();
        if (sectionId != null) {
            list = list.stream().filter(d -> d.getSectionId().equals(sectionId)).collect(Collectors.toList());
        }
        if (status != null && !status.isEmpty()) {
            list = list.stream().filter(d -> status.equals(d.getStatus())).collect(Collectors.toList());
        }
        int total = list.size();
        int start = pageRequest.getPageIndex() * pageRequest.getSize();
        int end = Math.min(start + pageRequest.getSize(), total);
        List<Device> records = list.subList(start, end);

        return Uni.createFrom().item(PageResult.of(records, total, pageRequest.getPage(), pageRequest.getSize()));
    }

    public Uni<List<Device>> getBySectionId(Long sectionId) {
        List<Device> list = mockDataService.getMockDevices().stream()
                .filter(d -> d.getSectionId().equals(sectionId))
                .collect(Collectors.toList());
        return Uni.createFrom().item(list);
    }

    public Uni<Device> getById(Long id) {
        Device entity = mockDataService.getDevice(id);
        if (entity == null) {
            throw new BusinessException(404, "设备不存在");
        }
        return Uni.createFrom().item(entity);
    }

    public Uni<Device> create(Device entity) {
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return repository.persist(entity);
    }

    public Uni<Device> update(Long id, Device entity) {
        return repository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "设备不存在"))
                .onItem().transform(existing -> {
                    existing.setSectionId(entity.getSectionId() != null ? entity.getSectionId() : existing.getSectionId());
                    existing.setDeviceCode(entity.getDeviceCode() != null ? entity.getDeviceCode() : existing.getDeviceCode());
                    existing.setDeviceName(entity.getDeviceName() != null ? entity.getDeviceName() : existing.getDeviceName());
                    existing.setDeviceType(entity.getDeviceType() != null ? entity.getDeviceType() : existing.getDeviceType());
                    existing.setManufacturer(entity.getManufacturer() != null ? entity.getManufacturer() : existing.getManufacturer());
                    existing.setModel(entity.getModel() != null ? entity.getModel() : existing.getModel());
                    existing.setSerialNumber(entity.getSerialNumber() != null ? entity.getSerialNumber() : existing.getSerialNumber());
                    existing.setInstallDate(entity.getInstallDate() != null ? entity.getInstallDate() : existing.getInstallDate());
                    existing.setLastMaintenanceDate(entity.getLastMaintenanceDate() != null ? entity.getLastMaintenanceDate() : existing.getLastMaintenanceDate());
                    existing.setNextMaintenanceDate(entity.getNextMaintenanceDate() != null ? entity.getNextMaintenanceDate() : existing.getNextMaintenanceDate());
                    existing.setMileage(entity.getMileage() != null ? entity.getMileage() : existing.getMileage());
                    existing.setInstallPositionX(entity.getInstallPositionX() != null ? entity.getInstallPositionX() : existing.getInstallPositionX());
                    existing.setInstallPositionY(entity.getInstallPositionY() != null ? entity.getInstallPositionY() : existing.getInstallPositionY());
                    existing.setInstallPositionZ(entity.getInstallPositionZ() != null ? entity.getInstallPositionZ() : existing.getInstallPositionZ());
                    existing.setStatus(entity.getStatus() != null ? entity.getStatus() : existing.getStatus());
                    existing.setBatteryLevel(entity.getBatteryLevel() != null ? entity.getBatteryLevel() : existing.getBatteryLevel());
                    existing.setCommunicationMode(entity.getCommunicationMode() != null ? entity.getCommunicationMode() : existing.getCommunicationMode());
                    existing.setLastHeartbeat(entity.getLastHeartbeat() != null ? entity.getLastHeartbeat() : existing.getLastHeartbeat());
                    existing.setUpdateTime(LocalDateTime.now());
                    existing.setRemark(entity.getRemark() != null ? entity.getRemark() : existing.getRemark());
                    return existing;
                });
    }

    public Uni<Boolean> delete(Long id) {
        return repository.deleteById(id);
    }
}
