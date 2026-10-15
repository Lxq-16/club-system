<template>
  <div class="container">
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
      <!-- 新增成员身份筛选下拉框，对应club_member表字段 -->
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
      <!-- 表格只展示年月日，截取T前面部分 -->
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
        <!-- 手动输入年月日 -->
        <el-form-item label="成立时间" prop="createTime">
          <el-input
            v-model="form.createTime"
            placeholder="请填写日期，格式：2026-01-01"
          />
        </el-form-item>
      </el-form>

      <!-- 弹窗底部按钮 -->
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
import { ElMessage } from 'element-plus'

// 后端接口基础地址（Vite代理匹配后端 /club 前缀）
const baseUrl = "/club"

// 表格数据源
const tableData = ref([])
// 弹窗显示控制
const dialogVisible = ref(false)
// 区分新增/编辑标识 true=编辑 false=新增
const isEdit = ref(false)
// 单条查询ID（数字类型）
const singleId = ref('')
// 全局加载状态，防止重复点击
const loading = ref(false)
// 表单DOM实例，用于表单校验
const formRef = ref(null)

// 多条件搜索参数：新增memberRole匹配三表查询
const search = reactive({
  clubType: '',
  stuName: '',
  memberRole: ''
})

// 表单绑定对象（新增/编辑共用）
const form = reactive({
  id: null,
  clubName: '',
  clubType: '',
  leaderId: null,
  createTime: ''
})

// 表单校验规则：限制只能输入 yyyy-MM-dd
const rules = reactive({
  clubName: [{ required: true, message: '请输入社团名称', trigger: 'blur' }],
  clubType: [{ required: true, message: '请选择社团类型', trigger: 'change' }],
  leaderId: [{ required: true, message: '请输入负责人ID', trigger: 'blur' }],
  createTime: [
    { required: true, message: '请填写成立时间', trigger: 'blur' },
    { pattern: /^\d{4}-\d{2}-\d{2}$/, message: '日期格式错误，请填写 2026-01-01 格式', trigger: 'blur' }
  ]
})

/**
 * 清空表单所有字段，弹窗关闭/新增时重置
 */
const clearAllForm = () => {
  form.id = null
  form.clubName = ''
  form.clubType = ''
  form.leaderId = null
  form.createTime = ''
  // 清除校验红色提示
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

/**
 * 加载全部社团列表数据
 */
function loadAll() {
  axios.get(`${baseUrl}/list`)
    .then(res => {
      tableData.value = res.data.data
    })
    .catch(err => {
      ElMessage.error('加载列表失败：' + (err.response?.data?.msg || '服务器异常'))
    })
}

// 页面初始化自动加载数据
loadAll()

/**
 * 多条件联合搜索：负责人姓名 + 社团类型 + 成员身份（三表多字段查询）
 */
function searchData() {
  // URL参数传递三个筛选条件：stuName、clubType、memberRole
  axios.get(`${baseUrl}/list`, { params: search })
    .then(res => {
      tableData.value = res.data.data
    })
    .catch(err => {
      ElMessage.error('搜索失败：' + (err.response?.data?.msg || '服务器异常'))
    })
}

/**
 * 根据ID单条查询社团
 */
function getSingle() {
  if (!singleId.value) {
    ElMessage.warning('请输入社团ID')
    return
  }
  axios.get(`${baseUrl}/get/${singleId.value}`)
    .then(res => {
      tableData.value = res.data.data ? [res.data.data] : []
    })
    .catch(err => {
      ElMessage.error('查询单条数据失败：' + (err.response?.data?.msg || '服务器异常'))
    })
}

/**
 * 重置搜索条件，恢复全量列表（新增清空memberRole）
 */
function resetSearch() {
  search.stuName = ''
  search.clubType = ''
  search.memberRole = ''
  loadAll()
}

/**
 * 清空单查ID输入框，恢复全量列表
 */
function resetSingle() {
  singleId.value = ''
  loadAll()
}

/**
 * 打开新增弹窗
 */
function openAdd() {
  isEdit.value = false
  clearAllForm()
  dialogVisible.value = true
}

/**
 * 关闭弹窗
 */
function closeDialog() {
  dialogVisible.value = false
}

/**
 * 打开编辑弹窗，回显行数据
 * @param {Object} row 表格单行数据
 */
function openEdit(row) {
  isEdit.value = true
  clearAllForm()
  form.id = row.id
  form.clubName = row.clubName
  form.clubType = row.clubType
  form.leaderId = row.leaderId
  // 后端带T的时间，只截取年月日展示到输入框
  if(row.createTime){
    form.createTime = row.createTime.split('T')[0]
  }else{
    form.createTime = ''
  }
  dialogVisible.value = true
}

/**
 * 提交表单（新增/编辑共用）
 */
function submit() {
  formRef.value.validate((valid) => {
    if (!valid) return
    loading.value = true
    // 拼接时分秒，传给后端标准LocalDateTime格式
    const submitData = { ...form }
    submitData.createTime = form.createTime + 'T00:00:00'

    if (!isEdit.value) {
      // 新增社团 POST
      axios.post(`${baseUrl}/add`, submitData)
        .then(res => {
          ElMessage.success(res.data.msg)
          dialogVisible.value = false
          loadAll()
        })
        .catch(err => {
          ElMessage.error('新增失败：' + (err.response?.data?.msg || '服务器异常'))
        })
        .finally(() => {
          loading.value = false
        })
    } else {
      // 修改社团 PUT
      axios.put(`${baseUrl}/update`, submitData)
        .then(res => {
          ElMessage.success(res.data.msg)
          dialogVisible.value = false
          loadAll()
        })
        .catch(err => {
          ElMessage.error('修改失败：' + (err.response?.data?.data?.msg || err.response?.data?.error || '服务器时间解析异常，请检查后端Jackson配置'))
        })
        .finally(() => {
          loading.value = false
        })
    }
  })
}

/**
 * 删除社团
 * @param {Number} id 社团主键ID
 */
function del(id) {
  if (window.confirm("确定删除该社团？")) {
    loading.value = true
    axios.delete(`${baseUrl}/remove/${id}`)
      .then(res => {
        ElMessage.success(res.data.msg)
        loadAll()
      })
      .catch(err => {
        ElMessage.error('删除失败：' + (err.response?.data?.msg || '服务器异常'))
      })
      .finally(() => {
        loading.value = false
      })
  }
}
</script>