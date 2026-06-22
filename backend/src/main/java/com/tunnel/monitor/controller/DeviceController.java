package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.entity.Device;
import com.tunnel.monitor.service.DeviceService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "设备管理", description = "监测设备信息管理接口")
public class DeviceController {

    @Inject
    DeviceService service;

    @GET
    @Operation(summary = "分页查询设备列表", description = "获取设备分页列表")
    public Uni<Result<PageResult<Device>>> list(
            @Parameter(description = "页码") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "每页大小") @QueryParam("size") @DefaultValue("10") Integer size,
            @Parameter(description = "节段ID") @QueryParam("sectionId") Long sectionId,
            @Parameter(description = "设备状态") @QueryParam("status") String status) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        return service.list(pageRequest, sectionId, status)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/section/{sectionId}")
    @Operation(summary = "获取节段下所有设备", description = "根据节段ID获取所有设备列表")
    public Uni<Result<List<Device>>> getBySectionId(@Parameter(description = "节段ID") @PathParam("sectionId") Long sectionId) {
        return service.getBySectionId(sectionId)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "获取设备详情", description = "根据ID获取设备详细信息")
    public Uni<Result<Device>> getById(@Parameter(description = "设备ID") @PathParam("id") Long id) {
        return service.getById(id)
                .onItem().transform(Result::success);
    }

    @POST
    @Operation(summary = "创建设备", description = "创建新设备信息")
    public Uni<Result<Device>> create(Device entity) {
        return service.create(entity)
                .onItem().transform(Result::success);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "更新设备", description = "根据ID更新设备信息")
    public Uni<Result<Device>> update(@Parameter(description = "设备ID") @PathParam("id") Long id, Device entity) {
        return service.update(id, entity)
                .onItem().transform(Result::success);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "删除设备", description = "根据ID删除设备信息")
    public Uni<Result<Boolean>> delete(@Parameter(description = "设备ID") @PathParam("id") Long id) {
        return service.delete(id)
                .onItem().transform(Result::success);
    }
}
