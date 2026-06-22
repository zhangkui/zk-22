import type {
  User,
  TunnelStructure,
  TunnelSection,
  AlarmEvent,
  Device,
  LifetimeAssessment,
  InspectionTask,
  LeakLocation,
  AcousticData,
  RiskHeatmap,
  SystemConfig
} from '@/types'
import { generateMockData, formatDate } from './index'

const tunnelNames = ['秦岭隧道', '太行山隧道', '武夷山隧道', '大别山隧道', '雪峰山隧道']
const sectionTypes = ['圆形断面', '马蹄形断面', '矩形断面', '城门洞形']
const liningTypes = ['单层衬砌', '双层衬砌', '复合式衬砌', '装配式衬砌']
const deviceTypes = ['声学传感器', '振动传感器', '温湿度传感器', '水位传感器', '应力传感器', '位移传感器']
const manufacturers = ['华为技术', '海康威视', '大华股份', '中电科', '西门子', '施耐德']
const alarmTypes = ['结构异常', '渗漏水', '设备故障', '超限报警', '环境异常']
const alarmLevels: ('critical' | 'warning' | 'info')[] = ['critical', 'warning', 'info']
const taskStatuses: ('pending' | 'in_progress' | 'completed' | 'cancelled')[] = ['pending', 'in_progress', 'completed', 'cancelled']
const riskLevels = ['high', 'medium', 'low']

export function mockUser(): User {
  return {
    id: 1,
    username: 'admin',
    realName: '系统管理员',
    email: 'admin@tunnel.com',
    phone: '13800138000',
    role: 'admin',
    avatar: '',
    status: 1,
    createTime: formatDate(Date.now() - 86400000 * 365),
    updateTime: formatDate(Date.now())
  }
}

export function mockLoginResponse() {
  return {
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock-token.' + Math.random().toString(36).substring(2),
    user: mockUser()
  }
}

export function mockTunnelList(count: number = 5): TunnelStructure[] {
  return generateMockData((index) => ({
    id: index + 1,
    name: tunnelNames[index % tunnelNames.length],
    code: `TN-${String(index + 1).padStart(4, '0')}`,
    length: Math.floor(Math.random() * 5000) + 1000,
    width: Math.floor(Math.random() * 5) + 10,
    height: Math.floor(Math.random() * 5) + 8,
    location: `陕西省西安市${['长安区', '灞桥区', '雁塔区', '未央区'][index % 4]}`,
    status: 1,
    description: `${tunnelNames[index % tunnelNames.length]}是一条重要的交通隧道，全长约${Math.floor(Math.random() * 5000) + 1000}米。`,
    createTime: formatDate(Date.now() - Math.random() * 86400000 * 365)
  }), count)
}

export function mockSectionList(tunnelId: number, count: number = 10): TunnelSection[] {
  return generateMockData((index) => ({
    id: index + 1,
    tunnelId,
    tunnelName: tunnelNames[(tunnelId - 1) % tunnelNames.length],
    sectionName: `第${index + 1}管片`,
    startMileage: index * 100,
    endMileage: (index + 1) * 100,
    sectionType: sectionTypes[index % sectionTypes.length],
    liningType: liningTypes[index % liningTypes.length],
    status: Math.random() > 0.2 ? 1 : 0,
    createTime: formatDate(Date.now() - Math.random() * 86400000 * 365)
  }), count)
}

export function mockAlarmList(count: number = 20): AlarmEvent[] {
  return generateMockData((index) => {
    const level = alarmLevels[index % alarmLevels.length]
    const status: ('pending' | 'processing' | 'resolved') = 
      index % 3 === 0 ? 'pending' : index % 3 === 1 ? 'processing' : 'resolved'
    return {
      id: index + 1,
      alarmType: alarmTypes[index % alarmTypes.length],
      alarmLevel: level,
      alarmTime: formatDate(Date.now() - Math.random() * 86400000 * 7),
      location: `${tunnelNames[index % tunnelNames.length]} K${(Math.random() * 10).toFixed(2)}`,
      mileage: Math.floor(Math.random() * 5000),
      deviceId: Math.floor(Math.random() * 100) + 1,
      deviceName: `${deviceTypes[index % deviceTypes.length]}-${String(index + 1).padStart(3, '0')}`,
      description: `${alarmTypes[index % alarmTypes.length]}告警，请及时处理。`,
      status,
      handler: status === 'pending' ? '' : ['张三', '李四', '王五'][index % 3],
      handleTime: status === 'pending' ? '' : formatDate(Date.now() - Math.random() * 86400000),
      handleResult: status === 'pending' ? '' : '已处理，问题已解决'
    }
  }, count)
}

