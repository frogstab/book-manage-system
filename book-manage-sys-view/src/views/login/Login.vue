<template>
  <div class="login-container">
    <div class="login-wrapper">
      <!-- 左侧功能介绍区域 -->
      <div class="feature-section">
        <h1 class="welcome-title">欢迎使用智慧图书管理系统</h1>
        <p class="welcome-desc">
          一个集图书管理、借阅追踪、会员管理、数据分析于一体的现代化图书管理系统。
          帮助图书馆、学校和企业实现图书资源的智能化管理。
        </p>
        
        <ul class="feature-list">
          <li class="feature-item">
            <span class="feature-icon">📚</span>
            <span>智能图书管理，支持扫码入库</span>
          </li>
          <li class="feature-item">
            <span class="feature-icon">🔍</span>
            <span>快速检索系统，支持多条件查询</span>
          </li>
          <li class="feature-item">
            <span class="feature-icon">👥</span>
            <span>会员管理系统，记录借阅历史</span>
          </li>
          <li class="feature-item">
            <span class="feature-icon">📊</span>
            <span>数据统计分析，生成可视化报表</span>
          </li>
          <li class="feature-item">
            <span class="feature-icon">🔔</span>
            <span>智能提醒功能，逾期自动通知</span>
          </li>
        </ul>
      </div>

      <!-- 右侧登录区域 -->
      <div class="login-section">
        <div class="login-card">
          <h2 class="login-title">管理员登录</h2>
          
          <form @submit.prevent="handleLogin" class="login-form">
            <div class="form-group">
              <label class="form-label">用户名</label>
              <input
                v-model="loginForm.username"
                type="text"
                placeholder="请输入管理员账号"
                class="form-input"
                required
                autofocus
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">密码</label>
              <input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入登录密码"
                class="form-input"
                required
                @keyup.enter="handleLogin"
              />
            </div>
            
            <!-- 角色选择 -->
            <div class="form-group">
              <label class="form-label">登录身份</label>
              <div class="role-select">
                <el-radio-group v-model="loginForm.role" size="medium">
                  <el-radio :label="1" border>管理员</el-radio>
                  <el-radio :label="2" border>普通用户</el-radio>
                </el-radio-group>
              </div>
            </div>
            
            <!-- 错误提示 -->
            <div v-if="errorMessage" class="error-message">
              {{ errorMessage }}
            </div>
            
            <button
              type="submit"
              class="login-btn"
              :disabled="loading"
            >
              <span v-if="loading" class="loading-spinner"></span>
              {{ loading ? '登录中...' : '登录系统' }}
            </button>
            
            <div class="register-link">
              没有账户？
              <router-link to="/register" class="register-text">立即注册</router-link>
            </div>
          </form>
          
          <div class="copyright">
            © 2025 智慧图书管理系统 版权所有<br/>
            技术支持：张许烨、陈丽、许梓晗
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { setToken, setRole } from '@/utils/storage.js'
import { ElMessage } from 'element-ui'

const router = useRouter()
const route = useRoute()

const loginForm = reactive({
  username: '',
  password: '',
  role: 1 // 默认管理员
})

const loading = ref(false)
const errorMessage = ref('')

// 模拟用户数据（实际应该调用API）
const mockUsers = [
  { username: 'admin', password: 'admin123', role: 1, name: '系统管理员' },
  { username: 'user1', password: 'user123', role: 2, name: '普通用户' },
  { username: 'zhangsan', password: '123456', role: 1, name: '张三' },
  { username: 'lisi', password: '123456', role: 2, name: '李四' }
]

const handleLogin = async () => {
  // 重置错误信息
  errorMessage.value = ''
  
  // 表单验证
  if (!loginForm.username.trim()) {
    errorMessage.value = '请输入用户名'
    return
  }
  
  if (!loginForm.password.trim()) {
    errorMessage.value = '请输入密码'
    return
  }
  
  loading.value = true
  
  try {
    // 模拟API调用延迟
    await new Promise(resolve => setTimeout(resolve, 800))
    
    // 模拟用户验证（实际项目中应该调用后端API）
    const user = mockUsers.find(u => 
      u.username === loginForm.username && 
      u.password === loginForm.password
    )
    
    if (user) {
      // 检查角色是否匹配
      if (user.role !== loginForm.role) {
        errorMessage.value = '请选择正确的登录身份'
        return
      }
      
      // 存储用户信息
      setToken('mock-token-' + Date.now())
      setRole(user.role)
      sessionStorage.setItem('userName', user.name)
      sessionStorage.setItem('userId', user.username)
      
      // 清除错误信息
      errorMessage.value = ''
      
      ElMessage.success({
        message: `登录成功！欢迎 ${user.name}`,
        duration: 2000
      })
      
      // 根据角色跳转到不同页面
      const redirectPath = route.query.redirect || (user.role === 1 ? '/admin/adminLayout' : '/user/main')
      
      setTimeout(() => {
        router.push(redirectPath)
      }, 1000)
      
    } else {
      // 验证失败
      errorMessage.value = '用户名或密码错误'
      loginForm.password = ''
    }
    
  } catch (error) {
    console.error('登录错误:', error)
    errorMessage.value = '登录失败，请检查网络连接'
  } finally {
    loading.value = false
  }
}

