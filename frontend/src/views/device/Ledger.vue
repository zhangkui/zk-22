<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Delete, Plus, Search, Refresh, Download, Setting, Upload, FolderAdd } from '@element-plus/icons-vue'
import { useTable } from '@/composables/useTable'
import { useMockApi } from '@/composables/useMockData'
import { mockDeviceList } from '@/utils/mock'
import type { Device } from '@/types'
import { useECharts } from '@/composables/useECharts'

const loading = ref<HTMLElement | null>(null)
const { chartOption, setOption } = useECharts(loading as any)

const searchForm = reactive({
  deviceCode: '',
  deviceName: '',
  deviceType: '',
  status: '',
  installLocation: ''
})

const deviceTypeOptions = [
  { label: '传感器', value: 'sensor' },
  { label: '采集器', value: 'collector' },
  { label: '通信设备', value: 'communication' },
  { label: '服务器', value: 'server' },
  { label: '其他', value: 'other' }
]

const statusOptions = [
  { label: '正常运行', value: 'running' },
  { label: '待维护', value: 'maintenance' },
  { label: '故障', value: 'fault' },
  { label: '报废', value: 'scrapped' }
]

const {
  tableData,
  total,
  pagination,
  getList,
  handleSearch,
  handleReset,
  handleSizeChange,
  handleCurrentChange
} = useTable<Device>(async (params) => {
  return useMockApi(mockDeviceList, { ...params, ...searchForm })
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formType = ref<'add' | 'edit'>('add')
const currentRow = ref<Partial<Device>>({})

const formRef = ref()

const formData = reactive<Partial<Device>>({
  deviceCode: '',
  deviceName: '',
  deviceType: 'sensor',
  status: 'running',
  installLocation: '',
  installDate: '',
  manufacturer: '',
  model: '',
  serialNumber: '',
  warrantyPeriod: 12,
  description: ''
})

const rules = {
  deviceCode: [{ required: true, message: '请输入设备编码', trigger: 'blur' }],
  deviceName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
  deviceType: [{ required: true, message: '请选择设备类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择设备状态', trigger: 'change' }],
  installLocation: [{ required: true, message: '请输入安装位置', trigger: 'blur' }],
  installDate: [{ required: true, message: '请选择安装日期', trigger: 'change' }],
  manufacturer: [{ required: true, message: '请输入生产厂商', trigger: 'blur' }],
  model: [{ required: true, message: '请输入设备型号', trigger: 'blur' }],
  serialNumber: [{ required: true, message: '请输入序列号', trigger: 'blur' }]
}

function handleAdd() {
  dialogTitle.value = '新增设备'
  formType.value = 'add'
  Object.assign(formData, {
    deviceCode: '',
    deviceName: '',
    deviceType: 'sensor',
    status: 'running',
    installLocation: '',
    installDate: '',
    manufacturer: '',
    model: '',
    serialNumber: '',
    warrantyPeriod: 12,
    description: ''
  })
  dialogVisible.value = true
}

function handleEdit(row: Device) {
  dialogTitle.value = '编辑设备'
  formType.value = 'edit'
  currentRow.value = row
  Object.assign(formData, row)
  dialogVisible.value = true
}

async function handleDelete(row: Device) {
  try {
    await ElMessageBox.confirm(`确定要删除设备「${row.deviceName}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('删除成功')
    getList()
  } catch {
  }
}

async function handleSubmit() {
  await formRef.value?.validate()
  ElMessage.success(formType.value === 'add' ? '新增成功' : '编辑成功')
  dialogVisible.value = false
  getList()
}

function getStatusTagType(status: string) {
  const map: Record<string, string> = {
    running: 'success',
    maintenance: 'warning',
    fault: 'danger',
    scrapped: 'info'
  }
  return map[status] || 'info'
}

function getStatusText(status: string) {
  const map: Record<string, string> = {
    running: '正常运行',
    maintenance: '待维护',
    fault: '故障',
    scrapped: '报废'
  }
  return map[status] || '未知'
}

function getDeviceTypeText(type: string) {
  const map: Record<string, string> = {
    sensor: '传感器',
    collector: '采集器',
    communication: '通信设备',
    server: '服务器',
    other: '其他'
  }
  return map[type] || '未知'
}

const stats = reactive({
  total: 156,
  running: 142,
  maintenance: 8,
  fault: 4,
  scrapped: 2
})

const chartRef = ref<HTMLElement | null>(null)

function initChart() {
  const categories = ['传感器', '采集器', '通信设备', '服务器', '其他']
  const data = [86, 32, 18, 12, 8]
  const option = {
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
      radius: ['40%', '70%'],
      center: ['35%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 4,
        borderColor: '#0A1628',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
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
      data: categories.map((name, i) => ({
        value: data[i],
        name,
        itemStyle: {
          color: [
            '#00F5D4',
            '#3B82F6',
            '#8B5CF6',
            '#F59E0B',
            '#EF4444'
          ][i]
        }
      }))
    }]
  }
  setOption(option)
}

getList()
initChart()
</script>

<template>
  <div class="p-6">
    <div class="mb-6">
      <h2 class="text-2xl font-bold text-white mb-2">设备台账</h2>
      <p class="text-gray-400">管理所有设备的基础信息和运行状态</p>
    </div>

    <div class="grid grid-cols-5 gap-4 mb-6">
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">设备总数</p>
            <p class="text-3xl font-bold text-white mt-1">{{ stats.total }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-blue-500/20 flex items-center justify-center">
            <span class="text-blue-400 text-2xl">📦</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">正常运行</p>
            <p class="text-3xl font-bold text-green-400 mt-1">{{ stats.running }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-green-500/20 flex items-center justify-center">
            <span class="text-green-400 text-2xl">✅</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">待维护</p>
            <p class="text-3xl font-bold text-yellow-400 mt-1">{{ stats.maintenance }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-yellow-500/20 flex items-center justify-center">
            <span class="text-yellow-400 text-2xl">🔧</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">故障</p>
            <p class="text-3xl font-bold text-red-400 mt-1">{{ stats.fault }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-red-500/20 flex items-center justify-center">
            <span class="text-red-400 text-2xl">⚠️</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">已报废</p>
            <p class="text-3xl font-bold text-gray-400 mt-1">{{ stats.scrapped }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-gray-500/20 flex items-center justify-center">
            <span class="text-gray-400 text-2xl">🗑️</span>
          </div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-3 gap-6 mb-6">
      <div class="col-span-2 bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">设备类型分布</h3>
        <div ref="loading" class="h-64"></div>
      </div>
      <div class="bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">设备健康度</h3>
        <div class="space-y-4">
          <div>
            <div class="flex justify-between text-sm mb-1">
            <span class="text-gray-400">整体健康度</span>
            <span class="text-green-400">92.8%</span>
          </div>
          <div class="w-full bg-dark-bg rounded-full h-2">
            <div class="bg-green-400 h-2 rounded-full" style="width: 92.8%"></div>
          </div>
          </div>
          <div>
            <div class="flex justify-between text-sm mb-1">
            <span class="text-gray-400">传感器健康度</span>
            <span class="text-accent-500">95.3%</span>
          </div>
          <div class="w-full bg-dark-bg rounded-full h-2">
            <div class="bg-accent-500 h-2 rounded-full" style="width: 95.3%"></div>
          </div>
          </div>
          <div>
            <div class="flex justify-between text-sm mb-1">
            <span class="text-gray-400">采集器健康度</span>
            <span class="text-blue-400">90.6%</span>
          </div>
          <div class="w-full bg-dark-bg rounded-full h-2">
            <div class="bg-blue-400 h-2 rounded-full" style="width: 90.6%"></div>
          </div>
          </div>
          <div>
            <div class="flex justify-between text-sm mb-1">
            <span class="text-gray-400">通信设备健康度</span>
            <span class="text-purple-400">88.9%</span>
          </div>
          <div class="w-full bg-dark-bg rounded-full h-2">
            <div class="bg-purple-400 h-2 rounded-full" style="width: 88.9%"></div>
          </div>
          </div>
        </div>
      </div>
    </div>

    <div class="bg-dark-card rounded-lg border border-dark-border p-4 mb-6">
      <div class="flex flex-wrap gap-4 mb-4">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="设备编码">
            <el-input v-model="searchForm.deviceCode" placeholder="请输入设备编码" clearable class="w-48" />
          </el-form-item>
          <el-form-item label="设备名称">
            <el-input v-model="searchForm.deviceName" placeholder="请输入设备名称" clearable class="w-48" />
          </el-form-item>
          <el-form-item label="设备类型">
            <el-select v-model="searchForm.deviceType" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in deviceTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="设备状态">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="安装位置">
            <el-input v-model="searchForm.installLocation" placeholder="请输入安装位置" clearable class="w-48" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            <el-button :icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="flex justify-between mb-4">
        <div>
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增设备</el-button>
          <el-button :icon="FolderAdd">批量导入</el-button>
          <el-button :icon="Upload">批量导出</el-button>
        </div>
        <div>
          <el-button :icon="Download">导出模板</el-button>
        </div>
      </div>

      <el-table :data="tableData" v-loading="false" border stripe style="width: 100%">
        <el-table-column prop="deviceCode" label="设备编码" min-width="120" />
        <el-table-column prop="deviceName" label="设备名称" min-width="120" />
        <el-table-column prop="deviceType" label="设备类型" min-width="100">
          <template #default="{ row }">
            {{ getDeviceTypeText(row.deviceType) }}
          </template>
        </el-table-column>
        <el-table-column prop="model" label="设备型号" min-width="120" />
        <el-table-column prop="status" label="设备状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="installLocation" label="安装位置" min-width="150" />
        <el-table-column prop="installDate" label="安装日期" min-width="120" />
        <el-table-column prop="manufacturer" label="生产厂商" min-width="120" />
        <el-table-column label="操作" min-width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备编码" prop="deviceCode">
              <el-input v-model="formData.deviceCode" placeholder="请输入设备编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备名称" prop="deviceName">
              <el-input v-model="formData.deviceName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备类型" prop="deviceType">
              <el-select v-model="formData.deviceType" placeholder="请选择" class="w-full">
                <el-option v-for="item in deviceTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择" class="w-full">
                <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安装位置" prop="installLocation">
              <el-input v-model="formData.installLocation" placeholder="请输入安装位置" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安装日期" prop="installDate">
              <el-date-picker v-model="formData.installDate" type="date" placeholder="选择日期" class="w-full" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产厂商" prop="manufacturer">
              <el-input v-model="formData.manufacturer" placeholder="请输入生产厂商" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备型号" prop="model">
              <el-input v-model="formData.model" placeholder="请输入设备型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="序列号" prop="serialNumber">
              <el-input v-model="formData.serialNumber" placeholder="请输入序列号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="质保期(月)">
              <el-input-number v-model="formData.warrantyPeriod" :min="1" :max="120" class="w-full" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="设备描述">
              <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入设备描述" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
