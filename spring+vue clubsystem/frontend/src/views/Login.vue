<template>
  <div class="login-wrap">
    <el-card class="login-card">
      <h2 style="text-align:center">社团管理系统</h2>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" style="width:220px" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" style="width:220px" @keyup.enter="submit" />
        </el-form-item>
        <el-button type="primary" style="width:100%" :loading="loading" @click="submit">登录</el-button>
      </el-form>
      <p style="text-align:center;color:#999;font-size:12px">默认账号 admin / 123456</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const emit = defineEmits(['login-success'])
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

function submit() {
  formRef.value.validate(valid => {
    if (!valid) return
    loading.value = true
    axios.post('/admin/login', form).then(res => {
      if (res.data.code === 200) {
        localStorage.setItem('token', res.data.data.token)
        localStorage.setItem('username', res.data.data.username)
        ElMessage.success('登录成功')
        emit('login-success', res.data.data.username)
      } else {
        ElMessage.error(res.data.msg)
      }
    }).catch(() => {
      ElMessage.error('登录请求失败')
    }).finally(() => loading.value = false)
  })
}
</script>

<style scoped>
.login-wrap {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f0f2f5;
}
.login-card {
  width: 340px;
}
</style>
