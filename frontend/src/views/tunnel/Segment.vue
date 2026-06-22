<template>
  <div class="page-wrapper">
    <div class="page-header flex-between">
      <div>
        <h1 class="page-title">管片管理</h1>
        <p class="page-subtitle">隧道管片信息管理</p>
      </div>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增管片
      </el-button>
    </div>

    <div class="card mb-6">
      <div class="card-body">
        <el-form :inline="true" :model="searchForm" class="flex flex-wrap gap-4">
          <el-form-item label="隧道">
            <el-select v-model="searchForm.tunnelId" placeholder="选择隧道" clearable style="width: 180px">
              <el-option
                v-for="tunnel in tunnels"
                :key="tunnel.id"
                :label="tunnel.name"
                :value="tunnel.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="管片名称">
            <el-input
              v-model="searchForm.keyword"
              placeholder="请输入管片名称"
              clearable
              style="width: 180px"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 120px">
              <el-option label="正常" :value="1" />
              <el-option label="异常" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><RefreshLeft /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <div class="card">
      <div class="card-body">
        <el-table
          v-loading="loading"
          :data="dataList"
          stripe
          style="width: 100%"
        >
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="tunnelName" label="所属隧道" min-width="120" />
          <el-table-column prop="sectionName" label="管片名称" min-width="120" />
          <el-table-column label="里程范围" min-width="180">
            <template #default="{ row }">
              <span>K{{ (row.startMileage / 1000).toFixed(3) }} - K{{ (row.endMileage / 1000).toFixed(3) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="sectionType" label="断面类型" width="120" />
          <el-table-column prop="liningType" label="衬砌类型" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">
                {{ row.status === 1 ? '正常' : '异常' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link size="small" @click="handleView(row)">查看</el-button>
              <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
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

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      destroy-on-close
    >
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="所属隧道" prop="tunnelId">
          <el-select v-model="form.tunnelId" placeholder="选择隧道" style="width: 100%">
            <el-option
              v-for="tunnel in tunnels"
              :key="tunnel.id"
              :label="tunnel.name"
              :value="tunnel.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="管片名称" prop="sectionName">
          <el-input v-model="form.sectionName" placeholder="请输入管片名称" />
        </el-form-item>
        <el-form-item label="起始里程" prop="startMileage">
          <el-input-number v-model="form.startMileage" :min="0" placeholder="米" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束里程" prop="endMileage">
          <el-input-number v-model="form.endMileage" :min="0" placeholder="米" style="width: 100%" />
        </el-form-item>
        <el-form-item label="断面类型" prop="sectionType">
          <el-select v-model="form.sectionType" placeholder="选择断面类型" style="width: 100%">
            <el-option label="圆形断面" value="圆形断面" />
            <el-option label="马蹄形断面" value="马蹄形断面" />
            <el-option label="矩形断面" value="矩形断面" />
            <el-option label="城门洞形" value="城门洞形" />
          </el-select>
        </el-form-item>
        <el-form-item label="衬砌类型" prop="liningType">
          <el-select v-model="form.liningType" placeholder="选择衬砌类型" style="width: 100%">
            <el-option label="单层衬砌" value="单层衬砌" />
            <el-option label="双层衬砌" value="双层衬砌" />
            <el-option label="复合式衬砌" value="复合式衬砌" />
            <el-option label="装配式衬砌" value="装配式衬砌" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">正常</el-radio>
            <el-radio :value="0">异常</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useMockPageData } from '@/composables/useMockData'
import { mockSectionList, mockTunnelList } from '@/utils/mock'
import { useMockApi } from '@/composables/useMockData'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Search, RefreshLeft } from '@element-plus/icons-vue'
import type { TunnelSection, TunnelStructure } from '@/types'

const loading = ref(false)
const dataList = ref<TunnelSection[]>([])
const tunnels = ref<TunnelStructure[]>([])
const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const formRef = ref<FormInstance>()

const searchForm = reactive({
  tunnelId: undefined as number | undefined,
  keyword: '',
  status: undefined as number | undefined
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  pageSizes: [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper'
})

const form = reactive<Partial<TunnelSection>>({
  tunnelId: undefined,
  sectionName: '',
  startMileage: 0,
  endMileage: 0,
  sectionType: '',
  liningType: '',
  status: 1
})

const formRules: FormRules = {
  tunnelId: [{ required: true, message: '请选择隧道', trigger: 'change' }],
  sectionName: [{ required: true, message: '请输入管片名称', trigger: 'blur' }],
  startMileage: [{ required: true, message: '请输入起始里程', trigger: 'blur' }],
  endMileage: [{ required: true, message: '请输入结束里程', trigger: 'blur' }]
}

const dialogTitle = computed(() => dialogMode.value === 'add' ? '新增管片' : '编辑管片')

const fetchSections = useMockPageData(mockSectionList.bind(null, searchForm.tunnelId || 1))
const getTunnels = useMockApi(mockTunnelList)

async function loadData() {
  loading.value = true
  try {
    const res = await fetchSections({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    })
    if (res.code === 200) {
      dataList.value = res.data.list
      pagination.total = res.data.total
    }
  } finally {
    loading.value = false
  }
}

async function loadTunnels() {
  const res = await getTunnels(5)
  if (res.code === 200) {
    tunnels.value = res.data
  }
}

function handleSearch() {
  pagination.pageNum = 1
  loadData()
}

function handleReset() {
  searchForm.tunnelId = undefined
  searchForm.keyword = ''
  searchForm.status = undefined
  pagination.pageNum = 1
  loadData()
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

function handleAdd() {
  dialogMode.value = 'add'
  Object.assign(form, {
    tunnelId: undefined,
    sectionName: '',
    startMileage: 0,
    endMileage: 0,
    sectionType: '',
    liningType: '',
    status: 1
  })
  dialogVisible.value = true
}

function handleEdit(row: TunnelSection) {
  dialogMode.value = 'edit'
  Object.assign(form, row)
  dialogVisible.value = true
}

function handleView(row: TunnelSection) {
  ElMessage.info(`查看管片: ${row.sectionName}`)
}

function handleDelete(row: TunnelSection) {
  ElMessageBox.confirm(`确定要删除管片 "${row.sectionName}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate((valid) => {
    if (valid) {
      ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '编辑成功')
      dialogVisible.value = false
      loadData()
    }
  })
}

onMounted(() => {
  loadTunnels()
  loadData()
})
</script>
