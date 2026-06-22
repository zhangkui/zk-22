import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

export function formatDate(date: string | Date | number, format: string = 'YYYY-MM-DD HH:mm:ss'): string {
  return dayjs(date).format(format)
}

export function formatDateCN(date: string | Date | number): string {
  return dayjs(date).format('YYYY年MM月DD日 HH:mm:ss')
}

export function fromNow(date: string | Date | number): string {
  return dayjs(date).fromNow()
}

export function formatNumber(num: number, decimals: number = 2): string {
  if (num >= 100000000) {
    return (num / 100000000).toFixed(decimals) + '亿'
  } else if (num >= 10000) {
    return (num / 10000).toFixed(decimals) + '万'
  }
  return num.toFixed(decimals)
}

export function formatPercent(num: number, decimals: number = 2): string {
  return (num * 100).toFixed(decimals) + '%'
}

export function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

export function randomId(): string {
  return Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15)
}

export function debounce<T extends (...args: any[]) => any>(
  fn: T,
  delay: number = 300
): (...args: Parameters<T>) => void {
  let timer: ReturnType<typeof setTimeout> | null = null
  return function (this: any, ...args: Parameters<T>) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      fn.apply(this, args)
    }, delay)
  }
}

export function throttle<T extends (...args: any[]) => any>(
  fn: T,
  delay: number = 300
): (...args: Parameters<T>) => void {
  let last = 0
  return function (this: any, ...args: Parameters<T>) {
    const now = Date.now()
    if (now - last > delay) {
      last = now
      fn.apply(this, args)
    }
  }
}

export function deepClone<T>(obj: T): T {
  if (obj === null || typeof obj !== 'object') {
    return obj
  }
  if (obj instanceof Date) {
    return new Date(obj.getTime()) as any
  }
  if (obj instanceof Array) {
    return obj.map(item => deepClone(item)) as any
  }
  const clonedObj: any = {}
  for (const key in obj) {
    if (Object.prototype.hasOwnProperty.call(obj, key)) {
      clonedObj[key] = deepClone(obj[key])
    }
  }
  return clonedObj as T
}

export function getRiskLevelColor(level: string): string {
  const colorMap: Record<string, string> = {
    'high': '#EF4444',
    'critical': '#EF4444',
    'danger': '#EF4444',
    'medium': '#F59E0B',
    'warning': '#F59E0B',
    'low': '#10B981',
    'safe': '#10B981',
    'info': '#3B82F6'
  }
  return colorMap[level.toLowerCase()] || '#94A3B8'
}

export function getStatusColor(status: string): string {
  const colorMap: Record<string, string> = {
    'normal': '#10B981',
    'success': '#10B981',
    'active': '#10B981',
    'online': '#10B981',
    'running': '#10B981',
    'warning': '#F59E0B',
    'pending': '#F59E0B',
    'processing': '#3B82F6',
    'in_progress': '#3B82F6',
    'offline': '#64748B',
    'stopped': '#64748B',
    'fault': '#EF4444',
    'error': '#EF4444',
    'danger': '#EF4444'
  }
  return colorMap[status.toLowerCase()] || '#94A3B8'
}

export function getStatusText(status: string): string {
  const textMap: Record<string, string> = {
    'normal': '正常',
    'success': '成功',
    'active': '活跃',
    'online': '在线',
    'running': '运行中',
    'warning': '警告',
    'pending': '待处理',
    'processing': '处理中',
    'in_progress': '进行中',
    'completed': '已完成',
    'resolved': '已解决',
    'offline': '离线',
    'stopped': '已停止',
    'fault': '故障',
    'error': '错误',
    'danger': '危险',
    'cancelled': '已取消'
  }
  return textMap[status.toLowerCase()] || status
}

export function downloadFile(url: string, filename?: string): void {
  const link = document.createElement('a')
  link.href = url
  link.download = filename || ''
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

export function copyToClipboard(text: string): Promise<void> {
  return navigator.clipboard.writeText(text)
}

export function generateMockData<T>(generator: (index: number) => T, count: number): T[] {
  return Array.from({ length: count }, (_, index) => generator(index))
}

export { dayjs }
