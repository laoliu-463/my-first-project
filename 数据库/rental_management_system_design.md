# 租房管理系统数据库设计

## 概述

本设计文档描述了一个完整的租房管理系统的数据库结构。该系统旨在帮助房东、物业管理人员或中介公司管理多个房产及其租赁相关信息。

## 项目结构

```
.
├── 数据库/
│   ├── house_management_system.sql    # 数据库创建和初始化脚本
│   └── rental_management_system_design.md  # 数据库设计文档
└── jdbc-connection/               # JDBC连接和数据访问层
    ├── DatabaseConnection.java    # 数据库连接管理类
    ├── House.java                 # 房屋实体类
    ├── HouseDAO.java              # 房屋数据访问对象
    ├── Payment.java               # 支付实体类
    ├── PaymentDAO.java            # 支付数据访问对象
    ├── User.java                  # 用户实体类
    ├── UserDAO.java               # 用户数据访问对象
    └── RentalManagementSystem.java # 系统演示主类
```

## 核心实体和表设计

### 1. 房屋列表表 (houselist)
用于管理房屋的基本信息。

```sql
CREATE TABLE houselist (
    house_id INT NOT NULL AUTO_INCREMENT,
    house_number VARCHAR(20) NOT NULL,
    house_status ENUM('available', 'occupied', 'maintenance') DEFAULT 'available',
    house_detail VARCHAR(155),
    house_address VARCHAR(255),
    house_price DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (house_id),
    UNIQUE KEY uk_house_number (house_number)
);
```

### 2. 用户表 (userlist)
用于管理系统用户信息。

```sql
CREATE TABLE userlist (
    user_id INT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(50) NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    user_status ENUM('管理员','用户'),
    user_phone VARCHAR(11) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_user_phone (user_phone),
    UNIQUE KEY uk_user_name (user_name)
);
```

### 3. 通知表 (notice)
用于管理系统通知信息。

```sql
CREATE TABLE notice (
    notice_id INT NOT NULL AUTO_INCREMENT,
    notice_title VARCHAR(100) NOT NULL,
    notice_content VARCHAR(2048),
    notice_time DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (notice_id)
);
```

### 4. 支付记录表 (payment)
用于管理房屋租金支付记录。

```sql
CREATE TABLE payment (
    payment_id INT NOT NULL AUTO_INCREMENT,
    house_id INT NOT NULL,
    user_id INT NOT NULL,
    payment_amount DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method ENUM('cash', 'bank_transfer', 'check', 'online_payment') DEFAULT 'bank_transfer',
    payment_status ENUM('paid', 'unpaid', 'partial', 'overdue') DEFAULT 'unpaid',
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (payment_id),
    FOREIGN KEY (house_id) REFERENCES houselist(house_id),
    FOREIGN KEY (user_id) REFERENCES userlist(user_id),
    INDEX idx_payment_date (payment_date)
);
```

## 存储过程设计

### 1. 房屋状态更新存储过程
用于更新房屋状态，当房屋状态发生变化时自动更新相关字段。

```sql
DELIMITER //
CREATE PROCEDURE UpdateHouseStatus(
    IN p_house_id INT,
    IN p_new_status ENUM('available', 'occupied', 'maintenance')
)
BEGIN
    UPDATE houselist 
    SET house_status = p_new_status,
        updated_at = CURRENT_TIMESTAMP
    WHERE house_id = p_house_id;
END //
DELIMITER ;
```

### 2. 租金支付处理存储过程
用于处理租金支付，包括验证支付信息、更新支付状态和生成支付记录。

```sql
DELIMITER //
CREATE PROCEDURE ProcessRentPayment(
    IN p_house_id INT,
    IN p_user_id INT,
    IN p_amount DECIMAL(10,2),
    IN p_payment_method ENUM('cash', 'bank_transfer', 'check', 'online_payment'),
    IN p_remarks TEXT
)
BEGIN
    DECLARE v_payment_date DATE;
    SET v_payment_date = CURDATE();
    
    INSERT INTO payment (
        house_id, 
        user_id, 
        payment_amount, 
        payment_date, 
        payment_method, 
        payment_status, 
        remarks
    ) VALUES (
        p_house_id, 
        p_user_id, 
        p_amount, 
        v_payment_date, 
        p_payment_method, 
        'paid', 
        p_remarks
    );
    
    -- 更新房屋状态为已占用
    IF (SELECT house_status FROM houselist WHERE house_id = p_house_id) = 'available' THEN
        UPDATE houselist 
        SET house_status = 'occupied',
            updated_at = CURRENT_TIMESTAMP
        WHERE house_id = p_house_id;
    END IF;
END //
DELIMITER ;
```

