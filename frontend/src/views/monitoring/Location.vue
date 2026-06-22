<template>
  <div class="page-wrapper">
    <div class="page-header flex-between">
      <div>
        <h1 class="page-title">漏水定位</h1>
        <p class="page-subtitle">隧道渗漏水位置精准定位</p>
      </div>
      <el-select v-model="selectedTunnel" size="default" placeholder="选择隧道" style="width: 200px">
        <el-option label="秦岭隧道" :value="1" />
        <el-option label="太行山隧道" :value="2" />
      </el-select>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-4 gap-6">
      <div class="lg:col-span-3">
        <div class="card h-[500px]">
          <div class="card-header">
            <h3 class="text-lg font-semibold text-dark-text">隧道平面图</h3>
            <div class="flex items-center space-x-4">
              <span class="flex items-center text-sm">
                <span class="w-3 h-3 rounded-full bg-danger mr-2"></span>
                <span class="text-dark-muted">高风险</span>
              </span>
              <span class="flex items-center text-sm">
                <span class="w-3 h-3 rounded-full bg-warning mr-2"></span>
                <span class="text-dark-muted">中风险</span>
              </span>
              <span class="flex items-center text-sm">
                <span class="w-3 h-3 rounded-full bg-success mr-2"></span>
                <span class="text-dark-muted">低风险</span>
              </span>
            </div>
          </div>
          <div class="card-body relative">
            <div class="absolute inset-4 border-2 border-dashed border-dark-border rounded-lg">
              <div class="absolute top-1/2 left-0 right-0 h-32 -translate-y-1/2 bg-gradient-to-r from-accent-500/10 to-accent-500/5 border-y border-accent-500/30">
                <div
                  v-for="leak in leakLocations"
                  :key="leak.id"
                  class="absolute top-1/2 -translate-y-1/2 cursor-pointer group"
                  :style="{ left: `${(leak.mileage / 5000) * 100}%` }"
                  @click="selectLeak(leak)"
                >
                  <div
                    class="w-4 h-4 rounded-full animate-pulse"
                    :class="{
                      'bg-danger shadow-lg shadow-danger/50': leak.leakLevel === 'high',
                      'bg-warning shadow-lg shadow-warning/50': leak.leakLevel === 'medium',
                      'bg-success shadow-lg shadow-success/50': leak.leakLevel === 'low'
                    }"
                  ></div>
                  <div class="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 bg-dark-card border border-dark-border rounded-lg p-3 opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap z-10">
                    <p class="text-dark-text text-sm font-medium">{{ leak.location }}</p>
                    <p class="text-dark-muted text-xs">类型: {{ leak.leakType }}</p>
                    <p class="text-dark-muted text-xs">等级: {{ leak.leakLevel === 'high' ? '高' : leak.leakLevel === 'medium' ? '中' : '低' }}</p>
                  </div>
                </div>
              </div>
              <div class="absolute top-2 left-4 text-dark-muted text-sm">入口 K0+000</div>
              <div class="absolute top-2 right-4 text-dark-muted text-sm">出口 K5+000</div>
            </div>
            <div class="absolute inset-0 flex items-center justify-center pointer-events-none">
              <div class="text-center">
                <el-icon :size="48" class="text-accent-400/30 mb-2"><LocationFilled /></el-icon>
                <p class="text-dark-muted">隧道平面示意图</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="space-y-6">
        <div class="card">
          <div class="card-header">
            <h3 class="text-lg font-semibold text-dark-text">漏水点统计</h3>
          </div>
          <div class="card-body">
            <div class="space-y-4">
              <div class="flex-between">
                <span class="text-dark-muted">高风险</span>
                <span class="text-danger font-bold">{{ stats.highRisk }}</span>
              </div>
              <el-progress :percentage="(stats.highRisk / stats.total) * 100" color="#EF4444" :stroke-width="8" />
              
              <div class="flex-between">
                <span class="text-dark-muted">中风险</span>
                <span class="text-warning font-bold">{{ stats.mediumRisk }}</span>
              </div>
              <el-progress :percentage="(stats.mediumRisk / stats.total) * 100" color="#F59E0B" :stroke-width="8" />
              
              <div class="flex-between">
                <span class="text-dark-muted">低风险</span>
                <span class="text-success font-bold">{{ stats.lowRisk }}</span>
              </div>
              <el-progress :percentage="(stats.lowRisk / stats.total) * 100" color="#10B981" :stroke-width="8" />
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <h3 class="text-lg font-semibold text-dark-text">选中漏水点</h3>
          </div>
          <div class="card-body">
            <div v-if="selectedLeakData" class="space-y-3">
              <div>
                <p class="text-dark-muted text-xs">位置</p>
                <p class="text-dark-text font-medium">{{ selectedLeakData.location }}</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">漏水类型</p>
                <p class="text-dark-text">{{ selectedLeakData.leakType }}</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">风险等级</p>
                <el-tag
                  :type="selectedLeakData.leakLevel === 'high' ? 'danger' : selectedLeakData.leakLevel === 'medium' ? 'warning' : 'success'"
                  size="small"
                >
                  {{ selectedLeakData.leakLevel === 'high' ? '高风险' : selectedLeakData.leakLevel === 'medium' ? '中风险' : '低风险' }}
                </el-tag>
              </div>
              <div>
                <p class="text-dark-muted text-xs">检测时间</p>
                <p class="text-dark-text">{{ selectedLeakData.detectionTime }}</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">状态</p>
                <el-tag :type="selectedLeakData.status === 1 ? 'warning' : 'success'" size="small">
                  {{ selectedLeakData.status === 1 ? '未处理' : '已处理' }}
                </el-tag>
              </div>
            </div>
            <div v-else class="text-center text-dark-muted py-8">
              <el-icon :size="32" class="mb-2"><Location /></el-icon>
              <p>点击地图上的漏水点查看详情</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="card mt-6">
      <div class="card-header">
        <h3 class="text-lg font-semibold text-dark-text">漏水点列表</h3>
      </div>
      <div class="card-body">
        <el-table
          v-loading="loading"
          :data="dataList"
          stripe
          style="width: 100%"
        >
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="location" label="位置" min-width="180" />
          <el-table-column prop="mileage" label="里程 (m)" width="120" />
          <el-table-column prop="leakType" label="漏水类型" width="100" />
          <el-table-column prop="leakLevel" label="风险等级" width="100">
            <template #default="{ row }">
              <el-tag
                :type="row.leakLevel === 'high' ? 'danger' : row.leakLevel === 'medium' ? 'warning' : 'success'"
                size="small"
              >
                {{ row.leakLevel === 'high' ? '高' : row.leakLevel === 'medium' ? '中' : '低' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="detectionTime" label="检测时间" width="180" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'warning' : 'success'" size="small">
                {{ row.status === 1 ? '未处理' : '已处理' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button type="primary" link size="small" @click="handleView(row)">详情</el-button>
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
import { useMockPageData, useMockApi } from '@/composables/useMockData'
import { mockLeakLocationList } from '@/utils/mock'
import { ElMessage } from 'element-plus'
import { LocationFilled, Location } from '@element-plus/icons-vue'
import type { LeakLocation } from '@/types'

const loading = ref(false)
const selectedTunnel = ref(1)
const dataList = ref<LeakLocation[]>([])
const leakLocations = ref<LeakLocation[]>([])
const selectedLeakData = ref<LeakLocation | null>(null)

const stats = reactive({
  total: 10,
  highRisk: 2,
  mediumRisk: 5,
  lowRisk: 3
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  pageSizes: [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper'
})

const fetchData = useMockPageData(mockLeakLocationList)
const getLeakLocations = useMockApi(mockLeakLocationList)

async function loadData() {
  loading.value = true
  try {
    const res = await fetchData({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      tunnelId: selectedTunnel.value
    })
    if (res.code === 200) {
      dataList.value = res.data.list
      pagination.total = res.data.total
    }
  } finally {
    loading.value = false
  }
}

async function loadMapData() {
  const res = await getLeakLocations(10)
  if (res.code === 200) {
    leakLocations.value = res.data
    stats.total = res.data.length
    stats.highRisk = res.data.filter((l: LeakLocation) => l.leakLevel === 'high').length
    stats.mediumRisk = res.data.filter((l: LeakLocation) => l.leakLevel === 'medium').length
    stats.lowRisk = res.data.filter((l: LeakLocation) => l.leakLevel === 'low').length
  }
}

function selectLeak(leak: LeakLocation) {
  selectedLeakData.value = leak
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

function handleView(row: LeakLocation) {
  selectLeak(row)
  ElMessage.info(`查看漏水点详情: ${row.location}`)
}

onMounted(() => {
  loadData()
  loadMapData()
})
</script>
