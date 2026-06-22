# 隧道结构健康监测系统 - 后端

基于 Quarkus 3.8 的隧道结构健康监测系统后端服务。

## 技术栈

- **框架**: Quarkus 3.8.4
- **Java 版本**: Java 17
- **构建工具**: Maven
- **响应式编程**: Mutiny
- **ORM**: Hibernate Reactive Panache
- **数据库**: PostgreSQL (Reactive)
- **缓存**: Redis
- **消息队列**: Kafka
- **认证**: JWT (SmallRye JWT)
- **API 文档**: OpenAPI / Swagger UI
- **监控**: Micrometer + Prometheus
- **实时通信**: WebSocket

## 核心依赖

- quarkus-resteasy-reactive
- quarkus-resteasy-reactive-jackson
- quarkus-hibernate-reactive-panache
- quarkus-reactive-pg-client
- quarkus-security-jwt
- quarkus-smallrye-jwt
- quarkus-smallrye-openapi
- quarkus-websockets
- quarkus-redis-client
- quarkus-micrometer-registry-prometheus
- quarkus-kafka-client

## 项目结构

```
src/main/java/com/tunnel/monitor/
├── controller/          # REST 控制器
├── service/            # 业务逻辑
├── repository/         # 数据访问层
├── entity/             # 实体类
├── dto/                # 数据传输对象
├── config/             # 配置类
├── security/           # 安全认证
├── util/               # 工具类
└── websocket/          # WebSocket 处理
```

## 核心实体

1. **User** - 用户
2. **TunnelStructure** - 隧道结构
3. **TunnelSection** - 隧道节段
4. **Device** - 监测设备
5. **LeakLocation** - 渗漏位置
6. **AlarmEvent** - 告警事件
7. **InspectionTask** - 巡检任务
8. **LifetimeAssessment** - 寿命评估
9. **RiskHeatmap** - 风险热力图
10. **SystemConfig** - 系统配置

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- PostgreSQL 14+
- Redis 6+
- Kafka 2.8+

### 开发模式运行

```bash
cd backend
mvn quarkus:dev
```

### 打包构建

```bash
# JVM 模式
mvn package

# Native 模式
mvn package -Dnative
```

### 运行

```bash
# JVM 模式
java -jar target/monitor-1.0.0-SNAPSHOT-runner.jar

# Native 模式
./target/monitor-1.0.0-SNAPSHOT-runner
```

## 访问地址

- 应用服务: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui
- OpenAPI: http://localhost:8080/openapi
- 健康检查: http://localhost:8080/health
- 监控指标: http://localhost:8080/q/metrics
- WebSocket: ws://localhost:8080/ws/data/{userId}

## Mock 数据

项目内置了完整的 Mock 数据服务，启动时自动初始化，方便前端测试。

默认测试账号:
- 用户名: `admin1` ~ `admin10`
- 密码: `123456`

## API 接口

### 认证管理
- `POST /api/auth/login` - 用户登录

### 用户管理
- `GET /api/users` - 分页查询用户列表
- `GET /api/users/{id}` - 获取用户详情
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户

### 隧道管理
- `GET /api/tunnels` - 分页查询隧道列表
- `GET /api/tunnels/all` - 获取所有隧道
- `GET /api/tunnels/{id}` - 获取隧道详情
- `POST /api/tunnels` - 创建隧道
- `PUT /api/tunnels/{id}` - 更新隧道
- `DELETE /api/tunnels/{id}` - 删除隧道

### 其他模块

隧道节段、设备、渗漏位置、告警事件、巡检任务、寿命评估、风险热力图、系统配置等模块均提供完整的 CRUD 接口。

## 配置说明

主要配置文件: `src/main/resources/application.properties`

### 数据库配置

```properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=tunnel
quarkus.datasource.password=tunnel123
quarkus.datasource.reactive.url=postgresql://localhost:5432/tunnel_monitor
```

### JWT 配置

```properties
mp.jwt.verify.publickey.location=publicKey.pem
mp.jwt.verify.issuer=https://tunnel-monitor.com
smallrye.jwt.sign.key.location=privateKey.pem
```

### Redis 配置

```properties
quarkus.redis.hosts=redis://localhost:6379
```

### Kafka 配置

```properties
kafka.bootstrap.servers=localhost:9092
```
