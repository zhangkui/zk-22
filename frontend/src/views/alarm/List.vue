<template>
  <div class="page-wrapper">
    <div class="page-header">
      <h1 class="page-title">告警列表</h1>
      <p class="page-subtitle">隧道告警事件管理与处理</p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">告警总数</p>
            <p class="text-3xl font-bold mt-2 text-info">{{ stats.total }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-blue-500/20 flex-center">
            <el-icon :size="24" class="text-blue-400"><Bell /></el-icon>
          </div>
        </div>
      </div>
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">紧急告警</p>
            <p class="text-3xl font-bold mt-2 text-danger">{{ stats.critical }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-red-500/20 flex-center">
            <el-icon :size="24" class="text-red-400"><Warning /></el-icon>
          </div>
        </div>
      </div>
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">待处理</p>
            <p class="text-3xl font-bold mt-2 text-warning">{{ stats.pending }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-yellow-500/20 flex-center">
            <el-icon :size="24" class="text-yellow-400"><Clock /></el-icon>
          </div>
        </div>
      </div>
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">今日新增</p>
            <p class="text-3xl font-bold mt-2 text-accent-400">{{ stats.todayCount }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-accent-500/20 flex-center">
            <el-icon :size="24" class="text-accent-400"><TrendCharts /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-body">
        <el-form :inline="true" :model="searchForm" class="flex flex-wrap gap-4 mb-4">
          <el-form-item label="告警级别">
            <el-select v-model="searchForm.level" placeholder="选择级别" clearable style="width: 150px">
              <el-option label="紧急" value="critical" />
              <el-option label="警告" value="warning" />
              <el-option label="提示" value="info" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 150px">
              <el-option label="待处理" value="pending" />
              <el-option label="处理中" value="processing" />
              <el-option label="已解决" value="resolved" />
            </el-select>
          </el-form-item>
          <el-form-item label="告警类型">
            <el-input v-model="searchForm.keyword" placeholder="请输入关键词" clearable style="width: 180px" />
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 280px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><RefreshLeft /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>

        <el-table
          v-loading="loading"
          :data="dataList"
          stripe
          style="width: 100%"
        >
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="alarmType" label="告警类型" width="120" />
          <el-table-column prop="alarmLevel" label="级别" width="100">
            <template #default="{ row }">
              <el-tag
                :type="row.alarmLevel === 'critical' ? 'danger' : row.alarmLevel === 'warning' ? 'warning' : 'info'"
                size="small"
              >
                {{ row.alarmLevel === 'critical' ? '紧急' : row.alarmLevel === 'warning' ? '警告' : '提示' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="location" label="位置" min-width="180" />
          <el-table-column prop="deviceName" label="关联设备" width="150" />
          <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
          <el-table-column prop="alarmTime" label="告警时间" width="180" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag
                :type="row.status === 'resolved' ? 'success' : row.status === 'processing' ? 'primary' : 'warning'"
                size="small"
              >
                {{ row.status === 'resolved' ? '已解决' : row.status === 'processing' ? '处理中' : '待处理' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="handler" label="处理人" width="100" />
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="{ row }">
              <el-button v-if="row.status === 'pending'" type="primary" size="small" @click="handleProcess(row)">
                处理
              </el-button>
              <el-button type="primary" link size="small" @click="handleView(row)">详情</el-button>
              <el-button type="primary" link size="small" @click="handleReplay(row)">回放</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="mt-4 flex justify-end">
          <el-pagination
            v-model:current-page="pagination.pageNum"
            v-model:page-size="pagination.pageSize"
            :page-sizes="pagination.pageSizes"
            :total="pagination.total"
            :layout="pagination.layout"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <el-dialog
      v-model="processDialogVisible"
      title="处理告警"
      width="500px"
    >
      <el-form :model="processForm" label-width="80px">
        <el-form-item label="处理结果">
          <el-radio-group v-model="processForm.status">
            <el-radio value="processing">标记处理中</el-radio>
            <el-radio value="resolved">标记已解决</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理说明">
          <el-input
            v-model="processForm.handleResult"
            type="textarea"
            :rows="4"
            placeholder="请输入处理说明"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitProcess">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMockPageData } from '@/composables/useMockData'
import { mockAlarmList } from '@/utils/mock'
import { ElMessage } from 'element-plus'
import { Search, RefreshLeft, Bell, Warning, Clock, TrendCharts } from '@element-plus/icons-vue'
import type { AlarmEvent } from '@/types'

const router = useRouter()
const loading = ref(false)
const dataList = ref<AlarmEvent[]>([])
const processDialogVisible = ref(false)
const currentAlarm = ref<AlarmEvent | null>(null)

const stats = reactive({
  total: 156,
  critical: 12,
  pending: 28,
  todayCount: 8
})

const searchForm = reactive({
  level: undefined as string | undefined,
  status: undefined as string | undefined,
  keyword: '',
  dateRange: [] as any[]
})

const processForm = reactive({
  status: 'processing',
  handleResult: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  pageSizes: [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper'
})

const fetchData = useMockPageData(mockAlarmList)

async function loadData() {
  loading.value = true
  try {
    const res = await fetchData({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      level: searchForm.level,
      status: searchForm.status,
      keyword: searchForm.keyword
    })
    if (res.code === 200) {
      dataList.value = res.data.list
      pagination.total = res.data.total
    }
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.pageNum = 1
  loadData()
}

function handleReset() {
  searchForm.level = undefined
  searchForm.status = undefined
  searchForm.keyword = ''
  searchForm.dateRange = []
  pagination.pageNum = 1
  loadData()
}

function handleProcess(row: AlarmEvent) {
  currentAlarm.value = row
  processForm.status = 'processing'
  processForm.handleResult = ''
  processDialogVisible.value = true
}

function submitProcess() {
  if (currentAlarm.value) {
    currentAlarm.value.status = processForm.status as any
    currentAlarm.value.handleResult = processForm.handleResult
    currentAlarm.value.handler = '当前用户'
    currentAlarm.value.handleTime = new Date().toISOString()
    ElMessage.success('告警处理成功')
    processDialogVisible.value = false
  }
}

function handleView(row: AlarmEvent) {
  ElMessage.info(`查看告警详情 ID: ${row.id}`)
}

function handleReplay(row: AlarmEvent) {
  router.push(`/alarm/replay?id=${row.id}`)
}

function handleSizeChange(size: number) {
  pagination.pageSize = size
  pagination.pageNum = 1
  loadData()
}

function handleCurrentChange(page: number) {
  pagination.pageNum = page
  loadData()
}

onMounted(() => {
  loadData()
})
</script>