### 3. 用户认证存储过程
用于用户登录验证，检查用户名和密码是否匹配。

```sql
DELIMITER //
CREATE PROCEDURE AuthenticateUser(
    IN p_user_name VARCHAR(50),
    IN p_user_password VARCHAR(255),
    OUT p_auth_result BOOLEAN
)
BEGIN
    DECLARE v_count INT;
    
    SELECT COUNT(*) INTO v_count
    FROM userlist 
    WHERE user_name = p_user_name 
    AND user_password = p_user_password;
    
    IF v_count > 0 THEN
        SET p_auth_result = TRUE;
    ELSE
        SET p_auth_result = FALSE;
    END IF;
END //
DELIMITER ;
```

### 4. 逾期支付检查存储过程
定期检查逾期未支付的租金记录，并更新相关状态。

```sql
DELIMITER //
CREATE PROCEDURE CheckOverduePayments()
BEGIN
    UPDATE payment 
    SET payment_status = 'overdue'
    WHERE payment_date < DATE_SUB(CURDATE(), INTERVAL 30 DAY)
    AND payment_status = 'unpaid';
END //
DELIMITER ;
```

### 5. 房屋统计信息存储过程
统计各类房屋数量，包括可用、已占用和维修中的房屋数量。

```sql
DELIMITER //
CREATE PROCEDURE GetHouseStatistics(
    OUT p_available_count INT,
    OUT p_occupied_count INT,
    OUT p_maintenance_count INT
)
BEGIN
    SELECT COUNT(*) INTO p_available_count
    FROM houselist 
    WHERE house_status = 'available';
    
    SELECT COUNT(*) INTO p_occupied_count
    FROM houselist 
    WHERE house_status = 'occupied';
    
    SELECT COUNT(*) INTO p_maintenance_count
    FROM houselist 
    WHERE house_status = 'maintenance';
END //
DELIMITER ;
```

### 6. 收入统计存储过程
按月份或年份统计租金收入情况。

```sql
DELIMITER //
CREATE PROCEDURE GetIncomeStatistics(
    IN p_year INT,
    OUT p_total_income DECIMAL(12,2)
)
BEGIN
    SELECT COALESCE(SUM(payment_amount), 0) INTO p_total_income
    FROM payment 
    WHERE YEAR(payment_date) = p_year
    AND payment_status = 'paid';
END //
DELIMITER ;
```

## 事务处理设计

### 1. 房屋租赁事务
处理房屋租赁操作，包括创建租赁记录和更新房屋状态，确保数据一致性。

```sql
DELIMITER //
CREATE PROCEDURE LeaseHouse(
    IN p_house_id INT,
    IN p_user_id INT,
    IN p_start_date DATE,
    IN p_end_date DATE,
    IN p_monthly_rent DECIMAL(10,2),
    IN p_deposit DECIMAL(10,2)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- 插入租赁记录
    INSERT INTO payment (
        house_id, 
        user_id, 
        payment_amount, 
        payment_date, 
        payment_method, 
        payment_status, 
        remarks
    ) VALUES (
        p_house_id, 
        p_user_id, 
        p_deposit, 
        p_start_date, 
        'bank_transfer', 
        'paid', 
        '押金'
    );
    
    -- 更新房屋状态为已占用
    UPDATE houselist 
    SET house_status = 'occupied',
        updated_at = CURRENT_TIMESTAMP
    WHERE house_id = p_house_id;
    
    COMMIT;
END //
DELIMITER ;
```

### 2. 房屋退租事务
处理房屋退租操作，包括更新房屋状态和生成退租记录，确保数据一致性。

```sql
DELIMITER //
CREATE PROCEDURE ReleaseHouse(
    IN p_house_id INT,
    IN p_user_id INT,
    IN p_release_date DATE,
    IN p_refund_amount DECIMAL(10,2)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- 插入退租退款记录
    INSERT INTO payment (
        house_id, 
        user_id, 
        payment_amount, 
        payment_date, 
        payment_method, 
        payment_status, 
        remarks
    ) VALUES (
        p_house_id, 
        p_user_id, 
        p_refund_amount, 
        p_release_date, 
        'bank_transfer', 
        'paid', 
        '退租退款'
    );
    
    -- 更新房屋状态为可用
    UPDATE houselist 
    SET house_status = 'available',
        updated_at = CURRENT_TIMESTAMP
    WHERE house_id = p_house_id;
    
    COMMIT;
END //
DELIMITER ;
```

