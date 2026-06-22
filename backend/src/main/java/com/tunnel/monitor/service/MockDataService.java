package com.tunnel.monitor.service;

import com.tunnel.monitor.entity.*;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class MockDataService {

    private static final Logger log = LoggerFactory.getLogger(MockDataService.class);

    private final ConcurrentHashMap<Long, User> userMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, TunnelStructure> tunnelMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, TunnelSection> sectionMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Device> deviceMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, LeakLocation> leakMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, AlarmEvent> alarmMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, InspectionTask> taskMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, LifetimeAssessment> assessmentMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, RiskHeatmap> heatmapMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, SystemConfig> configMap = new ConcurrentHashMap<>();

    private final AtomicLong userIdGenerator = new AtomicLong(100);
    private final AtomicLong tunnelIdGenerator = new AtomicLong(100);
    private final AtomicLong sectionIdGenerator = new AtomicLong(100);
    private final AtomicLong deviceIdGenerator = new AtomicLong(100);
    private final AtomicLong leakIdGenerator = new AtomicLong(100);
    private final AtomicLong alarmIdGenerator = new AtomicLong(100);
    private final AtomicLong taskIdGenerator = new AtomicLong(100);
    private final AtomicLong assessmentIdGenerator = new AtomicLong(100);
    private final AtomicLong heatmapIdGenerator = new AtomicLong(100);
    private final AtomicLong configIdGenerator = new AtomicLong(100);

    void onStart(@Observes StartupEvent ev) {
        log.info("正在初始化 Mock 数据...");
        initMockData();
        log.info("Mock 数据初始化完成");
    }

    private void initMockData() {
        initUsers();
        initTunnels();
        initSections();
        initDevices();
        initLeaks();
        initAlarms();
        initTasks();
        initAssessments();
        initHeatmaps();
        initConfigs();
    }

    private void initUsers() {
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("admin" + i);
            user.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7i");
            user.setRealName("管理员" + i);
            user.setPhone("1380013800" + String.format("%02d", i));
            user.setEmail("admin" + i + "@tunnel.com");
            user.setRole(i == 1 ? "ADMIN" : "USER");
            user.setEnabled(true);
            user.setCreateTime(LocalDateTime.now().minusDays(i));
            user.setUpdateTime(LocalDateTime.now());
            userMap.put(user.getId(), user);
        }
        userIdGenerator.set(11);
    }

    private void initTunnels() {
        String[] tunnelNames = {"长江隧道", "黄河隧道", "珠江隧道", "淮河隧道", "海河隧道"};
        String[] locations = {"上海市浦东新区", "河南省郑州市", "广东省广州市", "安徽省合肥市", "天津市滨海新区"};
        String[] types = {"盾构隧道", "矿山法隧道", "明挖法隧道", "沉管隧道", "TBM隧道"};
        String[] methods = {"盾构法", "新奥法", "明挖法", "沉管法", "TBM法"};

        for (int i = 1; i <= 5; i++) {
            TunnelStructure tunnel = new TunnelStructure();
            tunnel.setId((long) i);
            tunnel.setTunnelCode("TUN" + String.format("%04d", i));
            tunnel.setTunnelName(tunnelNames[i - 1]);
            tunnel.setLocation(locations[i - 1]);
            tunnel.setTotalLength(new BigDecimal(3000 + i * 500));
            tunnel.setWidth(new BigDecimal("12.5"));
            tunnel.setHeight(new BigDecimal("8.2"));
            tunnel.setStructureType(types[i - 1]);
            tunnel.setConstructionMethod(methods[i - 1]);
            tunnel.setBuildDate(LocalDate.of(2018 + i, 3, 15));
            tunnel.setOpenDate(LocalDate.of(2021 + i, 6, 30));
            tunnel.setDesignUnit("中交第二公路勘察设计研究院");
            tunnel.setConstructionUnit("中国建筑第八工程局");
            tunnel.setSupervisionUnit("中咨工程管理咨询有限公司");
            tunnel.setStatus("ACTIVE");
            tunnel.setCreateTime(LocalDateTime.now().minusMonths(i));
            tunnel.setUpdateTime(LocalDateTime.now());
            tunnelMap.put(tunnel.getId(), tunnel);
        }
        tunnelIdGenerator.set(6);
    }

    private void initSections() {
        String[] sectionTypes = {"标准段", "加宽段", "变截面段", "连接通道", "通风井"};
        long sectionId = 1;

        for (long tunnelId = 1; tunnelId <= 5; tunnelId++) {
            for (int i = 1; i <= 10; i++) {
                TunnelSection section = new TunnelSection();
                section.setId(sectionId);
                section.setTunnelId(tunnelId);
                section.setSectionCode("SEC" + String.format("%06d", sectionId));
                section.setSectionName("第" + i + "节段");
                section.setStartMileage(new BigDecimal((i - 1) * 300));
                section.setEndMileage(new BigDecimal(i * 300));
                section.setLength(new BigDecimal(300));
                section.setSectionType(sectionTypes[i % 5]);
                section.setDesignThickness(new BigDecimal("0.55"));
                section.setConcreteGrade("C50");
                section.setReinforcementGrade("HRB400");
                section.setSlope(new BigDecimal("2.5"));
                section.setLongitude(new BigDecimal("121.4737" + i));
                section.setLatitude(new BigDecimal("31.2304" + i));
                section.setElevation(new BigDecimal("-15.5"));
                section.setStatus("ACTIVE");
                section.setCreateTime(LocalDateTime.now().minusWeeks(i));
                section.setUpdateTime(LocalDateTime.now());
                sectionMap.put(section.getId(), section);
                sectionId++;
            }
        }
        sectionIdGenerator.set(sectionId);
    }

    private void initDevices() {
        String[] deviceTypes = {"渗压计", "应变计", "钢筋计", "测缝计", "温度计", "水位计", "流量计", "沉降仪", "倾斜仪", "加速度计"};
        String[] manufacturers = {"基康仪器", "欧美大地", "南京葛南", "上海岩联", "武汉中岩"};
        String[] statuses = {"ONLINE", "ONLINE", "ONLINE", "OFFLINE", "MAINTENANCE"};

        long deviceId = 1;
        for (long sectionId = 1; sectionId <= 20; sectionId++) {
            for (int i = 1; i <= 5; i++) {
                Device device = new Device();
                device.setId(deviceId);
                device.setSectionId(sectionId);
                device.setDeviceCode("DEV" + String.format("%08d", deviceId));
                device.setDeviceName(deviceTypes[i - 1] + "-" + deviceId);
                device.setDeviceType(deviceTypes[i - 1]);
                device.setManufacturer(manufacturers[i - 1]);
                device.setModel("MODEL-" + (1000 + deviceId));
                device.setSerialNumber("SN" + String.format("%010d", deviceId));
                device.setInstallDate(LocalDate.of(2022, 1, 1).plusDays(deviceId));
                device.setLastMaintenanceDate(LocalDate.now().minusMonths(i));
                device.setNextMaintenanceDate(LocalDate.now().plusMonths(6 - i));
                device.setMileage(new BigDecimal(i * 60));
                device.setInstallPositionX(new BigDecimal(i * 2.5));
                device.setInstallPositionY(new BigDecimal("0.0"));
                device.setInstallPositionZ(new BigDecimal("-" + i * 0.5));
                device.setStatus(statuses[i % 5]);
                device.setBatteryLevel(new BigDecimal(95 - i * 3));
                device.setCommunicationMode("LoRa");
                device.setLastHeartbeat(LocalDateTime.now().minusMinutes(i * 5));
                device.setCreateTime(LocalDateTime.now().minusDays(deviceId));
                device.setUpdateTime(LocalDateTime.now());
                deviceMap.put(device.getId(), device);
                deviceId++;
            }
        }
        deviceIdGenerator.set(deviceId);
    }

    private void initLeaks() {
        String[] leakTypes = {"点漏", "线漏", "面漏", "缝漏", "管片接缝渗漏"};
        String[] severities = {"MINOR", "MODERATE", "SEVERE", "CRITICAL"};
        String[] statuses = {"PENDING", "PROCESSING", "REPAIRED", "CLOSED"};
        String[] methods = {"人工巡查", "视频监控", "传感器检测", "无人机巡检", "声波探测"};

        for (long i = 1; i <= 30; i++) {
            LeakLocation leak = new LeakLocation();
            leak.setId(i);
            leak.setSectionId((i % 50) + 1);
            leak.setLeakCode("LEAK" + String.format("%06d", i));
            leak.setMileage(new BigDecimal(i * 100));
            leak.setPositionX(new BigDecimal(i * 1.5));
            leak.setPositionY(new BigDecimal(i * 0.8));
            leak.setLeakType(leakTypes[(int) (i % 5)]);
            leak.setSeverity(severities[(int) (i % 4)]);
            leak.setArea(new BigDecimal(i * 0.25));
            leak.setFlowRate(new BigDecimal(i * 0.05));
            leak.setStatus(statuses[(int) (i % 4)]);
            leak.setDetectionMethod(methods[(int) (i % 5)]);
            leak.setDetectTime(LocalDateTime.now().minusDays(i));
            if (i % 3 == 0) {
                leak.setRepairTime(LocalDateTime.now().minusDays(i / 2));
                leak.setRepairMethod("注浆封堵");
                leak.setRepairStatus("COMPLETED");
            }
            leak.setCreateTime(LocalDateTime.now().minusDays(i));
            leak.setUpdateTime(LocalDateTime.now());
            leakMap.put(leak.getId(), leak);
        }
        leakIdGenerator.set(31);
    }

    private void initAlarms() {
        String[] alarmTypes = {"设备离线", "数据异常", "阈值超限", "通信故障", "电量低"};
        String[] levels = {"INFO", "WARNING", "ERROR", "CRITICAL"};
        String[] statuses = {"ACTIVE", "ACKNOWLEDGED", "RESOLVED", "CLOSED"};

        for (long i = 1; i <= 50; i++) {
            AlarmEvent alarm = new AlarmEvent();
            alarm.setId(i);
            alarm.setDeviceId((i % 100) + 1);
            alarm.setLeakId(i % 30 == 0 ? (i % 30) + 1 : null);
            alarm.setAlarmCode("ALM" + String.format("%08d", i));
            alarm.setAlarmType(alarmTypes[(int) (i % 5)]);
            alarm.setAlarmLevel(levels[(int) (i % 4)]);
            alarm.setAlarmTitle(alarmTypes[(int) (i % 5)] + "告警");
            alarm.setAlarmContent("设备检测到" + alarmTypes[(int) (i % 5)] + "，请及时处理");
            alarm.setAlarmValue(new BigDecimal(50 + i * 2));
            alarm.setThresholdValue(new BigDecimal(60));
            alarm.setStatus(statuses[(int) (i % 4)]);
            alarm.setAlarmTime(LocalDateTime.now().minusHours(i));
            if (i % 2 == 0) {
                alarm.setAckTime(LocalDateTime.now().minusHours(i / 2));
                alarm.setAckUserId(1L);
                alarm.setAckUserName("管理员1");
            }
            if (i % 4 == 0) {
                alarm.setResolveTime(LocalDateTime.now().minusHours(i / 4));
                alarm.setResolveUserId(2L);
                alarm.setResolveUserName("管理员2");
                alarm.setResolveMethod("已修复设备故障");
            }
            alarm.setCreateTime(LocalDateTime.now().minusHours(i));
            alarm.setUpdateTime(LocalDateTime.now());
            alarmMap.put(alarm.getId(), alarm);
        }
        alarmIdGenerator.set(51);
    }

    private void initTasks() {
        String[] taskTypes = {"日常巡检", "定期检查", "专项检测", "渗漏排查", "结构检测"};
        String[] priorities = {"LOW", "MEDIUM", "HIGH", "URGENT"};
        String[] statuses = {"DRAFT", "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED"};

        for (long i = 1; i <= 25; i++) {
            InspectionTask task = new InspectionTask();
            task.setId(i);
            task.setTunnelId((i % 5) + 1);
            task.setTaskCode("TASK" + String.format("%06d", i));
            task.setTaskName(taskTypes[(int) (i % 5)] + "任务-" + i);
            task.setTaskType(taskTypes[(int) (i % 5)]);
            task.setPriority(priorities[(int) (i % 4)]);
            task.setStatus(statuses[(int) (i % 5)]);
            task.setPlanStartTime(LocalDateTime.now().plusDays(i));
            task.setPlanEndTime(LocalDateTime.now().plusDays(i + 1));
            if (i % 3 == 0) {
                task.setActualStartTime(LocalDateTime.now().plusDays(i / 2));
                task.setActualEndTime(LocalDateTime.now().plusDays(i / 2 + 1));
            }
            task.setInspectorId((i % 10) + 1);
            task.setInspectorName("管理员" + ((i % 10) + 1));
            task.setInspectionScope("隧道全长");
            task.setInspectionContent("检查隧道结构完整性、渗漏情况、设备状态等");
            if (i % 2 == 0) {
                task.setInspectionResult("检查完成，发现少量渗漏点");
                task.setProblemDescription("发现3处渗漏点，需要处理");
                task.setSuggestion("建议进行注浆封堵处理");
            }
            task.setCreateUserId(1L);
            task.setCreateUserName("管理员1");
            task.setCreateTime(LocalDateTime.now().minusDays(i));
            task.setUpdateTime(LocalDateTime.now());
            taskMap.put(task.getId(), task);
        }
        taskIdGenerator.set(26);
    }

    private void initAssessments() {
        String[] types = {"年度评估", "季度评估", "月度评估", "专项评估", "应急评估"};
        String[] levels = {"EXCELLENT", "GOOD", "FAIR", "POOR", "CRITICAL"};
        String[] deteriorationTypes = {"混凝土碳化", "钢筋锈蚀", "裂缝发展", "渗漏侵蚀", "冻融破坏"};

        for (long i = 1; i <= 20; i++) {
            LifetimeAssessment assessment = new LifetimeAssessment();
            assessment.setId(i);
            assessment.setSectionId((i % 50) + 1);
            assessment.setAssessmentCode("ASSESS" + String.format("%06d", i));
            assessment.setAssessmentName(types[(int) (i % 5)] + "-" + i);
            assessment.setAssessmentType(types[(int) (i % 5)]);
            assessment.setAssessmentDate(LocalDate.now().minusMonths(i));
            assessment.setDesignLife(new BigDecimal(100));
            assessment.setUsedLife(new BigDecimal(5 + i * 0.5));
            assessment.setRemainingLife(new BigDecimal(95 - i * 0.5));
            assessment.setPredictedRemainingLife(new BigDecimal(90 - i * 0.5));
            assessment.setHealthLevel(levels[(int) (i % 5)]);
            assessment.setHealthScore(new BigDecimal(95 - i * 2));
            assessment.setDeteriorationRate(new BigDecimal(0.5 + i * 0.1));
            assessment.setMainDeteriorationType(deteriorationTypes[(int) (i % 5)]);
            assessment.setDeteriorationDescription(deteriorationTypes[(int) (i % 5)] + "现象明显");
            assessment.setRiskFactors("地下水侵蚀、荷载作用、材料老化");
            assessment.setMaintenanceSuggestions("建议定期进行防腐处理，加强监测");
            assessment.setAssessmentConclusion("结构整体状况" + levels[(int) (i % 5)] + "，建议加强维护");
            assessment.setAssessorId((i % 10) + 1);
            assessment.setAssessorName("管理员" + ((i % 10) + 1));
            assessment.setStatus("PUBLISHED");
            assessment.setCreateTime(LocalDateTime.now().minusMonths(i));
            assessment.setUpdateTime(LocalDateTime.now());
            assessmentMap.put(assessment.getId(), assessment);
        }
        assessmentIdGenerator.set(21);
    }

    private void initHeatmaps() {
        String[] riskTypes = {"渗漏风险", "结构风险", "设备风险", "环境风险", "运营风险"};
        String[] levels = {"LOW", "MEDIUM", "HIGH", "CRITICAL"};
        String[] colors = {"#00FF00", "#FFFF00", "#FFA500", "#FF0000"};

        for (long i = 1; i <= 40; i++) {
            RiskHeatmap heatmap = new RiskHeatmap();
            heatmap.setId(i);
            heatmap.setTunnelId((i % 5) + 1);
            heatmap.setSectionId((i % 50) + 1);
            heatmap.setHeatmapCode("HEAT" + String.format("%06d", i));
            heatmap.setHeatmapName(riskTypes[(int) (i % 5)] + "热力图");
            heatmap.setRiskType(riskTypes[(int) (i % 5)]);
            heatmap.setStartMileage(new BigDecimal((i - 1) * 75));
            heatmap.setEndMileage(new BigDecimal(i * 75));
            heatmap.setPositionX(new BigDecimal(i * 3.0));
            heatmap.setPositionY(new BigDecimal(i * 1.5));
            heatmap.setPositionZ(new BigDecimal(-10 + i * 0.2));
            heatmap.setRiskValue(new BigDecimal(20 + i * 1.5));
            heatmap.setRiskLevel(levels[(int) (i % 4)]);
            heatmap.setColorCode(colors[(int) (i % 4)]);
            heatmap.setStatisticTime(LocalDateTime.now().minusHours(i));
            heatmap.setOccurrenceProbability(new BigDecimal(30 + i));
            heatmap.setConsequenceSeverity(new BigDecimal(50 + i * 0.5));
            heatmap.setRiskDescription(riskTypes[(int) (i % 5)] + "评估结果");
            heatmap.setMitigationMeasures("加强监测，定期检查，制定应急预案");
            heatmap.setStatus("ACTIVE");
            heatmap.setCreateTime(LocalDateTime.now().minusHours(i));
            heatmap.setUpdateTime(LocalDateTime.now());
            heatmapMap.put(heatmap.getId(), heatmap);
        }
        heatmapIdGenerator.set(41);
    }

    private void initConfigs() {
        String[][] configs = {
                {"system.title", "隧道结构健康监测系统", "系统名称", "STRING"},
                {"system.version", "1.0.0", "系统版本", "STRING"},
                {"system.timezone", "Asia/Shanghai", "系统时区", "STRING"},
                {"alarm.enable", "true", "告警开关", "BOOLEAN"},
                {"alarm.auto.ack", "false", "自动确认告警", "BOOLEAN"},
                {"alarm.escalation.minutes", "30", "告警升级时间(分钟)", "NUMBER"},
                {"jwt.expire.hours", "24", "JWT过期时间(小时)", "NUMBER"},
                {"jwt.issuer", "https://tunnel-monitor.com", "JWT签发者", "STRING"},
                {"data.retention.days", "365", "数据保留天数", "NUMBER"},
                {"backup.enable", "true", "数据备份开关", "BOOLEAN"},
                {"backup.time", "02:00", "备份执行时间", "STRING"},
                {"upload.max.size", "100", "上传文件最大大小(MB)", "NUMBER"},
                {"report.export.format", "PDF,EXCEL", "报表导出格式", "STRING"},
                {"websocket.enable", "true", "WebSocket开关", "BOOLEAN"},
                {"kafka.enable", "true", "Kafka开关", "BOOLEAN"}
        };

        for (int i = 0; i < configs.length; i++) {
            SystemConfig config = new SystemConfig();
            config.setId((long) (i + 1));
            config.setConfigKey(configs[i][0]);
            config.setConfigValue(configs[i][1]);
            config.setConfigName(configs[i][2]);
            config.setConfigType(configs[i][3]);
            config.setDescription(configs[i][2] + "配置");
            config.setEnabled(true);
            config.setIsSystem(i < 3);
            config.setCreateTime(LocalDateTime.now().minusMonths(1));
            config.setUpdateTime(LocalDateTime.now());
            configMap.put(config.getId(), config);
        }
        configIdGenerator.set(16L);
    }

    public List<User> getMockUsers() {
        return new ArrayList<>(userMap.values());
    }

    public List<TunnelStructure> getMockTunnels() {
        return new ArrayList<>(tunnelMap.values());
    }

    public List<TunnelSection> getMockSections() {
        return new ArrayList<>(sectionMap.values());
    }

    public List<Device> getMockDevices() {
        return new ArrayList<>(deviceMap.values());
    }

    public List<LeakLocation> getMockLeaks() {
        return new ArrayList<>(leakMap.values());
    }

    public List<AlarmEvent> getMockAlarms() {
        return new ArrayList<>(alarmMap.values());
    }

    public List<InspectionTask> getMockTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public List<LifetimeAssessment> getMockAssessments() {
        return new ArrayList<>(assessmentMap.values());
    }

    public List<RiskHeatmap> getMockHeatmaps() {
        return new ArrayList<>(heatmapMap.values());
    }

    public List<SystemConfig> getMockConfigs() {
        return new ArrayList<>(configMap.values());
    }

    public User getUser(Long id) {
        return userMap.get(id);
    }

    public User getUserByUsername(String username) {
        return userMap.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public TunnelStructure getTunnel(Long id) {
        return tunnelMap.get(id);
    }

    public TunnelSection getSection(Long id) {
        return sectionMap.get(id);
    }

    public Device getDevice(Long id) {
        return deviceMap.get(id);
    }

    public LeakLocation getLeak(Long id) {
        return leakMap.get(id);
    }

    public AlarmEvent getAlarm(Long id) {
        return alarmMap.get(id);
    }

    public InspectionTask getTask(Long id) {
        return taskMap.get(id);
    }

    public LifetimeAssessment getAssessment(Long id) {
        return assessmentMap.get(id);
    }

    public RiskHeatmap getHeatmap(Long id) {
        return heatmapMap.get(id);
    }

    public SystemConfig getConfig(Long id) {
        return configMap.get(id);
    }
}
