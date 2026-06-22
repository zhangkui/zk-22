import { get } from '@/utils/request'
import type { AcousticData, LeakLocation, PageResult, PageRequest, Result } from '@/types'

export function getAcousticDataList(params?: PageRequest & { deviceId?: number; startTime?: string; endTime?: string }): Promise<Result<PageResult<AcousticData>>> {
  return get<PageResult<AcousticData>>('/monitoring/acoustic/list', params)
}

export function getAcousticDataDetail(id: number): Promise<Result<AcousticData>> {
  return get<AcousticData>(`/monitoring/acoustic/${id}`)
}

export function getRealTimeAcousticData(deviceId: number): Promise<Result<AcousticData>> {
  return get<AcousticData>(`/monitoring/acoustic/realtime/${deviceId}`)
}

export function getAcousticSpectrum(deviceId: number): Promise<Result<{ frequency: number[]; amplitude: number[] }>> {
  return get(`/monitoring/acoustic/spectrum/${deviceId}`)
}

export function getLeakLocationList(params?: PageRequest & { tunnelId?: number; level?: string; status?: number }): Promise<Result<PageResult<LeakLocation>>> {
  return get<PageResult<LeakLocation>>('/monitoring/leak/list', params)
}

export function getLeakLocationDetail(id: number): Promise<Result<LeakLocation>> {
  return get<LeakLocation>(`/monitoring/leak/${id}`)
}

export function getLeakLocationMap(tunnelId: number): Promise<Result<LeakLocation[]>> {
  return get<LeakLocation[]>(`/monitoring/leak/map/${tunnelId}`)
}

export function getMonitoringDashboard(): Promise<Result<{
  activeDevices: number
  dataPointsToday: number
  abnormalEvents: number
  avgNoiseLevel: number
  realTimeData: { time: string; value: number; deviceId: number }[]
}>> {
  return get('/monitoring/dashboard')
}
