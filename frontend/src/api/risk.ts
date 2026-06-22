import { get, post } from '@/utils/request'
import type { RiskHeatmap, PageResult, PageRequest, Result } from '@/types'

export function getRiskHeatmapList(params?: PageRequest & { tunnelId?: number; riskLevel?: string }): Promise<Result<PageResult<RiskHeatmap>>> {
  return get<PageResult<RiskHeatmap>>('/risk/heatmap/list', params)
}

export function getRiskHeatmapDetail(id: number): Promise<Result<RiskHeatmap>> {
  return get<RiskHeatmap>(`/risk/heatmap/${id}`)
}

export function generateRiskHeatmap(tunnelId: number): Promise<Result<RiskHeatmap>> {
  return post<RiskHeatmap>(`/risk/heatmap/generate`, { tunnelId })
}

export function getCurrentRiskHeatmap(tunnelId: number): Promise<Result<RiskHeatmap>> {
  return get<RiskHeatmap>(`/risk/heatmap/current/${tunnelId}`)
}

export function getRiskReportList(params?: PageRequest & { tunnelId?: number; startDate?: string; endDate?: string }): Promise<Result<PageResult<{
  id: number
  tunnelId: number
  tunnelName: string
  reportType: string
  reportDate: string
  overallRisk: string
  summary: string
  pdfUrl: string
}>>> {
  return get('/risk/report/list', params)
}

export function getRiskReportDetail(id: number): Promise<Result<{
  id: number
  tunnelId: number
  tunnelName: string
  reportType: string
  reportDate: string
  overallRisk: string
  riskSummary: any
  riskDetails: any[]
  recommendations: string[]
  appendix: any
}>> {
  return get(`/risk/report/${id}`)
}

export function generateRiskReport(tunnelId: number, reportType: string): Promise<Result<{ reportId: number; pdfUrl: string }>> {
  return post('/risk/report/generate', { tunnelId, reportType })
}

export function getRiskStatistics(tunnelId?: number): Promise<Result<{
  highRisk: number
  mediumRisk: number
  lowRisk: number
  riskTrend: { date: string; high: number; medium: number; low: number }[]
  topRiskAreas: { area: string; riskLevel: string; score: number }[]
}>> {
  return get('/risk/statistics', { tunnelId })
}
