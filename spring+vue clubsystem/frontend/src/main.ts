import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'

const app = createApp(App)

// 注册ElementPlus组件库
app.use(ElementPlus)

// 请求拦截器：自动带上登录token
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = token
  }
  return config
})

// 响应拦截器：后端返回401说明登录过期，清掉本地态并刷新回登录页
axios.interceptors.response.use(
  res => {
    if (res.data && res.data.code === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      location.reload()
    }
    return res
  },
  err => Promise.reject(err)
)

app.mount('#app')