export function mockDeviceList(count: number = 30): Device[] {
  return generateMockData((index) => ({
    id: index + 1,
    deviceCode: `DEV-${String(index + 1).padStart(6, '0')}`,
    deviceName: `${deviceTypes[index % deviceTypes.length]}-${String(index + 1).padStart(3, '0')}`,
    deviceType: deviceTypes[index % deviceTypes.length],
    manufacturer: manufacturers[index % manufacturers.length],
    model: `MODEL-${String(index + 1).padStart(3, '0')}`,
    installDate: formatDate(Date.now() - Math.random() * 86400000 * 365 * 3),
    warrantyPeriod: Math.floor(Math.random() * 3) + 1,
    status: Math.floor(Math.random() * 4),
    location: `${tunnelNames[index % tunnelNames.length]} K${(index * 0.5).toFixed(2)}`,
    tunnelId: (index % 5) + 1,
    tunnelName: tunnelNames[index % tunnelNames.length],
    lastMaintenance: formatDate(Date.now() - Math.random() * 86400000 * 30),
    nextMaintenance: formatDate(Date.now() + Math.random() * 86400000 * 30)
  }), count)
}

export function mockLifetimeAssessment(deviceId: number): LifetimeAssessment {
  const healthScore = Math.floor(Math.random() * 40) + 60
  const itemNames = ['外观检查', '绝缘测试', '接地测试', '功能测试', '通信测试', '精度校准']
  return {
    id: 1,
    deviceId,
    deviceName: `${deviceTypes[deviceId % deviceTypes.length]}-${String(deviceId).padStart(3, '0')}`,
    assessmentDate: formatDate(Date.now()),
    healthScore,
    remainingLife: Math.floor(healthScore / 10),
    riskLevel: healthScore >= 80 ? 'low' : healthScore >= 60 ? 'medium' : 'high',
    assessmentItems: itemNames.map((name, i) => ({
      name,
      score: Math.floor(Math.random() * 40) + 60,
      weight: [0.15, 0.2, 0.15, 0.2, 0.15, 0.15][i],
      status: Math.random() > 0.2 ? 'normal' : 'warning'
    })),
    recommendations: healthScore >= 80 
      ? '设备状态良好，建议按正常周期维护。' 
      : healthScore >= 60 
        ? '设备存在一定损耗，建议缩短维护周期，重点关注绝缘和通信性能。'
        : '设备老化严重，建议制定更换计划，增加巡检频次。'
  }
}

export function mockInspectionTaskList(count: number = 15): InspectionTask[] {
  return generateMockData((index) => {
    const status = taskStatuses[index % taskStatuses.length]
    const startDate = Date.now() - Math.random() * 86400000 * 30
    const endDate = startDate + Math.random() * 86400000 * 7
    return {
      id: index + 1,
      taskCode: `INSP-${String(index + 1).padStart(6, '0')}`,
      taskName: `${tunnelNames[index % tunnelNames.length]}${['日常巡检', '月度检查', '季度专项检查', '年度检测'][index % 4]}`,
      taskType: ['日常巡检', '专项检查', '定期检测', '应急检查'][index % 4],
      tunnelId: (index % 5) + 1,
      tunnelName: tunnelNames[index % tunnelNames.length],
      startTime: formatDate(startDate),
      endTime: formatDate(endDate),
      inspector: ['张三', '李四', '王五', '赵六', '钱七'][index % 5],
      status,
      checkItems: [
        { id: 1, name: '衬砌外观检查', standard: '无明显裂缝、变形', result: '', status: status === 'completed' ? (Math.random() > 0.1 ? 'pass' : 'fail') : 'pending', remark: '' },
        { id: 2, name: '渗漏水检查', standard: '无渗漏水现象', result: '', status: status === 'completed' ? (Math.random() > 0.15 ? 'pass' : 'fail') : 'pending', remark: '' },
        { id: 3, name: '路面状况检查', standard: '平整无破损', result: '', status: status === 'completed' ? (Math.random() > 0.05 ? 'pass' : 'fail') : 'pending', remark: '' },
        { id: 4, name: '照明系统检查', standard: '照明正常', result: '', status: status === 'completed' ? (Math.random() > 0.1 ? 'pass' : 'fail') : 'pending', remark: '' },
        { id: 5, name: '通风系统检查', standard: '通风良好', result: '', status: status === 'completed' ? (Math.random() > 0.1 ? 'pass' : 'fail') : 'pending', remark: '' }
      ],
      createTime: formatDate(startDate - 86400000)
    }
  }, count)
}

