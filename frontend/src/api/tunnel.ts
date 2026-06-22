import { get, post, put, del } from '@/utils/request'
import type { TunnelStructure, TunnelSection, PageResult, PageRequest, Result } from '@/types'

export function getTunnelList(params?: PageRequest): Promise<Result<PageResult<TunnelStructure>>> {
  return get<PageResult<TunnelStructure>>('/tunnel/list', params)
}

export function getTunnelDetail(id: number): Promise<Result<TunnelStructure>> {
  return get<TunnelStructure>(`/tunnel/${id}`)
}

export function createTunnel(data: Partial<TunnelStructure>): Promise<Result<TunnelStructure>> {
  return post<TunnelStructure>('/tunnel', data)
}

export function updateTunnel(id: number, data: Partial<TunnelStructure>): Promise<Result<TunnelStructure>> {
  return put<TunnelStructure>(`/tunnel/${id}`, data)
}

export function deleteTunnel(id: number): Promise<Result<void>> {
  return del<void>(`/tunnel/${id}`)
}

export function getSectionList(tunnelId?: number, params?: PageRequest): Promise<Result<PageResult<TunnelSection>>> {
  return get<PageResult<TunnelSection>>('/tunnel/section/list', { tunnelId, ...params })
}

export function getSectionDetail(id: number): Promise<Result<TunnelSection>> {
  return get<TunnelSection>(`/tunnel/section/${id}`)
}

export function createSection(data: Partial<TunnelSection>): Promise<Result<TunnelSection>> {
  return post<TunnelSection>('/tunnel/section', data)
}

export function updateSection(id: number, data: Partial<TunnelSection>): Promise<Result<TunnelSection>> {
  return put<TunnelSection>(`/tunnel/section/${id}`, data)
}

export function deleteSection(id: number): Promise<Result<void>> {
  return del<void>(`/tunnel/section/${id}`)
}

export function getTunnel3DModel(id: number): Promise<Result<{ modelUrl: string; modelData: any }>> {
  return get<{ modelUrl: string; modelData: any }>(`/tunnel/${id}/3d-model`)
}
