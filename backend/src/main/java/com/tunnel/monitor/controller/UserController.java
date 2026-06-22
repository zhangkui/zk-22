package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.entity.User;
import com.tunnel.monitor.service.UserService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "用户管理", description = "用户信息管理接口")
public class UserController {

    @Inject
    UserService userService;

    @GET
    @Operation(summary = "分页查询用户列表", description = "获取用户分页列表")
    public Uni<Result<PageResult<User>>> list(
            @Parameter(description = "页码") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "每页大小") @QueryParam("size") @DefaultValue("10") Integer size) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        return userService.list(pageRequest)
                .onItem().transform(Result::success);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详细信息")
    public Uni<Result<User>> getById(@Parameter(description = "用户ID") @PathParam("id") Long id) {
        return userService.getById(id)
                .onItem().transform(Result::success);
    }

    @POST
    @Operation(summary = "创建用户", description = "创建新用户")
    public Uni<Result<User>> create(User user) {
        return userService.create(user)
                .onItem().transform(Result::success);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "更新用户", description = "根据ID更新用户信息")
    public Uni<Result<User>> update(@Parameter(description = "用户ID") @PathParam("id") Long id, User user) {
        return userService.update(id, user)
                .onItem().transform(Result::success);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    public Uni<Result<Boolean>> delete(@Parameter(description = "用户ID") @PathParam("id") Long id) {
        return userService.delete(id)
                .onItem().transform(Result::success);
    }
}
