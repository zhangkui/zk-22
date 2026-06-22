package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.entity.TunnelSection;
import com.tunnel.monitor.service.TunnelSectionService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/sections")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "隧道节段管理", description = "隧道节段信息管理接口")
public class TunnelSectionController {

    @Inject
    TunnelSectionService service;

    @GET
    @Operation(summary = "分页查询节段列表", description = "获取节段分页列表")
    public Uni<Result<PageResult<TunnelSection>>> list(
            @Parameter(description = "页码") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "每页大小") @QueryParam("size") @DefaultValue("10") Integer size,
            @Parameter(description = "隧道ID") @QueryParam("tunnelId") Long tunnelId) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        return service.list(pageRequest, tunnelId)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/tunnel/{tunnelId}")
    @Operation(summary = "获取隧道下所有节段", description = "根据隧道ID获取所有节段列表")
    public Uni<Result<List<TunnelSection>>> getByTunnelId(@Parameter(description = "隧道ID") @PathParam("tunnelId") Long tunnelId) {
        return service.getByTunnelId(tunnelId)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "获取节段详情", description = "根据ID获取节段详细信息")
    public Uni<Result<TunnelSection>> getById(@Parameter(description = "节段ID") @PathParam("id") Long id) {
        return service.getById(id)
                .onItem().transform(Result::success);
    }

    @POST
    @Operation(summary = "创建节段", description = "创建新节段信息")
    public Uni<Result<TunnelSection>> create(TunnelSection entity) {
        return service.create(entity)
                .onItem().transform(Result::success);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "更新节段", description = "根据ID更新节段信息")
    public Uni<Result<TunnelSection>> update(@Parameter(description = "节段ID") @PathParam("id") Long id, TunnelSection entity) {
        return service.update(id, entity)
                .onItem().transform(Result::success);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "删除节段", description = "根据ID删除节段信息")
    public Uni<Result<Boolean>> delete(@Parameter(description = "节段ID") @PathParam("id") Long id) {
        return service.delete(id)
                .onItem().transform(Result::success);
    }
}
