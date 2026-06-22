export interface User {
  id: number
  username: string
  realName: string
  email: string
  phone: string
  role: string
  avatar: string
  status: number
  createTime: string
  updateTime: string
}

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  user: User
}

export interface PageRequest {
  pageNum: number
  pageSize: number
  keyword?: string
}

export interface PageResult<T> {
  list: T[]
  total: number
  pageNum: number
  pageSize: number
  pages: number
}

export interface Result<T = any> {
  code: number
  message: string
  data: T
  timestamp: number
}

export interface TunnelStructure {
  id: number
  name: string
  code: string
  length: number
  width: number
  height: number
  location: string
  status: number
  description: string
  createTime: string
}

export interface TunnelSection {
  id: number
  tunnelId: number
  tunnelName: string
  sectionName: string
  startMileage: number
  endMileage: number
  sectionType: string
  liningType: string
  status: number
  createTime: string
}

export interface AlarmEvent {
  id: number
  alarmType: string
  alarmLevel: 'critical' | 'warning' | 'info'
  alarmTime: string
  location: string
  mileage: number
  deviceId: number
  deviceName: string
  description: string
  status: 'pending' | 'processing' | 'resolved'
  handler: string
  handleTime: string
  handleResult: string
}

export interface Device {
  id: number
  deviceCode: string
  deviceName: string
  deviceType: string
  manufacturer: string
  model: string
  installDate: string
  warrantyPeriod: number
  status: number
  location: string
  tunnelId: number
  tunnelName: string
  lastMaintenance: string
  nextMaintenance: string
}

export interface LifetimeAssessment {
  id: number
  deviceId: number
  deviceName: string
  assessmentDate: string
  healthScore: number
  remainingLife: number
  riskLevel: string
  assessmentItems: AssessmentItem[]
  recommendations: string
}

export interface AssessmentItem {
  name: string
  score: number
  weight: number
  status: string
}

export interface InspectionTask {
  id: number
  taskCode: string
  taskName: string
  taskType: string
  tunnelId: number
  tunnelName: string
  startTime: string
  endTime: string
  inspector: string
  status: 'pending' | 'in_progress' | 'completed' | 'cancelled'
  checkItems: CheckItem[]
  createTime: string
}

export interface CheckItem {
  id: number
  name: string
  standard: string
  result: string
  status: 'pass' | 'fail' | 'pending'
  remark: string
}

export interface LeakLocation {
  id: number
  mileage: number
  location: string
  leakType: string
  leakLevel: string
  detectionTime: string
  acousticData: AcousticData
  status: number
}

export interface AcousticData {
  id: number
  deviceId: number
  collectTime: string
  frequency: number
  amplitude: number
  duration: number
  waveformData: number[]
  spectrumData: number[]
}

export interface RiskHeatmap {
  id: number
  tunnelId: number
  tunnelName: string
  generateTime: string
  riskLevel: string
  heatmapData: HeatmapPoint[]
  riskFactors: RiskFactor[]
}

export interface HeatmapPoint {
  x: number
  y: number
  value: number
  riskLevel: string
}

export interface RiskFactor {
  name: string
  weight: number
  score: number
  description: string
}

export interface SystemConfig {
  id: number
  configKey: string
  configValue: string
  configType: string
  description: string
  updateTime: string
}

export interface MenuItem {
  path: string
  name: string
  icon?: string
  children?: MenuItem[]
  hidden?: boolean
}

export interface ChartData {
  name: string
  value: number
  color?: string
}

export interface TimeSeriesData {
  time: string
  value: number
  name?: string
}
