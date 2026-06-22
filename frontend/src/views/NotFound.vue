<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

function goHome() {
  router.push('/dashboard')
}

function goBack() {
  router.back()
}
</script>

<template>
  <div class="min-h-screen bg-dark-bg flex items-center justify-center">
    <div class="text-center">
      <div class="mb-8">
        <h1 class="text-[150px] font-bold bg-gradient-to-r from-accent-500 to-blue-500 bg-clip-text text-transparent leading-none">
          404
        </h1>
      </div>
      <h2 class="text-3xl font-bold text-white mb-4">页面未找到</h2>
      <p class="text-gray-400 text-lg mb-8 max-w-md mx-auto">
        抱歉，您访问的页面不存在或已被移除。请检查URL是否正确，或返回首页继续浏览。
      </p>
      <div class="flex gap-4 justify-center">
        <el-button type="primary" size="large" @click="goHome">
          返回首页
        </el-button>
        <el-button size="large" @click="goBack">
          返回上一页
        </el-button>
      </div>
      <div class="mt-12 flex justify-center">
        <div class="flex items-center gap-8 text-gray-500">
          <div class="flex items-center gap-2">
            <span class="w-3 h-3 rounded-full bg-accent-500 animate-pulse"></span>
            <span>系统运行正常</span>
          </div>
          <div class="flex items-center gap-2">
            <span class="w-3 h-3 rounded-full bg-green-500 animate-pulse"></span>
            <span>服务已连接</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
