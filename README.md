# 地下综合管廊渗漏声学定位与巡检推演平台

## 项目概述

本项目是一套针对城市地下综合管廊运维管理的智能化系统，通过声学传感技术实现渗漏点的精准定位，结合巡检机器人路径规划与WebGIS可视化，为管廊运维提供全方位的安全监测与决策支持。

### 核心价值

- **提升运维效率**：自动化巡检 + 智能分析，提升运维效率 300%
- **降低事故风险**：实时监测 + 精准定位，降低渗漏事故风险 80%
- **延长设备寿命**：健康度评估 + 预测性维护，延长设备使用寿命 40%
- **智能决策支持**：多维度数据分析 + 风险预警，辅助科学决策

---

## 技术架构

### 技术栈

| 层级 | 技术选型 |
|------|----------|
| **前端** | Vue 3 + TypeScript + Vite 5 + Pinia + Element Plus + ECharts + Three.js + Cesium |
| **后端** | Java 17 + Quarkus 3.8 + Hibernate Reactive + Mutiny + JAX-RS |
| **数据库** | PostgreSQL 16 (关系型) + TDengine 3.0 (时序数据) |
| **中间件** | Apache Kafka 3.6 + Redis 7.0 + Elasticsearch 8.12 |
| **部署** | Docker + Docker Compose + Nginx |
| **协议** | RESTful + WebSocket + MQTT |

### 系统架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                        前端展示层 (Vue 3)                       │
│  ┌─────────┬─────────┬─────────┬─────────┬─────────┬─────────┐  │
│  │ WebGIS  │ 实时看板 │ 告警管理 │ 巡检调度 │ 设备管理 │ 风险分析 │  │
│  └─────────┴─────────┴─────────┴─────────┴─────────┴─────────┘  │
└───────────────────────────────┬─────────────────────────────────┘
                                │ HTTP/WebSocket
┌───────────────────────────────▼─────────────────────────────────┐
│                       网关层 (Quarkus)                          │
│  ┌─────────┬─────────┬─────────┬─────────┐                     │
│  │ API网关 │ WebSocket│  JWT认证 │ 限流熔断 │                     │
│  └─────────┴─────────┴─────────┴─────────┘                     │
└───────────────────────────────┬─────────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────────┐
│                    业务服务层 (Quarkus 微服务)                   │
│  ┌─────────┬─────────┬─────────┬─────────┬─────────┬─────────┐  │
│  │ 管廊建模 │ 数据接入 │ 渗漏定位 │ 巡检规划 │ 告警管理 │ 设备评估 │  │
│  └─────────┴─────────┴─────────┴─────────┴─────────┴─────────┘  │
│  ┌─────────┐                                                     │
│  │ 风险分析 │                                                     │
│  └─────────┘                                                     │
└─────────┬───────────────────────────┬───────────────────────────┘
          │                           │
┌─────────▼─────────┐     ┌───────────▼────────────┐
│   Kafka 消息队列   │     │   Redis 缓存            │
│  实时数据/告警/指令 │     │   热点数据/会话        │
└─────────┬─────────┘     └────────────────────────┘
          │
┌─────────▼───────────────────────────────────────────────────────┐
│                        数据存储层                                 │
│  ┌──────────────┬──────────────┬──────────────────────────────┐ │
│  │ PostgreSQL   │  TDengine    │   Elasticsearch              │ │
│  │ 关系型数据    │  时序数据    │   全文检索/日志              │ │
│  └──────────────┴──────────────┴──────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

---

## 功能模块

### 1. 管廊结构建模
- 管廊三维结构 WebGIS 可视化
- 设备点位标注与管理
- 管廊分段管理与属性编辑
- 支持 GeoJSON 格式导入导出

### 2. 声学传感数据接入
- 多通道声学传感器实时数据流接入
- 数据质量校验与异常过滤
- 波形图实时展示与频谱分析
- 历史数据查询与导出

### 3. 渗漏点定位
- AI 渗漏特征信号识别
- TDOA 时差定位算法
- 定位精度可视化展示
- 渗漏点状态管理（待确认/已确认/已解决）

### 4. 巡检机器人路径规划
- 基于管廊结构自动生成最优路径
- 避障点标记与路径调整
- 巡检任务创建与调度
- 路径仿真与预览

