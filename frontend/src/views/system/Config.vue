<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElTabs } from 'element-plus'
import { Upload, Refresh, Download, Tools, Bell, Lock, DataBoard, Monitor, Setting, FolderAdd } from '@element-plus/icons-vue'
import { useMockApi } from '@/composables/useMockData'

const activeTab = ref('basic')

const basicConfig = reactive({
  systemName: '隧道运营风险智能监测平台',
  systemVersion: 'v2.0.0',
  systemDescription: '基于AI的隧道结构安全与渗漏风险智能监测系统',
  copyright: '© 2024 隧道安全监测中心 版权所有',
  recordNumber: '京ICP备XXXXXXXX号',
  logoUrl: '',
  faviconUrl: '',
  loginBgUrl: '',
  enableRegister: false,
  enableCaptcha: true,
  sessionTimeout: 7200,
  maxLoginAttempts: 5,
  lockDuration: 1800
})

const alarmConfig = reactive({
  enableEmailNotify: true,
  enableSmsNotify: true,
  enableWechatNotify: false,
  emailServer: 'smtp.example.com',
  emailPort: 465,
  emailUser: 'alarm@example.com',
  emailPassword: '',
  smsProvider: 'aliyun',
  smsAccessKey: '',
  smsSecretKey: '',
  smsTemplateCode: 'SMS_XXXXXX',
  wechatCorpId: '',
  wechatSecret: '',
  wechatAgentId: '',
  alarmSoundEnabled: true,
  alarmSoundVolume: 80,
  autoAcknowledgeTimeout: 1800
})

const monitorConfig = reactive({
  dataCollectionInterval: 30,
  dataRetentionDays: 365,
  alertDataRetentionDays: 730,
  enableAutoAnalysis: true,
  analysisInterval: 300,
  riskCalculationModel: 'ensemble',
  leakDetectionSensitivity: 80,
  structureWarningThreshold: 70,
  equipmentWarningThreshold: 60,
  enableDataBackup: true,
  backupInterval: 86400,
  backupRetentionDays: 30,
  enableDataCompression: true,
  compressionLevel: 6
})

const securityConfig = reactive({
  enableHttps: true,
  enableCors: true,
  corsOrigins: '*',
  enableRateLimit: true,
  rateLimitRequests: 100,
  rateLimitWindow: 60,
  enableIpWhitelist: false,
  ipWhitelist: [],
  enableIpBlacklist: false,
  ipBlacklist: [],
  enableSqlInjectionProtection: true,
  enableXssProtection: true,
  enableCsrfProtection: true,
  passwordMinLength: 8,
  passwordRequireUppercase: true,
  passwordRequireLowercase: true,
  passwordRequireNumber: true,
  passwordRequireSpecial: true,
  passwordExpireDays: 90,
  passwordHistoryCount: 5
})

const systemStatus = reactive({
  cpuUsage: 35.6,
  memoryUsage: 62.4,
  diskUsage: 45.2,
  networkIn: 125.6,
  networkOut: 89.3,
  activeConnections: 156,
  databaseStatus: 'healthy',
  redisStatus: 'healthy',
  mqStatus: 'healthy',
  lastBackupTime: '2024-01-15 02:00:00',
  systemUptime: '15天 6小时 32分钟',
  apiResponseTime: 45,
  errorRate: 0.02
})

const configTabs = [
  { label: '基础配置', name: 'basic', icon: Setting },
  { label: '告警配置', name: 'alarm', icon: Bell },
  { label: '监测配置', name: 'monitor', icon: Monitor },
  { label: '安全配置', name: 'security', icon: Lock }
]

async function handleSave() {
  try {
    await useMockApi(() => Promise.resolve({ success: true }))
    ElMessage.success('配置保存成功')
  } catch {
    ElMessage.error('配置保存失败')
  }
}

