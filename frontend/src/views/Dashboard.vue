<template>
  <div class="page-wrapper">
    <div class="page-header">
      <h1 class="page-title">数据概览</h1>
      <p class="page-subtitle">实时监控隧道结构健康状态</p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-4 mb-6">
      <div
        v-for="stat in stats"
        :key="stat.title"
        class="card p-5 card-hover cursor-pointer"
      >
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">{{ stat.title }}</p>
            <p class="text-3xl font-bold mt-2" :class="stat.color">
              {{ stat.value }}
            </p>
            <p class="text-xs text-dark-muted mt-1">
              <span :class="stat.trend > 0 ? 'text-danger' : 'text-success'">
                {{ stat.trend > 0 ? '↑' : '↓' }}
              </span>
              {{ Math.abs(stat.trend) }}%
            </p>
          </div>
          <div
            class="w-12 h-12 rounded-xl flex-center"
            :class="stat.bgColor"
          >
            <el-icon :size="24" :class="stat.iconColor">
              <component :is="stat.icon" />
            </el-icon>
          </div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
      <div class="card">
        <div class="card-header">
          <h3 class="text-lg font-semibold text-dark-text">告警趋势</h3>
          <el-select v-model="alarmTrendDays" size="small" style="width: 100px">
            <el-option label="近7天" :value="7" />
            <el-option label="近15天" :value="15" />
            <el-option label="近30天" :value="30" />
          </el-select>
        </div>
        <div class="card-body">
          <div ref="alarmTrendChartRef" class="h-64"></div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h3 class="text-lg font-semibold text-dark-text">设备状态分布</h3>
        </div>
        <div class="card-body">
          <div ref="deviceStatusChartRef" class="h-64"></div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
      <div class="card">
        <div class="card-header">
          <h3 class="text-lg font-semibold text-dark-text">监测数据趋势</h3>
        </div>
        <div class="card-body">
          <div ref="monitoringChartRef" class="h-64"></div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h3 class="text-lg font-semibold text-dark-text">隧道健康排名</h3>
        </div>
        <div class="card-body">
          <div ref="tunnelHealthChartRef" class="h-64"></div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h3 class="text-lg font-semibold text-dark-text">最近告警</h3>
          <el-button type="primary" link class="text-accent-400">查看全部</el-button>
        </div>
        <div class="card-body pt-0">
          <div class="space-y-3">
            <div
              v-for="alarm in recentAlarms"
              :key="alarm.id"
              class="flex items-center justify-between py-3 border-b border-dark-border last:border-0"
            >
              <div class="flex items-center">
                <span
                  class="status-dot"
                  :class="{
                    'danger': alarm.alarmLevel === 'critical',
                    'warning': alarm.alarmLevel === 'warning',
                    'info': alarm.alarmLevel === 'info'
                  }"
                ></span>
                <div class="ml-2">
                  <p class="text-dark-text text-sm">{{ alarm.alarmType }}</p>
                  <p class="text-dark-muted text-xs">{{ alarm.location }}</p>
                </div>
              </div>
              <div class="text-right">
                <el-tag
                  :type="alarm.status === 'pending' ? 'warning' : alarm.status === 'processing' ? 'info' : 'success'"
                  size="small"
                >
                  {{ getStatusText(alarm.status) }}
                </el-tag>
                <p class="text-dark-muted text-xs mt-1">{{ formatDate(alarm.alarmTime, 'MM-DD HH:mm') }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { useMockApi } from '@/composables/useMockData'
import { mockDashboardStats,
  mockAlarmTrend,
  mockDeviceStatus,
  mockTunnelHealthRank,
  mockRecentAlarms,
  mockMonitoringData
} from '@/utils/mock'
import { useECharts, getDefaultChartOptions } from '@/composables/useECharts'
import { formatDate, getStatusText } from '@/utils/index'
import {
  DataAnalysis,
  Warning,
  Monitor,
  Tickets,
  DataBoard,
  SuccessFilled
} from '@element-plus/icons-vue'
import type { EChartsOption } from 'echarts'

const alarmTrendDays = ref(7)

const stats = reactive([
  { title: '隧道总数', value: '5', trend: 0, color: 'text-accent-400', bgColor: 'bg-accent-500/20', iconColor: 'text-accent-400', icon: DataAnalysis },
  { title: '设备总数', value: '128', trend: 2.5, color: 'text-info', bgColor: 'bg-blue-500/20', iconColor: 'text-blue-400', icon: Monitor },
  { title: '活跃告警', value: '12', trend: -5.2, color: 'text-danger', bgColor: 'bg-red-500/20', iconColor: 'text-red-400', icon: Warning },
  { title: '待巡检', value: '8', trend: 3.1, color: 'text-warning', bgColor: 'bg-yellow-500/20', iconColor: 'text-yellow-400', icon: Tickets },
  { title: '今日数据点', value: '12.6万', trend: 8.7, color: 'text-success', bgColor: 'bg-green-500/20', iconColor: 'text-green-400', icon: DataBoard },
  { title: '系统健康度', value: '92%', trend: 1.2, color: 'text-accent-400', bgColor: 'bg-accent-500/20', iconColor: 'text-accent-400', icon: SuccessFilled }
])

const alarmTrendChartRef = ref<HTMLElement | null>(null)
const deviceStatusChartRef = ref<HTMLElement | null>(null)
const monitoringChartRef = ref<HTMLElement | null>(null)
const tunnelHealthChartRef = ref<HTMLElement | null>(null)

const { setOption: setAlarmTrendOption } = useECharts(alarmTrendChartRef)
const { setOption: setDeviceStatusOption } = useECharts(deviceStatusChartRef)
const { setOption: setMonitoringOption } = useECharts(monitoringChartRef)
const { setOption: setTunnelHealthOption } = useECharts(tunnelHealthChartRef)

const getAlarmTrend = useMockApi(mockAlarmTrend)
const getDeviceStatus = useMockApi(mockDeviceStatus)
const getTunnelHealthRank = useMockApi(mockTunnelHealthRank)
const getRecentAlarms = useMockApi(mockRecentAlarms)
const getMonitoringData = useMockApi(mockMonitoringData)

const recentAlarms = ref<any[]>([])

async function loadAlarmTrendChart() {
  const res = await getAlarmTrend(alarmTrendDays.value)
  if (res.code === 200) {
    const data = res.data
    const option: EChartsOption = {
      ...getDefaultChartOptions(),
      legend: {
        data: ['紧急', '警告', '提示']
      },
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: data.map((item: any) => item.date)
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '紧急',
          type: 'bar',
          stack: 'total',
          data: data.map((item: any) => item.critical),
          itemStyle: { color: '#EF4444' }
        },
        {
          name: '警告',
          type: 'bar',
          stack: 'total',
          data: data.map((item: any) => item.warning),
          itemStyle: { color: '#F59E0B' }
        },
        {
          name: '提示',
          type: 'bar',
          stack: 'total',
          data: data.map((item: any) => item.info),
          itemStyle: { color: '#3B82F6' }
        }
      ]
    }
    setAlarmTrendOption(option)
  }
}

