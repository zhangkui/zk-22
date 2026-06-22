<template>
  <div class="page-wrapper">
    <div class="page-header">
      <h1 class="page-title">巡检任务</h1>
      <p class="page-subtitle">隧道巡检任务执行管理</p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
      <div class="lg:col-span-2">
        <div class="card">
          <div class="card-header">
            <h3 class="text-lg font-semibold text-dark-text">任务列表</h3>
            <div class="flex items-center space-x-2">
              <el-radio-group v-model="statusFilter" size="small">
                <el-radio-button value="all">全部</el-radio-button>
                <el-radio-button value="pending">待开始</el-radio-button>
                <el-radio-button value="in_progress">进行中</el-radio-button>
                <el-radio-button value="completed">已完成</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div class="card-body">
            <div class="space-y-3 max-h-[500px] overflow-y-auto">
              <div
                v-for="task in filteredTasks"
                :key="task.id"
                class="p-4 rounded-lg border border-dark-border hover:border-accent-500/50 transition-colors cursor-pointer"
                :class="{ 'bg-accent-500/10 border-accent-500': selectedTask?.id === task.id }"
                @click="selectTask(task)"
              >
                <div class="flex-between">
                  <div>
                    <h4 class="text-dark-text font-medium">{{ task.taskName }}</h4>
                    <p class="text-dark-muted text-xs mt-1">{{ task.tunnelName }} · {{ task.taskType }}</p>
                  </div>
                  <el-tag
                    :type="task.status === 'completed' ? 'success' : task.status === 'in_progress' ? 'primary' : task.status === 'pending' ? 'warning' : 'info'"
                    size="small"
                  >
                    {{ getStatusText(task.status) }}
                  </el-tag>
                </div>
                <div class="flex items-center justify-between mt-3 pt-3 border-t border-dark-border">
                  <div class="flex items-center text-dark-muted text-xs">
                    <el-icon class="mr-1"><User /></el-icon>
                    <span>{{ task.inspector }}</span>
                    <el-icon class="ml-4 mr-1"><Clock /></el-icon>
                    <span>{{ formatDate(task.startTime, 'MM-DD HH:mm') }}</span>
                  </div>
                  <div class="flex items-center space-x-2">
                    <el-button v-if="task.status === 'pending'" type="primary" size="small" @click.stop="startTask(task)">
                      开始
                    </el-button>
                    <el-button v-if="task.status === 'in_progress'" type="success" size="small" @click.stop="completeTask(task)">
                      完成
                    </el-button>
                    <el-button type="primary" link size="small" @click.stop="viewDetail(task)">
                      详情
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div>
        <div class="card">
          <div class="card-header">
            <h3 class="text-lg font-semibold text-dark-text">任务详情</h3>
          </div>
          <div class="card-body">
            <div v-if="selectedTask" class="space-y-4">
              <div>
                <p class="text-dark-muted text-xs">任务编号</p>
                <p class="text-dark-text font-medium">{{ selectedTask.taskCode }}</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">任务名称</p>
                <p class="text-dark-text">{{ selectedTask.taskName }}</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">隧道</p>
                <p class="text-dark-text">{{ selectedTask.tunnelName }}</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">时间</p>
                <p class="text-dark-text">{{ selectedTask.startTime }} ~ {{ selectedTask.endTime }}</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">检查项</p>
                <div class="mt-2 space-y-2">
                  <div
                    v-for="item in selectedTask.checkItems"
                    :key="item.id"
                    class="p-3 rounded-lg bg-dark-bg border border-dark-border"
                  >
                    <div class="flex-between">
                      <span class="text-dark-text text-sm">{{ item.name }}</span>
                      <el-tag
                        :type="item.status === 'pass' ? 'success' : item.status === 'fail' ? 'danger' : 'info'"
                        size="small"
                      >
                        {{ item.status === 'pass' ? '通过' : item.status === 'fail' ? '不通过' : '待检查' }}
                      </el-tag>
                    </div>
                    <p class="text-dark-muted text-xs mt-1">标准: {{ item.standard }}</p>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center text-dark-muted py-12">
              <el-icon :size="48" class="mb-3"><Tickets /></el-icon>
              <p>选择任务查看详情</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <h3 class="text-lg font-semibold text-dark-text">全部任务</h3>
      </div>
      <div class="card-body">
        <el-table
          v-loading="loading"
          :data="dataList"
          stripe
          style="width: 100%"
        >
          <el-table-column prop="taskCode" label="任务编号" width="150" />
          <el-table-column prop="taskName" label="任务名称" min-width="180" />
          <el-table-column prop="tunnelName" label="隧道" width="120" />
          <el-table-column prop="taskType" label="类型" width="100" />
          <el-table-column prop="inspector" label="负责人" width="100" />
          <el-table-column prop="startTime" label="开始时间" width="160" />
          <el-table-column prop="endTime" label="结束时间" width="160" />
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
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link size="small" @click="viewDetail(row)">详情</el-button>
              <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { useMockPageData } from '@/composables/useMockData'
import { mockInspectionTaskList } from '@/utils/mock'
import { ElMessage } from 'element-plus'
import { User, Clock, Tickets } from '@element-plus/icons-vue'
import { getStatusText, formatDate } from '@/utils/index'
import type { InspectionTask } from '@/types'

const loading = ref(false)
const statusFilter = ref('all')
const dataList = ref<InspectionTask[]>([])
const selectedTask = ref<InspectionTask | null>(null)

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  pageSizes: [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper'
})

const filteredTasks = computed(() => {
  if (statusFilter.value === 'all') {
    return dataList.value.slice(0, 5)
  }
  return dataList.value.filter(t => t.status === statusFilter.value).slice(0, 5)
})

const fetchData = useMockPageData(mockInspectionTaskList)

async function loadData() {
  loading.value = true
  try {
    const res = await fetchData({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    if (res.code === 200) {
      dataList.value = res.data.list
      pagination.total = res.data.total
      if (res.data.list.length > 0 && !selectedTask.value) {
        selectedTask.value = res.data.list[0]
      }
    }
  } finally {
    loading.value = false
  }
}

function selectTask(task: InspectionTask) {
  selectedTask.value = task
}

function startTask(task: InspectionTask) {
  ElMessage.success(`任务 "${task.taskName}" 已开始`)
  task.status = 'in_progress'
}

function completeTask(task: InspectionTask) {
  ElMessage.success(`任务 "${task.taskName}" 已完成`)
  task.status = 'completed'
}

function viewDetail(task: InspectionTask) {
  selectedTask.value = task
  ElMessage.info(`查看任务详情: ${task.taskName}`)
}

function handleEdit(row: InspectionTask) {
  ElMessage.info(`编辑任务: ${row.taskName}`)
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
