<template>
  <div class="page-wrapper">
    <div class="page-header flex-between">
      <div>
        <h1 class="page-title">告警回放</h1>
        <p class="page-subtitle">告警事件时序回放分析</p>
      </div>
      <el-button @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回列表
      </el-button>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
      <div class="lg:col-span-2">
        <div class="card">
          <div class="card-header">
            <h3 class="text-lg font-semibold text-dark-text">告警详情</h3>
          </div>
          <div class="card-body">
            <div v-if="alarmInfo" class="space-y-4">
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <p class="text-dark-muted text-xs">告警类型</p>
                  <p class="text-dark-text text-lg font-medium">{{ alarmInfo.alarmType }}</p>
                </div>
                <div>
                  <p class="text-dark-muted text-xs">告警级别</p>
                  <el-tag
                    :type="alarmInfo.alarmLevel === 'critical' ? 'danger' : alarmInfo.alarmLevel === 'warning' ? 'warning' : 'info'"
                    size="large"
                  >
                    {{ alarmInfo.alarmLevel === 'critical' ? '紧急' : alarmInfo.alarmLevel === 'warning' ? '警告' : '提示' }}
                  </el-tag>
                </div>
                <div>
                  <p class="text-dark-muted text-xs">位置</p>
                  <p class="text-dark-text">{{ alarmInfo.location }}</p>
                </div>
                <div>
                  <p class="text-dark-muted text-xs">关联设备</p>
                  <p class="text-dark-text">{{ alarmInfo.deviceName }}</p>
                </div>
                <div>
                  <p class="text-dark-muted text-xs">告警时间</p>
                  <p class="text-dark-text">{{ alarmInfo.alarmTime }}</p>
                </div>
                <div>
                  <p class="text-dark-muted text-xs">状态</p>
                  <el-tag
                    :type="alarmInfo.status === 'resolved' ? 'success' : alarmInfo.status === 'processing' ? 'primary' : 'warning'"
                  >
                    {{ alarmInfo.status === 'resolved' ? '已解决' : alarmInfo.status === 'processing' ? '处理中' : '待处理' }}
                  </el-tag>
                </div>
              </div>
              <div>
                <p class="text-dark-muted text-xs">告警描述</p>
                <p class="text-dark-text mt-1">{{ alarmInfo.description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div>
        <div class="card">
          <div class="card-header">
            <h3 class="text-lg font-semibold text-dark-text">回放控制</h3>
          </div>
          <div class="card-body">
            <div class="text-center mb-6">
              <div class="text-4xl font-mono text-accent-400 mb-2">
                {{ formatPlayTime(currentTime) }}
              </div>
              <div class="text-dark-muted text-sm">
                总时长: {{ formatPlayTime(totalDuration) }}
              </div>
            </div>
            
            <div class="space-y-4">
              <el-slider
                v-model="currentTime"
                :max="totalDuration"
                :step="1000"
                :show-tooltip="false"
                @change="seekTo"
              />
              
              <div class="flex items-center justify-center space-x-4">
                <el-button circle @click="speedDown">
                  <el-icon><VideoPlay /></el-icon>
                </el-button>
                <el-button type="primary" circle :size="large" @click="togglePlay">
                  <el-icon :size="20">
                    <component :is="isPlaying ? 'VideoPause' : 'VideoPlay'" />
                  </el-icon>
                </el-button>
                <el-button circle @click="speedUp">
                  <el-icon><VideoPlay /></el-icon>
                </el-button>
              </div>
              
              <div class="text-center">
                <span class="text-dark-muted text-sm">播放速度: </span>
                <el-select v-model="playSpeed" size="small" style="width: 100px">
                  <el-option label="0.5x" :value="0.5" />
                  <el-option label="1x" :value="1" />
                  <el-option label="2x" :value="2" />
                  <el-option label="4x" :value="4" />
                </el-select>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
      <div class="card">
        <div class="card-header">
          <h3 class="text-lg font-semibold text-dark-text">传感器数据</h3>
        </div>
        <div class="card-body">
          <div ref="sensorChartRef" class="h-64"></div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h3 class="text-lg font-semibold text-dark-text">事件时间线</h3>
        </div>
        <div class="card-body max-h-64 overflow-y-auto">
          <div class="relative">
            <div class="absolute left-3 top-0 bottom-0 w-0.5 bg-dark-border"></div>
            <div
              v-for="(event, index) in timeline"
              :key="index"
              class="relative pl-8 pb-6 last:pb-0"
            >
              <div
                class="absolute left-1 w-5 h-5 rounded-full border-2 border-accent-500 bg-dark-bg"
                :class="{ 'bg-accent-500': event.time <= currentTime }"
              ></div>
              <div class="p-3 rounded-lg bg-dark-bg border border-dark-border">
                <div class="flex-between mb-1">
                  <span class="text-dark-text text-sm font-medium">{{ event.event }}</span>
                  <span class="text-dark-muted text-xs">{{ formatPlayTime(event.time) }}</span>
                </div>
                <p class="text-dark-muted text-xs">{{ event.data }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useMockApi } from '@/composables/useMockData'
import { mockAlarmList } from '@/utils/mock'
import { useECharts, getDefaultChartOptions } from '@/composables/useECharts'
import { ArrowLeft, VideoPlay, VideoPause } from '@element-plus/icons-vue'
import type { EChartsOption } from 'echarts'
import type { AlarmEvent } from '@/types'

const route = useRoute()
const router = useRouter()
const alarmId = computed(() => route.query.id)

const alarmInfo = ref<AlarmEvent | null>(null)
const sensorChartRef = ref<HTMLElement | null>(null)
const { setOption } = useECharts(sensorChartRef)

const isPlaying = ref(false)
const currentTime = ref(0)
const totalDuration = ref(60000)
const playSpeed = ref(1)
let playInterval: ReturnType<typeof setInterval> | null = null

const timeline = reactive([
  { time: 0, event: '系统正常', data: '所有传感器数据正常' },
  { time: 10000, event: '数据异常', data: '声学传感器-003 振幅开始上升' },
  { time: 20000, event: '阈值触发', data: '振幅超过预警阈值 50dB' },
  { time: 30000, event: '告警生成', data: '系统自动生成告警事件' },
  { time: 35000, event: '通知发送', data: '已通知相关负责人' },
  { time: 45000, event: '响应开始', data: '运维人员开始处理' },
  { time: 55000, event: '问题定位', data: '确认渗漏水位置 K2+350' },
  { time: 60000, event: '临时处理', data: '已采取临时措施' }
])

const getAlarm = useMockApi(mockAlarmList)
const getSensorData = useMockApi(() => {
  const data = []
  for (let i = 0; i <= 60; i++) {
    data.push({
      time: i,
      value: i < 10 ? 30 + Math.random() * 10 : i < 30 ? 40 + Math.random() * 20 : 50 + Math.random() * 30
    })
  }
  return data
})

function formatPlayTime(ms: number): string {
  const totalSeconds = Math.floor(ms / 1000)
  const minutes = Math.floor(totalSeconds / 60)
  const seconds = totalSeconds % 60
  return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
}

function togglePlay() {
  if (isPlaying.value) {
    stopPlay()
  } else {
    startPlay()
  }
}

function startPlay() {
  if (currentTime.value >= totalDuration.value) {
    currentTime.value = 0
  }
  isPlaying.value = true
  playInterval = setInterval(() => {
    currentTime.value += 1000 * playSpeed.value
    if (currentTime.value >= totalDuration.value) {
      currentTime.value = totalDuration.value
      stopPlay()
    }
  }, 1000)
}

function stopPlay() {
  isPlaying.value = false
  if (playInterval) {
    clearInterval(playInterval)
    playInterval = null
  }
}

function seekTo(time: number) {
  currentTime.value = time
}

function speedUp() {
  if (playSpeed.value < 4) {
    playSpeed.value *= 2
  }
}

function speedDown() {
  if (playSpeed.value > 0.5) {
    playSpeed.value /= 2
  }
}

async function loadAlarmInfo() {
  const res = await getAlarm(1)
  if (res.code === 200 && res.data.length > 0) {
    alarmInfo.value = res.data[0]
  }
}

async function loadSensorChart() {
  const res = await getSensorData()
  if (res.code === 200) {
    const data = res.data
    const option: EChartsOption = {
      ...getDefaultChartOptions(),
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: data.map((d: any) => `${d.time}s`)
      },
      yAxis: {
        type: 'value',
        name: '振幅 (dB)'
      },
      series: [
        {
          type: 'line',
          data: data.map((d: any) => d.value),
          smooth: true,
          lineStyle: {
            color: '#00F5D4',
            width: 2
          },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0, y: 0, x2: 0, y2: 1,
              colorStops: [
                { offset: 0, color: 'rgba(0, 245, 212, 0.3)' },
                { offset: 1, color: 'rgba(0, 245, 212, 0.05)' }
              ]
            }
          },
          markLine: {
            silent: true,
            data: [{ yAxis: 50, name: '预警阈值', lineStyle: { color: '#F59E0B', type: 'dashed' } }]
          }
        }
      ]
    }
    setOption(option)
  }
}

function goBack() {
  router.push('/alarm/list')
}

onMounted(() => {
  loadAlarmInfo()
  loadSensorChart()
})

onUnmounted(() => {
  stopPlay()
})
</script>
