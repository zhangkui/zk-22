<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useECharts } from '@/composables/useECharts'
import { useMockApi } from '@/composables/useMockData'
import { mockDeviceList } from '@/utils/mock'
import { Search, Refresh, Filter, Download } from '@element-plus/icons-vue'

const searchForm = reactive({
  deviceType: '',
  riskLevel: '',
  remainingLife: ''
})

const deviceTypeOptions = [
  { label: '传感器', value: 'sensor' },
  { label: '采集器', value: 'collector' },
  { label: '通信设备', value: 'communication' },
  { label: '服务器', value: 'server' }
]

const riskLevelOptions = [
  { label: '低风险', value: 'low' },
  { label: '中风险', value: 'medium' },
  { label: '高风险', value: 'high' },
  { label: '极高风险', value: 'critical' }
]

const remainingLifeOptions = [
  { label: '< 1年', value: '1' },
  { label: '1-3年', value: '1-3' },
  { label: '3-5年', value: '3-5' },
  { label: '> 5年', value: '5' }
]

const loading1 = ref<HTMLElement | null>(null)
const loading2 = ref<HTMLElement | null>(null)
const loading3 = ref<HTMLElement | null>(null)
const loading4 = ref<HTMLElement | null>(null)

const currentPage = ref(1)
const pageSize = ref(10)

const { setOption: setOption1 } = useECharts(loading1 as any)
const { setOption: setOption2 } = useECharts(loading2 as any)
const { setOption: setOption3 } = useECharts(loading3 as any)
const { setOption: setOption4 } = useECharts(loading4 as any)

const stats = reactive({
  avgRemainingLife: 5.2,
  criticalDevices: 12,
  avgHealthScore: 78.5,
  needMaintenance: 28
})

const deviceLifeData = ref<any[]>([])
const maintenanceCostData = ref<any[]>([])

function getDevices() {
  useMockApi(mockDeviceList).then(res => {
    deviceLifeData.value = res.data.map((device: any, index: number) => ({
      ...device,
      totalLife: 10,
      usedLife: 4.8,
      remainingLife: 5.2,
      healthScore: Math.floor(Math.random() * 40) + 60,
      riskLevel: ['low', 'medium', 'high', 'critical'][Math.floor(Math.random() * 4)],
      predictedFailureDate: '2029-06-15',
      maintenanceCost: Math.floor(Math.random() * 50000) + 10000
    }))
  })
}

