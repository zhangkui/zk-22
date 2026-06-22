import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption, ECharts } from 'echarts'

export function useECharts(chartRef: { value: HTMLElement | null }, theme: string = 'dark') {
  const chartInstance = ref<ECharts | null>(null)
  const isReady = ref(false)

  function initChart() {
    if (!chartRef.value) return
    
    if (chartInstance.value) {
      chartInstance.value.dispose()
    }
    
    chartInstance.value = echarts.init(chartRef.value, theme, {
      renderer: 'canvas'
    })
    
    isReady.value = true
    
    window.addEventListener('resize', handleResize)
  }

  function setOption(option: EChartsOption, notMerge: boolean = false) {
    if (!chartInstance.value) {
      nextTick(() => {
        chartInstance.value?.setOption(option, notMerge)
      })
      return
    }
    chartInstance.value.setOption(option, notMerge)
  }

  function handleResize() {
    chartInstance.value?.resize()
  }

  function resize() {
    chartInstance.value?.resize()
  }

  function showLoading() {
    chartInstance.value?.showLoading()
  }

  function hideLoading() {
    chartInstance.value?.hideLoading()
  }

  function dispose() {
    if (chartInstance.value) {
      chartInstance.value.dispose()
      chartInstance.value = null
      isReady.value = false
      window.removeEventListener('resize', handleResize)
    }
  }

  onMounted(() => {
    nextTick(() => {
      initChart()
    })
  })

  onUnmounted(() => {
    dispose()
  })

  watch(() => chartRef.value, (newVal) => {
    if (newVal && !chartInstance.value) {
      initChart()
    }
  })

  return {
    chartInstance,
    isReady,
    initChart,
    setOption,
    resize,
    showLoading,
    hideLoading,
    dispose
  }
}

export function getDefaultChartOptions(): EChartsOption {
  return {
    backgroundColor: 'transparent',
    color: ['#00F5D4', '#10B981', '#F59E0B', '#EF4444', '#3B82F6', '#8B5CF6'],
    textStyle: {
      color: '#94A3B8',
      fontFamily: 'Inter, system-ui, sans-serif'
    },
    title: {
      textStyle: {
        color: '#E2E8F0',
        fontSize: 16,
        fontWeight: 600
      },
      subtextStyle: {
        color: '#64748B'
      }
    },
    legend: {
      textStyle: {
        color: '#94A3B8'
      },
      itemWidth: 14,
      itemHeight: 14
    },
    tooltip: {
      backgroundColor: 'rgba(19, 32, 56, 0.95)',
      borderColor: '#1E3A5F',
      borderWidth: 1,
      textStyle: {
        color: '#E2E8F0'
      },
      axisPointer: {
        lineStyle: {
          color: '#00F5D4',
          type: 'dashed'
        }
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      axisLine: {
        lineStyle: {
          color: '#1E3A5F'
        }
      },
      axisLabel: {
        color: '#64748B'
      },
      axisTick: {
        show: false
      },
      splitLine: {
        show: false
      }
    },
    yAxis: {
      axisLine: {
        show: false
      },
      axisLabel: {
        color: '#64748B'
      },
      axisTick: {
        show: false
      },
      splitLine: {
        lineStyle: {
          color: '#1E3A5F',
          type: 'dashed'
        }
      }
    }
  }
}
