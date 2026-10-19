<template>
  <div class="container">
    <!-- 顶部用户栏 -->
    <div class="top-bar">
      <span>欢迎，{{ username }}</span>
      <el-button size="small" @click="logout">退出登录</el-button>
    </div>

    <!-- 多条件跨表查询区域 -->
    <el-card style="margin-bottom: 10px">
      <el-input
        v-model="search.stuName"
        placeholder="输入负责人姓名(学生表)"
        style="width: 200px; margin-right: 10px"
      />
      <el-select
        v-model="search.clubType"
        placeholder="选择社团类型"
        style="width: 180px; margin-right: 10px"
      >
        <el-option label="体育类" value="体育类" />
        <el-option label="文娱类" value="文娱类" />
        <el-option label="学术类" value="学术类" />
      </el-select>
      <el-select
        v-model="search.memberRole"
        placeholder="成员身份筛选"
        style="width:160px;margin-right:10px"
      >
        <el-option label="全部" value="" />
        <el-option label="队员" value="队员" />
        <el-option label="干事" value="干事" />
      </el-select>
      <el-button type="primary" @click="searchData">搜索</el-button>
      <el-button @click="resetSearch">重置条件</el-button>
      <el-button @click="openAdd">新增社团</el-button>
    </el-card>

    <!-- 单ID精准查询区域 -->
    <el-card style="margin-bottom: 10px">
      <el-input
        v-model.number="singleId"
        placeholder="输入社团ID查询"
        style="width: 200px; margin-right: 10px"
        type="number"
      />
      <el-button type="success" @click="getSingle">单条查询</el-button>
      <el-button @click="resetSingle">清空ID</el-button>
    </el-card>

    <!-- 社团数据表格 -->
    <el-table :data="tableData" border>
      <el-table-column label="ID" prop="id" />
      <el-table-column label="社团名称" prop="clubName" />
      <el-table-column label="社团类型" prop="clubType" />
      <el-table-column label="负责人姓名(学生)" prop="leaderName" />
      <el-table-column label="成立时间">
        <template #default="scope">
          {{ scope.row.createTime ? scope.row.createTime.split('T')[0] : '' }}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="openEdit(scope.row)">编辑</el-button>
          <el-button type="danger" @click="del(scope.row.id)" :loading="loading">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页条 -->
    <el-pagination
      style="margin-top: 10px; justify-content: flex-end"
      background
      layout="total, prev, pager, next, sizes"
      :total="query.total"
      :current-page="query.pageNum"
      :page-size="query.pageSize"
      :page-sizes="[5, 10, 20]"
      @current-change="handlePageChange"
      @size-change="handleSizeChange"
    />

    <!-- 新增/编辑统一弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="社团信息"
      @close="clearAllForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item label="社团名称" prop="clubName">
          <el-input v-model="form.clubName" />
        </el-form-item>
        <el-form-item label="社团类型" prop="clubType">
          <el-select v-model="form.clubType">
            <el-option label="体育类" value="体育类" />
            <el-option label="文娱类" value="文娱类" />
            <el-option label="学术类" value="学术类" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人ID" prop="leaderId">
          <el-input v-model.number="form.leaderId" type="number" />
        </el-form-item>
        <el-form-item label="成立时间" prop="createTime">
          <el-input
            v-model="form.createTime"
            placeholder="请填写日期，格式：2026-01-01"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="submit" :loading="loading">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

defineProps({ username: { type: String, default: '' } })
const emit = defineEmits(['logout'])

const baseUrl = "/club"

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const singleId = ref('')
const loading = ref(false)
const formRef = ref(null)

// 分页参数
const query = reactive({ pageNum: 1, pageSize: 5, total: 0 })

const search = reactive({
  clubType: '',
  stuName: '',
  memberRole: ''
})

const form = reactive({
  id: null,
  clubName: '',
  clubType: '',
  leaderId: null,
  createTime: ''
})

