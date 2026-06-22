-- TDengine 时序数据库初始化脚本
-- 地下综合管廊渗漏声学定位与巡检推演平台

CREATE DATABASE IF NOT EXISTS tunnel_monitor 
KEEP 1095 
DURATION 14 
BUFFER 256 
WAL_FSYNC_PERIOD 3000
TABLE_PREFIX 4;

USE tunnel_monitor;

-- ============================================
-- 声学传感器数据表
-- 存储声学传感器采集的原始波形数据和分析结果
-- ============================================
CREATE STABLE IF NOT EXISTS acoustic_data (
    ts TIMESTAMP,
    frequency FLOAT,
    amplitude FLOAT,
    sample_rate INT,
    waveform BINARY(4096),
    rms FLOAT,
    peak FLOAT,
    crest_factor FLOAT
) TAGS (
    device_id NCHAR(36),
    section_id NCHAR(36),
    tunnel_id NCHAR(36),
    sensor_type NCHAR(20)
);

-- ============================================
-- 传感器状态数据表
-- 存储传感器运行状态、环境参数等
-- ============================================
CREATE STABLE IF NOT EXISTS sensor_status (
    ts TIMESTAMP,
    temperature FLOAT,
    humidity FLOAT,
    voltage FLOAT,
    current FLOAT,
    signal_strength INT,
    status NCHAR(20),
    error_code INT
) TAGS (
    device_id NCHAR(36)
);

-- ============================================
-- 巡检机器人遥测数据表
-- 存储巡检机器人实时位置、状态等遥测数据
-- ============================================
CREATE STABLE IF NOT EXISTS robot_telemetry (
    ts TIMESTAMP,
    position_x FLOAT,
    position_y FLOAT,
    position_z FLOAT,
    mileage FLOAT,
    speed FLOAT,
    heading FLOAT,
    battery_level FLOAT,
    temperature FLOAT,
    status NCHAR(20),
    error_code INT
) TAGS (
    robot_id NCHAR(36),
    task_id NCHAR(36)
);

-- ============================================
-- 渗漏检测实时结果表
-- 存储AI引擎实时检测到的渗漏可疑信号
-- ============================================
CREATE STABLE IF NOT EXISTS leak_detection_results (
    ts TIMESTAMP,
    frequency_start FLOAT,
    frequency_end FLOAT,
    duration FLOAT,
    energy FLOAT,
    snr FLOAT,
    probability FLOAT,
    is_leak BOOLEAN
) TAGS (
    device_id NCHAR(36),
    section_id NCHAR(36)
);

-- ============================================
-- 环境监测数据表
-- 存储管廊内环境传感器数据（温湿度、有害气体等）
-- ============================================
CREATE STABLE IF NOT EXISTS environment_data (
    ts TIMESTAMP,
    temperature FLOAT,
    humidity FLOAT,
    co2 FLOAT,
    ch4 FLOAT,
    h2s FLOAT,
    o2 FLOAT,
    pressure FLOAT,
    water_level FLOAT
) TAGS (
    device_id NCHAR(36),
    section_id NCHAR(36)
);

-- ============================================
-- 系统性能监控表
-- 存储各服务节点的性能指标
-- ============================================
CREATE STABLE IF NOT EXISTS system_metrics (
    ts TIMESTAMP,
    cpu_usage FLOAT,
    memory_usage FLOAT,
    disk_usage FLOAT,
    network_in FLOAT,
    network_out FLOAT,
    request_count INT,
    error_count INT,
    avg_response_time FLOAT
) TAGS (
    service_name NCHAR(50),
    host NCHAR(50)
);