async function handleReset() {
  try {
    await ElMessageBox.confirm('确定要重置当前配置吗？重置后将恢复为默认值。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('配置已重置为默认值')
  } catch {
  }
}

async function handleTestConnection(type: string) {
  ElMessage.info(`正在测试${type}连接...`)
  setTimeout(() => {
    ElMessage.success(`${type}连接测试成功`)
  }, 1500)
}

function getStatusColor(value: number) {
  if (value >= 80) return 'text-red-400'
  if (value >= 60) return 'text-yellow-400'
  return 'text-green-400'
}

function getStatusWidth(value: number) {
  return `${value}%`
}

function getHealthStatusColor(status: string) {
  return status === 'healthy' ? 'text-green-400' : status === 'warning' ? 'text-yellow-400' : 'text-red-400'
}

function getHealthStatusText(status: string) {
  return status === 'healthy' ? '正常' : status === 'warning' ? '警告' : '异常'
}

function handleUploadLogo() {
  ElMessage.info('上传Logo功能')
}

function handleExportConfig() {
  ElMessage.success('配置导出成功')
}

function handleImportConfig() {
  ElMessage.info('导入配置功能')
}

onMounted(() => {
})
</script>

<template>
  <div class="p-6">
    <div class="mb-6">
      <div class="flex justify-between items-start">
        <div>
          <h2 class="text-2xl font-bold text-white mb-2">系统配置</h2>
          <p class="text-gray-400">配置系统参数、告警规则、监测设置和安全策略</p>
        </div>
        <div class="flex gap-2">
          <el-button :icon="FolderAdd" @click="handleImportConfig">导入配置</el-button>
          <el-button :icon="Download" @click="handleExportConfig">导出配置</el-button>
          <el-button type="primary" :icon="Upload" @click="handleSave">保存配置</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置配置</el-button>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-4 gap-4 mb-6">
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">CPU使用率</p>
            <p :class="getStatusColor(systemStatus.cpuUsage)" class="text-3xl font-bold mt-1">{{ systemStatus.cpuUsage }}%</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-blue-500/20 flex items-center justify-center">
            <span class="text-blue-400 text-2xl">💻</span>
          </div>
        </div>
        <div class="mt-2 w-full bg-dark-bg rounded-full h-1.5">
          <div :class="systemStatus.cpuUsage >= 80 ? 'bg-red-400' : systemStatus.cpuUsage >= 60 ? 'bg-yellow-400' : 'bg-green-400'" class="h-1.5 rounded-full" :style="{ width: getStatusWidth(systemStatus.cpuUsage) }"></div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">内存使用率</p>
            <p :class="getStatusColor(systemStatus.memoryUsage)" class="text-3xl font-bold mt-1">{{ systemStatus.memoryUsage }}%</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-purple-500/20 flex items-center justify-center">
            <span class="text-purple-400 text-2xl">🧠</span>
          </div>
        </div>
        <div class="mt-2 w-full bg-dark-bg rounded-full h-1.5">
          <div :class="systemStatus.memoryUsage >= 80 ? 'bg-red-400' : systemStatus.memoryUsage >= 60 ? 'bg-yellow-400' : 'bg-green-400'" class="h-1.5 rounded-full" :style="{ width: getStatusWidth(systemStatus.memoryUsage) }"></div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">磁盘使用率</p>
            <p :class="getStatusColor(systemStatus.diskUsage)" class="text-3xl font-bold mt-1">{{ systemStatus.diskUsage }}%</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-orange-500/20 flex items-center justify-center">
            <span class="text-orange-400 text-2xl">💾</span>
          </div>
        </div>
        <div class="mt-2 w-full bg-dark-bg rounded-full h-1.5">
          <div :class="systemStatus.diskUsage >= 80 ? 'bg-red-400' : systemStatus.diskUsage >= 60 ? 'bg-yellow-400' : 'bg-green-400'" class="h-1.5 rounded-full" :style="{ width: getStatusWidth(systemStatus.diskUsage) }"></div>
        </div>
      </div>
      <div class="bg-dark-card rounded-lg p-4 border border-dark-border">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-400 text-sm">活跃连接</p>
            <p class="text-3xl font-bold text-accent-500 mt-1">{{ systemStatus.activeConnections }}</p>
          </div>
          <div class="w-12 h-12 rounded-full bg-accent-500/20 flex items-center justify-center">
            <span class="text-accent-500 text-2xl">🌐</span>
          </div>
        </div>
        <div class="mt-2 text-sm text-gray-400">
          运行时间: {{ systemStatus.systemUptime }}
        </div>
      </div>
    </div>

    <div class="grid grid-cols-3 gap-6 mb-6">
      <div class="bg-dark-card rounded-lg border border-dark-border p-4">
        <h3 class="text-lg font-semibold text-white mb-4">服务状态</h3>
        <div class="space-y-3">
          <div class="flex items-center justify-between">
            <span class="text-gray-400">数据库</span>
            <span :class="getHealthStatusColor(systemStatus.databaseStatus)" class="font-medium">
              ● {{ getHealthStatusText(systemStatus.databaseStatus) }}
            </span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-400">Redis缓存</span>
            <span :class="getHealthStatusColor(systemStatus.redisStatus)" class="font-medium">
              ● {{ getHealthStatusText(systemStatus.redisStatus) }}
            </span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-400">消息队列</span>
            <span :class="getHealthStatusColor(systemStatus.mqStatus)" class="font-medium">
              ● {{ getHealthStatusText(systemStatus.mqStatus) }}
            </span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-400">API响应时间</span>
            <span class="text-green-400 font-medium">{{ systemStatus.apiResponseTime }}ms</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-400">错误率</span>
            <span class="text-green-400 font-medium">{{ systemStatus.errorRate }}%</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-400">最后备份</span>
            <span class="text-gray-300 font-medium">{{ systemStatus.lastBackupTime }}</span>
          </div>
        </div>
      </div>

      <div class="col-span-2 bg-dark-card rounded-lg border border-dark-border p-4">
        <el-tabs v-model="activeTab" class="config-tabs">
          <el-tab-pane 
            v-for="tab in configTabs" 
            :key="tab.name" 
            :label="tab.label" 
            :name="tab.name"
          >
            <div v-if="activeTab === 'basic'" class="space-y-6">
              <div class="grid grid-cols-2 gap-6">
                <div>
                  <el-form-item label="系统名称">
                    <el-input v-model="basicConfig.systemName" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="系统版本">
                    <el-input v-model="basicConfig.systemVersion" disabled />
                  </el-form-item>
                </div>
                <div class="col-span-2">
                  <el-form-item label="系统描述">
                    <el-input v-model="basicConfig.systemDescription" type="textarea" :rows="2" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="版权信息">
                    <el-input v-model="basicConfig.copyright" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="备案号">
                    <el-input v-model="basicConfig.recordNumber" />
                  </el-form-item>
                </div>
              </div>
              <el-divider />
              <div class="grid grid-cols-2 gap-6">
                <div>
                  <el-form-item label="会话超时(秒)">
                    <el-input-number v-model="basicConfig.sessionTimeout" :min="300" :max="86400" class="w-full" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="最大登录尝试次数">
                    <el-input-number v-model="basicConfig.maxLoginAttempts" :min="1" :max="20" class="w-full" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="锁定时长(秒)">
                    <el-input-number v-model="basicConfig.lockDuration" :min="60" :max="86400" class="w-full" />
                  </el-form-item>
                </div>
              </div>
              <el-divider />
              <div class="flex flex-wrap gap-8">
                <el-form-item label="启用注册">
                  <el-switch v-model="basicConfig.enableRegister" />
                </el-form-item>
                <el-form-item label="启用验证码">
                  <el-switch v-model="basicConfig.enableCaptcha" />
                </el-form-item>
              </div>
            </div>

            <div v-if="activeTab === 'alarm'" class="space-y-6">
              <div class="flex flex-wrap gap-8 mb-4">
                <el-form-item label="邮件通知">
                  <el-switch v-model="alarmConfig.enableEmailNotify" />
                </el-form-item>
                <el-form-item label="短信通知">
                  <el-switch v-model="alarmConfig.enableSmsNotify" />
                </el-form-item>
                <el-form-item label="微信通知">
                  <el-switch v-model="alarmConfig.enableWechatNotify" />
                </el-form-item>
                <el-form-item label="告警声音">
                  <el-switch v-model="alarmConfig.alarmSoundEnabled" />
                </el-form-item>
              </div>
              <el-divider />
              <div v-if="alarmConfig.enableEmailNotify" class="space-y-4">
                <h4 class="text-lg font-medium text-white">邮件配置</h4>
                <div class="grid grid-cols-2 gap-6">
                  <div>
                    <el-form-item label="SMTP服务器">
                      <el-input v-model="alarmConfig.emailServer" />
                    </el-form-item>
                  </div>
                  <div>
                    <el-form-item label="端口">
                      <el-input-number v-model="alarmConfig.emailPort" :min="1" :max="65535" class="w-full" />
                    </el-form-item>
                  </div>
                  <div>
                    <el-form-item label="邮箱账号">
                      <el-input v-model="alarmConfig.emailUser" />
                    </el-form-item>
                  </div>
                  <div>
                    <el-form-item label="授权码">
                      <el-input v-model="alarmConfig.emailPassword" type="password" show-password />
                    </el-form-item>
                  </div>
                </div>
                <el-button :icon="Tools" @click="handleTestConnection('邮件')">测试连接</el-button>
              </div>
              <el-divider />
              <div class="grid grid-cols-2 gap-6">
                <div>
                  <el-form-item label="告警音量">
                    <el-slider v-model="alarmConfig.alarmSoundVolume" :min="0" :max="100" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="自动确认超时(秒)">
                    <el-input-number v-model="alarmConfig.autoAcknowledgeTimeout" :min="300" :max="86400" class="w-full" />
                  </el-form-item>
                </div>
              </div>
            </div>

            <div v-if="activeTab === 'monitor'" class="space-y-6">
              <div class="grid grid-cols-2 gap-6">
                <div>
                  <el-form-item label="数据采集间隔(秒)">
                    <el-input-number v-model="monitorConfig.dataCollectionInterval" :min="5" :max="3600" class="w-full" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="分析间隔(秒)">
                    <el-input-number v-model="monitorConfig.analysisInterval" :min="60" :max="3600" class="w-full" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="数据保留天数">
                    <el-input-number v-model="monitorConfig.dataRetentionDays" :min="30" :max="3650" class="w-full" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="告警数据保留天数">
                    <el-input-number v-model="monitorConfig.alertDataRetentionDays" :min="30" :max="3650" class="w-full" />
                  </el-form-item>
                </div>
              </div>
              <el-divider />
              <div class="grid grid-cols-2 gap-6">
                <div>
                  <el-form-item label="渗漏检测灵敏度">
                    <el-slider v-model="monitorConfig.leakDetectionSensitivity" :min="1" :max="100" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="结构预警阈值">
                    <el-slider v-model="monitorConfig.structureWarningThreshold" :min="1" :max="100" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="设备预警阈值">
                    <el-slider v-model="monitorConfig.equipmentWarningThreshold" :min="1" :max="100" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="风险计算模型">
                    <el-select v-model="monitorConfig.riskCalculationModel" class="w-full">
                      <el-option label="集成学习模型" value="ensemble" />
                      <el-option label="深度学习模型" value="deep_learning" />
                      <el-option label="传统统计模型" value="statistical" />
                    </el-select>
                  </el-form-item>
                </div>
              </div>
              <el-divider />
              <div class="flex flex-wrap gap-8">
                <el-form-item label="自动分析">
                  <el-switch v-model="monitorConfig.enableAutoAnalysis" />
                </el-form-item>
                <el-form-item label="数据备份">
                  <el-switch v-model="monitorConfig.enableDataBackup" />
                </el-form-item>
                <el-form-item label="数据压缩">
                  <el-switch v-model="monitorConfig.enableDataCompression" />
                </el-form-item>
              </div>
              <div v-if="monitorConfig.enableDataBackup" class="grid grid-cols-2 gap-6">
                <div>
                  <el-form-item label="备份间隔(秒)">
                    <el-input-number v-model="monitorConfig.backupInterval" :min="3600" :max="86400" class="w-full" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="备份保留天数">
                    <el-input-number v-model="monitorConfig.backupRetentionDays" :min="7" :max="365" class="w-full" />
                  </el-form-item>
                </div>
              </div>
            </div>

            <div v-if="activeTab === 'security'" class="space-y-6">
              <div class="flex flex-wrap gap-8 mb-4">
                <el-form-item label="HTTPS">
                  <el-switch v-model="securityConfig.enableHttps" />
                </el-form-item>
                <el-form-item label="CORS">
                  <el-switch v-model="securityConfig.enableCors" />
                </el-form-item>
                <el-form-item label="限流">
                  <el-switch v-model="securityConfig.enableRateLimit" />
                </el-form-item>
                <el-form-item label="SQL注入防护">
                  <el-switch v-model="securityConfig.enableSqlInjectionProtection" />
                </el-form-item>
                <el-form-item label="XSS防护">
                  <el-switch v-model="securityConfig.enableXssProtection" />
                </el-form-item>
                <el-form-item label="CSRF防护">
                  <el-switch v-model="securityConfig.enableCsrfProtection" />
                </el-form-item>
              </div>
              <el-divider />
              <div class="grid grid-cols-2 gap-6">
                <div>
                  <el-form-item label="密码最小长度">
                    <el-input-number v-model="securityConfig.passwordMinLength" :min="6" :max="32" class="w-full" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="密码过期天数">
                    <el-input-number v-model="securityConfig.passwordExpireDays" :min="0" :max="365" class="w-full" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="历史密码保留数">
                    <el-input-number v-model="securityConfig.passwordHistoryCount" :min="0" :max="20" class="w-full" />
                  </el-form-item>
                </div>
              </div>
              <div class="flex flex-wrap gap-8">
                <el-form-item label="要求大写字母">
                  <el-switch v-model="securityConfig.passwordRequireUppercase" />
                </el-form-item>
                <el-form-item label="要求小写字母">
                  <el-switch v-model="securityConfig.passwordRequireLowercase" />
                </el-form-item>
                <el-form-item label="要求数字">
                  <el-switch v-model="securityConfig.passwordRequireNumber" />
                </el-form-item>
                <el-form-item label="要求特殊字符">
                  <el-switch v-model="securityConfig.passwordRequireSpecial" />
                </el-form-item>
              </div>
              <el-divider />
              <div v-if="securityConfig.enableRateLimit" class="grid grid-cols-2 gap-6">
                <div>
                  <el-form-item label="限流请求数">
                    <el-input-number v-model="securityConfig.rateLimitRequests" :min="10" :max="10000" class="w-full" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="时间窗口(秒)">
                    <el-input-number v-model="securityConfig.rateLimitWindow" :min="1" :max="3600" class="w-full" />
                  </el-form-item>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<style scoped>
.config-tabs :deep(.el-tabs__header) {
  margin-bottom: 24px;
  border-bottom-color: #1E3A5F;
}

.config-tabs :deep(.el-tabs__nav-wrap::after) {
  background-color: #1E3A5F;
}

.config-tabs :deep(.el-tabs__item) {
  color: #8FA3BF;
}

.config-tabs :deep(.el-tabs__item.is-active) {
  color: #00F5D4;
}

.config-tabs :deep(.el-tabs__active-bar) {
  background-color: #00F5D4;
}
</style>
