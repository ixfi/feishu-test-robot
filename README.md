# 🤖 飞书 AI 测试机器人

基于 Spring Boot + LangChain4j 开发的飞书群聊 AI 机器人模板，支持 Ollama / OpenAI / DeepSeek 等多种模型，一键部署即可在飞书中实现智能对话。

---

## ✨ 功能特性
- 🌐 **飞书消息全兼容**：完美适配飞书 `schema 2.0` 事件协议，支持单聊、群聊@机器人、群聊普通消息
- 🧠 **多模型无缝切换**：内置 Ollama 本地模型、OpenAI/DeepSeek 等远程模型适配，配置文件一键切换
- 🔌 **开箱即用模板**：所有敏感信息已脱敏为占位符，仅需修改配置文件即可快速部署
- 📦 **轻量易扩展架构**：可基于此扩展知识库问答（RAG）、多轮对话记忆、定时任务、消息推送等功能
- 🛡️ **安全请求校验**：自带飞书请求签名校验逻辑，防止恶意请求与消息伪造

---

## 📋 前置依赖
| 工具/服务 | 版本要求 | 说明 |
|----------|----------|------|
| JDK | 17+ | 推荐使用 IDEA 自带的 JetBrains Runtime |
| Spring Boot | 3.x | 项目核心框架 |
| Maven | 3.6+ | 依赖管理与打包工具 |
| 内网穿透 | ngrok/cpolar | 免费版即可，用于飞书公网回调 |
| 飞书应用 | 已创建 | 需开启机器人能力，获取 AppID/Secret |
| AI模型 | Ollama/OpenAI | 本地模型或远程API均可 |

---

## 🚀 安装与部署
### 1. 克隆项目

git clone git@gitee.com:你的用户名/feishu-test-ai-bot.git
cd feishu-test-ai-bot

---

## 修改配置文件
打开 src/main/resources/application.yml，修改以下核心配置：
# 飞书机器人配置
feishu:
  app-id: cli_你的飞书应用ID
  app-secret: 你的飞书应用密钥
  verification-token: 你的飞书事件校验Token

## AI模型配置（Ollama示例）
langchain4j:
  ollama:
    chat-model:
      base-url: http://localhost:11434
      model-name: qwen:7b # 支持 qwen:0.5b/llama3/gemma 等

---

## 打包项目
使用JDK17启动（路径替换为你的JDK路径）
java -jar target/langchain4j.jar

---

## 配置飞书事件订阅
1. 启动 ngrok 内网穿透：ngrok http 8080
2. 复制公网地址，格式为 https://xxx.ngrok-free.dev/webhook
3. 打开飞书开放平台 → 你的应用 → 事件与回调 → 事件订阅
4. 将「请求地址」替换为上述公网地址，保存并发布应用版本

---

## 使用说明
1. 启动 Spring Boot 服务和 ngrok 穿透
2. 在飞书单聊 / 群聊中 @你的机器人 发送消息
3. 机器人会自动清洗 @信息，调用 AI 模型生成回复并发送
4. 如需更换模型，仅需修改 application.yml 中的模型配置即可

---

## ❓ 常见问题（FAQ）
- 1：机器人无响应？
检查 Spring Boot 服务是否正常启动，端口 8080 是否被占用
检查 ngrok 穿透是否正常，公网地址是否已更新到飞书后台
检查飞书应用是否已发布，机器人是否开启可用范围
- 2：飞书返回 400 Bad Request？
检查 AppID/AppSecret 是否正确，重置密钥后需重新配置
检查 Verification Token 是否与飞书后台一致
检查请求地址是否以 /webhook 结尾，格式为 https://xxx.ngrok-free.dev/webhook
- 3：Java 版本不兼容报错？
确保使用 JDK17 运行，推荐使用 IDEA 自带的 JetBrains Runtime
脚本中指定 JAVA17 路径为 你的IDEA安装目录/jbr/bin/java.exe
