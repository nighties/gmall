# 快速开始指南

欢迎使用美团小程序项目！本指南将帮助你快速启动项目。

## 📋 前置要求

请确保你的开发环境已安装以下软件:

- ✅ **Node.js** 16+ (前端开发)
- ✅ **JDK** 11/17 (后端开发)
- ✅ **Maven** 3.6+ (后端构建)
- ✅ **Git** (版本控制)
- ✅ **MySQL** 8.0+ (数据库)
- ✅ **Redis** 6.0+ (缓存)
- ✅ **微信开发者工具** (小程序开发)

## 🚀 快速启动

### 1. 克隆项目

```bash
git clone https://github.com/nighties/gmall.git
cd gmall
```

### 2. 初始化项目

**Windows 用户**:
```powershell
.\init.ps1
```

**Mac/Linux 用户**:
```bash
chmod +x init.sh
./init.sh
```

### 3. 数据库配置

#### 3.1 创建数据库

```bash
mysql -u root -p < docs/db/gmall.sql
```

#### 3.2 配置数据库连接

编辑 `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gmall?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root      # 修改为你的数据库用户名
    password: your_password  # 修改为你的数据库密码
```

#### 3.3 配置 Redis

编辑 `backend/src/main/resources/application.yml`:

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password:  # 如果有密码请配置
```

### 4. 启动后端服务

```bash
cd backend
mvn spring-boot:run
```

启动成功后会看到:
```
====================================
    美团小程序后台启动成功!
    API 文档：http://localhost:8080/doc.html
====================================
```

**访问 API 文档**: http://localhost:8080/doc.html

### 5. 启动前端

#### 5.1 安装依赖

```bash
cd frontend
npm install
```

#### 5.2 配置微信小程序 AppID

编辑 `frontend/manifest.json`:

```json
{
  "mp-weixin": {
    "appid": "你的微信小程序 AppID"
  }
}
```

#### 5.3 运行小程序

```bash
# 微信小程序
npm run dev:mp-weixin

# H5 (浏览器)
npm run dev:h5
```

#### 5.4 使用微信开发者工具

1. 打开微信开发者工具
2. 导入项目：选择 `frontend` 目录
3. 配置 AppID (如果还没有配置)
4. 编译运行

## 📁 项目结构说明

```
gmall/
├── frontend/              # 前端项目 (uni-app)
│   ├── pages/            # 页面目录
│   │   ├── index/        # 首页
│   │   ├── search/       # 搜索页
│   │   ├── goods/        # 商品相关页面
│   │   ├── cart/         # 购物车
│   │   ├── order/        # 订单相关页面
│   │   └── user/         # 用户中心
│   ├── static/           # 静态资源
│   ├── store/            # 状态管理 (Pinia)
│   ├── utils/            # 工具函数
│   ├── api/              # API 接口封装
│   ├── components/       # 公共组件
│   ├── App.vue           # 应用入口
│   ├── main.js           # 主入口文件
│   ├── pages.json        # 页面配置
│   ├── manifest.json     # 应用配置
│   ├── vite.config.js    # Vite 配置
│   └── package.json      # 依赖配置
│
├── backend/              # 后端项目 (Spring Boot)
│   ├── src/main/
│   │   ├── java/com/gmall/
│   │   │   ├── GmallApplication.java  # 主应用类
│   │   │   ├── common/     # 通用模块
│   │   │   ├── config/     # 配置类
│   │   │   ├── controller/ # 控制器
│   │   │   ├── service/    # 服务层
│   │   │   ├── mapper/     # DAO 层
│   │   │   ├── entity/     # 实体类
│   │   │   ├── dto/        # 数据传输对象
│   │   │   └── vo/         # 视图对象
│   │   └── resources/
│   │       ├── mapper/     # MyBatis XML
│   │       ├── application.yml  # 配置文件
│   │       └── static/     # 静态资源
│   └── pom.xml            # Maven 配置
│
├── docs/
│   └── db/
│       └── gmall.sql      # 数据库初始化脚本
│
├── README.md              # 项目说明
├── PROJECT_PROGRESS.md    # 项目进度报告
├── feature_list.json      # 功能列表
├── agent-progress.txt     # 开发进度日志
└── init.ps1               # 初始化脚本
```

## 🔧 常用命令

### 前端

```bash
# 安装依赖
npm install

# 开发模式 - 微信小程序
npm run dev:mp-weixin

# 开发模式 - H5
npm run dev:h5

# 构建 - 微信小程序
npm run build:mp-weixin

# 构建 - H5
npm run build:h5
```

### 后端

```bash
# 运行 Spring Boot
mvn spring-boot:run

# 打包
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests

# 运行 JAR
java -jar target/gmall-backend-1.0.0.jar
```

## 📝 开发流程

### 1. 查看功能列表

```bash
# 查看功能列表
cat feature_list.json

# 查看完成率
jq '(.features | map(select(.passes == true)) | length) / (.features | length) * 100' feature_list.json
```

### 2. 选择功能开发

从 `feature_list.json` 中选择一个高优先级的功能开始开发。

### 3. 创建功能分支

```bash
git checkout -b feature/feature-001-homepage
```

### 4. 开发并测试

- 前端：实现页面功能
- 后端：实现 API 接口
- 编写测试用例

### 5. 提交代码

```bash
git add .
git commit -m "feat: 实现 feature-001 首页功能"
git push origin feature/feature-001-homepage
```

### 6. 创建 Pull Request

在 GitHub 上创建 PR，等待代码审查。

### 7. 更新进度

- 更新 `feature_list.json` 中对应功能的 `passes` 状态
- 更新 `agent-progress.txt` 日志

## 🐛 常见问题

### 1. 前端依赖安装失败

**问题**: npm install 报错

**解决**:
```bash
# 清除缓存
npm cache clean --force

# 删除 node_modules
rm -rf node_modules package-lock.json

# 重新安装
npm install
```

### 2. 后端启动失败

**问题**: 数据库连接失败

**解决**:
- 检查 MySQL 是否启动
- 检查数据库配置是否正确
- 检查数据库 `gmall` 是否已创建

**问题**: 端口被占用

**解决**:
- 修改 `application.yml` 中的端口配置
- 或者关闭占用端口的程序

### 3. 微信小程序无法请求本地 API

**解决**:
1. 打开微信开发者工具
2. 详情 -> 本地设置
3. 勾选 "不校验合法域名、web-view(业务域名)、TLS 版本以及 HTTPS 证书"

### 4. Redis 连接失败

**解决**:
```bash
# Windows: 启动 Redis 服务
redis-server

# Mac/Linux
sudo systemctl start redis
```

## 📚 学习资源

- [uni-app 官方文档](https://uniapp.dcloud.net.cn/)
- [Vue 3 官方文档](https://v3.vuejs.org/)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [微信小程序官方文档](https://developers.weixin.qq.com/miniprogram/dev/framework/)

## 🎯 下一步

1. **阅读项目文档**
   - [README.md](README.md) - 项目总体介绍
   - [PROJECT_PROGRESS.md](PROJECT_PROGRESS.md) - 详细进度报告
   - [feature_list.json](feature_list.json) - 功能列表

2. **熟悉代码结构**
   - 浏览前端页面结构
   - 了解后端 API 设计

3. **开始开发**
   - 选择一个简单的功能开始
   - 按照开发流程进行开发

## 💬 获取帮助

如有问题，请:
1. 查看 [PROJECT_PROGRESS.md](PROJECT_PROGRESS.md) 中的常见问题
2. 提交 Issue 到 GitHub
3. 联系项目维护者

---

祝你开发愉快！🎉
