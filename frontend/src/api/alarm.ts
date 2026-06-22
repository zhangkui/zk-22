import { get, post, put } from '@/utils/request'
import type { AlarmEvent, PageResult, PageRequest, Result } from '@/types'

export function getAlarmList(params?: PageRequest & { status?: string; level?: string }): Promise<Result<PageResult<AlarmEvent>>> {
  return get<PageResult<AlarmEvent>>('/alarm/list', params)
}

export function getAlarmDetail(id: number): Promise<Result<AlarmEvent>> {
  return get<AlarmEvent>(`/alarm/${id}`)
}

export function handleAlarm(id: number, data: { status: string; handler: string; handleResult: string }): Promise<Result<AlarmEvent>> {
  return put<AlarmEvent>(`/alarm/${id}/handle`, data)
}

export function getAlarmStatistics(): Promise<Result<{
  total: number
  critical: number
  warning: number
  info: number
  pending: number
  processing: number
  resolved: number
  todayCount: number
  weekCount: number
  monthCount: number
}>> {
  return get('/alarm/statistics')
}

export function getAlarmReplayData(id: number): Promise<Result<{
  alarmInfo: AlarmEvent
  timeline: { time: string; event: string; data: any }[]
  sensorData: { time: string; value: number }[]
}>> {
  return get(`/alarm/${id}/replay`)
}

export function getAlarmTrend(days: number = 7): Promise<Result<{ date: string; count: number; level: string }[]>> {
  return get('/alarm/trend', { days })
}
