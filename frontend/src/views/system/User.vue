<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh, Lock, RefreshRight, Setting } from '@element-plus/icons-vue'
import { useTable } from '@/composables/useTable'
import { useMockApi } from '@/composables/useMockData'
import { mockUserList } from '@/utils/mock'
import type { User } from '@/types'

const searchForm = reactive({
  username: '',
  realName: '',
  role: '',
  status: ''
})

const roleOptions = [
  { label: '超级管理员', value: 'admin' },
  { label: '系统管理员', value: 'system' },
  { label: '运营管理员', value: 'operator' },
  { label: '运维人员', value: 'maintenance' },
  { label: '普通用户', value: 'user' },
  { label: '访客', value: 'guest' }
]

const statusOptions = [
  { label: '启用', value: 'active' },
  { label: '禁用', value: 'inactive' },
  { label: '锁定', value: 'locked' }
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
} = useTable<User>(async (params) => {
  return useMockApi(mockUserList, { ...params, ...searchForm })
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formType = ref<'add' | 'edit'>('add')
const currentRow = ref<Partial<User>>({})

const formRef = ref()

const formData = reactive<Partial<User>>({
  username: '',
  realName: '',
  email: '',
  phone: '',
  role: 'user',
  status: 'active',
  department: '',
  description: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

function handleAdd() {
  dialogTitle.value = '新增用户'
  formType.value = 'add'
  Object.assign(formData, {
    username: '',
    realName: '',
    email: '',
    phone: '',
    role: 'user',
    status: 'active',
    department: '',
    description: ''
  })
  dialogVisible.value = true
}

function handleEdit(row: User) {
  dialogTitle.value = '编辑用户'
  formType.value = 'edit'
  currentRow.value = row
  Object.assign(formData, row)
  dialogVisible.value = true
}

async function handleDelete(row: User) {
  try {
    await ElMessageBox.confirm(`确定要删除用户「${row.realName}」吗？`, '提示', {
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

async function handleResetPassword(row: User) {
  try {
    await ElMessageBox.confirm(`确定要重置用户「${row.realName}」的密码吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('密码已重置为默认密码：123456')
  } catch {
  }
}

async function handleToggleStatus(row: User) {
  const newStatus = row.status === 'active' ? 'inactive' : 'active'
  try {
    await ElMessageBox.confirm(`确定要${newStatus === 'active' ? '启用' : '禁用'}用户「${row.realName}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    row.status = newStatus
    ElMessage.success(`${newStatus === 'active' ? '启用' : '禁用'}成功`)
  } catch {
  }
}

function getRoleText(role: string) {
  const map: Record<string, string> = {
    admin: '超级管理员',
    system: '系统管理员',
    operator: '运营管理员',
    maintenance: '运维人员',
    user: '普通用户',
    guest: '访客'
  }
  return map[role] || '未知'
}

function getStatusTagType(status: string) {
  const map: Record<string, string> = {
    active: 'success',
    inactive: 'info',
    locked: 'danger'
  }
  return map[status] || 'info'
}

function getStatusText(status: string) {
  const map: Record<string, string> = {
    active: '启用',
    inactive: '禁用',
    locked: '锁定'
  }
  return map[status] || '未知'
}

const stats = reactive({
  totalUsers: 128,
  activeUsers: 96,
  onlineUsers: 24,
  newUsersToday: 3
})

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="p-6">
    <div class="mb-6">
      <h2 class="text-2xl font-bold text-white mb-2">用户管理</h2>
      <p class="text-gray-400">管理系统用户账号、角色和权限</p>
    </div>

    <div class="grid grid-cols-4 gap-4 mb-6">
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">用户总数</p>
            <p class="text-3xl font-bold text-white mt-1">{{ stats.totalUsers }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-blue-500/20 flex items-center justify-center">
            <span class="text-blue-400 text-2xl">👥</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">活跃用户</p>
            <p class="text-3xl font-bold text-green-400 mt-1">{{ stats.activeUsers }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-green-500/20 flex items-center justify-center">
            <span class="text-green-400 text-2xl">✅</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">在线用户</p>
            <p class="text-3xl font-bold text-accent-500 mt-1">{{ stats.onlineUsers }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-accent-500/20 flex items-center justify-center">
            <span class="text-accent-500 text-2xl">🟢</span>
          </div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">今日新增</p>
            <p class="text-3xl font-bold text-yellow-400 mt-1">{{ stats.newUsersToday }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-yellow-500/20 flex items-center justify-center">
            <span class="text-yellow-400 text-2xl">➕</span>
          </div>
        </div>
      </div>
    </div>

    <div class="bg-dark-card rounded-lg border border-dark-border p-4">
      <div class="flex flex-wrap gap-4 mb-4">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="用户名">
            <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable class="w-40" />
          </el-form-item>
          <el-form-item label="真实姓名">
            <el-input v-model="searchForm.realName" placeholder="请输入真实姓名" clearable class="w-40" />
          </el-form-item>
          <el-form-item label="角色">
            <el-select v-model="searchForm.role" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in roleOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable class="w-40">
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            <el-button :icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="flex justify-between mb-4">
        <div>
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增用户</el-button>
        </div>
      </div>

      <el-table :data="tableData" v-loading="false" border stripe style="width: 100%">
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="realName" label="真实姓名" min-width="100" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="phone" label="手机号" min-width="120" />
        <el-table-column label="角色" min-width="120">
          <template #default="{ row }">
            <el-tag type="primary" effect="plain">{{ getRoleText(row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="部门" min-width="120" />
        <el-table-column label="状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录" min-width="150" />
        <el-table-column prop="createTime" label="创建时间" min-width="150" />
        <el-table-column label="操作" min-width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link :icon="RefreshRight" @click="handleResetPassword(row)">重置密码</el-button>
            <el-button 
              :type="row.status === 'active' ? 'warning' : 'success'" 
              link 
              :icon="Lock" 
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 'active' ? '禁用' : '启用' }}
            </el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="formData.username" placeholder="请输入用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="formData.realName" placeholder="请输入真实姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色" prop="role">
              <el-select v-model="formData.role" placeholder="请选择" class="w-full">
                <el-option v-for="item in roleOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择" class="w-full">
                <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门">
              <el-input v-model="formData.department" placeholder="请输入部门" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述">
              <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入描述" />
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
