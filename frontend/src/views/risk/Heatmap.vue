<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useECharts } from '@/composables/useECharts'
import { Search, Refresh, Download, ZoomIn, ZoomOut, FullScreen } from '@element-plus/icons-vue'

const searchForm = reactive({
  timeRange: '',
  riskType: '',
  minRiskLevel: ''
})

const timeRangeOptions = [
  { label: '今日', value: 'today' },
  { label: '本周', value: 'week' },
  { label: '本月', value: 'month' },
  { label: '全年', value: 'year' }
]

const riskTypeOptions = [
  { label: '渗漏风险', value: 'leakage' },
  { label: '结构风险', value: 'structure' },
  { label: '设备风险', value: 'equipment' },
  { label: '沉降风险', value: 'settlement' }
]

const riskLevelOptions = [
  { label: '低风险以上', value: 'low' },
  { label: '中风险以上', value: 'medium' },
  { label: '高风险以上', value: 'high' },
  { label: '极高风险', value: 'critical' }
]

const loading1 = ref<HTMLElement | null>(null)
const loading2 = ref<HTMLElement | null>(null)
const loading3 = ref<HTMLElement | null>(null)

const { setOption: setOption1 } = useECharts(loading1 as any)
const { setOption: setOption2 } = useECharts(loading2 as any)
const { setOption: setOption3 } = useECharts(loading3 as any)

const stats = reactive({
  totalRiskPoints: 1258,
  highRiskPoints: 42,
  avgRiskScore: 35.8,
  riskTrend: 'down'
})

const tunnelLength = 2500
const segments = 50
const segmentLength = tunnelLength / segments

const heatmapData = computed(() => {
  const data: number[][] = []
  for (let i = 0; i < segments; i++) {
    for (let j = 0; j < 12; j++) {
      const baseRisk = Math.sin(i / 10) * 20 + Math.cos(j / 3) * 15 + 30
      const noise = Math.random() * 15
      const risk = Math.min(Math.max(baseRisk + noise, 5), 95)
      data.push([i, j, Math.round(risk)])
    }
  }
  return data
})

const riskPoints = ref<any[]>([])
const currentPage = ref(1)
const pageSize = ref(10)

