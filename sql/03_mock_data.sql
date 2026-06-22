-- 模拟数据初始化脚本
-- 用于演示和开发测试

-- ============================================
-- 模拟用户数据
-- ============================================
INSERT INTO users (username, password_hash, role, real_name, email, phone) VALUES
('operator1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'OPERATOR', '张工', 'zhang@tunnel.com', '13800138001'),
('operator2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'OPERATOR', '李工', 'li@tunnel.com', '13800138002'),
('monitor1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MONITOR', '王值班', 'wang@tunnel.com', '13800138003'),
('manager1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MANAGER', '赵经理', 'zhao@tunnel.com', '13800138004');

-- ============================================
-- 模拟管廊结构数据
-- ============================================
INSERT INTO tunnel_structures (name, length, depth, geometry, description, created_by)
SELECT 
    '科技园区综合管廊A段', 
    2500.00, 
    8.5,
    '{"type":"LineString","coordinates":[
        [116.397428,39.90923,8.5],
        [116.398428,39.90923,8.5],
        [116.399428,39.91023,8.5],
        [116.400428,39.91123,8.5],
        [116.401428,39.91223,8.5]
    ]}',
    '园区主干道下方综合管廊，包含电力、通信、给水、热力等管线',
    id
FROM users WHERE username = 'admin'
RETURNING id AS tunnel_a_id
\gset

INSERT INTO tunnel_structures (name, length, depth, geometry, description, created_by)
SELECT 
    '科技园区综合管廊B段', 
    1800.00, 
    6.0,
    '{"type":"LineString","coordinates":[
        [116.398428,39.90723,6.0],
        [116.399428,39.90723,6.0],
        [116.400428,39.90823,6.0],
        [116.401428,39.90923,6.0]
    ]}',
    '园区次干道下方管廊，包含电力和通信管线',
    id
FROM users WHERE username = 'admin'
RETURNING id AS tunnel_b_id
\gset

-- ============================================
-- 模拟管廊分段数据
-- ============================================
INSERT INTO tunnel_sections (tunnel_id, name, start_mileage, end_mileage, risk_level)
SELECT 
    :'tunnel_a_id',
    'A段-01区',
    0.000, 500.000, 'LOW'
UNION ALL
SELECT 
    :'tunnel_a_id',
    'A段-02区',
    500.000, 1000.000, 'MEDIUM'
UNION ALL
SELECT 
    :'tunnel_a_id',
    'A段-03区',
    1000.000, 1500.000, 'HIGH'
UNION ALL
SELECT 
    :'tunnel_a_id',
    'A段-04区',
    1500.000, 2000.000, 'MEDIUM'
UNION ALL
SELECT 
    :'tunnel_a_id',
    'A段-05区',
    2000.000, 2500.000, 'LOW'
UNION ALL
SELECT 
    :'tunnel_b_id',
    'B段-01区',
    0.000, 600.000, 'LOW'
UNION ALL
SELECT 
    :'tunnel_b_id',
    'B段-02区',
    600.000, 1200.000, 'MEDIUM'
UNION ALL
SELECT 
    :'tunnel_b_id',
    'B段-03区',
    1200.000, 1800.000, 'LOW';

-- ============================================
-- 模拟设备数据 - 声学传感器
-- ============================================
INSERT INTO devices (section_id, name, type, model, serial_number, install_date, expected_lifetime, current_health, status, position)
SELECT 
    ts.id,
    '声学传感器-A' || lpad(ROW_NUMBER() OVER (PARTITION BY ts.tunnel_id ORDER BY ts.id)::text, 2, '0'),
    'ACOUSTIC_SENSOR',
    'AS-2000',
    'AS' || to_char(now(), 'YYYYMMDD') || lpad((ROW_NUMBER() OVER (ORDER BY ts.id))::text, 4, '0'),
    '2024-01-15',
    3650,
    100 - (random() * 20)::int,
    CASE WHEN random() > 0.1 THEN 'ONLINE' ELSE 'OFFLINE' END,
    jsonb_build_object(
        'x', 116.397 + (random() * 0.005),
        'y', 39.907 + (random() * 0.006),
        'z', 6.0 + (random() * 3)
    )
FROM tunnel_sections ts
CROSS JOIN generate_series(1, 3)
LIMIT 20;

