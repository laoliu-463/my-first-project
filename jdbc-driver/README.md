# MySQL JDBC驱动使用说明

## 下载的驱动版本

已下载的JDBC驱动: mysql-connector-java-8.0.30.jar

## 使用方法

### 1. 命令行编译和运行

将JDBC驱动添加到类路径中:

```bash
# 进入项目根目录
cd /path/to/your/project

# 编译Java文件
javac -cp ".;jdbc-driver/mysql-connector-java-8.0.30.jar" jdbc-connection/*.java

# 运行程序
java -cp ".;jdbc-driver/mysql-connector-java-8.0.30.jar;jdbc-connection" RentalManagementSystem
```

注意: 
- 在Linux/Mac系统中使用冒号(:)而不是分号(;)作为类路径分隔符
- 确保在项目根目录下执行这些命令

### 2. IDE中使用

1. 在IDE中打开项目
2. 将 `jdbc-driver/mysql-connector-java-8.0.30.jar` 添加到项目依赖中
3. 运行 [RentalManagementSystem.java](file:///D:/basic%20test/jdbc-connection/RentalManagementSystem.java) 文件

### 3. Maven项目中使用

如果使用Maven，可以在 `pom.xml` 中添加以下依赖:

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.30</version>
</dependency>
```

### 4. Gradle项目中使用

如果使用Gradle，可以在 `build.gradle` 中添加以下依赖:

```gradle
dependencies {
    implementation 'mysql:mysql-connector-java:8.0.30'
}
```

## 故障排除

### ClassNotFoundException: com.mysql.cj.jdbc.Driver

这个错误表示JDBC驱动没有正确添加到类路径中。请确保：

1. 使用 `-cp` 参数正确指定JDBC驱动的位置
2. 驱动文件路径正确无误
3. 在Windows系统中使用分号(`;`)分隔类路径，在Linux/Mac系统中使用冒号(`:`)

### SQLException: No suitable driver found

这个错误通常由以下原因引起：

1. JDBC驱动未正确加载
2. 数据库URL格式不正确
3. 数据库服务未运行
4. 网络连接问题

## 完整的运行示例

在Windows系统中，完整的运行步骤如下：

```cmd
# 1. 进入项目根目录
cd D:\basic test

# 2. 编译Java源文件
javac -cp ".;jdbc-driver/mysql-connector-java-8.0.30.jar" jdbc-connection/*.java

# 3. 运行程序
java -cp ".;jdbc-driver/mysql-connector-java-8.0.30.jar;jdbc-connection" RentalManagementSystem
```

在Linux/Mac系统中，使用以下命令：

```bash
# 1. 进入项目根目录
cd /path/to/basic/test

# 2. 编译Java源文件
javac -cp ".:jdbc-driver/mysql-connector-java-8.0.30.jar" jdbc-connection/*.java

# 3. 运行程序
java -cp ".:jdbc-driver/mysql-connector-java-8.0.30.jar:jdbc-connection" RentalManagementSystem
```

## 运行结果说明

程序成功运行后，您将看到类似以下的输出：

```
租房管理系统演示

=== 房屋操作演示 ===
所有房屋:
House{houseId=1, houseNumber='H001', houseStatus='available', houseDetail='两室一厅，精装修', houseAddress='北京市朝阳区某某街道1号', housePrice=3500.00, createdAt=2025-09-03 18:14:47.0, updatedAt=2025-09-03 18:14:47.0}
...
成功添加新房屋
成功将房屋ID为1的状态更新为维修中

=== 用户操作演示 ===
所有用户:
User{userId=1, userName='张三', userPassword='123456', userStatus='管理员', userPhone='13800000001', createdAt=2025-09-03 18:14:47.0, updatedAt=2025-09-03 18:14:47.0}
...
用户'张三'认证结果: 失败

=== 支付操作演示 ===
所有支付记录:
Payment{paymentId=1, houseId=2, userId=2, paymentAmount=2500.00, paymentDate=2023-06-01, paymentMethod='bank_transfer', paymentStatus='paid', remarks='6月租金', createdAt=2025-09-03 18:14:47.0, updatedAt=2025-09-03 18:14:47.0}
...
成功添加新支付记录
成功处理租金支付
```

## 可能出现的其他错误

### SQLException: Parameter number X is not an OUT parameter

这个错误通常出现在存储过程调用中，表示尝试注册一个不是输出参数的参数为输出参数。

### SQLSyntaxErrorException: PROCEDURE database_name.procedure_name does not exist

这个错误表示尝试调用的存储过程在数据库中不存在。请确保数据库中已创建所有必需的存储过程。

## 注意事项

1. 确保MySQL服务正在运行
2. 确保数据库用户具有足够的权限
3. 检查连接参数是否正确配置
4. 确保防火墙设置允许数据库连接
5. 确保数据库 `house_management` 已创建
6. 确保数据库中已执行 [house.sql](file:///D:/basic%20test/%E6%95%B0%E6%8D%AE%E5%BA%93/house.sql) 脚本创建了所有必需的表和存储过程