export function mockAcousticDataList(count: number = 24): AcousticData[] {
  return generateMockData((index) => ({
    id: index + 1,
    deviceId: (index % 10) + 1,
    collectTime: formatDate(Date.now() - (23 - index) * 3600000),
    frequency: Math.floor(Math.random() * 1000) + 500,
    amplitude: Math.floor(Math.random() * 50) + 10,
    duration: Math.floor(Math.random() * 1000) + 100,
    waveformData: Array.from({ length: 100 }, () => Math.random() * 100 - 50),
    spectrumData: Array.from({ length: 50 }, () => Math.random() * 100)
  }), count)
}

export function mockLeakLocationList(count: number = 10): LeakLocation[] {
  return generateMockData((index) => ({
    id: index + 1,
    mileage: (index + 1) * 500,
    location: `${tunnelNames[index % tunnelNames.length]} K${((index + 1) * 0.5).toFixed(2)}`,
    leakType: ['点漏', '线漏', '面漏', '涌水'][index % 4],
    leakLevel: riskLevels[index % riskLevels.length],
    detectionTime: formatDate(Date.now() - Math.random() * 86400000 * 30),
    acousticData: mockAcousticDataList(1)[0],
    status: Math.random() > 0.3 ? 1 : 0
  }), count)
}

export function mockRiskHeatmap(tunnelId: number): RiskHeatmap {
  const width = 20
  const height = 10
  const heatmapData = []
  for (let y = 0; y < height; y++) {
    for (let x = 0; x < width; x++) {
      const value = Math.random() * 100
      heatmapData.push({
        x,
        y,
        value,
        riskLevel: value > 70 ? 'high' : value > 40 ? 'medium' : 'low'
      })
    }
  }
  return {
    id: tunnelId,
    tunnelId,
    tunnelName: tunnelNames[(tunnelId - 1) % tunnelNames.length],
    generateTime: formatDate(Date.now()),
    riskLevel: riskLevels[Math.floor(Math.random() * 3)],
    heatmapData,
    riskFactors: [
      { name: '结构应力', weight: 0.3, score: Math.floor(Math.random() * 100), description: '衬砌结构应力监测数据' },
      { name: '渗漏水', weight: 0.25, score: Math.floor(Math.random() * 100), description: '渗漏水监测数据' },
      { name: '振动影响', weight: 0.2, score: Math.floor(Math.random() * 100), description: '车辆振动影响评估' },
      { name: '环境腐蚀', weight: 0.15, score: Math.floor(Math.random() * 100), description: '环境腐蚀程度评估' },
      { name: '材料老化', weight: 0.1, score: Math.floor(Math.random() * 100), description: '建筑材料老化程度' }
    ]
  }
}

export function mockUserList(count: number = 10): User[] {
  const names = ['张三', '李四', '王五', '赵六', '钱七', '孙八', '周九', '吴十', '郑十一', '王十二']
  const roles = ['admin', 'engineer', 'technician', 'viewer']
  return generateMockData((index) => ({
    id: index + 1,
    username: `user${index + 1}`,
    realName: names[index],
    email: `user${index + 1}@tunnel.com`,
    phone: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
    role: roles[index % roles.length],
    avatar: '',
    status: Math.random() > 0.1 ? 1 : 0,
    createTime: formatDate(Date.now() - Math.random() * 86400000 * 365),
    updateTime: formatDate(Date.now() - Math.random() * 86400000 * 30)
  }), count)
}

