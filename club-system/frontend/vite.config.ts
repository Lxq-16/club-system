import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// Vite项目配置
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      // @ 映射 src 目录
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  // 后端代理 解决跨域
  server: {
    proxy: {
      '/club': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})