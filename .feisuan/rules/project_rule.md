
# 开发规范指南

为保证代码质量、可维护性、安全性与可扩展性，请在开发过程中严格遵循以下规范。

## 一、技术栈要求

- **主框架**：Spring Boot 3.x  
- **语言版本**：Java 21  
- **构建工具**：Maven  
- **SDK 版本**：JDK 21.0.7  

### 核心依赖

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `lombok`

## 二、目录结构说明

```
basic test/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.example.demo/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── entity/
│   │   │       ├── dto/
│   │   │       └── config/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── static/
│   └── test/
│       └── java/
└── pom.xml
```

> 📝 注：当前项目根目录为 `D:\basic test`，所有源码应放置于该路径下对应模块中。

## 三、分层架构规范

| 层级        | 职责说明                         | 开发约束与注意事项                                               |
|-------------|----------------------------------|----------------------------------------------------------------|
| **Controller** | 处理 HTTP 请求与响应，定义 API 接口 | 不得直接访问数据库，必须通过 Service 层调用                  |
| **Service**    | 实现业务逻辑、事务管理与数据校验   | 必须通过 Repository 层访问数据库；返回 DTO 而非 Entity（除非必要） |
| **Repository** | 数据库访问与持久化操作             | 继承 `JpaRepository`；使用 `@EntityGraph` 避免 N+1 查询问题     |
| **Entity**     | 映射数据库表结构                   | 不得直接返回给前端（需转换为 DTO）；包名统一为 `entity`         |

### 接口与实现分离

- 所有接口实现类需放在接口所在包下的 `impl` 子包中。

## 四、安全与性能规范

### 输入校验

- 使用 `@Valid` 与 JSR-303 校验注解（如 `@NotBlank`, `@Size` 等）
  - 注意：Spring Boot 3.x 中校验注解位于 `jakarta.validation.constraints.*`

- 禁止手动拼接 SQL 字符串，防止 SQL 注入攻击。

### 事务管理

- `@Transactional` 注解仅用于 **Service 层**方法。
- 避免在循环中频繁提交事务，影响性能。

## 五、代码风格规范

### 命名规范

| 类型       | 命名方式             | 示例                  |
|------------|----------------------|-----------------------|
| 类名       | UpperCamelCase       | `UserServiceImpl`     |
| 方法/变量  | lowerCamelCase       | `saveUser()`          |
| 常量       | UPPER_SNAKE_CASE     | `MAX_LOGIN_ATTEMPTS`  |

### 注释规范

- 所有类、方法、字段需添加 **Javadoc** 注释。
- 使用中文进行注释以提升团队协作效率。

### 类型命名规范（阿里巴巴风格）

| 后缀 | 用途说明                     | 示例         |
|------|------------------------------|--------------|
| DTO  | 数据传输对象                 | `UserDTO`    |
| DO   | 数据库实体对象               | `UserDO`     |
| BO   | 业务逻辑封装对象             | `UserBO`     |
| VO   | 视图展示对象                 | `UserVO`     |
| Query| 查询参数封装对象             | `UserQuery`  |

### 实体类简化工具

- 使用 Lombok 注解替代手动编写 getter/setter/构造方法：
  - `@Data`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`

## 六、扩展性与日志规范

### 接口优先原则

- 所有业务逻辑通过接口定义（如 `UserService`），具体实现放在 `impl` 包中（如 `UserServiceImpl`）。

### 日志记录

- 使用 `@Slf4j` 注解代替 `System.out.println`

## 七、通用规则总结

- 所有 Java 文件编码格式统一采用 UTF-8；
- 所有代码提交前需运行单元测试并确保覆盖率达标；
- 代码提交时遵守 Git 提交信息规范（例如：feat: 添加用户注册功能）；
- 每个模块尽量独立部署或集成至微服务架构中。

## 八、作者声明

此项目由 **caojianing1** 编写和维护。如有任何疑问或建议，请联系开发者。

---
*更新时间：2025-09-03*