### 5. 告警事件回放
- 多维度告警事件记录
- 时间轴同步回放（数据+视频+位置）
- 播放速度调节与跳转
- 事件关联数据追溯

### 6. 设备寿命评估
- 设备健康度多维度评分
- 剩余寿命预测曲线
- 维护建议自动生成
- 维护计划管理

### 7. 高风险区段热力分析
- 管廊风险热力图可视化
- 多因子风险评估模型
- 风险等级动态划分
- 预警阈值设置与趋势分析

---

## 项目结构

```
zk-22/
├── backend/                    # 后端项目 (Java Quarkus)
│   ├── src/main/java/com/tunnel/monitor/
│   │   ├── controller/         # REST 控制器
│   │   ├── service/            # 业务逻辑层
│   │   ├── repository/         # 数据访问层
│   │   ├── entity/             # 实体类
│   │   ├── dto/                # 数据传输对象
│   │   ├── config/             # 配置类
│   │   ├── security/           # 安全认证
│   │   ├── util/               # 工具类
│   │   └── websocket/          # WebSocket 处理
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── publicKey.pem
│   │   └── privateKey.pem
│   ├── src/main/docker/
│   │   ├── Dockerfile.jvm
│   │   └── Dockerfile.native
│   └── pom.xml
│
├── frontend/                   # 前端项目 (Vue 3)
│   ├── src/
│   │   ├── api/                # API 接口定义
│   │   ├── assets/             # 静态资源
│   │   │   └── styles/         # 样式文件
│   │   ├── components/         # 公共组件
│   │   ├── composables/        # 组合式函数
│   │   ├── router/             # 路由配置
│   │   ├── stores/             # Pinia 状态管理
│   │   ├── types/              # TypeScript 类型
│   │   ├── utils/              # 工具函数
│   │   ├── views/              # 页面组件
│   │   │   ├── tunnel/
│   │   │   ├── monitoring/
│   │   │   ├── inspection/
│   │   │   ├── alarm/
│   │   │   ├── device/
│   │   │   ├── risk/
│   │   │   └── system/
│   │   ├── App.vue
│   │   └── main.ts
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── vite.config.ts
│   ├── tailwind.config.js
│   └── package.json
│
├── sql/                        # 数据库脚本
│   ├── 01_init_postgresql.sql  # PostgreSQL 初始化
│   ├── 02_init_tdengine.sql    # TDengine 初始化
│   └── 03_mock_data.sql        # 模拟数据
│
├── docker/                     # Docker 部署配置
│   ├── docker-compose.yml      # 完整服务编排
│   └── nginx.conf              # Nginx 配置
│
└── .trae/documents/            # 项目文档
    ├── PRD.md                  # 产品需求文档
    └── technical-architecture.md # 技术架构文档
```

---

## 快速开始

### 环境要求

| 软件 | 版本要求 |
|------|----------|
| JDK | 17+ |
| Maven | 3.8+ |
| Node.js | 20+ |
| npm | 9+ |
| Docker | 24+ |
| Docker Compose | 2.20+ |

---

### 方式一：Docker Compose 一键部署（推荐）

```bash
# 1. 进入部署目录
cd docker

# 2. 启动所有服务（首次会自动构建镜像）
docker-compose up -d

# 3. 查看服务状态
docker-compose ps

# 4. 查看日志
docker-compose logs -f backend
docker-compose logs -f frontend

# 5. 停止服务
docker-compose down

# 6. 停止并删除数据（谨慎使用）
docker-compose down -v
```

**访问地址：**
- 前端应用：http://localhost
- 后端 API：http://localhost:8080
- Swagger UI：http://localhost:8080/swagger-ui
- API 文档：http://localhost:8080/openapi
- 健康检查：http://localhost:8080/health

---

### 方式二：开发模式运行

#### 1. 启动中间件服务

```bash
cd docker
docker-compose up -d postgres tdengine kafka redis elasticsearch
```

#### 2. 运行后端服务

```bash
cd backend

# 编译项目
mvn clean package -DskipTests

# 开发模式运行（支持热重载）
mvn quarkus:dev

# 或运行打包后的 Jar
java -jar target/quarkus-app/quarkus-run.jar
```

#### 3. 运行前端服务

