# 用户模块 API 文档

## 模块说明

用户模块提供用户注册、登录、信息管理等功能。

## API 接口

### 1. 用户注册

**接口**: `POST /api/user/register`

**请求参数**:
```json
{
  "username": "string",     // 用户名 (必填)
  "password": "string",     // 密码 (必填)
  "phone": "string",        // 手机号 (必填，11 位数字)
  "nickname": "string"      // 昵称 (可选)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": 123456  // 用户 ID
}
```

**curl 示例**:
```bash
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "phone": "13800138000",
    "nickname": "测试用户"
  }'
```

---

### 2. 用户登录

**接口**: `POST /api/user/login`

**请求参数**:
```json
{
  "username": "string",     // 用户名或手机号 (必填)
  "password": "string"      // 密码 (必填)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "user": {
      "id": 123456,
      "username": "testuser",
      "nickname": "测试用户",
      "avatar": "https://...",
      "phone": "13800138000",
      "gender": 0,
      "points": 0,
      "level": 1
    }
  }
}
```

**curl 示例**:
```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

---

### 3. 获取用户信息

**接口**: `GET /api/user/info?userId={userId}`

**路径参数**:
- `userId`: 用户 ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 123456,
    "username": "testuser",
    "nickname": "测试用户",
    "avatar": "https://...",
    "phone": "13800138000",
    "gender": 0,
    "points": 0,
    "level": 1
  }
}
```

**curl 示例**:
```bash
curl -X GET http://localhost:8080/api/user/info?userId=123456
```

---

### 4. 更新用户信息

**接口**: `PUT /api/user/update?userId={userId}`

**路径参数**:
- `userId`: 用户 ID

**请求参数**:
```json
{
  "nickname": "string",     // 昵称 (可选)
  "avatar": "string",       // 头像 URL (可选)
  "gender": 0               // 性别 0-未知 1-男 2-女 (可选)
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

**curl 示例**:
```bash
curl -X PUT http://localhost:8080/api/user/update?userId=123456 \
  -H "Content-Type: application/json" \
  -d '{
    "nickname": "新昵称",
    "gender": 1
  }'
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200    | 成功 |
| 500    | 系统错误 |
| 400    | 请求参数错误 |
| 401    | 未授权 |
| 403    | 禁止访问 |
| 404    | 资源不存在 |

## 安全说明

1. **密码加密**: 使用 BCrypt 强哈希算法加密
2. **JWT 认证**: 登录后返回 JWT token，后续请求需携带 token
3. **参数验证**: 所有请求参数都经过严格验证

## 使用示例

### 前端调用示例 (JavaScript)

```javascript
// 用户注册
async function register(username, password, phone, nickname) {
  const response = await fetch('/api/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      username,
      password,
      phone,
      nickname
    })
  });
  const result = await response.json();
  if (result.code === 200) {
    console.log('注册成功，用户 ID:', result.data);
  }
  return result;
}

// 用户登录
async function login(username, password) {
  const response = await fetch('/api/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      username,
      password
    })
  });
  const result = await response.json();
  if (result.code === 200) {
    // 保存 token 到本地存储
    localStorage.setItem('token', result.data.token);
    localStorage.setItem('userInfo', JSON.stringify(result.data.user));
  }
  return result;
}

// 获取用户信息 (需要 token)
async function getUserInfo(userId) {
  const token = localStorage.getItem('token');
  const response = await fetch(`/api/user/info?userId=${userId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  const result = await response.json();
  return result.data;
}
```

## 数据库表结构

### user 表

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 用户 ID (主键) |
| username | varchar(50) | 用户名 (唯一) |
| password | varchar(100) | 密码 (BCrypt 加密) |
| nickname | varchar(50) | 昵称 |
| avatar | varchar(255) | 头像 URL |
| phone | varchar(20) | 手机号 (唯一) |
| gender | tinyint | 性别 (0-未知 1-男 2-女) |
| points | int | 积分 |
| level | int | 会员等级 |
| status | tinyint | 状态 (0-禁用 1-正常) |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |
| deleted | tinyint | 逻辑删除 (0-未删除 1-已删除) |

## 后续开发计划

- [ ] 微信授权登录
- [ ] 手机号验证码登录
- [ ] 忘记密码功能
- [ ] 用户实名认证
- [ ] 用户等级系统
- [ ] 积分规则完善
