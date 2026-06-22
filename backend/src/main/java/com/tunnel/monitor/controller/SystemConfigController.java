package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.entity.SystemConfig;
import com.tunnel.monitor.service.SystemConfigService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/configs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "系统配置管理", description = "系统配置管理接口")
public class SystemConfigController {

    @Inject
    SystemConfigService service;

    @GET
    @Operation(summary = "分页查询配置列表", description = "获取配置分页列表")
    public Uni<Result<PageResult<SystemConfig>>> list(
            @Parameter(description = "页码") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "每页大小") @QueryParam("size") @DefaultValue("10") Integer size) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        return service.list(pageRequest)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/all")
    @Operation(summary = "获取所有配置", description = "获取所有系统配置列表")
    public Uni<Result<List<SystemConfig>>> listAll() {
        return service.listAll()
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "获取配置详情", description = "根据ID获取配置详细信息")
    public Uni<Result<SystemConfig>> getById(@Parameter(description = "配置ID") @PathParam("id") Long id) {
        return service.getById(id)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/key/{configKey}")
    @Operation(summary = "根据Key获取配置", description = "根据配置Key获取配置值")
    public Uni<Result<SystemConfig>> getByKey(@Parameter(description = "配置Key") @PathParam("configKey") String configKey) {
        return service.getByKey(configKey)
                .onItem().transform(Result::success);
    }

    @POST
    @Operation(summary = "创建配置", description = "创建新配置")
    public Uni<Result<SystemConfig>> create(SystemConfig entity) {
        return service.create(entity)
                .onItem().transform(Result::success);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "更新配置", description = "根据ID更新配置信息")
    public Uni<Result<SystemConfig>> update(@Parameter(description = "配置ID") @PathParam("id") Long id, SystemConfig entity) {
        return service.update(id, entity)
                .onItem().transform(Result::success);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "删除配置", description = "根据ID删除配置")
    public Uni<Result<Boolean>> delete(@Parameter(description = "配置ID") @PathParam("id") Long id) {
        return service.delete(id)
                .onItem().transform(Result::success);
    }
}
