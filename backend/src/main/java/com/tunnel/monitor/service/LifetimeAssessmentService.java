package com.tunnel.monitor.service;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.entity.LifetimeAssessment;
import com.tunnel.monitor.repository.LifetimeAssessmentRepository;
import com.tunnel.monitor.util.BusinessException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LifetimeAssessmentService {

    @Inject
    LifetimeAssessmentRepository repository;

    @Inject
    MockDataService mockDataService;

    public Uni<PageResult<LifetimeAssessment>> list(PageRequest pageRequest, Long sectionId, String healthLevel) {
        List<LifetimeAssessment> list = mockDataService.getMockAssessments();
        if (sectionId != null) {
            list = list.stream().filter(a -> a.getSectionId().equals(sectionId)).collect(Collectors.toList());
        }
        if (healthLevel != null && !healthLevel.isEmpty()) {
            list = list.stream().filter(a -> healthLevel.equals(a.getHealthLevel())).collect(Collectors.toList());
        }
        int total = list.size();
        int start = pageRequest.getPageIndex() * pageRequest.getSize();
        int end = Math.min(start + pageRequest.getSize(), total);
        List<LifetimeAssessment> records = list.subList(start, end);

        return Uni.createFrom().item(PageResult.of(records, total, pageRequest.getPage(), pageRequest.getSize()));
    }

    public Uni<List<LifetimeAssessment>> getBySectionId(Long sectionId) {
        List<LifetimeAssessment> list = mockDataService.getMockAssessments().stream()
                .filter(a -> a.getSectionId().equals(sectionId))
                .collect(Collectors.toList());
        return Uni.createFrom().item(list);
    }

    public Uni<LifetimeAssessment> getById(Long id) {
        LifetimeAssessment entity = mockDataService.getAssessment(id);
        if (entity == null) {
            throw new BusinessException(404, "寿命评估不存在");
        }
        return Uni.createFrom().item(entity);
    }

    public Uni<LifetimeAssessment> create(LifetimeAssessment entity) {
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return repository.persist(entity);
    }

    public Uni<LifetimeAssessment> update(Long id, LifetimeAssessment entity) {
        return repository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "寿命评估不存在"))
                .onItem().transform(existing -> {
                    existing.setSectionId(entity.getSectionId() != null ? entity.getSectionId() : existing.getSectionId());
                    existing.setAssessmentCode(entity.getAssessmentCode() != null ? entity.getAssessmentCode() : existing.getAssessmentCode());
                    existing.setAssessmentName(entity.getAssessmentName() != null ? entity.getAssessmentName() : existing.getAssessmentName());
                    existing.setAssessmentType(entity.getAssessmentType() != null ? entity.getAssessmentType() : existing.getAssessmentType());
                    existing.setAssessmentDate(entity.getAssessmentDate() != null ? entity.getAssessmentDate() : existing.getAssessmentDate());
                    existing.setDesignLife(entity.getDesignLife() != null ? entity.getDesignLife() : existing.getDesignLife());
                    existing.setUsedLife(entity.getUsedLife() != null ? entity.getUsedLife() : existing.getUsedLife());
                    existing.setRemainingLife(entity.getRemainingLife() != null ? entity.getRemainingLife() : existing.getRemainingLife());
                    existing.setPredictedRemainingLife(entity.getPredictedRemainingLife() != null ? entity.getPredictedRemainingLife() : existing.getPredictedRemainingLife());
                    existing.setHealthLevel(entity.getHealthLevel() != null ? entity.getHealthLevel() : existing.getHealthLevel());
                    existing.setHealthScore(entity.getHealthScore() != null ? entity.getHealthScore() : existing.getHealthScore());
                    existing.setDeteriorationRate(entity.getDeteriorationRate() != null ? entity.getDeteriorationRate() : existing.getDeteriorationRate());
                    existing.setMainDeteriorationType(entity.getMainDeteriorationType() != null ? entity.getMainDeteriorationType() : existing.getMainDeteriorationType());
                    existing.setDeteriorationDescription(entity.getDeteriorationDescription() != null ? entity.getDeteriorationDescription() : existing.getDeteriorationDescription());
                    existing.setRiskFactors(entity.getRiskFactors() != null ? entity.getRiskFactors() : existing.getRiskFactors());
                    existing.setMaintenanceSuggestions(entity.getMaintenanceSuggestions() != null ? entity.getMaintenanceSuggestions() : existing.getMaintenanceSuggestions());
                    existing.setAssessmentConclusion(entity.getAssessmentConclusion() != null ? entity.getAssessmentConclusion() : existing.getAssessmentConclusion());
                    existing.setAssessorId(entity.getAssessorId() != null ? entity.getAssessorId() : existing.getAssessorId());
                    existing.setAssessorName(entity.getAssessorName() != null ? entity.getAssessorName() : existing.getAssessorName());
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
