import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAppStore = defineStore('app', () => {
  const sidebarCollapsed = ref(false)
  const theme = ref<'dark' | 'light'>('dark')
  const currentRoutePath = ref('/dashboard')

  const isDarkTheme = computed(() => theme.value === 'dark')

  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  function setTheme(newTheme: 'dark' | 'light') {
    theme.value = newTheme
    if (newTheme === 'dark') {
      document.documentElement.classList.add('dark')
    } else {
      document.documentElement.classList.remove('dark')
    }
    localStorage.setItem('app-theme', newTheme)
  }

  function initTheme() {
    const savedTheme = localStorage.getItem('app-theme') as 'dark' | 'light' | null
    if (savedTheme) {
      setTheme(savedTheme)
    } else {
      setTheme('dark')
    }
  }

  function setCurrentRoute(path: string) {
    currentRoutePath.value = path
  }

  return {
    sidebarCollapsed,
    theme,
    currentRoutePath,
    isDarkTheme,
    toggleSidebar,
    setTheme,
    initTheme,
    setCurrentRoute
  }
})
