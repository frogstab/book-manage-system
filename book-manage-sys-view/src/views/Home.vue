<template>
  <div class="home-container">
    <header class="header">
      <div class="header-content">
        <div class="logo-section">
          <img src="../assets/logo.png" alt="Logo" class="logo" />
          <h1>智慧图书管理系统</h1>
        </div>
        <div class="user-section">
          <span>欢迎，{{ username }}</span>
          <button @click="handleLogout" class="logout-btn">退出登录</button>
        </div>
      </div>
    </header>
    
    <main class="main-content">
      <div class="dashboard">
        <h2>系统仪表盘</h2>
        <div class="stats-grid">
          <div class="stat-card">
            <h3>图书总数</h3>
            <p class="stat-value">1,234</p>
          </div>
          <div class="stat-card">
            <h3>今日借阅</h3>
            <p class="stat-value">56</p>
          </div>
          <div class="stat-card">
            <h3>待还图书</h3>
            <p class="stat-value">89</p>
          </div>
          <div class="stat-card">
            <h3>会员总数</h3>
            <p class="stat-value">456</p>
          </div>
        </div>
        
        <div class="welcome-message">
          <h2>欢迎使用智慧图书管理系统！</h2>
          <p>请使用左侧导航菜单开始您的操作</p>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')

onMounted(() => {
  const user = localStorage.getItem('user')
  username.value = user || '管理员'
})

const handleLogout = () => {
  // 清除登录信息
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  localStorage.removeItem('loginTime')
  
  // 跳转到登录页
  router.push('/login')
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: linear-gradient(to right, #2c3e50, #4a6491);
  color: white;
  padding: 0 30px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 70px;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.logo {
  width: 40px;
  height: 40px;
  border-radius: 8px;
}

.logo-section h1 {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.user-section {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 16px;
}

.logout-btn {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.main-content {
  max-width: 1200px;
  margin: 30px auto;
  padding: 0 20px;
}

.dashboard h2 {
  color: #333;
  margin-bottom: 30px;
  font-size: 28px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  background: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  text-align: center;
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-card h3 {
  color: #666;
  font-size: 16px;
  margin-bottom: 15px;
  font-weight: 500;
}

.stat-value {
  color: #3498db;
  font-size: 36px;
  font-weight: 700;
  margin: 0;
}

.welcome-message {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  text-align: center;
}

.welcome-message h2 {
  color: #2c3e50;
  margin-bottom: 15px;
}

.welcome-message p {
  color: #666;
  font-size: 18px;
}
</style>