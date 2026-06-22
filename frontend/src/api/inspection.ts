import { get, post, put, del } from '@/utils/request'
import type { InspectionTask, PageResult, PageRequest, Result } from '@/types'

export function getInspectionPlanList(params?: PageRequest & { status?: string; tunnelId?: number }): Promise<Result<PageResult<InspectionTask>>> {
  return get<PageResult<InspectionTask>>('/inspection/plan/list', params)
}

export function getInspectionPlanDetail(id: number): Promise<Result<InspectionTask>> {
  return get<InspectionTask>(`/inspection/plan/${id}`)
}

export function createInspectionPlan(data: Partial<InspectionTask>): Promise<Result<InspectionTask>> {
  return post<InspectionTask>('/inspection/plan', data)
}

export function updateInspectionPlan(id: number, data: Partial<InspectionTask>): Promise<Result<InspectionTask>> {
  return put<InspectionTask>(`/inspection/plan/${id}`, data)
}

export function deleteInspectionPlan(id: number): Promise<Result<void>> {
  return del<void>(`/inspection/plan/${id}`)
}

export function getInspectionTaskList(params?: PageRequest & { status?: string; inspector?: string }): Promise<Result<PageResult<InspectionTask>>> {
  return get<PageResult<InspectionTask>>('/inspection/task/list', params)
}

export function getInspectionTaskDetail(id: number): Promise<Result<InspectionTask>> {
  return get<InspectionTask>(`/inspection/task/${id}`)
}

export function startInspectionTask(id: number): Promise<Result<InspectionTask>> {
  return put<InspectionTask>(`/inspection/task/${id}/start`)
}

export function completeInspectionTask(id: number, data: { checkItems: any[] }): Promise<Result<InspectionTask>> {
  return put<InspectionTask>(`/inspection/task/${id}/complete`, data)
}

export function getInspectionStatistics(): Promise<Result<{
  totalPlans: number
  totalTasks: number
  completedTasks: number
  pendingTasks: number
  inProgressTasks: number
  completionRate: number
  thisWeekTasks: number
}>> {
  return get('/inspection/statistics')
}
