package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.entity.TunnelStructure;
import com.tunnel.monitor.service.TunnelStructureService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/tunnels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "隧道管理", description = "隧道结构信息管理接口")
public class TunnelStructureController {

    @Inject
    TunnelStructureService service;

    @GET
    @Operation(summary = "分页查询隧道列表", description = "获取隧道分页列表")
    public Uni<Result<PageResult<TunnelStructure>>> list(
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
    @Operation(summary = "获取所有隧道", description = "获取所有隧道列表")
    public Uni<Result<List<TunnelStructure>>> listAll() {
        return service.listAll()
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "获取隧道详情", description = "根据ID获取隧道详细信息")
    public Uni<Result<TunnelStructure>> getById(@Parameter(description = "隧道ID") @PathParam("id") Long id) {
        return service.getById(id)
                .onItem().transform(Result::success);
    }

    @POST
    @Operation(summary = "创建隧道", description = "创建新隧道信息")
    public Uni<Result<TunnelStructure>> create(TunnelStructure entity) {
        return service.create(entity)
                .onItem().transform(Result::success);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "更新隧道", description = "根据ID更新隧道信息")
    public Uni<Result<TunnelStructure>> update(@Parameter(description = "隧道ID") @PathParam("id") Long id, TunnelStructure entity) {
        return service.update(id, entity)
                .onItem().transform(Result::success);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "删除隧道", description = "根据ID删除隧道信息")
    public Uni<Result<Boolean>> delete(@Parameter(description = "隧道ID") @PathParam("id") Long id) {
        return service.delete(id)
                .onItem().transform(Result::success);
    }
}