-- ============================================
-- 模拟设备数据 - 摄像头
-- ============================================
INSERT INTO devices (section_id, name, type, model, serial_number, install_date, expected_lifetime, current_health, status, position)
SELECT 
    ts.id,
    '摄像头-C' || lpad(ROW_NUMBER() OVER (ORDER BY ts.id)::text, 2, '0'),
    'CAMERA',
    'CAM-IP4K',
    'CAM' || to_char(now(), 'YYYYMMDD') || lpad((ROW_NUMBER() OVER (ORDER BY ts.id))::text, 4, '0'),
    '2024-02-20',
    1825,
    100 - (random() * 30)::int,
    CASE WHEN random() > 0.15 THEN 'ONLINE' ELSE 'FAULT' END,
    jsonb_build_object(
        'x', 116.397 + (random() * 0.005),
        'y', 39.907 + (random() * 0.006),
        'z', 6.0 + (random() * 3)
    )
FROM tunnel_sections ts
LIMIT 10;

-- ============================================
-- 模拟设备数据 - 巡检机器人
-- ============================================
INSERT INTO devices (section_id, name, type, model, serial_number, install_date, expected_lifetime, current_health, status, position)
VALUES
(
    (SELECT id FROM tunnel_sections LIMIT 1),
    '巡检机器人-R01',
    'ROBOT',
    'INSPECT-X1',
    'ROB' || to_char(now(), 'YYYYMMDD') || '0001',
    '2024-03-10',
    2190,
    92.5,
    'ONLINE',
    '{"x": 116.397428, "y": 39.90923, "z": 8.5}'
),
(
    (SELECT id FROM tunnel_sections LIMIT 1 OFFSET 5),
    '巡检机器人-R02',
    'ROBOT',
    'INSPECT-X1',
    'ROB' || to_char(now(), 'YYYYMMDD') || '0002',
    '2024-03-15',
    2190,
    88.0,
    'ONLINE',
    '{"x": 116.398428, "y": 39.90723, "z": 6.0}'
);

-- ============================================
-- 模拟设备数据 - 网关
-- ============================================
INSERT INTO devices (section_id, name, type, model, serial_number, install_date, expected_lifetime, current_health, status, position)
VALUES
(
    (SELECT id FROM tunnel_sections LIMIT 1),
    '数据网关-GW01',
    'GATEWAY',
    'GW-5000',
    'GW' || to_char(now(), 'YYYYMMDD') || '0001',
    '2023-12-01',
    3650,
    95.0,
    'ONLINE',
    '{"x": 116.397428, "y": 39.90923, "z": 8.5}'
);

-- ============================================
-- 模拟渗漏定位数据
-- ============================================
INSERT INTO leak_locations (timestamp, section_id, position, accuracy, confidence, severity, status, sensor_ids)
SELECT 
    now() - (random() * interval '7 days'),
    ts.id,
    jsonb_build_object(
        'x', 116.398 + (random() * 0.003),
        'y', 39.909 + (random() * 0.003),
        'z', 6.0 + (random() * 3)
    ),
    0.5 + (random() * 2.0),
    0.85 + (random() * 0.15),
    CASE WHEN random() > 0.7 THEN 'CRITICAL' 
         WHEN random() > 0.5 THEN 'SEVERE' 
         WHEN random() > 0.3 THEN 'MODERATE' 
         ELSE 'MINOR' END,
    CASE WHEN random() > 0.6 THEN 'RESOLVED' 
         WHEN random() > 0.3 THEN 'CONFIRMED' 
         ELSE 'PENDING' END,
    ARRAY(SELECT id FROM devices WHERE type = 'ACOUSTIC_SENSOR' ORDER BY random() LIMIT 3)
FROM tunnel_sections ts
ORDER BY random()
LIMIT 8;

