import { get, post, put, del } from '@/utils/request'
import type { Device, LifetimeAssessment, PageResult, PageRequest, Result } from '@/types'

export function getDeviceList(params?: PageRequest & { type?: string; status?: number; tunnelId?: number }): Promise<Result<PageResult<Device>>> {
  return get<PageResult<Device>>('/device/list', params)
}

export function getDeviceDetail(id: number): Promise<Result<Device>> {
  return get<Device>(`/device/${id}`)
}

export function createDevice(data: Partial<Device>): Promise<Result<Device>> {
  return post<Device>('/device', data)
}

export function updateDevice(id: number, data: Partial<Device>): Promise<Result<Device>> {
  return put<Device>(`/device/${id}`, data)
}

export function deleteDevice(id: number): Promise<Result<void>> {
  return del<void>(`/device/${id}`)
}

export function getDeviceStatistics(): Promise<Result<{
  total: number
  normal: number
  warning: number
  fault: number
  maintenance: number
  byType: { type: string; count: number }[]
}>> {
  return get('/device/statistics')
}

export function getLifetimeAssessmentList(deviceId?: number, params?: PageRequest): Promise<Result<PageResult<LifetimeAssessment>>> {
  return get<PageResult<LifetimeAssessment>>('/device/lifetime/list', { deviceId, ...params })
}

export function getLifetimeAssessmentDetail(id: number): Promise<Result<LifetimeAssessment>> {
  return get<LifetimeAssessment>(`/device/lifetime/${id}`)
}

export function createLifetimeAssessment(data: Partial<LifetimeAssessment>): Promise<Result<LifetimeAssessment>> {
  return post<LifetimeAssessment>('/device/lifetime', data)
}

export function getLifetimeTrend(deviceId: number, months: number = 12): Promise<Result<{ month: string; healthScore: number; remainingLife: number }[]>> {
  return get(`/device/${deviceId}/lifetime/trend`, { months })
}