```bash
cd frontend

# 安装依赖
npm install

# 开发模式运行
npm run dev

# 生产构建
npm run build

# 预览构建结果
npm run preview
```

**访问地址：**
- 前端应用：http://localhost:5173
- 后端 API：http://localhost:8080

---

## 默认账号

| 用户名 | 密码 | 角色 | 权限 |
|--------|------|------|------|
| admin | admin123 | 系统管理员 | 所有权限 |
| operator1 | 123456 | 运维工程师 | 数据查看、告警处理、设备管理 |
| monitor1 | 123456 | 监控值班员 | 实时监控、事件回放、报表导出 |
| manager1 | 123456 | 决策管理层 | 数据统计、风险分析、决策支持 |

---

## API 接口说明

### 认证接口
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/auth/login` | 用户登录，返回 JWT Token |
| GET | `/api/auth/info` | 获取当前用户信息 |

### 管廊管理
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/tunnel/structures` | 获取管廊结构列表 |
| POST | `/api/tunnel/structures` | 创建管廊结构 |
| PUT | `/api/tunnel/structures/{id}` | 更新管廊结构 |
| DELETE | `/api/tunnel/structures/{id}` | 删除管廊结构 |
| GET | `/api/tunnel/sections` | 获取管廊分段列表 |

### 监测数据
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/monitoring/acoustic/realtime` | 获取实时声学数据 |
| GET | `/api/monitoring/acoustic/history` | 获取历史声学数据 |
| GET | `/api/monitoring/leak-locations` | 获取渗漏定位记录 |
| POST | `/api/monitoring/leak-locations/confirm` | 确认渗漏点 |

### 巡检管理
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/inspection/paths/generate` | 自动生成巡检路径 |
| GET | `/api/inspection/tasks` | 获取巡检任务列表 |
| POST | `/api/inspection/tasks` | 创建巡检任务 |
| POST | `/api/inspection/tasks/{id}/start` | 启动巡检任务 |

### 告警管理
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/alarm/events` | 获取告警事件列表 |
| PUT | `/api/alarm/events/{id}/acknowledge` | 确认告警 |
| PUT | `/api/alarm/events/{id}/resolve` | 处理告警 |
| GET | `/api/alarm/replay/data` | 获取事件回放数据 |

### 设备管理
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/devices` | 获取设备列表 |
| POST | `/api/devices` | 创建设备 |
| GET | `/api/devices/{id}/lifetime` | 获取设备寿命评估 |

### 风险分析
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/risk/heatmap` | 获取风险热力图数据 |
| GET | `/api/risk/report` | 获取风险统计报表 |

### 系统管理
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/system/users` | 获取用户列表 |
| POST | `/api/system/users` | 创建用户 |
| PUT | `/api/system/config` | 更新系统配置 |

---

## 数据库配置

### PostgreSQL 连接信息
- 主机：localhost
- 端口：5432
- 数据库：tunnel_monitor
- 用户名：tunnel
- 密码：tunnel@2024

### TDengine 连接信息
- 主机：localhost
- 端口：6030
- 数据库：tunnel_monitor
- 用户名：root
- 密码：taosdata

### Redis 连接信息
- 主机：localhost
- 端口：6379
- 密码：tunnel@2024

### Kafka 连接信息
- 主机：localhost
- 端口：9094 (外部) / 9092 (内部)

---

## 部署说明

### 园区私有服务器部署建议

#### 1. 服务器配置建议

| 节点 | CPU | 内存 | 硬盘 | 数量 |
|------|-----|------|------|------|
| 应用服务器 | 16核 | 32GB | 500GB SSD | 2台（主备） |
| 数据库服务器 | 16核 | 64GB | 2TB SSD (RAID 10) | 3台（集群） |
| 时序数据库 | 8核 | 16GB | 4TB HDD | 3台（集群） |
| 消息/缓存服务器 | 8核 | 32GB | 500GB SSD | 3台（集群） |

#### 2. 网络架构