-- ============================================
-- 模拟告警事件数据
-- ============================================
INSERT INTO alarm_events (type, level, source, message, location, "timestamp", status)
SELECT 
    'LEAK',
    CASE WHEN random() > 0.7 THEN 'CRITICAL' 
         WHEN random() > 0.5 THEN 'ERROR' 
         WHEN random() > 0.3 THEN 'WARNING' 
         ELSE 'INFO' END,
    '声学传感器-' || lpad((random() * 20)::int::text, 2, '0'),
    CASE 
        WHEN random() > 0.7 THEN '检测到严重渗漏信号，建议立即处理'
        WHEN random() > 0.5 THEN '检测到疑似渗漏信号，请关注'
        ELSE '检测到异常声学特征，需进一步确认'
    END,
    jsonb_build_object(
        'x', 116.398 + (random() * 0.003),
        'y', 39.909 + (random() * 0.003),
        'z', 6.0 + (random() * 3)
    ),
    now() - (random() * interval '30 days'),
    CASE WHEN random() > 0.5 THEN 'RESOLVED' 
         WHEN random() > 0.3 THEN 'PROCESSING' 
         WHEN random() > 0.1 THEN 'ACKNOWLEDGED'
         ELSE 'NEW' END
FROM generate_series(1, 30);

INSERT INTO alarm_events (type, level, source, message, "timestamp", status)
SELECT 
    'DEVICE_FAULT',
    CASE WHEN random() > 0.8 THEN 'ERROR' ELSE 'WARNING' END,
    '设备-' || (CASE WHEN random() > 0.5 THEN 'AS' ELSE 'CAM' END) || lpad((random() * 20)::int::text, 2, '0'),
    CASE 
        WHEN random() > 0.7 THEN '设备离线，请检查设备状态'
        WHEN random() > 0.5 THEN '设备电压偏低，建议更换电池'
        ELSE '设备信号较弱，请检查通信'
    END,
    now() - (random() * interval '30 days'),
    CASE WHEN random() > 0.6 THEN 'RESOLVED' 
         WHEN random() > 0.3 THEN 'PROCESSING' 
         ELSE 'NEW' END
FROM generate_series(1, 15);

-- ============================================
-- 模拟巡检任务数据
-- ============================================
INSERT INTO inspection_tasks (name, robot_id, tunnel_id, path, status, start_time, end_time, created_by)
SELECT 
    '日常巡检-' || to_char(d, 'YYYY-MM-DD'),
    (SELECT id FROM devices WHERE type = 'ROBOT' LIMIT 1),
    (SELECT id FROM tunnel_structures LIMIT 1),
    '[{"x":116.397428,"y":39.90923,"z":8.5,"mileage":0,"action":"PASS"},
      {"x":116.398428,"y":39.90923,"z":8.5,"mileage":500,"action":"SCAN"},
      {"x":116.399428,"y":39.91023,"z":8.5,"mileage":1000,"action":"PHOTO"},
      {"x":116.400428,"y":39.91123,"z":8.5,"mileage":1500,"action":"SCAN"},
      {"x":116.401428,"y":39.91223,"z":8.5,"mileage":2000,"action":"STOP"}]',
    CASE 
        WHEN d::date = CURRENT_DATE THEN 'RUNNING'
        WHEN d::date > CURRENT_DATE - 7 THEN 'COMPLETED'
        ELSE 'COMPLETED'
    END,
    d::timestamp,
    d::timestamp + interval '2 hours',
    (SELECT id FROM users WHERE username = 'operator1')
FROM generate_series(
    CURRENT_DATE - interval '30 days',
    CURRENT_DATE,
    interval '1 day'
) AS d
LIMIT 15;

-- ============================================
-- 模拟设备寿命评估数据
-- ============================================
INSERT INTO lifetime_assessments (device_id, health_score, remaining_lifetime, risk_level, recommended_actions, assessment_date)
SELECT 
    d.id,
    60 + (random() * 40)::numeric(5,2),
    365 + (random() * 3285)::numeric(10,2),
    CASE 
        WHEN current_health < 60 THEN 'HIGH'
        WHEN current_health < 80 THEN 'MEDIUM'
        ELSE 'LOW'
    END,
    '["定期检查","清洁维护","校准检测"]'::jsonb,
    CURRENT_DATE
FROM devices d
ORDER BY random()
LIMIT 25;

-- ============================================
-- 模拟风险热力图数据
-- ============================================
INSERT INTO risk_heatmaps (section_id, risk_score, risk_level, factors, calculation_time)
SELECT 
    ts.id,
    20 + (random() * 80)::numeric(5,2),
    ts.risk_level,
    jsonb_build_object(
        'leakFrequency', random() * 100,
        'deviceHealth', 60 + (random() * 40),
        'age', 1 + (random() * 5),
        'environmental', random() * 100
    ),
    now()
FROM tunnel_sections ts;
