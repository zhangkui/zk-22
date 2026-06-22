package com.tunnel.monitor;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusMain
public class TunnelMonitorApplication implements QuarkusApplication {

    private static final Logger log = LoggerFactory.getLogger(TunnelMonitorApplication.class);

    public static void main(String... args) {
        Quarkus.run(TunnelMonitorApplication.class, args);
    }

    @Override
    public int run(String... args) {
        log.info("隧道结构健康监测系统后端服务启动成功");
        log.info("API 文档: http://localhost:8080/swagger-ui");
        log.info("OpenAPI 规范: http://localhost:8080/openapi");
        log.info("健康检查: http://localhost:8080/health");
        log.info("监控指标: http://localhost:8080/q/metrics");
        Quarkus.waitForExit();
        return 0;
    }
}