```
          互联网
             │
    ┌────────▼────────┐
    │   防火墙/负载均衡 │
    └────────┬────────┘
             │
    ┌────────▼────────┐
    │   Nginx 反向代理  │
    └────────┬────────┘
             │
    ┌────────▼────────┐
    │   应用服务集群    │
    └────────┬────────┘
             │
┌────────────┼────────────┐
│    ┌───────▼───────┐    │
│    │ PostgreSQL 集群 │    │
│    └───────────────┘    │
│    ┌───────────────┐    │
│    │  TDengine 集群  │    │  数据层
│    └───────────────┘    │
│    ┌───────────────┐    │
│    │   Kafka 集群    │    │
│    └───────────────┘    │
│    ┌───────────────┐    │
│    │   Redis 集群    │    │
│    └───────────────┘    │
└─────────────────────────┘
```

#### 3. 安全配置

1. **网络隔离**：所有服务部署在内网，仅 Nginx 暴露 80/443 端口
2. **HTTPS 加密**：配置 SSL 证书，所有通信加密传输
3. **防火墙规则**：限制数据库端口仅允许应用服务器访问
4. **定期备份**：
   - PostgreSQL：每日全量备份 + 实时 WAL 归档
   - TDengine：每日自动备份
   - 数据保留策略：原始数据保留3年，聚合数据永久保留
5. **访问控制**：
   - 最小权限原则分配数据库账号
   - API 接口 JWT 认证
   - 敏感操作审计日志

---

## 监控与运维

### 健康检查端点

| 路径 | 描述 |
|------|------|
| `/health` | 应用健康状态 |
| `/health/live` | 存活状态 |
| `/health/ready` | 就绪状态 |
| `/q/metrics` | Prometheus 指标 |
| `/q/openapi` | OpenAPI 文档 |
| `/swagger-ui` | Swagger UI |

### 日志管理

- 后端日志位置：`backend/logs/`
- 日志级别可通过 `application.properties` 配置
- 建议接入 ELK 或 Loki 进行日志集中管理

### 性能指标

系统自动采集以下指标：
- JVM 内存/CPU/GC
- HTTP 请求统计（QPS、响应时间、错误率）
- 数据库连接池状态
- Kafka 消费延迟
- 业务指标（告警数量、设备在线率等）

---

## 开发规范

### 后端开发规范

1. **代码风格**：遵循 Alibaba Java 开发规范
2. **命名规范**：
   - 类名：大驼峰 `UserService`
   - 方法名：小驼峰 `getUserById`
   - 常量：全大写下划线分隔 `MAX_PAGE_SIZE`
3. **API 规范**：
   - 统一返回格式 `Result<T>`
   - RESTful 风格 URL
   - 合理使用 HTTP 状态码
4. **异常处理**：统一使用 `BusinessException`，全局异常拦截

### 前端开发规范

1. **代码风格**：遵循 Vue 3 官方风格指南
2. **命名规范**：
   - 组件名：大驼峰 `UserCard.vue`
   - 组合函数：小驼峰 `useUserInfo`
   - 常量：全大写下划线分隔
3. **TypeScript**：优先使用类型注解，避免 `any`
4. **组件规范**：
   - 单文件组件 `<script setup lang="ts">`
   - 组件职责单一，不超过 300 行
   - Props 必须定义类型和默认值

---

## 常见问题

### Q1: Docker Compose 启动失败，端口被占用？
A: 修改 `docker/docker-compose.yml` 中的端口映射配置。

### Q2: 后端启动报数据库连接错误？
A: 检查 PostgreSQL 服务是否正常启动，确认 `application.properties` 中的连接配置。

### Q3: 前端无法连接后端 API？
A: 检查 `vite.config.ts` 中的代理配置，确认后端服务地址正确。

### Q4: 如何修改 JWT 密钥？
A: 替换 `backend/src/main/resources/` 下的 `publicKey.pem` 和 `privateKey.pem` 文件。

### Q5: 如何接入真实的传感器数据？
A: 实现 `DataIngestionService` 接口，对接数据采集网关的 Kafka Topic。

---

## 联系方式

如有问题或建议，请联系项目维护团队。

---

## 更新日志

### v1.0.0 (2026-06-21)
- ✅ 项目初始化完成
- ✅ 后端核心框架搭建（Quarkus 3.8）
- ✅ 前端核心框架搭建（Vue 3 + TypeScript）
- ✅ 数据库设计与初始化脚本
- ✅ 核心功能模块骨架
- ✅ Docker Compose 一键部署
- ✅ 完整的开发文档