function initCharts() {
  const hours = Array.from({ length: 24 }, (_, i) => `${i}:00`)
  const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  
  const heatmapData2 = []
  for (let i = 0; i < 7; i++) {
    for (let j = 0; j < 24; j++) {
      heatmapData2.push([j, i, Math.floor(Math.random() * 100)])
    }
  }

  const option1 = {
    tooltip: {
      position: 'top',
      formatter: (params: any) => {
        return `环号: ${params.data[0] + 1}<br/>位置: ${['拱顶', '拱肩左', '拱肩右', '边墙左', '边墙右', '拱腰左', '拱腰右', '墙角左', '墙角右', '拱底左', '拱底中', '拱底右'][params.data[1]]}<br/>风险值: ${params.data[2]}%`
      },
      backgroundColor: 'rgba(10, 22, 40, 0.9)',
      borderColor: '#1E3A5F',
      textStyle: { color: '#fff' }
    },
    grid: {
      left: '10%',
      right: '10%',
      top: '10%',
      bottom: '15%'
    },
    xAxis: {
      type: 'category',
      data: Array.from({ length: segments }, (_, i) => `${i + 1}`),
      splitArea: { show: true },
      axisLabel: { color: '#8FA3BF', interval: 4 },
      axisLine: { lineStyle: { color: '#1E3A5F' } },
      name: '环号',
      nameTextStyle: { color: '#8FA3BF' }
    },
    yAxis: {
      type: 'category',
      data: ['拱顶', '拱肩左', '拱肩右', '边墙左', '边墙右', '拱腰左', '拱腰右', '墙角左', '墙角右', '拱底左', '拱底中', '拱底右'],
      splitArea: { show: true },
      axisLabel: { color: '#8FA3BF', fontSize: 10 },
      axisLine: { lineStyle: { color: '#1E3A5F' } }
    },
    visualMap: {
      min: 0,
      max: 100,
      calculable: true,
      orient: 'horizontal',
      left: 'center',
      bottom: '0%',
      textStyle: { color: '#8FA3BF' },
      inRange: {
        color: ['#0A1628', '#0F2847', '#1E3A5F', '#3B82F6', '#F59E0B', '#EF4444', '#DC2626']
      }
    },
    series: [{
      name: '风险热力图',
      type: 'heatmap',
      data: heatmapData.value,
      label: { show: false },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(0, 245, 212, 0.5)'
        }
      }
    }]
  }
  setOption1(option1)

  const option2 = {
    tooltip: {
      position: 'top',
      backgroundColor: 'rgba(10, 22, 40, 0.9)',
      borderColor: '#1E3A5F',
      textStyle: { color: '#fff' }
    },
    grid: {
      left: '10%',
      right: '10%',
      top: '10%',
      bottom: '15%'
    },
    xAxis: {
      type: 'category',
      data: hours,
      splitArea: { show: true },
      axisLabel: { color: '#8FA3BF', interval: 2 },
      axisLine: { lineStyle: { color: '#1E3A5F' } }
    },
    yAxis: {
      type: 'category',
      data: days,
      splitArea: { show: true },
      axisLabel: { color: '#8FA3BF' },
      axisLine: { lineStyle: { color: '#1E3A5F' } }
    },
    visualMap: {
      min: 0,
      max: 100,
      calculable: true,
      orient: 'horizontal',
      left: 'center',
      bottom: '0%',
      textStyle: { color: '#8FA3BF' },
      inRange: {
        color: ['#0A1628', '#1E3A5F', '#3B82F6', '#00F5D4']
      }
    },
    series: [{
      name: '告警时段分布',
      type: 'heatmap',
      data: heatmapData2,
      label: { show: false },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(0, 245, 212, 0.5)'
        }
      }
    }]
  }
  setOption2(option2)

  const riskCategories = ['渗漏风险', '结构风险', '设备风险', '沉降风险', '其他风险']
  const riskValues = [42, 28, 35, 18, 12]

  const option3 = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      backgroundColor: 'rgba(10, 22, 40, 0.9)',
      borderColor: '#1E3A5F',
      textStyle: { color: '#fff' }
    },
    legend: {
      data: ['风险点数', '已处理', '处理中'],
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
      data: riskCategories,
      axisLine: { lineStyle: { color: '#1E3A5F' } },
      axisLabel: { color: '#8FA3BF' }
    },
    yAxis: {
      type: 'value',
      axisLine: { lineStyle: { color: '#1E3A5F' } },
      axisLabel: { color: '#8FA3BF' },
      splitLine: { lineStyle: { color: '#1E3A5F' } }
    },
    series: [
      {
        name: '风险点数',
        type: 'bar',
        data: riskValues,
        itemStyle: { color: '#EF4444', borderRadius: [4, 4, 0, 0] }
      },
      {
        name: '已处理',
        type: 'bar',
        data: [35, 22, 28, 15, 10],
        itemStyle: { color: '#00F5D4', borderRadius: [4, 4, 0, 0] }
      },
      {
        name: '处理中',
        type: 'bar',
        data: [7, 6, 7, 3, 2],
        itemStyle: { color: '#F59E0B', borderRadius: [4, 4, 0, 0] }
      }
    ]
  }
  setOption3(option3)
}

function generateRiskPoints() {
  const types = ['leakage', 'structure', 'equipment', 'settlement']
  const levels = ['low', 'medium', 'high', 'critical']
  const points = []

  for (let i = 0; i < 20; i++) {
    const type = types[Math.floor(Math.random() * types.length)]
    const level = levels[Math.floor(Math.random() * levels.length)]
    points.push({
      id: i + 1,
      position: `K${(Math.random() * 2.5).toFixed(2)}+${Math.floor(Math.random() * 500)}`,
      ringNumber: Math.floor(Math.random() * 50) + 1,
      type,
      level,
      riskScore: Math.floor(Math.random() * 90) + 10,
      description: getRiskDescription(type),
      detectTime: `2024-01-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}`,
      status: ['pending', 'processing', 'resolved'][Math.floor(Math.random() * 3)]
    })
  }
  riskPoints.value = points.sort((a, b) => b.riskScore - a.riskScore)
}

function getRiskDescription(type: string) {
  const descriptions: Record<string, string[]> = {
    leakage: ['管片接缝渗漏', '螺栓孔渗漏', '注浆孔渗漏', '裂缝渗漏'],
    structure: ['管片裂缝', '管片破损', '接缝错台', '螺栓松动'],
    equipment: ['传感器故障', '采集器离线', '通信中断', '电源异常'],
    settlement: ['沉降超标', '收敛变形', '水平位移', '纵向变形']
  }
  const list = descriptions[type] || ['未知风险']
  return list[Math.floor(Math.random() * list.length)]
}

function getRiskTypeText(type: string) {
  const map: Record<string, string> = {
    leakage: '渗漏风险',
    structure: '结构风险',
    equipment: '设备风险',
    settlement: '沉降风险'
  }
  return map[type] || '未知'
}