const rules = reactive({
  clubName: [{ required: true, message: '请输入社团名称', trigger: 'blur' }],
  clubType: [{ required: true, message: '请选择社团类型', trigger: 'change' }],
  leaderId: [{ required: true, message: '请输入负责人ID', trigger: 'blur' }],
  createTime: [
    { required: true, message: '请填写成立时间', trigger: 'blur' },
    { pattern: /^\d{4}-\d{2}-\d{2}$/, message: '日期格式错误，请填写 2026-01-01 格式', trigger: 'blur' }
  ]
})

const clearAllForm = () => {
  form.id = null
  form.clubName = ''
  form.clubType = ''
  form.leaderId = null
  form.createTime = ''
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 加载分页列表，带搜索条件
function loadAll() {
  axios.get(`${baseUrl}/list`, {
    params: { ...search, pageNum: query.pageNum, pageSize: query.pageSize }
  }).then(res => {
    tableData.value = res.data.data.records
    query.total = res.data.data.total
  }).catch(err => {
    ElMessage.error('加载列表失败：' + (err.response?.data?.msg || '服务器异常'))
  })
}

loadAll()

function searchData() {
  query.pageNum = 1
  loadAll()
}

function getSingle() {
  if (!singleId.value) {
    ElMessage.warning('请输入社团ID')
    return
  }
  axios.get(`${baseUrl}/get/${singleId.value}`)
    .then(res => {
      tableData.value = res.data.data ? [res.data.data] : []
      query.total = res.data.data ? 1 : 0
    })
    .catch(err => {
      ElMessage.error('查询单条数据失败：' + (err.response?.data?.msg || '服务器异常'))
    })
}

function resetSearch() {
  search.stuName = ''
  search.clubType = ''
  search.memberRole = ''
  query.pageNum = 1
  loadAll()
}

function resetSingle() {
  singleId.value = ''
  query.pageNum = 1
  loadAll()
}

function handlePageChange(p) {
  query.pageNum = p
  loadAll()
}

function handleSizeChange(s) {
  query.pageSize = s
  query.pageNum = 1
  loadAll()
}

function openAdd() {
  isEdit.value = false
  clearAllForm()
  dialogVisible.value = true
}

function closeDialog() {
  dialogVisible.value = false
}

function openEdit(row) {
  isEdit.value = true
  clearAllForm()
  form.id = row.id
  form.clubName = row.clubName
  form.clubType = row.clubType
  form.leaderId = row.leaderId
  if(row.createTime){
    form.createTime = row.createTime.split('T')[0]
  }else{
    form.createTime = ''
  }
  dialogVisible.value = true
}

function submit() {
  formRef.value.validate((valid) => {
    if (!valid) return
    loading.value = true
    const submitData = { ...form }
    submitData.createTime = form.createTime + 'T00:00:00'

    if (!isEdit.value) {
      axios.post(`${baseUrl}/add`, submitData)
        .then(res => {
          ElMessage.success(res.data.msg)
          dialogVisible.value = false
          loadAll()
        })
        .catch(err => {
          ElMessage.error('新增失败：' + (err.response?.data?.msg || '服务器异常'))
        })
        .finally(() => loading.value = false)
    } else {
      axios.put(`${baseUrl}/update`, submitData)
        .then(res => {
          ElMessage.success(res.data.msg)
          dialogVisible.value = false
          loadAll()
        })
        .catch(err => {
          ElMessage.error('修改失败：' + (err.response?.data?.msg || '服务器时间解析异常'))
        })
        .finally(() => loading.value = false)
    }
  })
}

function del(id) {
  ElMessageBox.confirm('确定删除该社团？', '提示', { type: 'warning' })
    .then(() => {
      loading.value = true
      axios.delete(`${baseUrl}/remove/${id}`)
        .then(res => {
          ElMessage.success(res.data.msg)
          loadAll()
        })
        .catch(err => {
          ElMessage.error('删除失败：' + (err.response?.data?.msg || '服务器异常'))
        })
        .finally(() => loading.value = false)
    })
    .catch(() => {})
}

function logout() {
  axios.post('/admin/logout').finally(() => {
    emit('logout')
  })
}
</script>

<style scoped>
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding: 0 5px;
}
</style>
