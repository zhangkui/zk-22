package com.tunnel.monitor.service;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.entity.TunnelSection;
import com.tunnel.monitor.repository.TunnelSectionRepository;
import com.tunnel.monitor.util.BusinessException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TunnelSectionService {

    @Inject
    TunnelSectionRepository repository;

    @Inject
    MockDataService mockDataService;

    public Uni<PageResult<TunnelSection>> list(PageRequest pageRequest, Long tunnelId) {
        List<TunnelSection> list = mockDataService.getMockSections();
        if (tunnelId != null) {
            list = list.stream().filter(s -> s.getTunnelId().equals(tunnelId)).collect(Collectors.toList());
        }
        int total = list.size();
        int start = pageRequest.getPageIndex() * pageRequest.getSize();
        int end = Math.min(start + pageRequest.getSize(), total);
        List<TunnelSection> records = list.subList(start, end);

        return Uni.createFrom().item(PageResult.of(records, total, pageRequest.getPage(), pageRequest.getSize()));
    }

    public Uni<List<TunnelSection>> getByTunnelId(Long tunnelId) {
        List<TunnelSection> list = mockDataService.getMockSections().stream()
                .filter(s -> s.getTunnelId().equals(tunnelId))
                .collect(Collectors.toList());
        return Uni.createFrom().item(list);
    }

    public Uni<TunnelSection> getById(Long id) {
        TunnelSection entity = mockDataService.getSection(id);
        if (entity == null) {
            throw new BusinessException(404, "隧道节段不存在");
        }
        return Uni.createFrom().item(entity);
    }

    public Uni<TunnelSection> create(TunnelSection entity) {
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return repository.persist(entity);
    }

    public Uni<TunnelSection> update(Long id, TunnelSection entity) {
        return repository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "隧道节段不存在"))
                .onItem().transform(existing -> {
                    existing.setTunnelId(entity.getTunnelId() != null ? entity.getTunnelId() : existing.getTunnelId());
                    existing.setSectionCode(entity.getSectionCode() != null ? entity.getSectionCode() : existing.getSectionCode());
                    existing.setSectionName(entity.getSectionName() != null ? entity.getSectionName() : existing.getSectionName());
                    existing.setStartMileage(entity.getStartMileage() != null ? entity.getStartMileage() : existing.getStartMileage());
                    existing.setEndMileage(entity.getEndMileage() != null ? entity.getEndMileage() : existing.getEndMileage());
                    existing.setLength(entity.getLength() != null ? entity.getLength() : existing.getLength());
                    existing.setSectionType(entity.getSectionType() != null ? entity.getSectionType() : existing.getSectionType());
                    existing.setDesignThickness(entity.getDesignThickness() != null ? entity.getDesignThickness() : existing.getDesignThickness());
                    existing.setConcreteGrade(entity.getConcreteGrade() != null ? entity.getConcreteGrade() : existing.getConcreteGrade());
                    existing.setReinforcementGrade(entity.getReinforcementGrade() != null ? entity.getReinforcementGrade() : existing.getReinforcementGrade());
                    existing.setSlope(entity.getSlope() != null ? entity.getSlope() : existing.getSlope());
                    existing.setLongitude(entity.getLongitude() != null ? entity.getLongitude() : existing.getLongitude());
                    existing.setLatitude(entity.getLatitude() != null ? entity.getLatitude() : existing.getLatitude());
                    existing.setElevation(entity.getElevation() != null ? entity.getElevation() : existing.getElevation());
                    existing.setStatus(entity.getStatus() != null ? entity.getStatus() : existing.getStatus());
                    existing.setUpdateTime(LocalDateTime.now());
                    existing.setRemark(entity.getRemark() != null ? entity.getRemark() : existing.getRemark());
                    return existing;
                });
    }

    public Uni<Boolean> delete(Long id) {
        return repository.deleteById(id);
    }
}
