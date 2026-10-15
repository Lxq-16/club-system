# 社团管理系统（前后端分离）

一个基于 **Spring Boot + Vue 3** 的社团信息管理系统，实现社团的多条件三表联合查询与增删改，前后端分离开发。

## 技术栈

| 端 | 技术 |
| --- | --- |
| 后端 | Spring Boot 2.7 · MyBatis-Plus · MySQL · Lombok · Jackson |
| 前端 | Vue 3 · TypeScript · Vite · Element Plus · Axios |

## 功能

- 社团新增 / 修改 / 删除 / 按 ID 单条查询
- 多条件**三表联合查询**：负责人姓名（student 表）+ 社团分类（club）+ 成员身份（club_member 表）
- 前端：多条件搜索、按 ID 精确查询、表单校验、时间格式处理
- 后端：统一返回结构 `{ code, msg, data }`、动态 SQL 分页查询、驼峰映射

## 项目结构

```
club-system
├── backend          # Spring Boot 后端（端口 8080）
│   ├── src/main/java/com/club/clubsystem
│   │   ├── controller   # Club / ClubMember / Student
│   │   ├── service      # 业务层
│   │   ├── mapper       # MyBatis-Plus Mapper
│   │   ├── domain       # 实体：Club / ClubMember / Student
│   │   └── config       # Jackson / MyBatis-Plus 配置
│   └── src/main/resources
└── frontend         # Vue3 前端（端口 5173）
    └── src/views/clubList.vue
```

## 快速开始

### 1. 数据库
创建数据库 `club_manager`（utf8mb4），按实体建表：
- `club`（社团，含负责人 leader_id）
- `student`（学生，负责人姓名）
- `club_member`（社团成员，含 role 身份）

### 2. 启动后端
1. 复制配置模板并填写数据库账号密码：
   ```sh
   cp backend/src/main/resources/application-example.yml backend/src/main/resources/application.yml
   ```
2. 启动：
   ```sh
   cd backend
   mvn spring-boot:run
   ```
   后端运行在 http://localhost:8080

### 3. 启动前端
```sh
cd frontend
npm install
npm run dev
```
前端运行在 http://localhost:5173，Vite 已配置代理把 `/club` 转发到后端 8080。

## 说明
- `application.yml`（含数据库账号密码）已通过 `.gitignore` 排除，不上传到仓库，保证本地凭据安全。
- 接口列表：`GET /club/list`（三表联查）、`GET /club/get/{id}`、`POST /club/add`、`PUT /club/update`、`DELETE /club/remove/{id}`。
