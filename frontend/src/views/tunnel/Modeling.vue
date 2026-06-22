<template>
  <div class="page-wrapper">
    <div class="page-header flex-between">
      <div>
        <h1 class="page-title">三维建模</h1>
        <p class="page-subtitle">隧道结构三维可视化展示</p>
      </div>
      <div class="flex space-x-3">
        <el-select v-model="selectedTunnel" size="default" placeholder="选择隧道" style="width: 200px">
          <el-option
            v-for="tunnel in tunnels"
            :key="tunnel.id"
            :label="tunnel.name"
            :value="tunnel.id"
          />
        </el-select>
        <el-button type="primary">
          <el-icon><RefreshRight /></el-icon>
          刷新模型
        </el-button>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-4 gap-6">
      <div class="lg:col-span-3">
        <div class="card h-[600px] overflow-hidden">
          <div ref="threeContainer" class="w-full h-full relative">
            <div class="absolute top-4 left-4 z-10 flex space-x-2">
              <el-button-group>
                <el-button size="small" @click="setViewMode('3d')" :type="viewMode === '3d' ? 'primary' : 'default'">3D视图</el-button>
                <el-button size="small" @click="setViewMode('front')" :type="viewMode === 'front' ? 'primary' : 'default'">正视图</el-button>
                <el-button size="small" @click="setViewMode('side')" :type="viewMode === 'side' ? 'primary' : 'default'">侧视图</el-button>
                <el-button size="small" @click="setViewMode('top')" :type="viewMode === 'top' ? 'primary' : 'default'">俯视图</el-button>
              </el-button-group>
            </div>
            <div class="absolute top-4 right-4 z-10 flex space-x-2">
              <el-button size="small" @click="zoomIn">
                <el-icon><ZoomIn /></el-icon>
              </el-button>
              <el-button size="small" @click="zoomOut">
                <el-icon><ZoomOut /></el-icon>
              </el-button>
              <el-button size="small" @click="resetView">
                <el-icon><RefreshRight /></el-icon>
              </el-button>
            </div>
            <div class="absolute bottom-4 left-4 z-10 bg-dark-card/90 p-4 rounded-lg border border-dark-border">
              <p class="text-sm text-dark-muted mb-2">模型信息</p>
              <div class="space-y-1 text-xs">
                <p><span class="text-dark-muted">隧道长度：</span><span class="text-dark-text">{{ currentTunnel?.length || 0 }}m</span></p>
                <p><span class="text-dark-muted">隧道宽度：</span><span class="text-dark-text">{{ currentTunnel?.width || 0 }}m</span></p>
                <p><span class="text-dark-muted">隧道高度：</span><span class="text-dark-text">{{ currentTunnel?.height || 0 }}m</span></p>
                <p><span class="text-dark-muted">管片数量：</span><span class="text-dark-text">{{ segments.length }} 环</span></p>
              </div>
            </div>
            <div class="absolute inset-0 flex items-center justify-center">
              <div class="text-center">
                <el-icon :size="64" class="text-accent-400 mb-4"><Box /></el-icon>
                <p class="text-dark-text text-lg font-semibold">隧道三维模型</p>
                <p class="text-dark-muted text-sm mt-2">Three.js 3D 渲染区域</p>
                <p class="text-dark-muted text-xs mt-1">（此处将集成 Three.js 进行隧道结构三维可视化）</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="space-y-6">
        <div class="card">
          <div class="card-header">
            <h3 class="text-lg font-semibold text-dark-text">管片列表</h3>
          </div>
          <div class="card-body max-h-[250px] overflow-y-auto p-0">
            <div
              v-for="segment in segments"
              :key="segment.id"
              class="px-4 py-3 border-b border-dark-border hover:bg-dark-hover cursor-pointer transition-colors"
              :class="{ 'bg-accent-500/10': selectedSegment === segment.id }"
              @click="selectSegment(segment.id)"
            >
              <div class="flex-between">
                <span class="text-dark-text">{{ segment.sectionName }}</span>
                <span
                  class="status-dot"
                  :class="segment.status === 1 ? 'success' : 'warning'"
                ></span>
              </div>
              <p class="text-dark-muted text-xs mt-1">
                里程: K{{ (segment.startMileage / 1000).toFixed(3) }} - K{{ (segment.endMileage / 1000).toFixed(3) }}
              </p>
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <h3 class="text-lg font-semibold text-dark-text">选中管片详情</h3>
          </div>
          <div class="card-body">
            <div v-if="currentSegment" class="space-y-3">
              <div>
                <p class="text-dark-muted text-xs">管片名称</p>
                <p class="text-dark-text font-medium">{{ currentSegment.sectionName }}</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">断面类型</p>
                <p class="text-dark-text">{{ currentSegment.sectionType }}</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">衬砌类型</p>
                <p class="text-dark-text">{{ currentSegment.liningType }}</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">里程范围</p>
                <p class="text-dark-text">{{ currentSegment.startMileage }}m - {{ currentSegment.endMileage }}m</p>
              </div>
              <div>
                <p class="text-dark-muted text-xs">状态</p>
                <el-tag :type="currentSegment.status === 1 ? 'success' : 'warning'" size="small">
                  {{ currentSegment.status === 1 ? '正常' : '异常' }}
                </el-tag>
              </div>
            </div>
            <div v-else class="text-center text-dark-muted py-8">
              <el-icon :size="32" class="mb-2"><InfoFilled /></el-icon>
              <p>请选择管片查看详情</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useMockApi } from '@/composables/useMockData'
import { mockTunnelList, mockSectionList } from '@/utils/mock'
import { Box, RefreshRight, ZoomIn, ZoomOut, InfoFilled } from '@element-plus/icons-vue'
import type { TunnelStructure, TunnelSection } from '@/types'

const selectedTunnel = ref(1)
const selectedSegment = ref<number | null>(null)
const viewMode = ref('3d')
const threeContainer = ref<HTMLElement | null>(null)

const getTunnels = useMockApi(mockTunnelList)
const getSections = useMockApi(mockSectionList)

const tunnels = ref<TunnelStructure[]>([])
const segments = ref<TunnelSection[]>([])

const currentTunnel = computed(() => tunnels.value.find(t => t.id === selectedTunnel.value))
const currentSegment = computed(() => segments.value.find(s => s.id === selectedSegment.value))

async function loadData() {
  const [tunnelRes, sectionRes] = await Promise.all([
    getTunnels(5),
    getSections(selectedTunnel.value, 20)
  ])
  
  if (tunnelRes.code === 200) {
    tunnels.value = tunnelRes.data
  }
  if (sectionRes.code === 200) {
    segments.value = sectionRes.data
  }
}

function selectSegment(id: number) {
  selectedSegment.value = id
}

function setViewMode(mode: string) {
  viewMode.value = mode
}

function zoomIn() {
  console.log('Zoom in')
}

function zoomOut() {
  console.log('Zoom out')
}

function resetView() {
  viewMode.value = '3d'
}

onMounted(() => {
  loadData()
})
</script>
