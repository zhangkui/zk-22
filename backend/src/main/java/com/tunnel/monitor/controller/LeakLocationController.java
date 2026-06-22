package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.entity.LeakLocation;
import com.tunnel.monitor.service.LeakLocationService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/leaks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "渗漏位置管理", description = "渗漏位置信息管理接口")
public class LeakLocationController {

    @Inject
    LeakLocationService service;

    @GET
    @Operation(summary = "分页查询渗漏列表", description = "获取渗漏分页列表")
    public Uni<Result<PageResult<LeakLocation>>> list(
            @Parameter(description = "页码") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "每页大小") @QueryParam("size") @DefaultValue("10") Integer size,
            @Parameter(description = "节段ID") @QueryParam("sectionId") Long sectionId,
            @Parameter(description = "严重程度") @QueryParam("severity") String severity,
            @Parameter(description = "状态") @QueryParam("status") String status) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        return service.list(pageRequest, sectionId, severity, status)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/section/{sectionId}")
    @Operation(summary = "获取节段下所有渗漏", description = "根据节段ID获取所有渗漏列表")
    public Uni<Result<List<LeakLocation>>> getBySectionId(@Parameter(description = "节段ID") @PathParam("sectionId") Long sectionId) {
        return service.getBySectionId(sectionId)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "获取渗漏详情", description = "根据ID获取渗漏详细信息")
    public Uni<Result<LeakLocation>> getById(@Parameter(description = "渗漏ID") @PathParam("id") Long id) {
        return service.getById(id)
                .onItem().transform(Result::success);
    }

    @POST
    @Operation(summary = "创建渗漏", description = "创建新渗漏信息")
    public Uni<Result<LeakLocation>> create(LeakLocation entity) {
        return service.create(entity)
                .onItem().transform(Result::success);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "更新渗漏", description = "根据ID更新渗漏信息")
    public Uni<Result<LeakLocation>> update(@Parameter(description = "渗漏ID") @PathParam("id") Long id, LeakLocation entity) {
        return service.update(id, entity)
                .onItem().transform(Result::success);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "删除渗漏", description = "根据ID删除渗漏信息")
    public Uni<Result<Boolean>> delete(@Parameter(description = "渗漏ID") @PathParam("id") Long id) {
        return service.delete(id)
                .onItem().transform(Result::success);
    }
}
