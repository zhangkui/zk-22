package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.entity.InspectionTask;
import com.tunnel.monitor.service.InspectionTaskService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "巡检任务管理", description = "巡检任务管理接口")
public class InspectionTaskController {

    @Inject
    InspectionTaskService service;

    @GET
    @Operation(summary = "分页查询任务列表", description = "获取任务分页列表")
    public Uni<Result<PageResult<InspectionTask>>> list(
            @Parameter(description = "页码") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "每页大小") @QueryParam("size") @DefaultValue("10") Integer size,
            @Parameter(description = "隧道ID") @QueryParam("tunnelId") Long tunnelId,
            @Parameter(description = "巡检人员ID") @QueryParam("inspectorId") Long inspectorId,
            @Parameter(description = "状态") @QueryParam("status") String status) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        return service.list(pageRequest, tunnelId, inspectorId, status)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "获取任务详情", description = "根据ID获取任务详细信息")
    public Uni<Result<InspectionTask>> getById(@Parameter(description = "任务ID") @PathParam("id") Long id) {
        return service.getById(id)
                .onItem().transform(Result::success);
    }

    @POST
    @Operation(summary = "创建任务", description = "创建新任务")
    public Uni<Result<InspectionTask>> create(InspectionTask entity) {
        return service.create(entity)
                .onItem().transform(Result::success);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "更新任务", description = "根据ID更新任务信息")
    public Uni<Result<InspectionTask>> update(@Parameter(description = "任务ID") @PathParam("id") Long id, InspectionTask entity) {
        return service.update(id, entity)
                .onItem().transform(Result::success);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "删除任务", description = "根据ID删除任务")
    public Uni<Result<Boolean>> delete(@Parameter(description = "任务ID") @PathParam("id") Long id) {
        return service.delete(id)
                .onItem().transform(Result::success);
    }
}
