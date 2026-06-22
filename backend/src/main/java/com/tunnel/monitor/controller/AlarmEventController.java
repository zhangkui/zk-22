package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.entity.AlarmEvent;
import com.tunnel.monitor.service.AlarmEventService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/api/alarms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "告警管理", description = "告警事件管理接口")
public class AlarmEventController {

    @Inject
    AlarmEventService service;

    @GET
    @Operation(summary = "分页查询告警列表", description = "获取告警分页列表")
    public Uni<Result<PageResult<AlarmEvent>>> list(
            @Parameter(description = "页码") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "每页大小") @QueryParam("size") @DefaultValue("10") Integer size,
            @Parameter(description = "设备ID") @QueryParam("deviceId") Long deviceId,
            @Parameter(description = "告警级别") @QueryParam("alarmLevel") String alarmLevel,
            @Parameter(description = "状态") @QueryParam("status") String status) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        return service.list(pageRequest, deviceId, alarmLevel, status)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/active")
    @Operation(summary = "获取活跃告警", description = "获取所有未处理的活跃告警")
    public Uni<Result<List<AlarmEvent>>> getActiveAlarms() {
        return service.getActiveAlarms()
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "获取告警详情", description = "根据ID获取告警详细信息")
    public Uni<Result<AlarmEvent>> getById(@Parameter(description = "告警ID") @PathParam("id") Long id) {
        return service.getById(id)
                .onItem().transform(Result::success);
    }

    @POST
    @Operation(summary = "创建告警", description = "创建新告警信息")
    public Uni<Result<AlarmEvent>> create(AlarmEvent entity) {
        return service.create(entity)
                .onItem().transform(Result::success);
    }

    @POST
    @Path("/{id}/acknowledge")
    @Operation(summary = "确认告警", description = "确认收到告警信息")
    public Uni<Result<AlarmEvent>> acknowledge(
            @Parameter(description = "告警ID") @PathParam("id") Long id,
            Map<String, Object> params) {
        Long userId = params.get("userId") != null ? Long.valueOf(params.get("userId").toString()) : null;
        String userName = params.get("userName") != null ? params.get("userName").toString() : null;
        return service.acknowledge(id, userId, userName)
                .onItem().transform(Result::success);
    }

    @POST
    @Path("/{id}/resolve")
    @Operation(summary = "处理告警", description = "标记告警已处理")
    public Uni<Result<AlarmEvent>> resolve(
            @Parameter(description = "告警ID") @PathParam("id") Long id,
            Map<String, Object> params) {
        Long userId = params.get("userId") != null ? Long.valueOf(params.get("userId").toString()) : null;
        String userName = params.get("userName") != null ? params.get("userName").toString() : null;
        String resolveMethod = params.get("resolveMethod") != null ? params.get("resolveMethod").toString() : null;
        return service.resolve(id, userId, userName, resolveMethod)
                .onItem().transform(Result::success);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "删除告警", description = "根据ID删除告警信息")
    public Uni<Result<Boolean>> delete(@Parameter(description = "告警ID") @PathParam("id") Long id) {
        return service.delete(id)
                .onItem().transform(Result::success);
    }
}
