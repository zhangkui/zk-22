package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.entity.RiskHeatmap;
import com.tunnel.monitor.service.RiskHeatmapService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/heatmaps")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "风险热力图管理", description = "风险热力图管理接口")
public class RiskHeatmapController {

    @Inject
    RiskHeatmapService service;

    @GET
    @Operation(summary = "分页查询热力图列表", description = "获取热力图分页列表")
    public Uni<Result<PageResult<RiskHeatmap>>> list(
            @Parameter(description = "页码") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "每页大小") @QueryParam("size") @DefaultValue("10") Integer size,
            @Parameter(description = "隧道ID") @QueryParam("tunnelId") Long tunnelId,
            @Parameter(description = "节段ID") @QueryParam("sectionId") Long sectionId,
            @Parameter(description = "风险等级") @QueryParam("riskLevel") String riskLevel,
            @Parameter(description = "风险类型") @QueryParam("riskType") String riskType) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        return service.list(pageRequest, tunnelId, sectionId, riskLevel, riskType)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/tunnel/{tunnelId}")
    @Operation(summary = "获取隧道下所有热力图", description = "根据隧道ID获取所有热力图列表")
    public Uni<Result<List<RiskHeatmap>>> getByTunnelId(@Parameter(description = "隧道ID") @PathParam("tunnelId") Long tunnelId) {
        return service.getByTunnelId(tunnelId)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "获取热力图详情", description = "根据ID获取热力图详细信息")
    public Uni<Result<RiskHeatmap>> getById(@Parameter(description = "热力图ID") @PathParam("id") Long id) {
        return service.getById(id)
                .onItem().transform(Result::success);
    }

    @POST
    @Operation(summary = "创建热力图", description = "创建新热力图")
    public Uni<Result<RiskHeatmap>> create(RiskHeatmap entity) {
        return service.create(entity)
                .onItem().transform(Result::success);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "更新热力图", description = "根据ID更新热力图信息")
    public Uni<Result<RiskHeatmap>> update(@Parameter(description = "热力图ID") @PathParam("id") Long id, RiskHeatmap entity) {
        return service.update(id, entity)
                .onItem().transform(Result::success);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "删除热力图", description = "根据ID删除热力图")
    public Uni<Result<Boolean>> delete(@Parameter(description = "热力图ID") @PathParam("id") Long id) {
        return service.delete(id)
                .onItem().transform(Result::success);
    }
}
