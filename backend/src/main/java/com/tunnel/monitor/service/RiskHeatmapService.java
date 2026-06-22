package com.tunnel.monitor.service;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.entity.RiskHeatmap;
import com.tunnel.monitor.repository.RiskHeatmapRepository;
import com.tunnel.monitor.util.BusinessException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RiskHeatmapService {

    @Inject
    RiskHeatmapRepository repository;

    @Inject
    MockDataService mockDataService;

    public Uni<PageResult<RiskHeatmap>> list(PageRequest pageRequest, Long tunnelId, Long sectionId, String riskLevel, String riskType) {
        List<RiskHeatmap> list = mockDataService.getMockHeatmaps();
        if (tunnelId != null) {
            list = list.stream().filter(h -> h.getTunnelId().equals(tunnelId)).collect(Collectors.toList());
        }
        if (sectionId != null) {
            list = list.stream().filter(h -> h.getSectionId().equals(sectionId)).collect(Collectors.toList());
        }
        if (riskLevel != null && !riskLevel.isEmpty()) {
            list = list.stream().filter(h -> riskLevel.equals(h.getRiskLevel())).collect(Collectors.toList());
        }
        if (riskType != null && !riskType.isEmpty()) {
            list = list.stream().filter(h -> riskType.equals(h.getRiskType())).collect(Collectors.toList());
        }
        int total = list.size();
        int start = pageRequest.getPageIndex() * pageRequest.getSize();
        int end = Math.min(start + pageRequest.getSize(), total);
        List<RiskHeatmap> records = list.subList(start, end);

        return Uni.createFrom().item(PageResult.of(records, total, pageRequest.getPage(), pageRequest.getSize()));
    }

    public Uni<List<RiskHeatmap>> getByTunnelId(Long tunnelId) {
        List<RiskHeatmap> list = mockDataService.getMockHeatmaps().stream()
                .filter(h -> h.getTunnelId().equals(tunnelId))
                .collect(Collectors.toList());
        return Uni.createFrom().item(list);
    }

    public Uni<RiskHeatmap> getById(Long id) {
        RiskHeatmap entity = mockDataService.getHeatmap(id);
        if (entity == null) {
            throw new BusinessException(404, "风险热力图不存在");
        }
        return Uni.createFrom().item(entity);
    }

    public Uni<RiskHeatmap> create(RiskHeatmap entity) {
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return repository.persist(entity);
    }

    public Uni<RiskHeatmap> update(Long id, RiskHeatmap entity) {
        return repository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "风险热力图不存在"))
                .onItem().transform(existing -> {
                    existing.setTunnelId(entity.getTunnelId() != null ? entity.getTunnelId() : existing.getTunnelId());
                    existing.setSectionId(entity.getSectionId() != null ? entity.getSectionId() : existing.getSectionId());
                    existing.setHeatmapCode(entity.getHeatmapCode() != null ? entity.getHeatmapCode() : existing.getHeatmapCode());
                    existing.setHeatmapName(entity.getHeatmapName() != null ? entity.getHeatmapName() : existing.getHeatmapName());
                    existing.setRiskType(entity.getRiskType() != null ? entity.getRiskType() : existing.getRiskType());
                    existing.setStartMileage(entity.getStartMileage() != null ? entity.getStartMileage() : existing.getStartMileage());
                    existing.setEndMileage(entity.getEndMileage() != null ? entity.getEndMileage() : existing.getEndMileage());
                    existing.setPositionX(entity.getPositionX() != null ? entity.getPositionX() : existing.getPositionX());
                    existing.setPositionY(entity.getPositionY() != null ? entity.getPositionY() : existing.getPositionY());
                    existing.setPositionZ(entity.getPositionZ() != null ? entity.getPositionZ() : existing.getPositionZ());
                    existing.setRiskValue(entity.getRiskValue() != null ? entity.getRiskValue() : existing.getRiskValue());
                    existing.setRiskLevel(entity.getRiskLevel() != null ? entity.getRiskLevel() : existing.getRiskLevel());
                    existing.setColorCode(entity.getColorCode() != null ? entity.getColorCode() : existing.getColorCode());
                    existing.setStatisticTime(entity.getStatisticTime() != null ? entity.getStatisticTime() : existing.getStatisticTime());
                    existing.setOccurrenceProbability(entity.getOccurrenceProbability() != null ? entity.getOccurrenceProbability() : existing.getOccurrenceProbability());
                    existing.setConsequenceSeverity(entity.getConsequenceSeverity() != null ? entity.getConsequenceSeverity() : existing.getConsequenceSeverity());
                    existing.setRiskDescription(entity.getRiskDescription() != null ? entity.getRiskDescription() : existing.getRiskDescription());
                    existing.setMitigationMeasures(entity.getMitigationMeasures() != null ? entity.getMitigationMeasures() : existing.getMitigationMeasures());
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
