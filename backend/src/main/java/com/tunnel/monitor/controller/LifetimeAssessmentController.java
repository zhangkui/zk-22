package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.entity.LifetimeAssessment;
import com.tunnel.monitor.service.LifetimeAssessmentService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/assessments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "寿命评估管理", description = "结构寿命评估管理接口")
public class LifetimeAssessmentController {

    @Inject
    LifetimeAssessmentService service;

    @GET
    @Operation(summary = "分页查询评估列表", description = "获取评估分页列表")
    public Uni<Result<PageResult<LifetimeAssessment>>> list(
            @Parameter(description = "页码") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "每页大小") @QueryParam("size") @DefaultValue("10") Integer size,
            @Parameter(description = "节段ID") @QueryParam("sectionId") Long sectionId,
            @Parameter(description = "健康等级") @QueryParam("healthLevel") String healthLevel) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        return service.list(pageRequest, sectionId, healthLevel)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/section/{sectionId}")
    @Operation(summary = "获取节段下所有评估", description = "根据节段ID获取所有评估列表")
    public Uni<Result<List<LifetimeAssessment>>> getBySectionId(@Parameter(description = "节段ID") @PathParam("sectionId") Long sectionId) {
        return service.getBySectionId(sectionId)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "获取评估详情", description = "根据ID获取评估详细信息")
    public Uni<Result<LifetimeAssessment>> getById(@Parameter(description = "评估ID") @PathParam("id") Long id) {
        return service.getById(id)
                .onItem().transform(Result::success);
    }

    @POST
    @Operation(summary = "创建评估", description = "创建新评估")
    public Uni<Result<LifetimeAssessment>> create(LifetimeAssessment entity) {
        return service.create(entity)
                .onItem().transform(Result::success);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "更新评估", description = "根据ID更新评估信息")
    public Uni<Result<LifetimeAssessment>> update(@Parameter(description = "评估ID") @PathParam("id") Long id, LifetimeAssessment entity) {
        return service.update(id, entity)
                .onItem().transform(Result::success);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "删除评估", description = "根据ID删除评估")
    public Uni<Result<Boolean>> delete(@Parameter(description = "评估ID") @PathParam("id") Long id) {
        return service.delete(id)
                .onItem().transform(Result::success);
    }
}
