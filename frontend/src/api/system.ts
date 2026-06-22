import { get, post, put, del } from '@/utils/request'
import type { User, SystemConfig, PageResult, PageRequest, Result } from '@/types'

export function getUserList(params?: PageRequest & { role?: string; status?: number }): Promise<Result<PageResult<User>>> {
  return get<PageResult<User>>('/system/user/list', params)
}

export function getUserDetail(id: number): Promise<Result<User>> {
  return get<User>(`/system/user/${id}`)
}

export function createUser(data: Partial<User> & { password: string }): Promise<Result<User>> {
  return post<User>('/system/user', data)
}

export function updateUser(id: number, data: Partial<User>): Promise<Result<User>> {
  return put<User>(`/system/user/${id}`, data)
}

export function deleteUser(id: number): Promise<Result<void>> {
  return del<void>(`/system/user/${id}`)
}

export function resetUserPassword(id: number, newPassword: string): Promise<Result<void>> {
  return put<void>(`/system/user/${id}/reset-password`, { newPassword })
}

export function getConfigList(params?: PageRequest & { configType?: string }): Promise<Result<PageResult<SystemConfig>>> {
  return get<PageResult<SystemConfig>>('/system/config/list', params)
}

export function getConfigByKey(key: string): Promise<Result<SystemConfig>> {
  return get<SystemConfig>(`/system/config/key/${key}`)
}

export function createConfig(data: Partial<SystemConfig>): Promise<Result<SystemConfig>> {
  return post<SystemConfig>('/system/config', data)
}

export function updateConfig(id: number, data: Partial<SystemConfig>): Promise<Result<SystemConfig>> {
  return put<SystemConfig>(`/system/config/${id}`, data)
}

export function deleteConfig(id: number): Promise<Result<void>> {
  return del<void>(`/system/config/${id}`)
}

export function getSystemInfo(): Promise<Result<{
  appName: string
  appVersion: string
  serverTime: string
  systemStatus: {
    cpuUsage: number
    memoryUsage: number
    diskUsage: number
    databaseStatus: string
    mqStatus: string
  }
}>> {
  return get('/system/info')
}
