# 社团管理系统（前后端分离）

基于 **Spring Boot 2.7 + Vue 3** 的社团信息管理系统，实现登录鉴权、三表联合动态查询、分页与增删改。

## 技术栈

| 端 | 技术 |
| --- | --- |
| 后端 | Spring Boot 2.7 · MyBatis-Plus · MySQL · Lombok · Jackson |
| 前端 | Vue 3 · TypeScript · Vite · Element Plus · Axios |

## 功能

- 登录鉴权：管理员登录签发 token，拦截器校验业务接口，未登录返回 401
- 社团新增 / 修改 / 删除 / 按 ID 单条查询
- 多条件**三表联合分页查询**：社团类型（club）+ 负责人姓名（student）+ 成员身份（club_member）
- 分页查询：MyBatis-Plus 分页插件，前端 Element Plus 分页条翻页
- 前端：多条件搜索、表单校验、日期格式处理、loading 防重复提交
- 后端：统一返回结构 `{ code, msg, data }`、动态 SQL、驼峰映射

## 项目结构

```
club-system
├── backend          # Spring Boot 后端（端口 8080）
│   ├── src/main/java/com/club/clubsystem
│   │   ├── controller   # Club / Admin
│   │   ├── service      # 业务层
│   │   ├── mapper       # MyBatis-Plus Mapper
│   │   ├── domain       # 实体：Club / Student / ClubMember / Admin
│   │   └── config       # Jackson / MyBatis-Plus / 拦截器配置
│   └── src/main/resources/mapper/ClubMapper.xml
├── frontend         # Vue3 前端（Vite dev server）
│   └── src/views/clubList.vue、Login.vue
└── sql/init.sql     # 建表 SQL
```

## 快速开始

### 1. 数据库

```sh
mysql -u root -p < sql/init.sql
```

### 2. 启动后端

```sh
cd backend
cp src/main/resources/application-example.yml src/main/resources/application.yml
# 编辑 application.yml 填数据库密码
mvn spring-boot:run
```

后端运行在 http://localhost:8080

### 3. 启动前端

```sh
cd frontend
npm install
npm run dev
```

Vite 已配置代理把 `/club`、`/admin` 转发到后端 8080。

### 4. 登录

默认账号：`admin / 123456`

## 接口列表

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | /admin/login | 登录，返回 token |
| POST | /admin/logout | 退出 |
| GET | /club/list?pageNum=1&pageSize=5 | 分页列表（支持 clubType/stuName/memberRole 条件） |
| GET | /club/get/{id} | 按 ID 查询 |
| POST | /club/add | 新增 |
| PUT | /club/update | 修改 |
| DELETE | /club/remove/{id} | 删除 |

## 说明


- 生产环境改进点：密码 BCrypt 加密、token 换 Redis、CORS 收敛到具体域名
