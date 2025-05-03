# AppDemo
**Author**: [caofu](https://github.com/ufoac)  
**Create Date**: 20250426
**Update Date**: 20240503
**GitHub**: [https://github.com/ufoac/app-demo](https://github.com/ufoac/app-demo)

---

## 1. 整体介绍
- 基于领域驱动设计（DDD）的账号/卡片Demo
- 提供约定的 RESTful API
- 支持参数校验、分页、异常处理、事件驱动等技术特性
- 支持各个模块的单元测试(50+case) 和典型流程的集成测试
- 已通过 GitHub Action 实现 CI/CD，自动化编译、打包、镜像、部署至 AWS 云环境（EC2 + RDS）

---

## 2. 技术栈
- **语言**: JDK 21
- **框架**: Spring Boot 3.3.3
- **数据库**: PostgreSQL 16
- **ORM**: MyBatis Plus 3.5.11
- **数据迁移**: Flyway 10.17.3
- **打包**: Docker Image
- **部署**: Docker Compose & K8S
- **CI/CD**: GitHub Actions

---

## 3. 工程目录
```plaintext
app-demo  
├── app-demo-domain       # 领域层（核心业务逻辑）  
│   └── model             # 领域模型（Account/Card）  
├── app-demo-application  # 应用层（服务编排）  
│   └── service           # 服务（AccountCardBizService）  
├── app-demo-interface    # 接口适配层  
│   ├── controller        # REST控制器（Account/Card Controller）  
│   └── excpeion          # 统一异常管理  
├── app-demo-infrastructure # 基础设施层  
│   ├── repository        # 持久层实现  
│   └── config            # ORM配置（Mapper扫描/乐观锁/分页插件）  
│   └── db.migration      # Flyway SQL脚本（V1.0.0__init.sql）  
└── start                 # 启动类（DemoApplication）
└── release               # 发布支持目录
    ├── deploy            # 部署脚本, 包括Docker Compoes和k8s部署包  
    └── images            # 镜像文件和手动脚本 
```

## 4. 关键文件路径
- **数据库 Schema**  
  `app-demo-infrastructure/src/main/resources/db/migration/V1.0.0__init.sql`
- **CI/CD 脚本**
    - Dockerfile: `release/images/app/Dockerfile`
    - Docker Compose 部署脚本: `release/deploy/app/docker-compose/`
    - K8S 部署脚本（待完善）: `release/deploy/app/k8s-todo/`
    - GitHub Actions 流程配置:
        - CI: `.github/workflows/ci.yml`
        - CD: `.github/workflows/cd.yml`
    - 集成测试: `start/src/test/java/com/demo/app/BaseIntegrationTest.java`
    - 单元测试: `各模块/src/test/Test*.java` **50+case**
## 5. CI/CD 流程
  - **CI**（代码提交触发）:
    ```plaintext
    拉取代码 ➡ 编译/单测 ➡ 构建 Docker 镜像 ➡ 推送至 Docker Hub  
    ```  
- **CD**（手动触发部署）:
  ```plaintext
  连接 EC2 ➡ 执行部署脚本（Docker Compose/K8S）  
  ```

## 6. 云上架构（AWS）
### 架构拓扑
```plaintext
用户  ➡ AWS Gateway ➡ EC2（App） ➡ RDS（PostgreSQL）
```
### 资源配置
| 资源   | 类型/配置              | 说明                          |
|--------|------------------------|-------------------------------|
| **EC2** | t2.micro（免费 tier）  | 1 vCPU + 1GB RAM + 8GB 存储   |
|        | 安全组                 | 开放 `8282` 端口（可配白名单） |
| **RDS** | PostgreSQL 16          | 1 vCPU + 1GB RAM + 20GB 存储  |
|        | 安全组                 | 开放 `5432` 端口，仅允许 EC2 实例访问 |
