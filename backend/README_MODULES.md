# 后端多模块架构说明

## 模块结构

```
backend/
├── pom.xml                          # 父 POM - 管理依赖版本和模块
├── gmall-common/                    # 通用模块
│   ├── pom.xml
│   └── src/main/java/com/gmall/common/
│       ├── Result.java              # 统一返回结果
│       ├── PageResult.java          # 分页结果
│       ├── exception/               # 异常处理
│       ├── constant/                # 常量定义
│       └── util/                    # 工具类
├── gmall-user/                      # 用户服务模块
│   ├── pom.xml
│   └── src/main/java/com/gmall/user/
│       ├── entity/                  # 实体类
│       │   └── User.java
│       ├── mapper/                  # Mapper 接口
│       │   └── UserMapper.java
│       ├── service/                 # 服务层
│       ├── controller/              # 控制器
│       └── dto/                     # 数据传输对象
├── gmall-product/                   # 商品服务模块
│   ├── pom.xml
│   └── src/main/java/com/gmall/product/
│       ├── entity/
│       ├── mapper/
│       ├── service/
│       └── controller/
├── gmall-order/                     # 订单服务模块
│   ├── pom.xml
│   └── src/main/java/com/gmall/order/
│       ├── entity/
│       ├── mapper/
│       ├── service/
│       └── controller/
├── gmall-cart/                      # 购物车服务模块
│   ├── pom.xml
│   └── src/main/java/com/gmall/cart/
│       ├── entity/
│       ├── mapper/
│       ├── service/
│       └── controller/
├── gmall-coupon/                    # 优惠券服务模块
│   ├── pom.xml
│   └── src/main/java/com/gmall/coupon/
│       ├── entity/
│       ├── mapper/
│       ├── service/
│       └── controller/
└── gmall-start/                     # 启动模块 (组装所有服务)
    ├── pom.xml
    └── src/main/
        ├── java/com/gmall/
        │   └── GmallStartApplication.java
        └── resources/
            └── application.yml
```

## 模块职责

### 1. gmall-common (通用模块)
**职责**: 提供通用的工具类、常量、异常处理等
**依赖**: 无
**被依赖**: 所有其他模块

**主要内容**:
- `Result<T>`: 统一 API 返回结果
- `PageResult<T>`: 分页结果封装
- `BusinessException`: 业务异常
- `Constants`: 常量定义
- `JwtUtil`: JWT 工具类
- `RedisUtil`: Redis 工具类

### 2. gmall-user (用户服务模块)
**职责**: 用户相关的业务逻辑
**依赖**: gmall-common
**被依赖**: gmall-start

**主要内容**:
- 用户注册/登录
- 用户信息管理
- 用户认证授权
- 收货地址管理

**实体**:
- `User`: 用户实体
- `Address`: 收货地址实体

### 3. gmall-product (商品服务模块)
**职责**: 商品和分类相关的业务逻辑
**依赖**: gmall-common
**被依赖**: gmall-start

**主要内容**:
- 商品 CRUD
- 商品分类管理
- 商品搜索
- 商品收藏

**实体**:
- `Product`: 商品实体
- `Category`: 分类实体
- `Shop`: 商家实体

### 4. gmall-order (订单服务模块)
**职责**: 订单相关的业务逻辑
**依赖**: gmall-common, gmall-user, gmall-product
**被依赖**: gmall-start

**主要内容**:
- 订单创建
- 订单查询
- 订单状态管理
- 订单评价

**实体**:
- `Order`: 订单实体
- `OrderItem`: 订单商品实体
- `Review`: 评价实体

### 5. gmall-cart (购物车服务模块)
**职责**: 购物车相关的业务逻辑
**依赖**: gmall-common, gmall-user, gmall-product
**被依赖**: gmall-start

**主要内容**:
- 添加商品到购物车
- 修改购物车商品数量
- 删除购物车商品
- 查询购物车列表

**实体**:
- `Cart`: 购物车实体

### 6. gmall-coupon (优惠券服务模块)
**职责**: 优惠券相关的业务逻辑
**依赖**: gmall-common, gmall-user
**被依赖**: gmall-start

**主要内容**:
- 优惠券管理
- 优惠券领取
- 优惠券使用
- 优惠券查询

**实体**:
- `Coupon`: 优惠券实体
- `UserCoupon`: 用户优惠券实体

### 7. gmall-start (启动模块)
**职责**: 组装所有服务模块，提供启动入口
**依赖**: 所有服务模块
**被依赖**: 无

**主要内容**:
- `GmallStartApplication`: Spring Boot 启动类
- `application.yml`: 应用配置文件

## 模块依赖关系

```
gmall-start
├── gmall-common
├── gmall-user
│   └── gmall-common
├── gmall-product
│   └── gmall-common
├── gmall-order
│   ├── gmall-common
│   ├── gmall-user
│   └── gmall-product
├── gmall-cart
│   ├── gmall-common
│   ├── gmall-user
│   └── gmall-product
└── gmall-coupon
    ├── gmall-common
    └── gmall-user
```

## 开发规范

### 1. 模块划分原则
- 单一职责：每个模块只负责一个业务领域
- 高内聚：相关的代码放在同一个模块中
- 低耦合：模块之间通过接口通信，减少直接依赖
- 可复用：通用代码放在 common 模块

### 2. 包命名规范
```
com.gmall.{module}.entity      # 实体类
com.gmall.{module}.mapper      # Mapper 接口
com.gmall.{module}.service     # 服务层接口
com.gmall.{module}.service.impl # 服务层实现
com.gmall.{module}.controller  # 控制器
com.gmall.{module}.dto         # 数据传输对象
com.gmall.{module}.vo          # 视图对象
com.gmall.{module}.config      # 配置类
```

### 3. 类命名规范
- 实体类：使用名词，如 `User`, `Product`, `Order`
- Mapper 接口：`{Entity}Mapper`，如 `UserMapper`
- Service 接口：`{Entity}Service`
- Service 实现：`{Entity}ServiceImpl`
- Controller: `{Entity}Controller`
- DTO: `{Entity}DTO` 或 `{Action}{Entity}DTO`
- VO: `{Entity}VO` 或 `{Entity}Response`

### 4. 接口设计规范
- RESTful API 设计规范
- 统一返回结果 `Result<T>`
- 统一异常处理
- JWT 认证

## 构建和运行

### 构建整个项目
```bash
cd backend
mvn clean install
```

### 运行项目
```bash
cd backend/gmall-start
mvn spring-boot:run
```

### 单独构建模块
```bash
cd backend/gmall-common
mvn clean install
```

## 新增模块流程

1. 在父 pom.xml 中添加新模块
2. 创建模块目录和 pom.xml
3. 添加必要的依赖
4. 实现业务逻辑
5. 在 gmall-start 模块中添加依赖
6. 测试和验证

## 优势

1. **模块化**: 清晰的模块边界，便于理解和维护
2. **可复用**: common 模块提供通用功能
3. **可扩展**: 新增功能只需添加新模块
4. **可测试**: 模块独立，便于单元测试
5. **团队协作**: 不同开发人员可以负责不同模块
6. **部署灵活**: 未来可以将模块拆分为微服务
