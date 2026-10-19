<template>
  <Login v-if="!isLogin" @login-success="onLoginSuccess" />
  <clubList v-else :username="username" @logout="onLogout" />
</template>

<script setup>
import { ref } from 'vue'
import clubList from './views/clubList.vue'
import Login from './views/Login.vue'

const token = localStorage.getItem('token')
const username = ref(localStorage.getItem('username') || '')
const isLogin = ref(!!token)

const onLoginSuccess = (name) => {
  username.value = name
  isLogin.value = true
}
const onLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  isLogin.value = false
}
</script>