// 自动聚焦到用户名输入框
onMounted(() => {
  // 如果已经登录，根据角色跳转到对应页面
  const token = sessionStorage.getItem('token')
  if (token) {
    const role = sessionStorage.getItem('role')
    if (role === '1') {
      router.push('/admin/adminLayout')
    } else if (role === '2') {
      router.push('/user/main')
    }
  }
  
  // 自动聚焦
  setTimeout(() => {
    const usernameInput = document.querySelector('input[type="text"]')
    if (usernameInput) usernameInput.focus()
  }, 100)
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-wrapper {
  width: 100%;
  max-width: 1200px;
  min-height: 600px;
  display: flex;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

/* 左侧区域 */
.feature-section {
  flex: 1;
  padding: 60px 50px;
  background: linear-gradient(135deg, #2c3e50 0%, #4a6491 100%);
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.welcome-title {
  font-size: 32px;
  margin-bottom: 20px;
  font-weight: 700;
  line-height: 1.3;
}

.welcome-desc {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 40px;
  opacity: 0.9;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  font-size: 16px;
  padding: 12px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.feature-icon {
  font-size: 20px;
  margin-right: 15px;
  width: 24px;
  text-align: center;
}

/* 右侧区域 */
.login-section {
  flex: 0 0 500px;
  padding: 40px;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background: white;
  padding: 40px 30px;
  border-radius: 15px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
}

.login-title {
  text-align: center;
  color: #333;
  font-size: 28px;
  margin-bottom: 30px;
  font-weight: 600;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  width: 100%;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-size: 14px;
  font-weight: 500;
}

.form-input {
  width: 100%;
  padding: 14px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  font-size: 16px;
  box-sizing: border-box;
  transition: all 0.3s;
  background: #f8f9fa;
}

.form-input:focus {
  outline: none;
  border-color: #409EFF;
  background: white;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.1);
}

.form-input::placeholder {
  color: #999;
}

.role-select {
  padding: 10px 0;
}

.role-select .el-radio-group {
  display: flex;
  gap: 10px;
}

.role-select .el-radio {
  flex: 1;
  margin: 0;
}

.error-message {
  color: #F56C6C;
  font-size: 14px;
  text-align: center;
  padding: 8px;
  background: #fef0f0;
  border-radius: 6px;
  margin: 5px 0;
}

.login-btn {
  width: 100%;
  padding: 16px;
  background: linear-gradient(to right, #409EFF, #337ecc);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 10px;
  position: relative;
  min-height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(64, 158, 255, 0.3);
}

.login-btn:active:not(:disabled) {
  transform: translateY(0);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s linear infinite;
  margin-right: 10px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.register-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
  font-size: 14px;
}

.register-text {
  color: #409EFF;
  text-decoration: none;
  font-weight: 500;
  margin-left: 5px;
  cursor: pointer;
  transition: color 0.3s;
}

.register-text:hover {
  color: #337ecc;
  text-decoration: underline;
}

.copyright {
  text-align: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #eee;
  color: #888;
  font-size: 12px;
  line-height: 1.6;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .login-wrapper {
    flex-direction: column;
    max-width: 600px;
    min-height: auto;
  }
  
  .feature-section {
    padding: 40px 30px;
  }
  
  .login-section {
    flex: none;
    width: 100%;
    padding: 30px;
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: 20px;
  }
  
  .login-wrapper {
    border-radius: 15px;
  }
  
  .welcome-title {
    font-size: 24px;
  }
  
  .login-title {
    font-size: 24px;
  }
  
  .login-card {
    padding: 30px 20px;
    box-shadow: none;
  }
  
  .feature-section {
    padding: 30px 20px;
  }
  
  .role-select .el-radio-group {
    flex-direction: column;
  }
}
</style>