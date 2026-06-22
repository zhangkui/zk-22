<template>
  <div class="page-wrapper">
    <div class="page-header flex-between">
      <div>
        <h1 class="page-title">巡检计划</h1>
        <p class="page-subtitle">隧道巡检计划管理</p>
      </div>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增计划
      </el-button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">总计划数</p>
            <p class="text-3xl font-bold mt-2 text-accent-400">{{ stats.totalPlans }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-accent-500/20 flex-center">
            <el-icon :size="24" class="text-accent-400"><Document /></el-icon>
          </div>
        </div>
      </div>
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">本周任务</p>
            <p class="text-3xl font-bold mt-2 text-info">{{ stats.thisWeekTasks }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-blue-500/20 flex-center">
            <el-icon :size="24" class="text-blue-400"><Calendar /></el-icon>
          </div>
        </div>
      </div>
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">完成率</p>
            <p class="text-3xl font-bold mt-2 text-success">{{ stats.completionRate }}%</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-green-500/20 flex-center">
            <el-icon :size="24" class="text-green-400"><CircleCheck /></el-icon>
          </div>
        </div>
      </div>
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">进行中</p>
            <p class="text-3xl font-bold mt-2 text-warning">{{ stats.inProgressTasks }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-yellow-500/20 flex-center">
            <el-icon :size="24" class="text-yellow-400"><Loading /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-body">
        <el-form :inline="true" :model="searchForm" class="flex flex-wrap gap-4 mb-4">
          <el-form-item label="隧道">
            <el-select v-model="searchForm.tunnelId" placeholder="选择隧道" clearable style="width: 180px">
              <el-option label="秦岭隧道" :value="1" />
              <el-option label="太行山隧道" :value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 150px">
              <el-option label="待开始" value="pending" />
              <el-option label="进行中" value="in_progress" />
              <el-option label="已完成" value="completed" />
              <el-option label="已取消" value="cancelled" />
            </el-select>
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
          <el-table-column prop="taskCode" label="计划编号" width="150" />
          <el-table-column prop="taskName" label="计划名称" min-width="180" />
          <el-table-column prop="tunnelName" label="所属隧道" width="120" />
          <el-table-column prop="taskType" label="类型" width="100" />
          <el-table-column prop="inspector" label="负责人" width="100" />
          <el-table-column label="时间范围" min-width="280">
            <template #default="{ row }">
              <div>
                <p class="text-dark-text">{{ row.startTime }}</p>
                <p class="text-dark-muted text-xs">至 {{ row.endTime }}</p>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag
                :type="row.status === 'completed' ? 'success' : row.status === 'in_progress' ? 'primary' : row.status === 'pending' ? 'warning' : 'info'"
                size="small"
              >
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link size="small" @click="handleView(row)">查看</el-button>
              <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button type="primary" link size="small" @click="handleGenerate(row)">生成任务</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useMockPageData } from '@/composables/useMockData'
import { mockInspectionTaskList } from '@/utils/mock'
import { ElMessage } from 'element-plus'
import { Plus, Search, RefreshLeft, Document, Calendar, CircleCheck, Loading } from '@element-plus/icons-vue'
import { getStatusText } from '@/utils/index'
import type { InspectionTask } from '@/types'

const loading = ref(false)
const dataList = ref<InspectionTask[]>([])

const stats = reactive({
  totalPlans: 45,
  thisWeekTasks: 8,
  completionRate: 78,
  inProgressTasks: 5
})

const searchForm = reactive({
  tunnelId: undefined as number | undefined,
  status: undefined as string | undefined
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  pageSizes: [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper'
})

const fetchData = useMockPageData(mockInspectionTaskList)

async function loadData() {
  loading.value = true
  try {
    const res = await fetchData({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
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
  searchForm.tunnelId = undefined
  searchForm.status = undefined
  pagination.pageNum = 1
  loadData()
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

function handleAdd() {
  ElMessage.info('新增巡检计划功能开发中')
}

function handleEdit(row: InspectionTask) {
  ElMessage.info(`编辑计划: ${row.taskName}`)
}

function handleView(row: InspectionTask) {
  ElMessage.info(`查看计划: ${row.taskName}`)
}

function handleGenerate(row: InspectionTask) {
  ElMessage.success(`已为计划 "${row.taskName}" 生成巡检任务`)
}

onMounted(() => {
  loadData()
})
</script>
