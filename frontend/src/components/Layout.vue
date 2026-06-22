<template>
  <div class="app-container tech-grid-bg">
    <aside
      class="sidebar bg-dark-bg border-r border-dark-border flex flex-col transition-all duration-300"
      :class="{ 'w-64': !appStore.sidebarCollapsed, 'w-16': appStore.sidebarCollapsed }"
    >
      <div class="h-16 flex items-center px-4 border-b border-dark-border">
        <div
          class="flex items-center"
          :class="{ 'justify-center w-full': appStore.sidebarCollapsed }"
        >
          <div class="w-8 h-8 rounded-lg bg-gradient-to-br from-accent-400 to-accent-600 flex-center text-primary-500 font-bold text-lg mr-3">
            隧
          </div>
          <transition name="fade">
            <span
              v-if="!appStore.sidebarCollapsed"
              class="text-lg font-bold text-gradient whitespace-nowrap"
            >
              隧道监测系统
            </span>
          </transition>
        </div>
      </div>

      <el-menu
        :default-active="activeMenu"
        :collapse="appStore.sidebarCollapsed"
        background-color="#0A1628"
        text-color="#94A3B8"
        active-text-color="#00F5D4"
        class="flex-1 border-none mt-2"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <el-sub-menu index="tunnel">
          <template #title>
            <el-icon><Engineering /></el-icon>
            <span>隧道管理</span>
          </template>
          <el-menu-item index="/tunnel/modeling">三维建模</el-menu-item>
          <el-menu-item index="/tunnel/segment">管片管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="monitoring">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>监测监控</span>
          </template>
          <el-menu-item index="/monitoring/acoustic">声学监测</el-menu-item>
          <el-menu-item index="/monitoring/location">漏水定位</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="inspection">
          <template #title>
            <el-icon><Clipboard /></el-icon>
            <span>巡检管理</span>
          </template>
          <el-menu-item index="/inspection/planning">巡检计划</el-menu-item>
          <el-menu-item index="/inspection/task">巡检任务</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="alarm">
          <template #title>
            <el-icon><Bell /></el-icon>
            <span>告警管理</span>
          </template>
          <el-menu-item index="/alarm/list">告警列表</el-menu-item>
          <el-menu-item index="/alarm/replay">告警回放</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="device">
          <template #title>
            <el-icon><Tools /></el-icon>
            <span>设备管理</span>
          </template>
          <el-menu-item index="/device/ledger">设备台账</el-menu-item>
          <el-menu-item index="/device/lifetime">寿命评估</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="risk">
          <template #title>
            <el-icon><TrendCharts /></el-icon>
            <span>风险评估</span>
          </template>
          <el-menu-item index="/risk/heatmap">风险热力图</el-menu-item>
          <el-menu-item index="/risk/report">风险报告</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">用户管理</el-menu-item>
          <el-menu-item index="/system/config">系统配置</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </aside>

    <div class="main-container">
      <header class="h-16 bg-dark-card border-b border-dark-border flex items-center justify-between px-6">
        <div class="flex items-center">
          <button
            class="w-8 h-8 rounded-lg hover:bg-dark-hover flex-center text-dark-muted hover:text-accent-400 transition-colors mr-4"
            @click="appStore.toggleSidebar"
          >
            <el-icon :size="18">
              <component :is="appStore.sidebarCollapsed ? 'Expand' : 'Fold'" />
            </el-icon>
          </button>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item
              v-for="(item, index) in breadcrumbs"
              :key="index"
              :to="index < breadcrumbs.length - 1 ? item.path : undefined"
            >
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="flex items-center space-x-4">
          <el-dropdown trigger="click" @command="handleCommand">
            <div class="flex items-center cursor-pointer hover:text-accent-400 transition-colors">
              <div class="w-8 h-8 rounded-full bg-gradient-to-br from-accent-400 to-accent-600 flex-center text-primary-500 font-semibold text-sm mr-2">
                {{ userStore.userInfo?.realName?.charAt(0) || 'U' }}
              </div>
              <span class="text-dark-text">{{ userStore.userInfo?.realName || '用户' }}</span>
              <el-icon class="ml-1"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-slide" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import {
  DataAnalysis,
  Engineering,
  Monitor,
  Clipboard,
  Bell,
  Tools,
  TrendCharts,
  Setting,
  User,
  SwitchButton,
  ArrowDown,
  Expand,
  Fold
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  return matched.map(item => ({
    path: item.path,
    title: item.meta?.title as string
  }))
})

async function handleCommand(command: string) {
  if (command === 'logout') {
    await userStore.logoutAction()
    router.push('/login')
  } else if (command === 'profile') {
    ElMessage.info('个人中心功能开发中')
  }
}
</script>

<style scoped>
.sidebar {
  min-height: 100vh;
}

:deep(.el-menu) {
  background-color: transparent !important;
}

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  margin: 2px 8px;
  border-radius: 8px;
  height: 44px;
  line-height: 44px;
}

:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background-color: #1E3A5F !important;
}

:deep(.el-menu-item.is-active) {
  background-color: rgba(0, 245, 212, 0.1) !important;
  color: #00F5D4 !important;
}

:deep(.el-breadcrumb__inner) {
  color: #94A3B8;
}

:deep(.el-breadcrumb__inner.is-link:hover) {
  color: #00F5D4;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #E2E8F0;
  font-weight: 500;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
