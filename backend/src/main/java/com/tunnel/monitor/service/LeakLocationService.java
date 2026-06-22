package com.tunnel.monitor.service;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.entity.LeakLocation;
import com.tunnel.monitor.repository.LeakLocationRepository;
import com.tunnel.monitor.util.BusinessException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LeakLocationService {

    @Inject
    LeakLocationRepository repository;

    @Inject
    MockDataService mockDataService;

    public Uni<PageResult<LeakLocation>> list(PageRequest pageRequest, Long sectionId, String severity, String status) {
        List<LeakLocation> list = mockDataService.getMockLeaks();
        if (sectionId != null) {
            list = list.stream().filter(l -> l.getSectionId().equals(sectionId)).collect(Collectors.toList());
        }
        if (severity != null && !severity.isEmpty()) {
            list = list.stream().filter(l -> severity.equals(l.getSeverity())).collect(Collectors.toList());
        }
        if (status != null && !status.isEmpty()) {
            list = list.stream().filter(l -> status.equals(l.getStatus())).collect(Collectors.toList());
        }
        int total = list.size();
        int start = pageRequest.getPageIndex() * pageRequest.getSize();
        int end = Math.min(start + pageRequest.getSize(), total);
        List<LeakLocation> records = list.subList(start, end);

        return Uni.createFrom().item(PageResult.of(records, total, pageRequest.getPage(), pageRequest.getSize()));
    }

    public Uni<List<LeakLocation>> getBySectionId(Long sectionId) {
        List<LeakLocation> list = mockDataService.getMockLeaks().stream()
                .filter(l -> l.getSectionId().equals(sectionId))
                .collect(Collectors.toList());
        return Uni.createFrom().item(list);
    }

    public Uni<LeakLocation> getById(Long id) {
        LeakLocation entity = mockDataService.getLeak(id);
        if (entity == null) {
            throw new BusinessException(404, "渗漏位置不存在");
        }
        return Uni.createFrom().item(entity);
    }

    public Uni<LeakLocation> create(LeakLocation entity) {
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return repository.persist(entity);
    }

    public Uni<LeakLocation> update(Long id, LeakLocation entity) {
        return repository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "渗漏位置不存在"))
                .onItem().transform(existing -> {
                    existing.setSectionId(entity.getSectionId() != null ? entity.getSectionId() : existing.getSectionId());
                    existing.setLeakCode(entity.getLeakCode() != null ? entity.getLeakCode() : existing.getLeakCode());
                    existing.setMileage(entity.getMileage() != null ? entity.getMileage() : existing.getMileage());
                    existing.setPositionX(entity.getPositionX() != null ? entity.getPositionX() : existing.getPositionX());
                    existing.setPositionY(entity.getPositionY() != null ? entity.getPositionY() : existing.getPositionY());
                    existing.setLeakType(entity.getLeakType() != null ? entity.getLeakType() : existing.getLeakType());
                    existing.setSeverity(entity.getSeverity() != null ? entity.getSeverity() : existing.getSeverity());
                    existing.setArea(entity.getArea() != null ? entity.getArea() : existing.getArea());
                    existing.setFlowRate(entity.getFlowRate() != null ? entity.getFlowRate() : existing.getFlowRate());
                    existing.setStatus(entity.getStatus() != null ? entity.getStatus() : existing.getStatus());
                    existing.setDetectionMethod(entity.getDetectionMethod() != null ? entity.getDetectionMethod() : existing.getDetectionMethod());
                    existing.setDetectTime(entity.getDetectTime() != null ? entity.getDetectTime() : existing.getDetectTime());
                    existing.setRepairTime(entity.getRepairTime() != null ? entity.getRepairTime() : existing.getRepairTime());
                    existing.setRepairMethod(entity.getRepairMethod() != null ? entity.getRepairMethod() : existing.getRepairMethod());
                    existing.setRepairStatus(entity.getRepairStatus() != null ? entity.getRepairStatus() : existing.getRepairStatus());
                    existing.setUpdateTime(LocalDateTime.now());
                    existing.setRemark(entity.getRemark() != null ? entity.getRemark() : existing.getRemark());
                    return existing;
                });
    }

    public Uni<Boolean> delete(Long id) {
        return repository.deleteById(id);
    }
}
