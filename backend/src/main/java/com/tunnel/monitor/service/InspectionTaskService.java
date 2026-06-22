package com.tunnel.monitor.service;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.entity.InspectionTask;
import com.tunnel.monitor.repository.InspectionTaskRepository;
import com.tunnel.monitor.util.BusinessException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class InspectionTaskService {

    @Inject
    InspectionTaskRepository repository;

    @Inject
    MockDataService mockDataService;

    public Uni<PageResult<InspectionTask>> list(PageRequest pageRequest, Long tunnelId, Long inspectorId, String status) {
        List<InspectionTask> list = mockDataService.getMockTasks();
        if (tunnelId != null) {
            list = list.stream().filter(t -> t.getTunnelId().equals(tunnelId)).collect(Collectors.toList());
        }
        if (inspectorId != null) {
            list = list.stream().filter(t -> inspectorId.equals(t.getInspectorId())).collect(Collectors.toList());
        }
        if (status != null && !status.isEmpty()) {
            list = list.stream().filter(t -> status.equals(t.getStatus())).collect(Collectors.toList());
        }
        int total = list.size();
        int start = pageRequest.getPageIndex() * pageRequest.getSize();
        int end = Math.min(start + pageRequest.getSize(), total);
        List<InspectionTask> records = list.subList(start, end);

        return Uni.createFrom().item(PageResult.of(records, total, pageRequest.getPage(), pageRequest.getSize()));
    }

    public Uni<InspectionTask> getById(Long id) {
        InspectionTask entity = mockDataService.getTask(id);
        if (entity == null) {
            throw new BusinessException(404, "巡检任务不存在");
        }
        return Uni.createFrom().item(entity);
    }

    public Uni<InspectionTask> create(InspectionTask entity) {
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return repository.persist(entity);
    }

    public Uni<InspectionTask> update(Long id, InspectionTask entity) {
        return repository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "巡检任务不存在"))
                .onItem().transform(existing -> {
                    existing.setTunnelId(entity.getTunnelId() != null ? entity.getTunnelId() : existing.getTunnelId());
                    existing.setTaskCode(entity.getTaskCode() != null ? entity.getTaskCode() : existing.getTaskCode());
                    existing.setTaskName(entity.getTaskName() != null ? entity.getTaskName() : existing.getTaskName());
                    existing.setTaskType(entity.getTaskType() != null ? entity.getTaskType() : existing.getTaskType());
                    existing.setPriority(entity.getPriority() != null ? entity.getPriority() : existing.getPriority());
                    existing.setStatus(entity.getStatus() != null ? entity.getStatus() : existing.getStatus());
                    existing.setPlanStartTime(entity.getPlanStartTime() != null ? entity.getPlanStartTime() : existing.getPlanStartTime());
                    existing.setPlanEndTime(entity.getPlanEndTime() != null ? entity.getPlanEndTime() : existing.getPlanEndTime());
                    existing.setActualStartTime(entity.getActualStartTime() != null ? entity.getActualStartTime() : existing.getActualStartTime());
                    existing.setActualEndTime(entity.getActualEndTime() != null ? entity.getActualEndTime() : existing.getActualEndTime());
                    existing.setInspectorId(entity.getInspectorId() != null ? entity.getInspectorId() : existing.getInspectorId());
                    existing.setInspectorName(entity.getInspectorName() != null ? entity.getInspectorName() : existing.getInspectorName());
                    existing.setInspectionScope(entity.getInspectionScope() != null ? entity.getInspectionScope() : existing.getInspectionScope());
                    existing.setInspectionContent(entity.getInspectionContent() != null ? entity.getInspectionContent() : existing.getInspectionContent());
                    existing.setInspectionResult(entity.getInspectionResult() != null ? entity.getInspectionResult() : existing.getInspectionResult());
                    existing.setProblemDescription(entity.getProblemDescription() != null ? entity.getProblemDescription() : existing.getProblemDescription());
                    existing.setSuggestion(entity.getSuggestion() != null ? entity.getSuggestion() : existing.getSuggestion());
                    existing.setUpdateTime(LocalDateTime.now());
                    existing.setRemark(entity.getRemark() != null ? entity.getRemark() : existing.getRemark());
                    return existing;
                });
    }

    public Uni<Boolean> delete(Long id) {
        return repository.deleteById(id);
    }
}
