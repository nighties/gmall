---
name: long-running-agent
description: "Manage long-term projects across multiple agent sessions using the dual-agent pattern. Use when: building complex applications, multi-session development, projects requiring incremental progress with clear documentation. Implements initializer + coding agent workflow."
---

# Long-Running Agent Skill

基于 Anthropic 研究成果的长周期任务管理技能。实现跨多个上下文窗口的有效协作。

## 核心理念

长周期代理的核心挑战：每个新会话都从空白上下文开始，就像轮班工作的工程师，不记得之前的工作。

**解决方案：双代理模式**
1. **初始化代理**：设置环境，创建基础架构
2. **编码代理**：每次会话只做增量进展，留下清晰文档

## 何时使用

✅ **使用此技能当：**
- 构建复杂应用（需要数小时/数天）
- 多会话开发项目
- 需要增量进展和清晰文档
- 跨上下文窗口的长期任务
- 全栈 Web 应用开发

❌ **不使用此技能当：**
- 单次会话可完成的小任务
- 简单的代码修改
- 不需要跨会话协调的任务

## 工作流程

### 阶段 1：初始化（第一次会话）

使用初始化代理设置项目环境：

```bash
# 1. 创建项目目录
mkdir my-project
cd my-project

# 2. 运行初始化代理
openclaw agent --message "创建一个博客系统" \
  --prompt-file long-running-agent-init.prompt
```

初始化代理会创建：
- `feature_list.json` - 功能列表（50-200 个功能）
- `agent-progress.txt` - 进度跟踪文件
- `init.ps1` / `init.sh` - 环境初始化脚本
- Git 仓库和初始提交
- `README.md` - 项目说明

### 阶段 2：编码（后续会话）

使用编码代理实现增量进展：

```bash
# 每次开始新会话
cd my-project

# 运行编码代理
openclaw agent --message "继续开发" \
  --prompt-file long-running-agent-coding.prompt
```

编码代理会：
1. 阅读进度日志了解状态
2. 选择一个功能实现
3. 编写代码和测试
4. 提交代码并更新进度

## 环境管理

### 1. 功能列表（feature_list.json）

结构化的 JSON 文件，列出所有需要实现的功能：

```json
{
  "features": [
    {
      "id": "feature-001",
      "category": "functional",
      "description": "用户可以点击新建聊天按钮创建对话",
      "priority": "high",
      "steps": [
        "导航到主界面",
        "点击'新建聊天'按钮",
        "验证创建了新对话"
      ],
      "passes": false,
      "testStatus": "pending"
    }
  ]
}
```

**重要规则**：
- 编码代理只能通过修改 `passes` 字段更新状态
- 禁止删除或修改测试步骤
- 使用 JSON 格式（模型不太可能错误修改）

### 2. 进度日志（agent-progress.txt）

记录每个会话的工作内容：

```
=== Session Log ===

Session: 2026-03-16 13:30:00
Run ID: 57c36512-29b3-463c-ac43-09e27df48d62

Work Completed:
- 实现了 feature-001：新建聊天功能
- 修复了侧边栏样式问题

Git Commits:
- abc123: feat: implement new chat button
- def456: fix: sidebar styling

Next Session Should:
- 实现 feature-002：消息发送功能

Status: SUCCESS
```

### 3. 初始化脚本（init.ps1 / init.sh）

快速重启开发环境和运行基本测试：

```powershell
# init.ps1 - Windows 版本
Write-Host "🚀 初始化开发环境..."

npm install
Start-Process npm run dev
Start-Sleep -Seconds 5

# 运行基本测试
node tests/basic-smoke-test.js

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ 基本测试通过"
} else {
    Write-Host "❌ 基本测试失败"
    exit 1
}
```

## 编码代理会话清单

每个会话**必须**按顺序执行：

1. `pwd` - 确认工作目录
2. `cat agent-progress.txt` - 阅读进度
3. `cat feature_list.json` - 查看功能列表
4. `git log --oneline -20` - 检查历史
5. `.\init.ps1` - 重启环境
6. 运行基本测试
7. 选择一个功能实现
8. 端到端测试
9. 更新 feature_list.json
10. git commit
11. 更新 agent-progress.txt

## 重要规则

### ✅ 必须做的

