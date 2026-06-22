-- PostgreSQL 初始化脚本
-- 地下综合管廊渗漏声学定位与巡检推演平台

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- ============================================
-- 用户表
-- ============================================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'OPERATOR', 'MONITOR', 'MANAGER')),
    real_name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 管廊结构表
-- ============================================
CREATE TABLE tunnel_structures (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    length DECIMAL(10,2) NOT NULL,
    depth DECIMAL(10,2),
    geometry JSONB,
    description TEXT,
    created_by UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 管廊分段表
-- ============================================
CREATE TABLE tunnel_sections (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tunnel_id UUID REFERENCES tunnel_structures(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    start_mileage DECIMAL(10,3) NOT NULL,
    end_mileage DECIMAL(10,3) NOT NULL,
    risk_level VARCHAR(20) DEFAULT 'LOW',
    properties JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 设备表
-- ============================================
CREATE TABLE devices (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    section_id UUID REFERENCES tunnel_sections(id),
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    model VARCHAR(100),
    serial_number VARCHAR(100),
    install_date DATE,
    expected_lifetime INT,
    current_health DECIMAL(5,2) DEFAULT 100.00,
    status VARCHAR(20) DEFAULT 'ONLINE',
    position JSONB NOT NULL,
    properties JSONB,
    last_heartbeat TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 渗漏定位表
-- ============================================
CREATE TABLE leak_locations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    timestamp TIMESTAMP NOT NULL,
    section_id UUID REFERENCES tunnel_sections(id),
    position JSONB NOT NULL,
    accuracy DECIMAL(10,2),
    confidence DECIMAL(5,2),
    severity VARCHAR(20),
    status VARCHAR(20) DEFAULT 'PENDING',
    sensor_ids UUID[],
    properties JSONB,
    confirmed_by UUID REFERENCES users(id),
    confirmed_at TIMESTAMP,
    resolved_by UUID REFERENCES users(id),
    resolved_at TIMESTAMP,
    resolution_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 告警事件表
-- ============================================
CREATE TABLE alarm_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    type VARCHAR(50) NOT NULL,
    level VARCHAR(20) NOT NULL,
    source VARCHAR(100),
    message TEXT NOT NULL,
    location JSONB,
    related_id UUID,
    "timestamp" TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW',
    acknowledged_by UUID REFERENCES users(id),
    acknowledged_at TIMESTAMP,
    processed_by UUID REFERENCES users(id),
    processed_at TIMESTAMP,
    processing_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 巡检任务表
-- ============================================
CREATE TABLE inspection_tasks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    robot_id UUID REFERENCES devices(id),
    tunnel_id UUID REFERENCES tunnel_structures(id),
    path JSONB,
    status VARCHAR(20) DEFAULT 'PENDING',
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    created_by UUID REFERENCES users(id),
    properties JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 设备寿命评估表
-- ============================================
CREATE TABLE lifetime_assessments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    device_id UUID REFERENCES devices(id) ON DELETE CASCADE,
    health_score DECIMAL(5,2) NOT NULL,
    remaining_lifetime DECIMAL(10,2),
    risk_level VARCHAR(20),
    recommended_actions JSONB,
    assessment_date DATE NOT NULL,
    properties JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 风险热力图表
-- ============================================
CREATE TABLE risk_heatmaps (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    section_id UUID REFERENCES tunnel_sections(id) ON DELETE CASCADE,
    risk_score DECIMAL(5,2) NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    factors JSONB,
    calculation_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 系统配置表
-- ============================================
CREATE TABLE system_configs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value TEXT,
    config_type VARCHAR(20) DEFAULT 'STRING',
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 创建索引
-- ============================================
CREATE INDEX idx_alarm_events_timestamp ON alarm_events("timestamp" DESC);
CREATE INDEX idx_alarm_events_status ON alarm_events(status);
CREATE INDEX idx_alarm_events_level ON alarm_events(level);
CREATE INDEX idx_alarm_events_type ON alarm_events(type);

CREATE INDEX idx_devices_section_id ON devices(section_id);
CREATE INDEX idx_devices_status ON devices(status);
CREATE INDEX idx_devices_type ON devices(type);

CREATE INDEX idx_leak_locations_timestamp ON leak_locations(timestamp DESC);
CREATE INDEX idx_leak_locations_status ON leak_locations(status);
CREATE INDEX idx_leak_locations_severity ON leak_locations(severity);

CREATE INDEX idx_inspection_tasks_status ON inspection_tasks(status);
CREATE INDEX idx_inspection_tasks_created_by ON inspection_tasks(created_by);
CREATE INDEX idx_inspection_tasks_tunnel_id ON inspection_tasks(tunnel_id);

CREATE INDEX idx_tunnel_sections_tunnel_id ON tunnel_sections(tunnel_id);
CREATE INDEX idx_tunnel_sections_risk_level ON tunnel_sections(risk_level);

CREATE INDEX idx_risk_heatmaps_section_id ON risk_heatmaps(section_id);
CREATE INDEX idx_risk_heatmaps_calculation_time ON risk_heatmaps(calculation_time DESC);

CREATE INDEX idx_lifetime_assessments_device_id ON lifetime_assessments(device_id);
CREATE INDEX idx_lifetime_assessments_assessment_date ON lifetime_assessments(assessment_date DESC);

-- ============================================
-- 初始化数据
-- ============================================
-- 默认管理员账户: admin / admin123
INSERT INTO users (username, password_hash, role, real_name, email, phone)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', '系统管理员', 'admin@tunnel.com', '13800138000');

-- 系统默认配置
INSERT INTO system_configs (config_key, config_value, config_type, description) VALUES
('leak_detection.threshold', '0.75', 'NUMBER', '渗漏检测置信度阈值'),
('leak_detection.min_severity', 'MODERATE', 'STRING', '最小告警严重级别'),
('inspection.default_speed', '1.5', 'NUMBER', '巡检机器人默认速度(m/s)'),
('risk_assessment.interval_hours', '24', 'NUMBER', '风险评估间隔(小时)'),
('data_retention.days', '1095', 'NUMBER', '数据保留天数'),
('notification.email_enabled', 'true', 'BOOLEAN', '邮件通知开关'),
('notification.sms_enabled', 'false', 'BOOLEAN', '短信通知开关');
