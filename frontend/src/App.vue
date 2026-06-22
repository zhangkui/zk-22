<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRoute } from 'vue-router'

const userStore = useUserStore()
const route = useRoute()

onMounted(() => {
  userStore.initUserInfo()
})
</script>

<template>
  <router-view />
</template>
