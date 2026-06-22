package com.tunnel.monitor.service;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.entity.SystemConfig;
import com.tunnel.monitor.repository.SystemConfigRepository;
import com.tunnel.monitor.util.BusinessException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SystemConfigService {

    @Inject
    SystemConfigRepository repository;

    @Inject
    MockDataService mockDataService;

    public Uni<PageResult<SystemConfig>> list(PageRequest pageRequest) {
        List<SystemConfig> list = mockDataService.getMockConfigs();
        int total = list.size();
        int start = pageRequest.getPageIndex() * pageRequest.getSize();
        int end = Math.min(start + pageRequest.getSize(), total);
        List<SystemConfig> records = list.subList(start, end);

        return Uni.createFrom().item(PageResult.of(records, total, pageRequest.getPage(), pageRequest.getSize()));
    }

    public Uni<List<SystemConfig>> listAll() {
        return Uni.createFrom().item(mockDataService.getMockConfigs());
    }

    public Uni<SystemConfig> getById(Long id) {
        SystemConfig entity = mockDataService.getConfig(id);
        if (entity == null) {
            throw new BusinessException(404, "系统配置不存在");
        }
        return Uni.createFrom().item(entity);
    }

    public Uni<SystemConfig> getByKey(String configKey) {
        return repository.findByConfigKey(configKey)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "系统配置不存在"));
    }

    public Uni<SystemConfig> create(SystemConfig entity) {
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return repository.persist(entity);
    }

    public Uni<SystemConfig> update(Long id, SystemConfig entity) {
        return repository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "系统配置不存在"))
                .onItem().transform(existing -> {
                    existing.setConfigKey(entity.getConfigKey() != null ? entity.getConfigKey() : existing.getConfigKey());
                    existing.setConfigValue(entity.getConfigValue() != null ? entity.getConfigValue() : existing.getConfigValue());
                    existing.setConfigName(entity.getConfigName() != null ? entity.getConfigName() : existing.getConfigName());
                    existing.setConfigType(entity.getConfigType() != null ? entity.getConfigType() : existing.getConfigType());
                    existing.setDescription(entity.getDescription() != null ? entity.getDescription() : existing.getDescription());
                    existing.setEnabled(entity.getEnabled() != null ? entity.getEnabled() : existing.getEnabled());
                    existing.setUpdateTime(LocalDateTime.now());
                    existing.setRemark(entity.getRemark() != null ? entity.getRemark() : existing.getRemark());
                    return existing;
                });
    }

    public Uni<Boolean> delete(Long id) {
        return repository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "系统配置不存在"))
                .chain(existing -> {
                    if (existing.getIsSystem()) {
                        throw new BusinessException(400, "系统配置不允许删除");
                    }
                    return repository.deleteById(id);
                });
    }
}