## 自定义函数设计

### 1. 计算租金总额函数
根据月租金和租赁月数计算总租金。

```sql
DELIMITER //
CREATE FUNCTION CalculateTotalRent(
    p_monthly_rent DECIMAL(10,2),
    p_months INT
) RETURNS DECIMAL(10,2)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE v_total_rent DECIMAL(10,2);
    SET v_total_rent = p_monthly_rent * p_months;
    RETURN v_total_rent;
END //
DELIMITER ;
```

### 2. 获取房屋状态描述函数
将房屋状态枚举值转换为中文描述。

```sql
DELIMITER //
CREATE FUNCTION GetHouseStatusDescription(
    p_house_status ENUM('available', 'occupied', 'maintenance')
) RETURNS VARCHAR(20)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE v_status_description VARCHAR(20);
    
    CASE p_house_status
        WHEN 'available' THEN
            SET v_status_description = '可租用';
        WHEN 'occupied' THEN
            SET v_status_description = '已占用';
        WHEN 'maintenance' THEN
            SET v_status_description = '维修中';
        ELSE
            SET v_status_description = '未知状态';
    END CASE;
    
    RETURN v_status_description;
END //
DELIMITER ;
```

### 3. 计算逾期天数函数
计算支付逾期天数。

```sql
DELIMITER //
CREATE FUNCTION CalculateOverdueDays(
    p_payment_date DATE
) RETURNS INT
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE v_overdue_days INT;
    SET v_overdue_days = DATEDIFF(CURDATE(), p_payment_date) - 30;
    
    IF v_overdue_days < 0 THEN
        SET v_overdue_days = 0;
    END IF;
    
    RETURN v_overdue_days;
END //
DELIMITER ;
```

### 4. 格式化金额函数
将金额格式化为带货币符号的字符串。

```sql
DELIMITER //
CREATE FUNCTION FormatCurrency(
    p_amount DECIMAL(10,2)
) RETURNS VARCHAR(20)
READS SQL DATA
DETERMINISTIC
BEGIN
    RETURN CONCAT('¥', FORMAT(p_amount, 2));
END //
DELIMITER ;
```

## 触发器设计

### 1. 支付记录插入触发器
当插入新的支付记录时，自动更新房屋的最后更新时间。

```sql
DELIMITER //
CREATE TRIGGER tr_payment_insert
AFTER INSERT ON payment
FOR EACH ROW
BEGIN
    UPDATE houselist 
    SET updated_at = CURRENT_TIMESTAMP 
    WHERE house_id = NEW.house_id;
END //
DELIMITER ;
```

### 2. 房屋状态更新触发器
当房屋状态更新时，记录状态变更日志。

```sql
DELIMITER //
CREATE TRIGGER tr_house_status_update
AFTER UPDATE ON houselist
FOR EACH ROW
BEGIN
    -- 只有当房屋状态发生变化时才记录
    IF OLD.house_status != NEW.house_status THEN
        INSERT INTO notice (notice_title, notice_content, notice_time)
        VALUES (
            '房屋状态变更', 
            CONCAT('房屋编号: ', NEW.house_number, ', 状态从 ', OLD.house_status, ' 变更为 ', NEW.house_status), 
            NOW()
        );
    END IF;
END //
DELIMITER ;
```

### 3. 用户信息更新触发器
当用户信息更新时，记录更新时间。

```sql
DELIMITER //
CREATE TRIGGER tr_user_update
BEFORE UPDATE ON userlist
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END //
DELIMITER ;
```

### 4. 支付状态变更触发器
当支付状态变更为逾期时，发送通知。

```sql
DELIMITER //
CREATE TRIGGER tr_payment_status_update
AFTER UPDATE ON payment
FOR EACH ROW
BEGIN
    -- 当支付状态变更为逾期时，发送通知
    IF OLD.payment_status != NEW.payment_status AND NEW.payment_status = 'overdue' THEN
        INSERT INTO notice (notice_title, notice_content, notice_time)
        VALUES (
            '支付逾期通知', 
            CONCAT('用户ID: ', NEW.user_id, ' 的支付已逾期，请及时处理'), 
            NOW()
        );
    END IF;
END //
DELIMITER ;
```

## 视图设计

