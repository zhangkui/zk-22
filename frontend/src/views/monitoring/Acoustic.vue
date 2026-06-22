<template>
  <div class="page-wrapper">
    <div class="page-header">
      <h1 class="page-title">声学监测</h1>
      <p class="page-subtitle">隧道声学信号实时监测与分析</p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">在线设备</p>
            <p class="text-3xl font-bold mt-2 text-accent-400">{{ stats.onlineDevices }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-accent-500/20 flex-center">
            <el-icon :size="24" class="text-accent-400"><Monitor /></el-icon>
          </div>
        </div>
      </div>
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">今日数据</p>
            <p class="text-3xl font-bold mt-2 text-info">{{ stats.todayData }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-blue-500/20 flex-center">
            <el-icon :size="24" class="text-blue-400"><DataLine /></el-icon>
          </div>
        </div>
      </div>
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">异常事件</p>
            <p class="text-3xl font-bold mt-2 text-danger">{{ stats.abnormalEvents }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-red-500/20 flex-center">
            <el-icon :size="24" class="text-red-400"><Warning /></el-icon>
          </div>
        </div>
      </div>
      <div class="card p-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-dark-muted text-sm">平均噪声</p>
            <p class="text-3xl font-bold mt-2 text-warning">{{ stats.avgNoise }} dB</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-yellow-500/20 flex-center">
            <el-icon :size="24" class="text-yellow-400"><Microphone /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
      <div class="card">
        <div class="card-header">
          <h3 class="text-lg font-semibold text-dark-text">实时波形图</h3>
          <el-select v-model="selectedDevice" size="small" style="width: 150px">
            <el-option label="设备-001" :value="1" />
            <el-option label="设备-002" :value="2" />
            <el-option label="设备-003" :value="3" />
          </el-select>
        </div>
        <div class="card-body">
          <div ref="waveformChartRef" class="h-64"></div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h3 class="text-lg font-semibold text-dark-text">频谱分析</h3>
        </div>
        <div class="card-body">
          <div ref="spectrumChartRef" class="h-64"></div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <h3 class="text-lg font-semibold text-dark-text">监测数据列表</h3>
      </div>
      <div class="card-body">
        <el-table
          v-loading="loading"
          :data="dataList"
          stripe
          style="width: 100%"
        >
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="deviceId" label="设备ID" width="100">
            <template #default="{ row }">
              <span>DEV-{{ String(row.deviceId).padStart(3, '0') }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="collectTime" label="采集时间" width="180" />
          <el-table-column prop="frequency" label="频率 (Hz)" width="120" />
          <el-table-column prop="amplitude" label="振幅" width="100" />
          <el-table-column prop="duration" label="时长 (ms)" width="120" />
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.amplitude > 50 ? 'warning' : 'success'" size="small">
                {{ row.amplitude > 50 ? '异常' : '正常' }}
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
import { mockAcousticDataList } from '@/utils/mock'
import { useECharts, getDefaultChartOptions } from '@/composables/useECharts'
import { ElMessage } from 'element-plus'
import { Monitor, DataLine, Warning, Microphone } from '@element-plus/icons-vue'
import type { EChartsOption } from 'echarts'
import type { AcousticData } from '@/types'

const loading = ref(false)
const selectedDevice = ref(1)
const dataList = ref<AcousticData[]>([])

const stats = reactive({
  onlineDevices: 48,
  todayData: '12.5万',
  abnormalEvents: 3,
  avgNoise: 68
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  pageSizes: [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper'
})

const waveformChartRef = ref<HTMLElement | null>(null)
const spectrumChartRef = ref<HTMLElement | null>(null)

const { setOption: setWaveformOption } = useECharts(waveformChartRef)
const { setOption: setSpectrumOption } = useECharts(spectrumChartRef)

const fetchData = useMockPageData(mockAcousticDataList)
const getAcousticData = useMockApi(mockAcousticDataList)

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
    }
  } finally {
    loading.value = false
  }
}

function loadWaveformChart(data: AcousticData) {
  const waveformData = data.waveformData
  const option: EChartsOption = {
    ...getDefaultChartOptions(),
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: waveformData.map((_, i) => i)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        type: 'line',
        data: waveformData,
        smooth: true,
        showSymbol: false,
        lineStyle: {
          color: '#00F5D4',
          width: 2
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(0, 245, 212, 0.3)' },
              { offset: 1, color: 'rgba(0, 245, 212, 0.05)' }
            ]
          }
        }
      }
    ]
  }
  setWaveformOption(option)
}

function loadSpectrumChart(data: AcousticData) {
  const spectrumData = data.spectrumData
  const option: EChartsOption = {
    ...getDefaultChartOptions(),
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: spectrumData.map((_, i) => `${i * 20}Hz`)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        type: 'bar',
        data: spectrumData,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#00F5D4' },
            { offset: 1, color: '#00B39C' }
          ])
        }
      }
    ]
  }
  setSpectrumOption(option)
}

async function loadCharts() {
  const res = await getAcousticData(1)
  if (res.code === 200 && res.data.length > 0) {
    loadWaveformChart(res.data[0])
    loadSpectrumChart(res.data[0])
  }
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

function handleView(row: AcousticData) {
  ElMessage.info(`查看数据 ID: ${row.id}`)
}

import * as echarts from 'echarts'

onMounted(() => {
  loadData()
  loadCharts()
})
</script>