function getRiskLevelTag(level: string) {
  const map: Record<string, { type: string; text: string }> = {
    low: { type: 'success', text: '低风险' },
    medium: { type: 'primary', text: '中风险' },
    high: { type: 'warning', text: '高风险' },
    critical: { type: 'danger', text: '极高风险' }
  }
  return map[level] || { type: 'info', text: '未知' }
}

function getStatusText(status: string) {
  const map: Record<string, string> = {
    pending: '待处理',
    processing: '处理中',
    resolved: '已解决'
  }
  return map[status] || '未知'
}

function getStatusTagType(status: string) {
  const map: Record<string, string> = {
    pending: 'warning',
    processing: 'primary',
    resolved: 'success'
  }
  return map[status] || 'info'
}

function handleSearch() {
  generateRiskPoints()
}

function handleReset() {
  Object.assign(searchForm, {
    timeRange: '',
    riskType: '',
    minRiskLevel: ''
  })
  generateRiskPoints()
}

onMounted(() => {
  initCharts()
  generateRiskPoints()
})
</script>

<template>
  <div class="p-6">
    <div class="mb-6">
      <h2 class="text-2xl font-bold text-white mb-2">风险热力图</h2>
      <p class="text-gray-400">可视化展示隧道各区域风险分布情况</p>
    </div>

    <div class="grid grid-cols-4 gap-4 mb-6">
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">风险点总数</p>
            <p class="text-3xl font-bold text-white mt-1">{{ stats.totalRiskPoints }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-blue-500/20 flex items-center justify-center">
            <span class="text-blue-400 text-2xl">📍</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">高风险点</p>
            <p class="text-3xl font-bold text-red-400 mt-1">{{ stats.highRiskPoints }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-red-500/20 flex items-center justify-center">
            <span class="text-red-400 text-2xl">🔴</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">平均风险值</p>
            <p class="text-3xl font-bold text-yellow-400 mt-1">{{ stats.avgRiskScore }}<span class="text-lg">%</span></p>
          </div>
          <div class="w-12 h-12 rounded-full bg-yellow-500/20 flex items-center justify-center">
            <span class="text-yellow-400 text-2xl">📊</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">风险趋势</p>
            <p class="text-3xl font-bold text-green-400 mt-1">{{ stats.riskTrend === 'down' ? '↓' : '↑' }}</p>
            <p class="text-xs text-green-400">较上周下降 8.2%</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-green-500/20 flex items-center justify-center">
            <span class="text-green-400 text-2xl">📉</span>
          </div>
        </div>
      </div>
    </div>

    <div class="bg-dark-card rounded-lg border border-dark-border p-4 mb-6">
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-lg font-semibold text-white">隧道结构风险热力图</h3>
        <div class="flex gap-2">
          <el-button size="small" :icon="ZoomIn">放大</el-button>
          <el-button size="small" :icon="ZoomOut">缩小</el-button>
          <el-button size="small" :icon="FullScreen">全屏</el-button>
          <el-button size="small" :icon="Download">导出</el-button>
        </div>
      </div>
      <div ref="loading1" class="h-80"></div>
    </div>

    <div class="grid grid-cols-2 gap-6 mb-6">
      <div class="bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">告警时段热力图</h3>
        <div ref="loading2" class="h-64"></div>
      </div>
      <div class="bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">风险类型统计</h3>
        <div ref="loading3" class="h-64"></div>
      </div>
    </div>

    <div class="bg-dark-card rounded-lg border border-dark-border p-4">
      <div class="flex flex-wrap gap-4 mb-4">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="时间范围">
            <el-select v-model="searchForm.timeRange" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in timeRangeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险类型">
            <el-select v-model="searchForm.riskType" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in riskTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="最低风险">
            <el-select v-model="searchForm.minRiskLevel" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in riskLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            <el-button :icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="riskPoints" v-loading="false" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="position" label="位置" min-width="120" />
        <el-table-column prop="ringNumber" label="环号" width="80" />
        <el-table-column label="风险类型" min-width="100">
          <template #default="{ row }">
            {{ getRiskTypeText(row.type) }}
          </template>
        </el-table-column>
        <el-table-column label="风险等级" min-width="100">
          <template #default="{ row }">
            <el-tag :type="getRiskLevelTag(row.level).type">
              {{ getRiskLevelTag(row.level).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险值" width="100">
          <template #default="{ row }">
            <span 
              class="font-bold"
              :class="row.riskScore >= 70 ? 'text-red-400' : row.riskScore >= 40 ? 'text-yellow-400' : 'text-green-400'"
            >{{ row.riskScore }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="风险描述" min-width="150" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="detectTime" label="检测时间" min-width="150" />
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="riskPoints.length"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </div>
  </div>
</template>
