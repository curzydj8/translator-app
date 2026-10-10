# 随身多语言翻译器 (Translator)

## 当前阶段：阶段 5 - 复制、分享、测试和安全边界

### 环境要求
- JDK 17
- Android SDK：platform-35, build-tools 35.0.0, platform-tools
- Gradle 8.9（通过项目内 Wrapper 使用，无需全局安装）

### 构建步骤
```bash
cd ~/translator-app
./gradlew --version
./gradlew clean
./gradlew test
./gradlew assembleDebug
```

### 已实现（阶段 1-3）
- 可构建的 Android 项目骨架（Kotlin 2.0.21 / AGP 8.7.3 / Gradle 8.9）
- 首页/翻译页/历史页 + 底部导航，返回键行为正常
- 英文文本输入、粘贴、清空、空文本提交提示
- 统一翻译任务模型、翻译引擎接口；无可用引擎时明确失败（"当前未安装可用翻译引擎。"），无伪翻译

### 已实现（阶段 4 新增：Room 历史数据）
- Room 2.6.1：TranslationRecord Entity、TranslationDao、AppDatabase（translator.db, v1）、TranslationRepository
- 成功（COMPLETED）任务自动保存；失败任务不保存，仅界面展示
- 历史页：RecyclerView 列表（源文本/译文/语言方向/时间/状态）、点击查看详情对话框、对话框内删除单条
- 数据库升级策略：v1 使用 fallbackToDestructiveMigration；升至 v2 时必须提供显式 Migration
- SQLite 仅经 Kotlin 数据层访问

### 已实现（阶段 5 新增：复制、分享、测试和安全边界）
- 翻译结果一键复制到系统剪贴板、一键经系统分享（ACTION_SEND 纯文本）
- 无结果（未翻译/翻译中/失败/清空）时复制/分享按钮禁用
- 仅分享界面已显示的译文正文：不含输入原文、任务 ID、引擎名、时间戳（shareableText 纯函数保证，可单元测试）
- 单元测试 24 项全过：引擎失败、历史保存/读取/删除/失败不保存、映射往返、分享文本规则、多行/特殊字符/Unicode 透传、空输入不崩溃
- 安全静态检查 6 项全过：零权限声明、无障碍/悬浮窗、无网络权限与网络代码、无 Log 调用、无后台服务、不读取其他应用

### 明确未实现
- 真实离线翻译引擎集成（当前环境无法集成）
- 屏幕捕获、OCR、无障碍服务、悬浮窗、文件翻译、在线服务、网络权限
- UI 自动化测试与真机验证（无设备；connectedDebugAndroidTest 未执行；应用重启后数据保留未在真机验证）

### 后续阶段
阶段 6：完整构建验收交付（clean / test / connectedDebugAndroidTest / assembleDebug + 安装启动记录）

### 权限和隐私边界
- 不申请任何权限（含网络权限），Manifest 零 uses-permission
- 不读取其他应用内容，不上传文本、历史、错误日志
- 日志不记录完整敏感文本（代码中无 Log 调用）
- 不启动后台屏幕读取、录制、覆盖层或网络任务
