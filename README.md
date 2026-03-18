# 美团风格微信小程序项目 (gmall)

> 🚀 仿美团的小程序电商平台，前后端分离架构

## 项目简介

这是一个模仿美团业务模式的微信小程序项目，采用前后端分离架构：

- **前端**: Vue 3 + 微信小程序 (uni-app 框架)
- **后端**: Java Spring Boot 2.7+ 
- **数据库**: MySQL 8.0+
- **缓存**: Redis
- **部署**: Docker

## 技术栈

### 前端技术栈
- Vue 3 (Composition API)
- uni-app (跨平台小程序框架)
- Vuex (状态管理)
- uView UI (小程序 UI 组件库)
- Axios (HTTP 客户端)

### 后端技术栈
- Java 11/17
- Spring Boot 2.7+
- MyBatis-Plus (ORM 框架)
- Spring Security + JWT (认证授权)
- Redis (缓存)
- MySQL (数据库)
- Swagger/Knife4j (API 文档)

## 项目结构

```
gmall/
├── README.md                 # 项目说明文档
├── feature_list.json         # 功能列表 (60+ 核心功能)
├── agent-progress.txt        # 开发进度日志
├── init.ps1                  # Windows 初始化脚本
├── frontend/                 # 前端项目 (uni-app)
│   ├── pages/                # 页面目录
│   │   ├── index/            # 首页
│   │   ├── search/           # 搜索页
│   │   ├── goods/            # 商品相关页面
│   │   ├── cart/             # 购物车
│   │   ├── order/            # 订单相关页面
│   │   └── user/             # 用户中心
│   ├── components/           # 公共组件
│   ├── static/               # 静态资源
│   ├── store/                # Vuex 状态管理
│   ├── utils/                # 工具函数
│   ├── api/                  # API 接口
│   └── manifest.json         # uni-app 配置
├── backend/                  # 后端项目 (Spring Boot)
│   ├── src/main/java
│   │   └── com/gmall/
│   │       ├── GmallApplication.java
│   │       ├── common/       # 通用模块
│   │       ├── config/       # 配置类
│   │       ├── controller/   # 控制器
│   │       ├── service/      # 服务层
│   │       ├── mapper/       # DAO 层
│   │       ├── entity/       # 实体类
│   │       ├── dto/          # 数据传输对象
│   │       └── vo/           # 视图对象
│   ├── src/main/resources
│   │   ├── mapper/           # MyBatis XML
│   │   ├── application.yml   # 配置文件
│   │   └── application-dev.yml
│   └── pom.xml
└── docs/                     # 文档目录
    ├── api/                  # API 文档
    └── db/                   # 数据库脚本
```

## 核心功能模块

### 用户端功能

#### 1. 首页模块
- 轮播图广告
- 分类导航 (美食、外卖、酒店等)
- 附近商家推荐
- 限时特价/满减活动
- 搜索入口

#### 2. 商品模块
- 商品列表 (支持筛选、排序)
- 商品详情 (图文详情、规格选择)
- 商品评价
- 商品收藏

#### 3. 购物车模块
- 添加/删除商品
- 修改数量
- 选中/全选
- 价格计算

#### 4. 订单模块
- 创建订单
- 订单列表 (按状态筛选)
- 订单详情
- 取消订单
- 确认收货

#### 5. 用户中心
- 个人信息管理
- 收货地址管理
- 我的优惠券
- 积分管理
- 我的足迹
- 会员中心

#### 6. 搜索模块
- 关键词搜索
- 历史搜索
- 筛选排序

### 管理端功能 (后台)

#### 1. 商品管理
- 商品 CRUD
- 商品上下架
- 库存管理
- 分类管理

#### 2. 订单管理
- 订单列表查询
- 订单状态管理
- 订单详情查看

#### 3. 用户管理
- 用户列表
- 用户详情
- 用户状态管理

#### 4. 营销管理
- 优惠券管理
- 活动管理
- 广告位管理

## 快速开始

### 环境要求

- Node.js 16+
- JDK 11/17
- MySQL 8.0+
- Redis 6.0+
- 微信开发者工具

### 初始化项目

```powershell
# Windows 用户
.\init.ps1
```

### 前端开发

```bash
cd frontend

# 安装依赖
npm install

# 运行小程序 (微信)
npm run dev:mp-weixin

# 运行 H5
npm run dev:h5
```

### 后端开发

```bash
cd backend

# 使用 Maven 运行
mvn spring-boot:run

# 或者使用 IDEA 直接运行 GmallApplication.java
```

### 数据库初始化

```bash
# 导入数据库脚本
mysql -u root -p < docs/db/gmall.sql
```

## API 接口文档

启动后端项目后，访问：
```
http://localhost:8080/doc.html
```

## 开发进度

查看 [feature_list.json](feature_list.json) 了解所有功能及完成状态。

当前进度：
```bash
# 查看完成率
jq '(.features | map(select(.passes == true)) | length) / (.features | length) * 100' feature_list.json
```

## 项目规范

### 代码规范

- 前端：遵循 Vue 3 官方风格指南
- 后端：遵循阿里巴巴 Java 开发手册

### Git 提交规范

```
feat: 新功能
fix: 修复 bug
docs: 文档更新
style: 代码格式调整
refactor: 重构代码
test: 测试相关
chore: 构建/工具链相关
```

### 分支管理

- `main`: 主分支，生产环境代码
- `dev`: 开发分支
- `feature/*`: 功能分支
- `bugfix/*`: 修复分支

## 常见问题

### 1. 小程序开发工具报错

确保在 [manifest.json](frontend/manifest.json) 中正确配置了 AppID。

### 2. 后端启动失败

检查 MySQL 和 Redis 是否启动，数据库配置是否正确。

### 3. 接口跨域

开发环境已在后端配置 CORS，生产环境使用 Nginx 反向代理。

## 贡献指南

1. Fork 本仓库
2. 创建 feature 分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: add AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 许可证

MIT License

## 联系方式

- 项目地址：https://github.com/nighties/gmall.git
- 问题反馈：提交 Issue

---

**注意**: 本项目仅供学习交流使用，请勿用于商业用途。