export function mockSystemConfigList(count: number = 20): SystemConfig[] {
  const configs = [
    { key: 'system.title', value: '隧道结构健康监测系统', type: 'basic', desc: '系统标题' },
    { key: 'system.logo', value: '/logo.png', type: 'basic', desc: '系统Logo' },
    { key: 'system.copyright', value: '© 2024 隧道监测技术有限公司', type: 'basic', desc: '版权信息' },
    { key: 'security.token_expire', value: '86400', type: 'security', desc: 'Token过期时间(秒)' },
    { key: 'security.password_min_length', value: '8', type: 'security', desc: '密码最小长度' },
    { key: 'security.login_fail_limit', value: '5', type: 'security', desc: '登录失败次数限制' },
    { key: 'monitoring.data_retention', value: '365', type: 'monitoring', desc: '监测数据保留天数' },
    { key: 'monitoring.realtime_interval', value: '1000', type: 'monitoring', desc: '实时监测间隔(毫秒)' },
    { key: 'alarm.auto_notify', value: 'true', type: 'alarm', desc: '告警自动通知' },
    { key: 'alarm.sound_enable', value: 'true', type: 'alarm', desc: '告警声音提示' },
    { key: 'alarm.email_notify', value: 'true', type: 'alarm', desc: '告警邮件通知' },
    { key: 'alarm.sms_notify', value: 'false', type: 'alarm', desc: '告警短信通知' },
    { key: 'inspection.remind_days', value: '3', type: 'inspection', desc: '巡检提醒天数' },
    { key: 'inspection.auto_create', value: 'true', type: 'inspection', desc: '自动创建巡检任务' },
    { key: 'report.auto_generate', value: 'true', type: 'report', desc: '自动生成报告' },
    { key: 'report.monthly_day', value: '1', type: 'report', desc: '月报生成日期' },
    { key: 'device.maintenance_warn', value: '7', type: 'device', desc: '维护预警天数' },
    { key: 'device.lifetime_warn', value: '30', type: 'device', desc: '寿命预警天数' },
    { key: 'map.default_zoom', value: '12', type: 'map', desc: '地图默认缩放级别' },
    { key: 'map.default_center', value: '108.9398,34.3416', type: 'map', desc: '地图默认中心点' }
  ]
  return generateMockData((index) => ({
    id: index + 1,
    configKey: configs[index % configs.length].key,
    configValue: configs[index % configs.length].value,
    configType: configs[index % configs.length].type,
    description: configs[index % configs.length].desc,
    updateTime: formatDate(Date.now() - Math.random() * 86400000 * 30)
  }), count)
}

export function mockDashboardStats() {
  return {
    totalTunnels: 5,
    totalDevices: 128,
    activeAlarms: 12,
    pendingInspections: 8,
    todayDataPoints: 125890,
    systemHealth: 92
  }
}

export function mockAlarmTrend(days: number = 7) {
  const data = []
  const now = Date.now()
  for (let i = days - 1; i >= 0; i--) {
    data.push({
      date: formatDate(now - i * 86400000, 'MM-DD'),
      critical: Math.floor(Math.random() * 3),
      warning: Math.floor(Math.random() * 8) + 2,
      info: Math.floor(Math.random() * 15) + 5
    })
  }
  return data
}

export function mockDeviceStatus() {
  return [
    { name: '正常运行', value: 105, color: '#10B981' },
    { name: '警告状态', value: 15, color: '#F59E0B' },
    { name: '故障离线', value: 5, color: '#EF4444' },
    { name: '维护中', value: 3, color: '#64748B' }
  ]
}

export function mockTunnelHealthRank() {
  return tunnelNames.map((name, index) => ({
    tunnelName: name,
    healthScore: Math.floor(Math.random() * 30) + 70,
    riskLevel: index === 0 ? 'medium' : 'low'
  })).sort((a, b) => b.healthScore - a.healthScore)
}

export function mockRecentAlarms(limit: number = 10) {
  return mockAlarmList(limit).map(alarm => ({
    id: alarm.id,
    alarmType: alarm.alarmType,
    alarmLevel: alarm.alarmLevel,
    alarmTime: alarm.alarmTime,
    location: alarm.location,
    status: alarm.status
  }))
}

export function mockMonitoringData(hours: number = 24) {
  const data = []
  const now = Date.now()
  for (let i = hours - 1; i >= 0; i--) {
    data.push({
      time: formatDate(now - i * 3600000, 'HH:00'),
      noiseLevel: Math.floor(Math.random() * 20) + 50,
      vibration: Math.floor(Math.random() * 10) + 5,
      temperature: Math.floor(Math.random() * 10) + 15
    })
  }
  return data
}
