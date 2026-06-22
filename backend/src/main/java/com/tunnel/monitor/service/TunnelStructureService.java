package com.tunnel.monitor.service;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.entity.TunnelStructure;
import com.tunnel.monitor.repository.TunnelStructureRepository;
import com.tunnel.monitor.util.BusinessException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class TunnelStructureService {

    @Inject
    TunnelStructureRepository repository;

    @Inject
    MockDataService mockDataService;

    public Uni<PageResult<TunnelStructure>> list(PageRequest pageRequest) {
        List<TunnelStructure> list = mockDataService.getMockTunnels();
        int total = list.size();
        int start = pageRequest.getPageIndex() * pageRequest.getSize();
        int end = Math.min(start + pageRequest.getSize(), total);
        List<TunnelStructure> records = list.subList(start, end);

        return Uni.createFrom().item(PageResult.of(records, total, pageRequest.getPage(), pageRequest.getSize()));
    }

    public Uni<List<TunnelStructure>> listAll() {
        return Uni.createFrom().item(mockDataService.getMockTunnels());
    }

    public Uni<TunnelStructure> getById(Long id) {
        TunnelStructure entity = mockDataService.getTunnel(id);
        if (entity == null) {
            throw new BusinessException(404, "隧道信息不存在");
        }
        return Uni.createFrom().item(entity);
    }

    public Uni<TunnelStructure> create(TunnelStructure entity) {
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return repository.persist(entity);
    }

    public Uni<TunnelStructure> update(Long id, TunnelStructure entity) {
        return repository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "隧道信息不存在"))
                .onItem().transform(existing -> {
                    existing.setTunnelCode(entity.getTunnelCode() != null ? entity.getTunnelCode() : existing.getTunnelCode());
                    existing.setTunnelName(entity.getTunnelName() != null ? entity.getTunnelName() : existing.getTunnelName());
                    existing.setLocation(entity.getLocation() != null ? entity.getLocation() : existing.getLocation());
                    existing.setTotalLength(entity.getTotalLength() != null ? entity.getTotalLength() : existing.getTotalLength());
                    existing.setWidth(entity.getWidth() != null ? entity.getWidth() : existing.getWidth());
                    existing.setHeight(entity.getHeight() != null ? entity.getHeight() : existing.getHeight());
                    existing.setStructureType(entity.getStructureType() != null ? entity.getStructureType() : existing.getStructureType());
                    existing.setConstructionMethod(entity.getConstructionMethod() != null ? entity.getConstructionMethod() : existing.getConstructionMethod());
                    existing.setBuildDate(entity.getBuildDate() != null ? entity.getBuildDate() : existing.getBuildDate());
                    existing.setOpenDate(entity.getOpenDate() != null ? entity.getOpenDate() : existing.getOpenDate());
                    existing.setDesignUnit(entity.getDesignUnit() != null ? entity.getDesignUnit() : existing.getDesignUnit());
                    existing.setConstructionUnit(entity.getConstructionUnit() != null ? entity.getConstructionUnit() : existing.getConstructionUnit());
                    existing.setSupervisionUnit(entity.getSupervisionUnit() != null ? entity.getSupervisionUnit() : existing.getSupervisionUnit());
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