### 1. 房屋详细信息视图
包含房屋及其相关支付信息，便于查看房屋的完整信息和支付历史。

```sql
CREATE VIEW v_house_payment_details AS
SELECT 
    h.house_id,
    h.house_number,
    h.house_status,
    GetHouseStatusDescription(h.house_status) AS house_status_description,
    h.house_detail,
    h.house_address,
    h.house_price,
    p.payment_id,
    p.user_id,
    p.payment_amount,
    FormatCurrency(p.payment_amount) AS formatted_amount,
    p.payment_date,
    p.payment_method,
    p.payment_status
FROM houselist h
LEFT JOIN payment p ON h.house_id = p.house_id
ORDER BY h.house_id, p.payment_date DESC;
```

### 2. 用户租赁历史视图
显示用户的租赁和支付历史，便于查看用户的租赁记录。

```sql
CREATE VIEW v_user_rental_history AS
SELECT 
    u.user_id,
    u.user_name,
    h.house_id,
    h.house_number,
    h.house_address,
    p.payment_id,
    p.payment_amount,
    FormatCurrency(p.payment_amount) AS formatted_amount,
    p.payment_date,
    p.payment_method,
    p.payment_status,
    p.remarks
FROM userlist u
LEFT JOIN payment p ON u.user_id = p.user_id
LEFT JOIN houselist h ON p.house_id = h.house_id
ORDER BY u.user_id, p.payment_date DESC;
```

### 3. 房屋状态统计视图
统计各类房屋数量，便于了解房屋资源的整体分布情况。

```sql
CREATE VIEW v_house_status_statistics AS
SELECT 
    GetHouseStatusDescription(house_status) AS status_description,
    house_status AS status_code,
    COUNT(*) AS count
FROM houselist
GROUP BY house_status;
```

### 4. 逾期支付视图
显示所有逾期支付记录，便于及时发现和处理逾期支付。

```sql
CREATE VIEW v_overdue_payments AS
SELECT 
    p.payment_id,
    h.house_number,
    h.house_address,
    u.user_name,
    u.user_phone,
    p.payment_amount,
    FormatCurrency(p.payment_amount) AS formatted_amount,
    p.payment_date,
    CalculateOverdueDays(p.payment_date) AS overdue_days
FROM payment p
JOIN houselist h ON p.house_id = h.house_id
JOIN userlist u ON p.user_id = u.user_id
WHERE p.payment_status = 'overdue'
ORDER BY p.payment_date;
```

## 数据库ER图

```
+------------------+       +------------------+       +------------------+
|    houselist     |1     n|     payment      |n     1|     userlist     |
+------------------+------>+------------------+------+------------------+
| house_id (PK)    |       | payment_id (PK)  |      | user_id (PK)     |
| house_number     |       | house_id (FK)    |      | user_name        |
| house_status     |       | user_id (FK)     |      | user_phone       |
| house_detail     |       | payment_amount   |      | user_password    |
| house_address    |       | payment_date     |      +------------------+
| house_price      |       | payment_method   |
| created_at       |       | payment_status   |
| updated_at       |       | remarks          |
+------------------+       | created_at       |
                           | updated_at       |
                           +------------------+
                                   |
                                   |1
                                   |n
                           +------------------+
                           |     notice       |
                           +------------------+
                           | notice_id (PK)   |
                           | notice_title     |
                           | notice_content   |
                           | notice_time      |
                           | created_at       |
                           | updated_at       |
                           +------------------+
```

ER图说明：
1. houselist表与payment表之间存在一对多关系（1:n），一个房屋可以有多个支付记录
2. userlist表与payment表之间存在一对多关系（1:n），一个用户可以有多个支付记录
3. payment表与notice表之间存在一对多关系（1:n），一个支付记录可以触发多个通知
4. 所有表都有自己的主键（PK）和常见的时间戳字段

## 核心功能模块

### 1. 房屋管理
- 房屋信息维护
- 房屋状态跟踪

### 2. 用户管理
- 用户信息维护
- 用户权限管理

### 3. 支付管理
- 租金收取记录
- 支付状态跟踪

### 4. 通知管理
- 系统通知发布
- 通知信息维护

## 索引建议

为了提高查询性能，建议在以下字段上创建索引：

1. houselist表: house_number (唯一索引)
2. userlist表: user_name (唯一索引), user_phone (唯一索引)
3. notice表: notice_time
4. payment表: house_id, user_id, payment_date, payment_status