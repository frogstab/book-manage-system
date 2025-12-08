<template>
  <div class="user-management">
    <div class="page-header">
      <h2>👥 用户管理</h2>
      <div class="header-actions">
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="搜索用户..."
          class="search-input"
        >
        <button @click="addUser" class="btn-primary">+ 添加用户</button>
      </div>
    </div>

    <!-- 用户表格 -->
    <div class="user-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>头像</th>
            <th>用户名</th>
            <th>邮箱</th>
            <th>角色</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in filteredUsers" :key="user.id">
            <td>{{ user.id }}</td>
            <td>
              <img :src="user.avatar" class="avatar">
            </td>
            <td>{{ user.name }}</td>
            <td>{{ user.email }}</td>
            <td>
              <span class="role-badge" :class="user.role">
                {{ user.role }}
              </span>
            </td>
            <td>
              <span class="status-badge" :class="user.status">
                {{ user.status }}
              </span>
            </td>
            <td class="actions">
              <button @click="editUser(user)" class="btn-edit">编辑</button>
              <button @click="deleteUser(user)" class="btn-delete">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 添加/编辑用户模态框 -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ editingUser ? '编辑用户' : '添加用户' }}</h3>
        
        <form @submit.prevent="saveUser">
          <div class="form-group">
            <label>用户名</label>
            <input type="text" v-model="formData.name" required>
          </div>
          
          <div class="form-group">
            <label>邮箱</label>
            <input type="email" v-model="formData.email" required>
          </div>
          
          <div class="form-group">
            <label>密码</label>
            <input type="password" v-model="formData.password" :required="!editingUser">
          </div>
          
          <div class="form-group">
            <label>角色</label>
            <select v-model="formData.role">
              <option value="user">普通用户</option>
              <option value="admin">管理员</option>
            </select>
          </div>
          
          <div class="form-actions">
            <button type="button" @click="closeModal" class="btn-cancel">取消</button>
            <button type="submit" class="btn-save">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// 数据
const users = ref([])
const searchQuery = ref('')
const showModal = ref(false)
const editingUser = ref(null)

// 表单数据
const formData = ref({
  id: '',
  name: '',
  email: '',
  password: '',
  role: 'user'
})

// 计算属性
const filteredUsers = computed(() => {
  if (!searchQuery.value) return users.value
  return users.value.filter(user => 
    user.name.includes(searchQuery.value) || 
    user.email.includes(searchQuery.value)
  )
})

// 方法
const loadUsers = () => {
  // 模拟数据
  users.value = [
    { id: 1, name: '张三', email: 'zhangsan@test.com', avatar: '/avatar1.jpg', role: 'admin', status: 'active' },
    { id: 2, name: '李四', email: 'lisi@test.com', avatar: '/avatar2.jpg', role: 'user', status: 'active' },
    { id: 3, name: '王五', email: 'wangwu@test.com', avatar: '/avatar3.jpg', role: 'user', status: 'inactive' }
  ]
}

const addUser = () => {
  resetForm()
  showModal.value = true
}

const editUser = (user) => {
  editingUser.value = user
  formData.value = { ...user }
  showModal.value = true
}

const deleteUser = (user) => {
  if (confirm(`确定要删除用户 ${user.name} 吗？`)) {
    users.value = users.value.filter(u => u.id !== user.id)
  }
}

const saveUser = () => {
  if (editingUser.value) {
    // 更新用户
    const index = users.value.findIndex(u => u.id === editingUser.value.id)
    users.value[index] = { ...formData.value, status: 'active' }
  } else {
    // 添加用户
    const newUser = {
      ...formData.value,
      id: Date.now(),
      status: 'active'
    }
    users.value.push(newUser)
  }
  
  closeModal()
}

const resetForm = () => {
  formData.value = {
    id: '',
    name: '',
    email: '',
    password: '',
    role: 'user'
  }
  editingUser.value = null
}

const closeModal = () => {
  showModal.value = false
  resetForm()
}

// 生命周期
onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 200px;
}

.btn-primary {
  padding: 8px 16px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.user-table {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: #f8f9fa;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.role-badge, .status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

.role-badge.admin {
  background: #e3f2fd;
  color: #1976d2;
}

.role-badge.user {
  background: #f3e5f5;
  color: #7b1fa2;
}

.status-badge.active {
  background: #e8f5e9;
  color: #2e7d32;
}

.status-badge.inactive {
  background: #ffebee;
  color: #c62828;
}

.actions {
  display: flex;
  gap: 5px;
}

.btn-edit, .btn-delete {
  padding: 4px 8px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-size: 12px;
}

.btn-edit {
  background: #4caf50;
  color: white;
}

.btn-delete {
  background: #f44336;
  color: white;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 10px;
  min-width: 400px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.btn-cancel, .btn-save {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cancel {
  background: #f5f5f5;
  color: #333;
}

.btn-save {
  background: #4caf50;
  color: white;
}
</style>