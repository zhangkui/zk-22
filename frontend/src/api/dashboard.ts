import { get } from '@/utils/request'
import type { Result } from '@/types'

export function getDashboardStats(): Promise<Result<{
  totalTunnels: number
  totalDevices: number
  activeAlarms: number
  pendingInspections: number
  todayDataPoints: number
  systemHealth: number
}>> {
  return get('/dashboard/stats')
}

export function getAlarmTrend(days: number = 7): Promise<Result<{
  date: string
  critical: number
  warning: number
  info: number
}[]>> {
  return get('/dashboard/alarm-trend', { days })
}

export function getDeviceStatus(): Promise<Result<{
  name: string
  value: number
  color: string
}[]>> {
  return get('/dashboard/device-status')
}

export function getTunnelHealthRank(): Promise<Result<{
  tunnelName: string
  healthScore: number
  riskLevel: string
}[]>> {
  return get('/dashboard/tunnel-health-rank')
}

export function getRecentAlarms(limit: number = 10): Promise<Result<{
  id: number
  alarmType: string
  alarmLevel: string
  alarmTime: string
  location: string
  status: string
}[]>> {
  return get('/dashboard/recent-alarms', { limit })
}

export function getMonitoringData(days: number = 24): Promise<Result<{
  time: string
  noiseLevel: number
  vibration: number
  temperature: number
}[]>> {
  return get('/dashboard/monitoring-data', { days })
}