function initCharts() {
  const categories = ['传感器', '采集器', '通信设备', '服务器']
  const avgLife = [8.5, 7.2, 6.8, 5.5]
  const usedLife = [4.2, 3.8, 3.5, 2.8]

  const option1 = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      backgroundColor: 'rgba(10, 22, 40, 0.9)',
      borderColor: '#1E3A5F',
      textStyle: { color: '#fff' }
    },
    legend: {
      data: ['设计寿命', '已使用年限'],
      textStyle: { color: '#8FA3BF' },
      top: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: categories,
      axisLine: { lineStyle: { color: '#1E3A5F' } },
      axisLabel: { color: '#8FA3BF' }
    },
    yAxis: {
      type: 'value',
      name: '年',
      nameTextStyle: { color: '#8FA3BF' },
      axisLine: { lineStyle: { color: '#1E3A5F' } },
      axisLabel: { color: '#8FA3BF' },
      splitLine: { lineStyle: { color: '#1E3A5F' } }
    },
    series: [
      {
        name: '设计寿命',
        type: 'bar',
        data: avgLife,
        itemStyle: { color: '#00F5D4', borderRadius: [4, 4, 0, 0] }
      },
      {
        name: '已使用年限',
        type: 'bar',
        data: usedLife,
        itemStyle: { color: '#3B82F6', borderRadius: [4, 4, 0, 0] }
      }
    ]
  }
  setOption1(option1)

  const healthScores = Array.from({ length: 156 }, () => Math.floor(Math.random() * 40) + 60)
  const bins = [0, 0, 0, 0, 0]
  healthScores.forEach(score => {
    if (score < 60) bins[0]++
    else if (score < 70) bins[1]++
    else if (score < 80) bins[2]++
    else if (score < 90) bins[3]++
    else bins[4]++
  })

  const option2 = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      backgroundColor: 'rgba(10, 22, 40, 0.9)',
      borderColor: '#1E3A5F',
      textStyle: { color: '#fff' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['<60分', '60-70分', '70-80分', '80-90分', '90-100分'],
      axisLine: { lineStyle: { color: '#1E3A5F' } },
      axisLabel: { color: '#8FA3BF' }
    },
    yAxis: {
      type: 'value',
      name: '设备数',
      nameTextStyle: { color: '#8FA3BF' },
      axisLine: { lineStyle: { color: '#1E3A5F' } },
      axisLabel: { color: '#8FA3BF' },
      splitLine: { lineStyle: { color: '#1E3A5F' } }
    },
    series: [{
      type: 'bar',
      data: bins,
      itemStyle: {
        color: (params: any) => {
          const colors = ['#EF4444', '#F59E0B', '#3B82F6', '#8B5CF6', '#00F5D4']
          return colors[params.dataIndex]
        },
        borderRadius: [4, 4, 0, 0]
      },
      label: {
        show: true,
        position: 'top',
        color: '#fff'
      }
    }]
  }
  setOption2(option2)

  const years = ['2024', '2025', '2026', '2027', '2028', '2029', '2030']
  const predictedFailures = [5, 8, 12, 18, 25, 32, 40]
  const maintenanceCost = [12, 18, 25, 35, 48, 62, 85]

  const option3 = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(10, 22, 40, 0.9)',
      borderColor: '#1E3A5F',
      textStyle: { color: '#fff' }
    },
    legend: {
      data: ['预计故障数', '维护成本(万元)'],
      textStyle: { color: '#8FA3BF' },
      top: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: years,
      axisLine: { lineStyle: { color: '#1E3A5F' } },
      axisLabel: { color: '#8FA3BF' }
    },
    yAxis: [
      {
        type: 'value',
        name: '故障数',
        nameTextStyle: { color: '#8FA3BF' },
        axisLine: { lineStyle: { color: '#1E3A5F' } },
        axisLabel: { color: '#8FA3BF' },
        splitLine: { lineStyle: { color: '#1E3A5F' } }
      },
      {
        type: 'value',
        name: '万元',
        nameTextStyle: { color: '#8FA3BF' },
        axisLine: { lineStyle: { color: '#1E3A5F' } },
        axisLabel: { color: '#8FA3BF' },
        splitLine: { show: false }
      }
    ],
    series: [
      {
        name: '预计故障数',
        type: 'line',
        data: predictedFailures,
        smooth: true,
        lineStyle: { color: '#EF4444', width: 3 },
        itemStyle: { color: '#EF4444' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(239, 68, 68, 0.3)' },
              { offset: 1, color: 'rgba(239, 68, 68, 0)' }
            ]
          }
        }
      },
      {
        name: '维护成本(万元)',
        type: 'line',
        yAxisIndex: 1,
        data: maintenanceCost,
        smooth: true,
        lineStyle: { color: '#F59E0B', width: 3 },
        itemStyle: { color: '#F59E0B' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(245, 158, 11, 0.3)' },
              { offset: 1, color: 'rgba(245, 158, 11, 0)' }
            ]
          }
        }
      }
    ]
  }
  setOption3(option3)

  const riskData = [
    { value: 12, name: '极高风险' },
    { value: 28, name: '高风险' },
    { value: 65, name: '中风险' },
    { value: 51, name: '低风险' }
  ]

  const option4 = {
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(10, 22, 40, 0.9)',
      borderColor: '#1E3A5F',
      textStyle: { color: '#fff' }
    },
    legend: {
      orient: 'vertical',
      right: '5%',
      top: 'center',
      textStyle: { color: '#8FA3BF' }
    },
    series: [{
      type: 'pie',
      radius: ['45%', '75%'],
      center: ['35%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 4,
        borderColor: '#0A1628',
        borderWidth: 2
      },
      label: {
        show: false
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 16,
          fontWeight: 'bold',
          color: '#00F5D4'
        }
      },
      labelLine: {
        show: false
      },
      data: riskData.map((item, i) => ({
        ...item,
        itemStyle: {
          color: ['#EF4444', '#F59E0B', '#3B82F6', '#00F5D4'][i]
        }
      }))
    }]
  }
  setOption4(option4)
}

function getRiskTagType(level: string) {
  const map: Record<string, string> = {
    critical: 'danger',
    high: 'warning',
    medium: 'primary',
    low: 'success'
  }
  return map[level] || 'info'
}

function getRiskText(level: string) {
  const map: Record<string, string> = {
    critical: '极高风险',
    high: '高风险',
    medium: '中风险',
    low: '低风险'
  }
  return map[level] || '未知'
}

function getHealthColor(score: number) {
  if (score >= 90) return 'text-green-400'
  if (score >= 80) return 'text-accent-500'
  if (score >= 70) return 'text-blue-400'
  if (score >= 60) return 'text-yellow-400'
  return 'text-red-400'
}

