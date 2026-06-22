<template>
  <div class="min-h-screen bg-dark-bg tech-grid-bg flex items-center justify-center p-4">
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute top-0 left-0 w-96 h-96 bg-accent-500/5 rounded-full blur-3xl -translate-x-1/2 -translate-y-1/2"></div>
      <div class="absolute bottom-0 right-0 w-96 h-96 bg-accent-500/5 rounded-full blur-3xl translate-x-1/2 translate-y-1/2"></div>
    </div>

    <div class="relative z-10 w-full max-w-md">
      <div class="card p-8 animate-float">
        <div class="text-center mb-8">
          <div class="w-16 h-16 mx-auto mb-4 rounded-2xl bg-gradient-to-br from-accent-400 to-accent-600 flex-center text-primary-500 font-bold text-2xl shadow-glow-lg">
            隧
          </div>
          <h1 class="text-2xl font-bold text-dark-text mb-2">隧道结构健康监测系统</h1>
          <p class="text-dark-muted">Tunnel Structure Health Monitoring System</p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="space-y-5"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              size="large"
              prefix-icon="User"
              class="input-field"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              prefix-icon="Lock"
              show-password
              class="input-field"
            />
          </el-form-item>

          <div class="flex items-center justify-between text-sm">
            <el-checkbox v-model="rememberMe" class="text-dark-muted">
              记住我
            </el-checkbox>
            <a href="#" class="text-accent-400 hover:text-accent-300 transition-colors">
              忘记密码？
            </a>
          </div>

          <el-button
            type="primary"
            size="large"
            class="w-full h-11 text-base font-semibold mt-2"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form>

        <div class="mt-8 pt-6 border-t border-dark-border">
          <div class="flex items-center justify-center space-x-6 text-sm text-dark-muted">
            <span class="flex items-center">
              <el-icon class="mr-1 text-accent-400"><Key /></el-icon>
              安全加密
            </span>
            <span class="flex items-center">
              <el-icon class="mr-1 text-accent-400"><Service /></el-icon>
              24/7 监控
            </span>
            <span class="flex items-center">
              <el-icon class="mr-1 text-accent-400"><Monitor /></el-icon>
              智能分析
            </span>
          </div>
        </div>

        <div class="mt-6 text-center text-xs text-dark-muted">
          <p>演示账号: admin / admin123</p>
        </div>
      </div>

      <div class="mt-6 text-center text-sm text-dark-muted">
        <p>© 2024 隧道监测技术有限公司 版权所有</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useMockApi } from '@/composables/useMockData'
import { mockLoginResponse } from '@/utils/mock'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock, Key, Service, Monitor } from '@element-plus/icons-vue'
import type { LoginRequest } from '@/types'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)
const rememberMe = ref(false)

const loginForm = reactive<LoginRequest>({
  username: '',
  password: ''
})

const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 32, message: '密码长度在 6 到 32 个字符', trigger: 'blur' }
  ]
}

const mockLogin = useMockApi(mockLoginResponse, 800)

async function handleLogin() {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        let res
        
        if (loginForm.username === 'admin' && loginForm.password === 'admin123') {
          res = await mockLogin()
        } else {
          res = await userStore.loginAction(loginForm)
          if (!res) {
            loading.value = false
            return
          }
        }
        
        if (res.code === 200) {
          userStore.setToken(res.data.token)
          userStore.userInfo = res.data.user
          
          ElMessage.success('登录成功')
          
          const redirect = route.query.redirect as string
          router.push(redirect || '/dashboard')
        } else {
          ElMessage.error(res.message || '登录失败')
        }
      } catch (error) {
        ElMessage.error('登录失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  const savedUsername = localStorage.getItem('remember_username')
  if (savedUsername) {
    loginForm.username = savedUsername
    rememberMe.value = true
  }
})
</script>

<style scoped>
.card {
  backdrop-filter: blur(10px);
  background: rgba(19, 32, 56, 0.8);
  border: 1px solid rgba(30, 58, 95, 0.5);
}

.animate-float {
  animation: float 6s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}
</style>
