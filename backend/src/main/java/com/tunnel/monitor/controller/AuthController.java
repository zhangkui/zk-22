package com.tunnel.monitor.controller;

import com.tunnel.monitor.dto.LoginRequest;
import com.tunnel.monitor.dto.LoginResponse;
import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.service.UserService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "认证管理", description = "用户登录认证接口")
public class AuthController {

    @Inject
    UserService userService;

    @POST
    @Path("/login")
    @Operation(summary = "用户登录", description = "使用用户名密码登录获取Token")
    public Uni<Result<LoginResponse>> login(LoginRequest request) {
        return userService.login(request)
                .onItem().transform(Result::success);
    }
}
