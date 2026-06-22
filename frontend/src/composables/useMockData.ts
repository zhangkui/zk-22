import { ref, onMounted } from 'vue'
import type { Result } from '@/types'

export function useMockData<T, P extends any[]>(
  mockFn: (...args: P) => T,
  delay: number = 300
) {
  const loading = ref(false)
  const error = ref<Error | null>(null)

  async function execute(...args: P): Promise<Result<T>> {
    loading.value = true
    error.value = null
    
    try {
      await new Promise(resolve => setTimeout(resolve, delay))
      const data = mockFn(...args)
      return {
        code: 200,
        message: 'success',
        data,
        timestamp: Date.now()
      }
    } catch (err) {
      error.value = err as Error
      return {
        code: 500,
        message: (err as Error).message,
        data: null as any,
        timestamp: Date.now()
      }
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    error,
    execute
  }
}

export function useMockApi<T, P extends any[]>(
  mockFn: (...args: P) => T,
  delay: number = 300
) {
  return function (...args: P): Promise<Result<T>> {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          code: 200,
          message: 'success',
          data: mockFn(...args),
          timestamp: Date.now()
        })
      }, delay)
    })
  }
}

export function useMockPageData<T>(
  mockListFn: (count: number) => T[],
  delay: number = 300
) {
  return async function (params: { pageNum: number; pageSize: number; [key: string]: any }) {
    await new Promise(resolve => setTimeout(resolve, delay))
    
    const allData = mockListFn(100)
    const { pageNum, pageSize } = params
    const start = (pageNum - 1) * pageSize
    const end = start + pageSize
    const list = allData.slice(start, end)
    
    return {
      code: 200,
      message: 'success',
      data: {
        list,
        total: allData.length,
        pageNum,
        pageSize,
        pages: Math.ceil(allData.length / pageSize)
      },
      timestamp: Date.now()
    }
  }
}
