<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useECharts } from '@/composables/useECharts'
import { Search, Refresh, Download, Plus, Edit, Delete, View, Printer } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const searchForm = reactive({
  reportType: '',
  status: '',
  startDate: '',
  endDate: ''
})

const reportTypeOptions = [
  { label: '日报', value: 'daily' },
  { label: '周报', value: 'weekly' },
  { label: '月报', value: 'monthly' },
  { label: '季度报', value: 'quarterly' },
  { label: '年报', value: 'yearly' }
]

const statusOptions = [
  { label: '草稿', value: 'draft' },
  { label: '待审核', value: 'pending' },
  { label: '已发布', value: 'published' },
  { label: '已驳回', value: 'rejected' }
]

const loading1 = ref<HTMLElement | null>(null)
const loading2 = ref<HTMLElement | null>(null)

const { setOption: setOption1 } = useECharts(loading1 as any)
const { setOption: setOption2 } = useECharts(loading2 as any)

const stats = reactive({
  totalReports: 156,
  pendingReview: 12,
  published: 138,
  avgRiskScore: 32.5
})

const reports = ref<any[]>([])
const currentPage = ref(1)
const pageSize = ref(10)

function generateReports() {
  const types = ['daily', 'weekly', 'monthly', 'quarterly', 'yearly']
  const statuses = ['draft', 'pending', 'published', 'rejected']
  const list = []

  for (let i = 0; i < 15; i++) {
    const type = types[Math.floor(Math.random() * types.length)]
    const status = statuses[Math.floor(Math.random() * statuses.length)]
    list.push({
      id: i + 1,
      reportNo: `RPT-${String(Date.now()).slice(-8)}-${String(i + 1).padStart(3, '0')}`,
      title: getReportTitle(type),
      type,
      status,
      riskLevel: ['low', 'medium', 'high', 'critical'][Math.floor(Math.random() * 4)],
      riskScore: Math.floor(Math.random() * 80) + 10,
      startDate: '2024-01-01',
      endDate: '2024-01-15',
      creator: ['张三', '李四', '王五', '赵六'][Math.floor(Math.random() * 4)],
      createTime: `2024-01-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}`,
      reviewer: status === 'published' ? ['主任', '总工', '经理'][Math.floor(Math.random() * 3)] : '',
      reviewTime: status === 'published' ? `2024-01-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}` : '',
      description: '基于隧道监测数据和AI分析模型生成的风险评估报告，包含结构安全、渗漏风险、设备状态等多维度评估。'
    })
  }
  reports.value = list.sort((a, b) => new Date(b.createTime).getTime() - new Date(a.createTime).getTime())
}

function getReportTitle(type: string) {
  const titles: Record<string, string[]> = {
    daily: ['1月15日隧道运营风险日报', '1月14日隧道运营风险日报', '1月13日隧道运营风险日报'],
    weekly: ['第3周隧道运营风险周报', '第2周隧道运营风险周报', '第1周隧道运营风险周报'],
    monthly: ['1月份隧道运营风险月报', '12月份隧道运营风险月报', '11月份隧道运营风险月报'],
    quarterly: ['Q1隧道运营风险季度报', 'Q4隧道运营风险季度报', 'Q3隧道运营风险季度报'],
    yearly: ['2024年度隧道运营风险年报', '2023年度隧道运营风险年报', '2022年度隧道运营风险年报']
  }
  const list = titles[type] || ['风险报告']
  return list[Math.floor(Math.random() * list.length)]
}

function initCharts() {
  const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
  const avgRiskScores = [28.5, 32.1, 35.8, 38.2, 36.5, 34.2, 31.8, 29.5, 33.6, 36.8, 34.5, 30.2]
  const highRiskCounts = [3, 5, 8, 12, 10, 7, 5, 4, 6, 9, 7, 4]

  const option1 = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(10, 22, 40, 0.9)',
      borderColor: '#1E3A5F',
      textStyle: { color: '#fff' }
    },
    legend: {
      data: ['平均风险值', '高风险事件数'],
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
      data: months,
      axisLine: { lineStyle: { color: '#1E3A5F' } },
      axisLabel: { color: '#8FA3BF' }
    },
    yAxis: [
      {
        type: 'value',
        name: '风险值',
        nameTextStyle: { color: '#8FA3BF' },
        axisLine: { lineStyle: { color: '#1E3A5F' } },
        axisLabel: { color: '#8FA3BF' },
        splitLine: { lineStyle: { color: '#1E3A5F' } }
      },
      {
        type: 'value',
        name: '事件数',
        nameTextStyle: { color: '#8FA3BF' },
        axisLine: { lineStyle: { color: '#1E3A5F' } },
        axisLabel: { color: '#8FA3BF' },
        splitLine: { show: false }
      }
    ],
    series: [
      {
        name: '平均风险值',
        type: 'line',
        data: avgRiskScores,
        smooth: true,
        lineStyle: { color: '#00F5D4', width: 3 },
        itemStyle: { color: '#00F5D4' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(0, 245, 212, 0.3)' },
              { offset: 1, color: 'rgba(0, 245, 212, 0)' }
            ]
          }
        },
        markLine: {
          silent: true,
          lineStyle: { color: '#EF4444', type: 'dashed' },
          data: [{ yAxis: 40, name: '预警阈值' }],
          label: { color: '#EF4444', formatter: '预警阈值' }
        }
      },
      {
        name: '高风险事件数',
        type: 'bar',
        yAxisIndex: 1,
        data: highRiskCounts,
        itemStyle: { color: '#EF4444', borderRadius: [4, 4, 0, 0] }
      }
    ]
  }
  setOption1(option1)

  const reportTypes = ['日报', '周报', '月报', '季报', '年报']
  const reportCounts = [86, 52, 12, 4, 2]

  const option2 = {
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
      data: reportTypes.map((name, i) => ({
        value: reportCounts[i],
        name,
        itemStyle: {
          color: ['#00F5D4', '#3B82F6', '#8B5CF6', '#F59E0B', '#EF4444'][i]
        }
      }))
    }]
  }
  setOption2(option2)
}

