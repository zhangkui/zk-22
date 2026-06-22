import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import { ElMessage } from 'element-plus'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    redirect: '/dashboard',
    meta: { requiresAuth: true }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { title: '首页', icon: 'DataAnalysis', requiresAuth: true }
  },
  {
    path: '/tunnel',
    name: 'Tunnel',
    meta: { title: '隧道管理', icon: 'Engineering', requiresAuth: true },
    children: [
      {
        path: 'modeling',
        name: 'TunnelModeling',
        component: () => import('@/views/tunnel/Modeling.vue'),
        meta: { title: '三维建模', requiresAuth: true }
      },
      {
        path: 'segment',
        name: 'TunnelSegment',
        component: () => import('@/views/tunnel/Segment.vue'),
        meta: { title: '管片管理', requiresAuth: true }
      }
    ]
  },
  {
    path: '/monitoring',
    name: 'Monitoring',
    meta: { title: '监测监控', icon: 'Monitor', requiresAuth: true },
    children: [
      {
        path: 'acoustic',
        name: 'MonitoringAcoustic',
        component: () => import('@/views/monitoring/Acoustic.vue'),
        meta: { title: '声学监测', requiresAuth: true }
      },
      {
        path: 'location',
        name: 'MonitoringLocation',
        component: () => import('@/views/monitoring/Location.vue'),
        meta: { title: '漏水定位', requiresAuth: true }
      }
    ]
  },
  {
    path: '/inspection',
    name: 'Inspection',
    meta: { title: '巡检管理', icon: 'Clipboard', requiresAuth: true },
    children: [
      {
        path: 'planning',
        name: 'InspectionPlanning',
        component: () => import('@/views/inspection/Planning.vue'),
        meta: { title: '巡检计划', requiresAuth: true }
      },
      {
        path: 'task',
        name: 'InspectionTask',
        component: () => import('@/views/inspection/Task.vue'),
        meta: { title: '巡检任务', requiresAuth: true }
      }
    ]
  },
  {
    path: '/alarm',
    name: 'Alarm',
    meta: { title: '告警管理', icon: 'Alarm', requiresAuth: true },
    children: [
      {
        path: 'list',
        name: 'AlarmList',
        component: () => import('@/views/alarm/List.vue'),
        meta: { title: '告警列表', requiresAuth: true }
      },
      {
        path: 'replay',
        name: 'AlarmReplay',
        component: () => import('@/views/alarm/Replay.vue'),
        meta: { title: '告警回放', requiresAuth: true }
      }
    ]
  },
  {
    path: '/device',
    name: 'Device',
    meta: { title: '设备管理', icon: 'Tools', requiresAuth: true },
    children: [
      {
        path: 'ledger',
        name: 'DeviceLedger',
        component: () => import('@/views/device/Ledger.vue'),
        meta: { title: '设备台账', requiresAuth: true }
      },
      {
        path: 'lifetime',
        name: 'DeviceLifetime',
        component: () => import('@/views/device/Lifetime.vue'),
        meta: { title: '寿命评估', requiresAuth: true }
      }
    ]
  },
  {
    path: '/risk',
    name: 'Risk',
    meta: { title: '风险评估', icon: 'HeatMap', requiresAuth: true },
    children: [
      {
        path: 'heatmap',
        name: 'RiskHeatmap',
        component: () => import('@/views/risk/Heatmap.vue'),
        meta: { title: '风险热力图', requiresAuth: true }
      },
      {
        path: 'report',
        name: 'RiskReport',
        component: () => import('@/views/risk/Report.vue'),
        meta: { title: '风险报告', requiresAuth: true }
      }
    ]
  },
  {
    path: '/system',
    name: 'System',
    meta: { title: '系统管理', icon: 'Setting', requiresAuth: true },
    children: [
      {
        path: 'user',
        name: 'SystemUser',
        component: () => import('@/views/system/User.vue'),
        meta: { title: '用户管理', requiresAuth: true }
      },
      {
        path: 'config',
        name: 'SystemConfig',
        component: () => import('@/views/system/Config.vue'),
        meta: { title: '系统配置', requiresAuth: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '404', requiresAuth: false }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, _from, next) => {
  const userStore = useUserStore()
  const appStore = useAppStore()
  
  document.title = `${to.meta.title || '隧道监测系统'} - 隧道结构健康监测系统`
  
  if (to.path === '/login') {
    if (userStore.isLoggedIn) {
      next('/dashboard')
    } else {
      next()
    }
    return
  }
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }
  
  if (userStore.isLoggedIn && !userStore.userInfo) {
    try {
      await userStore.fetchUserInfo()
    } catch (error) {
      userStore.clearToken()
      ElMessage.error('登录已过期，请重新登录')
      next({ path: '/login', query: { redirect: to.fullPath } })
      return
    }
  }
  
  appStore.setCurrentRoute(to.path)
  next()
})

export default router
