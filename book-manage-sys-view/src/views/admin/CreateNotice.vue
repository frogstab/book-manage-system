<template>
  <div class="create-notice">
    <h2>📝 创建通知</h2>
    
    <div class="notice-form">
      <div class="form-group">
        <label>通知标题</label>
        <input type="text" v-model="form.title" placeholder="请输入通知标题">
      </div>
      
      <div class="form-group">
        <label>通知内容</label>
        <textarea v-model="form.content" rows="8" placeholder="请输入通知内容"></textarea>
      </div>
      
      <div class="form-group">
        <label>通知类型</label>
        <div class="type-selector">
          <label v-for="type in noticeTypes" :key="type.value" 
                 :class="['type-option', { selected: form.type === type.value }]">
            <input type="radio" v-model="form.type" :value="type.value" hidden>
            <span class="type-icon">{{ type.icon }}</span>
            <span class="type-name">{{ type.name }}</span>
          </label>
        </div>
      </div>
      
      <div class="form-group">
        <label>发布设置</label>
        <div class="publish-settings">
          <div class="setting-item">
            <label>
              <input type="radio" v-model="form.publishType" value="now">
              立即发布
            </label>
          </div>
          <div class="setting-item">
            <label>
              <input type="radio" v-model="form.publishType" value="schedule">
              定时发布
            </label>
            <input v-if="form.publishType === 'schedule'" 
                   type="datetime-local" 
                   v-model="form.scheduleTime"
                   class="datetime-input">
          </div>
        </div>
      </div>
      
      <div class="form-group">
        <label>通知选项</label>
        <div class="notice-options">
          <label>
            <input type="checkbox" v-model="form.isImportant">
            <span class="option-label">重要通知</span>
          </label>
          <label>
            <input type="checkbox" v-model="form.needConfirm">
            <span class="option-label">需要确认</span>
          </label>
          <label>
            <input type="checkbox" v-model="form.sendEmail">
            <span class="option-label">发送邮件</span>
          </label>
        </div>
      </div>
      
      <div class="form-actions">
        <button @click="saveDraft" class="btn-draft">保存草稿</button>
        <button @click="publish" class="btn-publish">发布通知</button>
        <button @click="cancel" class="btn-cancel">取消</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 表单数据
const form = ref({
  title: '',
  content: '',
  type: 'normal',
  publishType: 'now',
  scheduleTime: '',
  isImportant: false,
  needConfirm: false,
  sendEmail: false
})

// 通知类型选项
const noticeTypes = [
  { value: 'normal', name: '普通通知', icon: '📢' },
  { value: 'important', name: '重要通知', icon: '⚠️' },
  { value: 'urgent', name: '紧急通知', icon: '🚨' }
]

// 方法
const validateForm = () => {
  if (!form.value.title.trim()) {
    alert('请填写通知标题')
    return false
  }
  if (!form.value.content.trim()) {
    alert('请填写通知内容')
    return false
  }
  return true
}

const saveDraft = () => {
  if (!validateForm()) return
  
  // 模拟保存草稿
  console.log('保存草稿:', form.value)
  alert('草稿保存成功！')
  router.push('/admin/notices')
}

const publish = () => {
  if (!validateForm()) return
  
  let message = '确定要发布通知吗？'
  
  if (form.value.publishType === 'schedule') {
    message = `确定要在 ${form.value.scheduleTime} 定时发布通知吗？`
  }
  
  if (confirm(message)) {
    // 模拟发布通知
    console.log('发布通知:', form.value)
    alert('通知发布成功！')
    router.push('/admin/notices')
  }
}

const cancel = () => {
  if (form.value.title || form.value.content) {
    if (confirm('确定要取消吗？未保存的内容将丢失。')) {
      router.push('/admin/notices')
    }
  } else {
    router.push('/admin/notices')
  }
}
</script>

<style scoped>
.create-notice {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

h2 {
  margin-bottom: 30px;
  color: #2c3e50;
  text-align: center;
}

.notice-form {
  background: white;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 2px 20px rgba(0,0,0,0.1);
}

.form-group {
  margin-bottom: 25px;
}

.form-group label {
  display: block;
  margin-bottom: 10px;
  color: #2c3e50;
  font-weight: 600;
  font-size: 16px;
}

.form-group input[type="text"],
.form-group textarea {
  width: 100%;
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 6px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.form-group input[type="text"]:focus,
.form-group textarea:focus {
  border-color: #3498db;
  outline: none;
}

.form-group textarea {
  resize: vertical;
  min-height: 200px;
}

.type-selector {
  display: flex;
  gap: 15px;
}

.type-option {
  flex: 1;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.type-option:hover {
  border-color: #3498db;
  background: #f8f9fa;
}

.type-option.selected {
  border-color: #3498db;
  background: #e3f2fd;
}

.type-icon {
  display: block;
  font-size: 24px;
  margin-bottom: 8px;
}

.type-name {
  font-weight: 500;
  color: #2c3e50;
}

.publish-settings {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.setting-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.setting-item label {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.datetime-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.notice-options {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.notice-options label {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.option-label {
  color: #34495e;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.btn-draft, .btn-publish, .btn-cancel {
  padding: 12px 30px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s;
}

.btn-draft {
  background: #f8f9fa;
  color: #6c757d;
  border: 2px solid #dee2e6;
}

.btn-draft:hover {
  background: #e9ecef;
}

.btn-publish {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-publish:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.btn-cancel {
  background: #f8f9fa;
  color: #dc3545;
  border: 2px solid #dc3545;
}

.btn-cancel:hover {
  background: #dc3545;
  color: white;
}
</style>