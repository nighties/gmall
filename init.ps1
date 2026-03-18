# 美团小程序项目初始化脚本
# 用于快速设置开发环境

Write-Host "🚀 开始初始化美团小程序项目开发环境..." -ForegroundColor Green
Write-Host ""

# 检查 Node.js
Write-Host "📦 检查 Node.js..." -ForegroundColor Cyan
if (Get-Command node -ErrorAction SilentlyContinue) {
    $nodeVersion = node --version
    Write-Host "✅ Node.js 已安装：$nodeVersion" -ForegroundColor Green
} else {
    Write-Host "❌ Node.js 未安装，请先安装 Node.js 16+" -ForegroundColor Red
    Write-Host "下载地址：https://nodejs.org/" -ForegroundColor Yellow
    exit 1
}

# 检查 JDK
Write-Host ""
Write-Host "☕ 检查 JDK..." -ForegroundColor Cyan
if (Get-Command java -ErrorAction SilentlyContinue) {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "✅ JDK 已安装：$javaVersion" -ForegroundColor Green
} else {
    Write-Host "⚠️  JDK 未安装，建议安装 JDK 11/17" -ForegroundColor Yellow
    Write-Host "下载地址：https://adoptium.net/" -ForegroundColor Yellow
}

# 检查 Maven
Write-Host ""
Write-Host "🔨 检查 Maven..." -ForegroundColor Cyan
if (Get-Command mvn -ErrorAction SilentlyContinue) {
    $mavenVersion = mvn -version | Select-String "Apache Maven"
    Write-Host "✅ Maven 已安装：$mavenVersion" -ForegroundColor Green
} else {
    Write-Host "⚠️  Maven 未安装，后端项目需要 Maven" -ForegroundColor Yellow
    Write-Host "下载地址：https://maven.apache.org/" -ForegroundColor Yellow
}

# 检查 Git
Write-Host ""
Write-Host "🔧 检查 Git..." -ForegroundColor Cyan
if (Get-Command git -ErrorAction SilentlyContinue) {
    $gitVersion = git --version
    Write-Host "✅ Git 已安装：$gitVersion" -ForegroundColor Green
} else {
    Write-Host "❌ Git 未安装" -ForegroundColor Red
    Write-Host "下载地址：https://git-scm.com/" -ForegroundColor Yellow
    exit 1
}

# 创建 docs 目录
Write-Host ""
Write-Host "📁 创建目录结构..." -ForegroundColor Cyan
if (-not (Test-Path "docs")) {
    New-Item -ItemType Directory -Path "docs/db" -Force | Out-Null
    Write-Host "✅ docs 目录创建成功" -ForegroundColor Green
} else {
    Write-Host "✅ docs 目录已存在" -ForegroundColor Green
}

# 创建 .gitignore
Write-Host ""
Write-Host "📝 创建 .gitignore..." -ForegroundColor Cyan
if (-not (Test-Path ".gitignore")) {
    @"
# 依赖
node_modules/
target/
!.mvn/wrapper/maven-wrapper.jar

# 编译产物
dist/
build/
*.class
*.jar
*.war

# 日志
logs/
*.log

# 系统文件
.DS_Store
Thumbs.db

# IDE
.idea/
.vscode/
*.iml
*.ipr
*.iws

# 环境配置
.env
.env.local
.env.*.local
application-local.yml

# 临时文件
tmp/
temp/
*.tmp

# 截图
tests/screenshots/
"@ | Out-File -FilePath ".gitignore" -Encoding UTF8
    Write-Host "✅ .gitignore 创建成功" -ForegroundColor Green
} else {
    Write-Host "⚠️  .gitignore 已存在" -ForegroundColor Yellow
}

# 初始化 Git
Write-Host ""
Write-Host "🔧 初始化 Git 仓库..." -ForegroundColor Cyan
if (-not (Test-Path ".git")) {
    git init
    git add .
    git commit -m "feat: 初始化项目结构"
    Write-Host "✅ Git 仓库初始化成功" -ForegroundColor Green
} else {
    Write-Host "⚠️  Git 仓库已存在" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "🎉 项目基础结构初始化完成!" -ForegroundColor Green
Write-Host ""
Write-Host "下一步操作:" -ForegroundColor Cyan
Write-Host "1. 手动创建前端项目 (uni-app):" -ForegroundColor White
Write-Host "   cd frontend" -ForegroundColor Gray
Write-Host "   npx degit dcloudio/uni-preset-vue#vite ." -ForegroundColor Gray
Write-Host "   npm install" -ForegroundColor Gray
Write-Host ""
Write-Host "2. 手动创建后端项目 (Spring Boot):" -ForegroundColor White
Write-Host "   使用 https://start.spring.io/ 生成项目并解压到 backend 目录" -ForegroundColor Gray
Write-Host ""
Write-Host "3. 导入数据库脚本:" -ForegroundColor White
Write-Host "   mysql -u root -p < docs/db/gmall.sql" -ForegroundColor Gray
Write-Host ""
Write-Host "详细文档请查看：README.md" -ForegroundColor Yellow
Write-Host ""