function getReportTypeText(type: string) {
  const map: Record<string, string> = {
    daily: '日报',
    weekly: '周报',
    monthly: '月报',
    quarterly: '季报',
    yearly: '年报'
  }
  return map[type] || '未知'
}

function getStatusTag(status: string) {
  const map: Record<string, { type: string; text: string }> = {
    draft: { type: 'info', text: '草稿' },
    pending: { type: 'warning', text: '待审核' },
    published: { type: 'success', text: '已发布' },
    rejected: { type: 'danger', text: '已驳回' }
  }
  return map[status] || { type: 'info', text: '未知' }
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

function handleAdd() {
  ElMessage.success('跳转到新增报告页面')
}

function handleView(row: any) {
  ElMessage.info(`查看报告: ${row.title}`)
}

function handleEdit(row: any) {
  ElMessage.info(`编辑报告: ${row.title}`)
}

async function handleDelete(row: any) {
  try {
    await ElMessageBox.confirm(`确定要删除报告「${row.title}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('删除成功')
    generateReports()
  } catch {
  }
}

function handleExport(row: any) {
  ElMessage.success(`正在导出报告: ${row.title}`)
}

function handlePrint(row: any) {
  ElMessage.info(`正在打印报告: ${row.title}`)
}

function handleSearch() {
  generateReports()
}

function handleReset() {
  Object.assign(searchForm, {
    reportType: '',
    status: '',
    startDate: '',
    endDate: ''
  })
  generateReports()
}

onMounted(() => {
  initCharts()
  generateReports()
})
</script>

<template>
  <div class="p-6">
    <div class="mb-6">
      <div class="flex justify-between items-start">
        <div>
          <h2 class="text-2xl font-bold text-white mb-2">风险报告</h2>
          <p class="text-gray-400">生成和管理隧道风险评估报告</p>
        </div>
        <el-button type="primary" :icon="Plus" @click="handleAdd">生成报告</el-button>
      </div>
    </div>

    <div class="grid grid-cols-4 gap-4 mb-6">
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">报告总数</p>
            <p class="text-3xl font-bold text-white mt-1">{{ stats.totalReports }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-blue-500/20 flex items-center justify-center">
            <span class="text-blue-400 text-2xl">📄</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">待审核</p>
            <p class="text-3xl font-bold text-yellow-400 mt-1">{{ stats.pendingReview }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-yellow-500/20 flex items-center justify-center">
            <span class="text-yellow-400 text-2xl">⏳</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">已发布</p>
            <p class="text-3xl font-bold text-green-400 mt-1">{{ stats.published }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-green-500/20 flex items-center justify-center">
            <span class="text-green-400 text-2xl">✅</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">平均风险值</p>
            <p class="text-3xl font-bold text-accent-500 mt-1">{{ stats.avgRiskScore }}<span class="text-lg">%</span></p>
          </div>
          <div class="w-12 h-12 rounded-full bg-accent-500/20 flex items-center justify-center">
            <span class="text-accent-500 text-2xl">📊</span>
          </div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-3 gap-6 mb-6">
      <div class="col-span-2 bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">年度风险趋势</h3>
        <div ref="loading1" class="h-64"></div>
      </div>
      <div class="bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">报告类型分布</h3>
        <div ref="loading2" class="h-64"></div>
      </div>
    </div>

    <div class="bg-dark-card rounded-lg border border-dark-border p-4">
      <div class="flex flex-wrap gap-4 mb-4">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="报告类型">
            <el-select v-model="searchForm.reportType" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in reportTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="日期范围">
            <el-date-picker
              v-model="searchForm.startDate"
              type="date"
              placeholder="开始日期"
              class="w-40"
            />
          </el-form-item>
          <el-form-item label="-">
            <el-date-picker
              v-model="searchForm.endDate"
              type="date"
              placeholder="结束日期"
              class="w-40"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            <el-button :icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="reports" v-loading="false" border stripe style="width: 100%">
        <el-table-column prop="reportNo" label="报告编号" min-width="180" />
        <el-table-column prop="title" label="报告标题" min-width="200" show-overflow-tooltip />
        <el-table-column label="报告类型" width="100">
          <template #default="{ row }">
            {{ getReportTypeText(row.type) }}
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getRiskLevelTag(row.riskLevel).type" size="small">
              {{ getRiskLevelTag(row.riskLevel).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险值" width="100">
          <template #default="{ row }">
            <span 
              class="font-bold"
              :class="row.riskScore >= 60 ? 'text-red-400' : row.riskScore >= 30 ? 'text-yellow-400' : 'text-green-400'"
            >{{ row.riskScore }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status).type" size="small">
              {{ getStatusTag(row.status).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="创建人" width="100" />
        <el-table-column prop="createTime" label="创建时间" min-width="150" />
        <el-table-column prop="reviewer" label="审核人" width="100" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link :icon="View" @click="handleView(row)">查看</el-button>
            <el-button type="primary" link :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link :icon="Download" @click="handleExport(row)">导出</el-button>
            <el-button type="primary" link :icon="Printer" @click="handlePrint(row)">打印</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="reports.length"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </div>
  </div>
</template>
