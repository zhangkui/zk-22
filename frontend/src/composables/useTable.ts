import { ref, reactive, computed } from 'vue'
import type { PageRequest, PageResult } from '@/types'

interface UseTableOptions<T> {
  fetchFn: (params: PageRequest & Record<string, any>) => Promise<{ code: number; data: PageResult<T>; message: string }>
  defaultParams?: Record<string, any>
  immediate?: boolean
}

export function useTable<T = any>(options: UseTableOptions<T>) {
  const { fetchFn, defaultParams = {}, immediate = true } = options

  const loading = ref(false)
  const dataList = ref<T[]>([])
  const total = ref(0)

  const pagination = reactive({
    pageNum: 1,
    pageSize: 10,
    total: 0,
    pageSizes: [10, 20, 50, 100],
    layout: 'total, sizes, prev, pager, next, jumper'
  })

  const searchParams = reactive<Record<string, any>>({ ...defaultParams })

  const hasData = computed(() => dataList.value.length > 0)

  async function fetchData() {
    loading.value = true
    try {
      const params: PageRequest & Record<string, any> = {
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize,
        ...searchParams
      }
      
      const res = await fetchFn(params)
      if (res.code === 200) {
        dataList.value = res.data.list
        total.value = res.data.total
        pagination.total = res.data.total
      }
    } catch (error) {
      console.error('获取数据失败:', error)
    } finally {
      loading.value = false
    }
  }

  function handleSizeChange(size: number) {
    pagination.pageSize = size
    pagination.pageNum = 1
    fetchData()
  }

  function handleCurrentChange(page: number) {
    pagination.pageNum = page
    fetchData()
  }

  function handleSearch() {
    pagination.pageNum = 1
    fetchData()
  }

  function handleReset() {
    Object.keys(searchParams).forEach(key => {
      if (key in defaultParams) {
        searchParams[key] = defaultParams[key]
      } else {
        delete searchParams[key]
      }
    })
    pagination.pageNum = 1
    fetchData()
  }

  function refresh() {
    fetchData()
  }

  if (immediate) {
    fetchData()
  }

  return {
    loading,
    dataList,
    total,
    pagination,
    searchParams,
    hasData,
    fetchData,
    handleSizeChange,
    handleCurrentChange,
    handleSearch,
    handleReset,
    refresh
  }
}
