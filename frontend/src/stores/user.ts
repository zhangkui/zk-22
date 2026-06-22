import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginRequest } from '@/types'
import { login, logout, getUserInfo } from '@/api/auth'
import { ElMessage } from 'element-plus'

const TOKEN_KEY = 'tunnel_monitor_token'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem(TOKEN_KEY) || '')
  const userInfo = ref<User | null>(null)

  const isLoggedIn = computed(() => !!token.value)

  function setToken(newToken: string) {
    token.value = newToken
    localStorage.setItem(TOKEN_KEY, newToken)
  }

  function clearToken() {
    token.value = ''
    localStorage.removeItem(TOKEN_KEY)
  }

  async function loginAction(params: LoginRequest) {
    try {
      const res = await login(params)
      if (res.code === 200) {
        setToken(res.data.token)
        userInfo.value = res.data.user
        ElMessage.success('登录成功')
        return true
      } else {
        ElMessage.error(res.message || '登录失败')
        return false
      }
    } catch (error) {
      ElMessage.error('登录失败，请稍后重试')
      return false
    }
  }

  async function logoutAction() {
    try {
      await logout()
    } catch (error) {
      console.error('退出登录失败', error)
    } finally {
      clearToken()
      userInfo.value = null
      ElMessage.success('已退出登录')
    }
  }

  async function fetchUserInfo() {
    if (!token.value) return null
    try {
      const res = await getUserInfo()
      if (res.code === 200) {
        userInfo.value = res.data
        return res.data
      }
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
    return null
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    loginAction,
    logoutAction,
    fetchUserInfo,
    setToken,
    clearToken
  }
})
