package com.tunnel.monitor.config;

import com.tunnel.monitor.dto.Result;
import com.tunnel.monitor.util.BusinessException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        log.error("系统异常:", exception);

        Result<?> result;

        if (exception instanceof BusinessException businessException) {
            result = Result.error(businessException.getCode(), businessException.getMessage());
            return Response.status(Response.Status.OK).entity(result).build();
        }

        if (exception instanceof NotFoundException) {
            result = Result.error(404, "资源不存在");
            return Response.status(Response.Status.NOT_FOUND).entity(result).build();
        }

        if (exception instanceof NotAuthorizedException) {
            result = Result.error(401, "未授权，请先登录");
            return Response.status(Response.Status.UNAUTHORIZED).entity(result).build();
        }

        if (exception instanceof ForbiddenException) {
            result = Result.error(403, "权限不足，禁止访问");
            return Response.status(Response.Status.FORBIDDEN).entity(result).build();
        }

        if (exception instanceof IllegalArgumentException) {
            result = Result.error(400, exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        result = Result.error(500, "服务器内部错误: " + exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
    }
}