- 每次会话只实现一个功能
- 必须进行端到端测试
- 必须更新进度日志
- 必须保持代码整洁
- 必须为下一个会话留下指引

### ❌ 禁止做的

- 一次实现多个功能
- 不测试就标记完成
- 修改测试步骤
- 留下半成品的代码
- 破坏已有的功能

## 进度监控

```bash
# 查看完成率
jq '(.features | map(select(.passes == true)) | length) / (.features | length) * 100' feature_list.json

# 查看已完成的功能
jq '.features[] | select(.passes == true) | .description' feature_list.json

# 查看高优先级未完成功能
jq '.features[] | select(.passes == false and .priority == "high") | .description' feature_list.json

# 查看最近的进度
tail -50 agent-progress.txt

# 查看 git 历史
git log --oneline --graph -30
```

## 失败模式与解决方案

| 问题 | 初始化代理方案 | 编码代理方案 |
|------|----------------|--------------|
| 过早宣布完成 | 创建功能列表 | 每次只做一个功能 |
| 留下 bug 或混乱 | 创建 git 和进度文件 | 开始阅读，结束提交 |
| 过早标记完成 | 创建测试步骤 | 必须自验证 |
| 浪费时间重启环境 | 编写 init 脚本 | 开始先读 init.sh |

## 成功标准

一个成功的会话应该：

1. ✅ 实现了一个完整的功能
2. ✅ 通过了所有测试
3. ✅ 提交了清晰的 git 历史
4. ✅ 更新了进度日志
5. ✅ 代码可以合并到主分支
6. ✅ 下一个会话可以轻松继续

## 最佳实践

### 功能粒度
- 每个功能 30-60 分钟完成
- 功能是用户可感知的价值
- 功能之间相对独立

### 测试策略
- 优先端到端测试
- 截图记录测试过程
- 自动化重复测试

### 文档质量
- 进度日志详细清晰
- git 提交信息描述性
- 为下一个会话着想

### 代码质量
- 保持代码整洁
- 及时重构
- 添加注释和文档

## 示例项目

### 待办事项应用

```bash
# 初始化项目
mkdir todo-app
cd todo-app

openclaw agent --message "创建一个待办事项管理应用" \
  --prompt-file long-running-agent-init.prompt

# 开发会话
openclaw agent --message "继续开发" \
  --prompt-file long-running-agent-coding.prompt
```

典型功能列表：
- feature-001: 添加待办事项
- feature-002: 标记完成
- feature-003: 删除待办
- feature-004: 编辑待办
- feature-005: 筛选功能
- feature-006: 数据持久化

## 工具配置

### Puppeteer MCP（浏览器自动化）

用于端到端测试：

```javascript
// tests/basic-smoke-test.js
const puppeteer = require('puppeteer');

async function runSmokeTest() {
    const browser = await puppeteer.launch({ headless: false });
    const page = await browser.newPage();
    
    await page.goto('http://localhost:3000');
    await page.click('[data-testid="new-chat-button"]');
    await page.waitForSelector('[data-testid="chat-input"]');
    
    await page.type('[data-testid="chat-input"]', '测试');
    await page.click('[data-testid="send-button"]');
    await page.waitForSelector('[data-testid="ai-response"]');
    
    await page.screenshot({ path: 'tests/screenshots/smoke-test.png' });
    
    await browser.close();
    return true;
}
```

## 参考资料

- [Anthropic: Effective harnesses for long-running agents](https://www.anthropic.com/engineering/effective-harnesses-for-long-running-agents)
- [Claude 4 Prompting Guide](https://docs.anthropic.com/claude/docs/prompting-guide)

## 相关文件

- `long-running-agent-init.prompt` - 初始化代理提示词
- `long-running-agent-coding.prompt` - 编码代理提示词
- `example-feature-list.json` - 功能列表示例
- `example-agent-progress.txt` - 进度日志示例

## 快速开始

```bash
# 1. 创建项目
mkdir my-first-project && cd my-first-project

# 2. 初始化
openclaw agent --message "创建一个博客系统" \
  --prompt-file long-running-agent-init.prompt

# 3. 开发
openclaw agent --message "继续开发" \
  --prompt-file long-running-agent-coding.prompt

# 4. 监控进度
jq '(.features | map(select(.passes == true)) | length) / (.features | length) * 100' feature_list.json
```

享受增量进展！🎉