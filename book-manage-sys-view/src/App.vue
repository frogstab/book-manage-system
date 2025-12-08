<template>
  <!-- 根容器 -->
  <div id="app">
    <!-- 加载动画 -->
    <div v-if="isLoading" class="app-loading">
      <div class="loading-spinner">
        <div class="spinner"></div>
        <p>智慧图书管理系统加载中...</p>
      </div>
    </div>

    <!-- 路由视图容器 -->
    <div v-else class="app-content">
      <!-- 错误边界处理 -->
      <div v-if="hasError" class="error-boundary">
        <div class="error-content">
          <h2>⚠️ 系统出现异常</h2>
          <p>抱歉，系统发生了一些错误，请刷新页面重试</p>
          <button @click="handleRetry" class="retry-btn">重新加载</button>
          <button @click="goToHome" class="home-btn">返回首页</button>
        </div>
      </div>

      <!-- 正常路由渲染 -->
      <router-view v-else />
    </div>

    <!-- 全局消息提示 -->
    <div v-if="globalMessage" class="global-message" :class="messageType">
      {{ globalMessage }}
      <button class="close-btn" @click="closeMessage">×</button>
    </div>

    <!-- 水印 -->
    <div class="watermark">智慧图书管理系统</div>
  </div>
</template>

<script setup>
import { ref, onMounted, onErrorCaptured } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 状态管理
const isLoading = ref(true)
const hasError = ref(false)
const globalMessage = ref('')
const messageType = ref('info')

// 模拟应用初始化
const initApp = () => {
  // 模拟加载过程
  setTimeout(() => {
    isLoading.value = false
    
    // 显示欢迎消息
    showGlobalMessage('欢迎使用智慧图书管理系统', 'success')
  }, 800)
}

// 全局消息提示
const showGlobalMessage = (message, type = 'info') => {
  globalMessage.value = message
  messageType.value = type
  
  // 3秒后自动关闭
  setTimeout(() => {
    globalMessage.value = ''
  }, 3000)
}

// 关闭消息
const closeMessage = () => {
  globalMessage.value = ''
}

// 错误重试
const handleRetry = () => {
  hasError.value = false
  window.location.reload()
}

// 返回首页
const goToHome = () => {
  hasError.value = false
  router.push('/')
}

// 全局错误捕获
onErrorCaptured((error) => {
  console.error('App捕获到错误:', error)
  hasError.value = true
  
  // 显示错误消息
  showGlobalMessage('系统发生错误，请刷新页面重试', 'error')
  
  // 阻止错误继续向上传播
  return false
})

// 监听路由变化
router.afterEach((to) => {
  // 路由切换时滚动到顶部
  window.scrollTo(0, 0)
  
  // 可以在这里添加路由变化的其他逻辑
  console.log(`导航到: ${to.path}`)
})

// 页面初始化
onMounted(() => {
  initApp()
  
  // 监听全局错误
  window.addEventListener('unhandledrejection', (event) => {
    console.error('未处理的Promise错误:', event.reason)
    showGlobalMessage('系统出现异常，请稍后重试', 'error')
  })
  
  // 监听离线/在线状态
  window.addEventListener('offline', () => {
    showGlobalMessage('网络连接已断开，请检查网络设置', 'warning')
  })
  
  window.addEventListener('online', () => {
    showGlobalMessage('网络连接已恢复', 'success')
  })
})

// 在组件卸载前清理
import { onBeforeUnmount } from 'vue'
onBeforeUnmount(() => {
  window.removeEventListener('unhandledrejection', null)
  window.removeEventListener('offline', null)
  window.removeEventListener('online', null)
})
</script>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 
               'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-size: 14px;
  line-height: 1.5;
  color: #333;
  background-color: #f5f7fa;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  min-height: 100vh;
  position: relative;
}

/* 加载动画 */
.app-loading {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.loading-spinner {
  text-align: center;
  color: white;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-spinner p {
  margin-top: 20px;
  font-size: 16px;
  opacity: 0.8;
}

/* 错误边界页面 */
.error-boundary {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.error-content {
  text-align: center;
  padding: 40px;
  background: white;
  border-radius: 15px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  width: 90%;
}

.error-content h2 {
  color: #ff4757;
  margin-bottom: 15px;
  font-size: 24px;
}

.error-content p {
  color: #666;
  margin-bottom: 25px;
  font-size: 16px;
}

.retry-btn, .home-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  margin: 0 10px;
}

.retry-btn {
  background: #6a11cb;
  color: white;
}

.retry-btn:hover {
  background: #2575fc;
  transform: translateY(-2px);
}

.home-btn {
  background: #f1f2f6;
  color: #333;
}

.home-btn:hover {
  background: #dfe4ea;
  transform: translateY(-2px);
}

/* 全局消息提示 */
.global-message {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 15px 20px;
  border-radius: 8px;
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-width: 300px;
  max-width: 400px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  z-index: 9999;
  animation: slideIn 0.3s ease-out;
  font-size: 14px;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

.global-message.info {
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
}

.global-message.success {
  background: linear-gradient(135deg, #2ecc71 0%, #27ae60 100%);
}

.global-message.warning {
  background: linear-gradient(135deg, #ffa502 0%, #ff7f00 100%);
}

.global-message.error {
  background: linear-gradient(135deg, #ff4757 0%, #ff3838 100%);
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
  margin-left: 15px;
  padding: 0 5px;
  opacity: 0.7;
  transition: opacity 0.3s;
}

.close-btn:hover {
  opacity: 1;
}

/* 水印 */
.watermark {
  position: fixed;
  bottom: 20px;
  right: 20px;
  color: rgba(0, 0, 0, 0.1);
  font-size: 14px;
  font-weight: bold;
  pointer-events: none;
  z-index: 999;
  user-select: none;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .global-message {
    left: 20px;
    right: 20px;
    max-width: none;
    min-width: auto;
  }
  
  .error-content {
    padding: 30px 20px;
  }
  
  .retry-btn, .home-btn {
    display: block;
    width: 100%;
    margin: 10px 0;
  }
}

/* 打印样式 */
@media print {
  .global-message,
  .watermark,
  .app-loading {
    display: none !important;
  }
}
</style>