async function loadDeviceStatusChart() {
  const res = await getDeviceStatus()
  if (res.code === 200) {
    const data = res.data
    const option: EChartsOption = {
      ...getDefaultChartOptions(),
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        right: '5%',
        top: 'center'
      },
      series: [
        {
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['35%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 8,
            borderColor: '#132038',
            borderWidth: 2
          },
          label: {
            show: false
          },
          emphasis: {
            label: {
              show: true
            }
          },
          data: data.map((item: any) => ({
            value: item.value,
            name: item.name,
            itemStyle: { color: item.color }
          }))
        }
      ]
    }
    setDeviceStatusOption(option)
  }
}

async function loadMonitoringChart() {
  const res = await getMonitoringData(24)
  if (res.code === 200) {
    const data = res.data
    const option: EChartsOption = {
      ...getDefaultChartOptions(),
      legend: {
        data: ['噪声', '振动', '温度']
      },
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: data.map((item: any) => item.time)
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '噪声',
          type: 'line',
          smooth: true,
          data: data.map((item: any) => item.noiseLevel),
          itemStyle: { color: '#00F5D4' },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                { offset: 0, color: 'rgba(0, 245, 212, 0.3' },
                { offset: 1, color: 'rgba(0, 245, 212, 0.05' }
              ]
            }
          }
        },
        {
          name: '振动',
          type: 'line',
          smooth: true,
          data: data.map((item: any) => item.vibration),
          itemStyle: { color: '#10B981' }
        },
        {
          name: '温度',
          type: 'line',
          smooth: true,
          data: data.map((item: any) => item.temperature),
          itemStyle: { color: '#F59E0B' }
        }
      ]
    }
    setMonitoringOption(option)
  }
}

async function loadTunnelHealthChart() {
  const res = await getTunnelHealthRank()
  if (res.code === 200) {
    const data = res.data.sort((a: any, b: any) => a.healthScore - b.healthScore)
    const option: EChartsOption = {
      ...getDefaultChartOptions(),
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: '15%',
        containLabel: true
      },
      xAxis: {
        type: 'value',
        max: 100
      },
      yAxis: {
        type: 'category',
        data: data.map((item: any) => item.tunnelName)
      },
      series: [
        {
          type: 'bar',
          data: data.map((item: any) => ({
            value: item.healthScore,
            itemStyle: {
              color: item.healthScore >= 80 ? '#10B981' : item.healthScore >= 60 ? '#F59E0B' : '#EF4444',
              borderRadius: [0, 4, 4, 0]
            }
          })),
          barWidth: 16,
          label: {
            show: true,
            position: 'right',
            formatter: '{c}%',
            color: '#94A3B8'
          }
        }
      ]
    }
    setTunnelHealthOption(option)
  }
}

async function loadRecentAlarms() {
  const res = await getRecentAlarms(5)
  if (res.code === 200) {
    recentAlarms.value = res.data
  }
}

watch(alarmTrendDays, () => {
  loadAlarmTrendChart()
})

onMounted(() => {
  loadAlarmTrendChart()
  loadDeviceStatusChart()
  loadMonitoringChart()
  loadTunnelHealthChart()
  loadRecentAlarms()
})
</script>