const lifeProgress = computed(() => (used: number, total: number) => {
  return Math.min((used / total) * 100, 100)
})

function handleSearch() {
  getDevices()
}

function handleReset() {
  Object.assign(searchForm, {
    deviceType: '',
    riskLevel: '',
    remainingLife: ''
  })
  getDevices()
}

onMounted(() => {
  getDevices()
  initCharts()
})
</script>

<template>
  <div class="p-6">
    <div class="mb-6">
      <h2 class="text-2xl font-bold text-white mb-2">设备寿命评估</h2>
      <p class="text-gray-400">基于设备运行数据和AI模型预测设备剩余使用寿命</p>
    </div>

    <div class="grid grid-cols-4 gap-4 mb-6">
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">平均剩余寿命</p>
            <p class="text-3xl font-bold text-accent-500 mt-1">{{ stats.avgRemainingLife }}<span class="text-lg">年</span></p>
          </div>
          <div class="w-12 h-12 rounded-full bg-accent-500/20 flex items-center justify-center">
            <span class="text-accent-500 text-2xl">⏱️</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">极高风险设备</p>
            <p class="text-3xl font-bold text-red-400 mt-1">{{ stats.criticalDevices }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-red-500/20 flex items-center justify-center">
            <span class="text-red-400 text-2xl">⚠️</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">平均健康评分</p>
            <p class="text-3xl font-bold text-green-400 mt-1">{{ stats.avgHealthScore }}<span class="text-lg">分</span></p>
          </div>
          <div class="w-12 h-12 rounded-full bg-green-500/20 flex items-center justify-center">
            <span class="text-green-400 text-2xl">💚</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">需关注设备</p>
            <p class="text-3xl font-bold text-yellow-400 mt-1">{{ stats.needMaintenance }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-yellow-500/20 flex items-center justify-center">
            <span class="text-yellow-400 text-2xl">🔧</span>
          </div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-2 gap-6 mb-6">
      <div class="bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">设备寿命分析</h3>
        <div ref="loading1" class="h-64"></div>
      </div>
      <div class="bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">健康评分分布</h3>
        <div ref="loading2" class="h-64"></div>
      </div>
      <div class="bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">故障预测与维护成本</h3>
        <div ref="loading3" class="h-64"></div>
      </div>
      <div class="bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">风险等级分布</h3>
        <div ref="loading4" class="h-64"></div>
      </div>
    </div>

    <div class="bg-dark-card rounded-lg border border-dark-border p-4">
      <div class="flex flex-wrap gap-4 mb-4">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="设备类型">
            <el-select v-model="searchForm.deviceType" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in deviceTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select v-model="searchForm.riskLevel" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in riskLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="剩余寿命">
            <el-select v-model="searchForm.remainingLife" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in remainingLifeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            <el-button :icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="deviceLifeData" v-loading="false" border stripe style="width: 100%">
        <el-table-column prop="deviceCode" label="设备编码" min-width="120" />
        <el-table-column prop="deviceName" label="设备名称" min-width="120" />
        <el-table-column prop="model" label="设备型号" min-width="120" />
        <el-table-column label="寿命进度" min-width="200">
          <template #default="{ row }">
            <div class="space-y-1">
              <div class="flex justify-between text-xs">
                <span class="text-gray-400">已用 {{ row.usedLife }}年</span>
                <span class="text-gray-400">剩余 {{ row.remainingLife }}年</span>
              </div>
              <div class="w-full bg-dark-bg rounded-full h-2">
                <div 
                  class="h-2 rounded-full transition-all"
                  :class="row.healthScore >= 80 ? 'bg-green-400' : row.healthScore >= 60 ? 'bg-yellow-400' : 'bg-red-400'"
                  :style="{ width: lifeProgress(row.usedLife, row.totalLife) + '%' }"
                ></div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="健康评分" min-width="100">
          <template #default="{ row }">
            <span :class="getHealthColor(row.healthScore)" class="font-bold text-lg">{{ row.healthScore }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" min-width="100">
          <template #default="{ row }">
            <el-tag :type="getRiskTagType(row.riskLevel)">
              {{ getRiskText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="predictedFailureDate" label="预计故障日期" min-width="120" />
        <el-table-column label="预计维护成本" min-width="120">
          <template #default="{ row }">
            ¥{{ row.maintenanceCost.toLocaleString() }}
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="deviceLifeData.length"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </div>
  </div>
</template>
