-- 创建数据库
CREATE DATABASE IF NOT EXISTS house_management 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE house_management;

-- 创建房屋列表表
CREATE TABLE IF NOT EXISTS houselist(
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

-- 创建用户表
CREATE TABLE IF NOT EXISTS userlist(
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

-- 创建公告通知表
CREATE TABLE IF NOT EXISTS notice(
  notice_id INT NOT NULL AUTO_INCREMENT,
  notice_title VARCHAR(100) NOT NULL,
  notice_content VARCHAR(2048),
  notice_time DATETIME,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (notice_id)
);

-- 创建支付记录表
CREATE TABLE IF NOT EXISTS payment(
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

-- 插入示例数据，先检查数据是否已存在
INSERT INTO houselist (house_number, house_status, house_detail, house_address, house_price) 
SELECT * FROM (SELECT 'H001', 'available', '两室一厅，精装修', '北京市朝阳区某某街道1号', 3500.00) AS tmp
WHERE NOT EXISTS (
    SELECT house_number FROM houselist WHERE house_number = 'H001'
) LIMIT 1;

INSERT INTO houselist (house_number, house_status, house_detail, house_address, house_price) 
SELECT * FROM (SELECT 'H002', 'occupied', '一室一厅，简装修', '北京市朝阳区某某街道2号', 2500.00) AS tmp
WHERE NOT EXISTS (
    SELECT house_number FROM houselist WHERE house_number = 'H002'
) LIMIT 1;

INSERT INTO houselist (house_number, house_status, house_detail, house_address, house_price) 
SELECT * FROM (SELECT 'H003', 'maintenance', '三室两厅，豪华装修', '北京市朝阳区某某街道3号', 5500.00) AS tmp
WHERE NOT EXISTS (
    SELECT house_number FROM houselist WHERE house_number = 'H003'
) LIMIT 1;

INSERT INTO houselist (house_number, house_status, house_detail, house_address, house_price) 
SELECT * FROM (SELECT 'H004', 'available', '单间，简单装修', '北京市朝阳区某某街道4号', 1500.00) AS tmp
WHERE NOT EXISTS (
    SELECT house_number FROM houselist WHERE house_number = 'H004'
) LIMIT 1;

INSERT INTO houselist (house_number, house_status, house_detail, house_address, house_price) 
SELECT * FROM (SELECT 'H005', 'occupied', '两室一厅，中等装修', '北京市朝阳区某某街道5号', 3000.00) AS tmp
WHERE NOT EXISTS (
    SELECT house_number FROM houselist WHERE house_number = 'H005'
) LIMIT 1;

-- 插入用户表示例数据
INSERT INTO userlist (user_name, user_password, user_status, user_phone) 
SELECT * FROM (SELECT '张三', '123456', '管理员', '13800000001') AS tmp
WHERE NOT EXISTS (
    SELECT user_name FROM userlist WHERE user_name = '张三'
) LIMIT 1;

INSERT INTO userlist (user_name, user_password, user_status, user_phone) 
SELECT * FROM (SELECT '李四', '123456', '用户', '13800000002') AS tmp
WHERE NOT EXISTS (
    SELECT user_name FROM userlist WHERE user_name = '李四'
) LIMIT 1;

INSERT INTO userlist (user_name, user_password, user_status, user_phone) 
SELECT * FROM (SELECT '王五', '123456', '用户', '13800000003') AS tmp
WHERE NOT EXISTS (
    SELECT user_name FROM userlist WHERE user_name = '王五'
) LIMIT 1;

INSERT INTO userlist (user_name, user_password, user_status, user_phone) 
SELECT * FROM (SELECT '赵六', '123456', '用户', '13800000004') AS tmp
WHERE NOT EXISTS (
    SELECT user_name FROM userlist WHERE user_name = '赵六'
) LIMIT 1;

INSERT INTO userlist (user_name, user_password, user_status, user_phone) 
SELECT * FROM (SELECT '钱七', '123456', '用户', '13800000005') AS tmp
WHERE NOT EXISTS (
    SELECT user_name FROM userlist WHERE user_name = '钱七'
) LIMIT 1;

-- 插入通知表示例数据
INSERT INTO notice (notice_title, notice_content, notice_time) 
SELECT * FROM (SELECT '租金调整通知', '尊敬的租户，下月起租金将调整为每月3000元。', '2023-06-01 09:00:00') AS tmp
WHERE NOT EXISTS (
    SELECT notice_title FROM notice WHERE notice_title = '租金调整通知'
) LIMIT 1;

INSERT INTO notice (notice_title, notice_content, notice_time) 
SELECT * FROM (SELECT '维修通知', '因水管维修，本周六上午9点至12点将停水。', '2023-06-02 14:30:00') AS tmp
WHERE NOT EXISTS (
    SELECT notice_title FROM notice WHERE notice_title = '维修通知'
) LIMIT 1;

INSERT INTO notice (notice_title, notice_content, notice_time) 
SELECT * FROM (SELECT '安全检查通知', '本月底将进行房屋安全检查，请各位租户配合。', '2023-06-03 10:15:00') AS tmp
WHERE NOT EXISTS (
    SELECT notice_title FROM notice WHERE notice_title = '安全检查通知'
) LIMIT 1;

-- 插入支付记录表示例数据
INSERT INTO payment (house_id, user_id, payment_amount, payment_date, payment_method, payment_status, remarks)
SELECT * FROM (SELECT 2, 2, 2500.00, '2023-06-01', 'bank_transfer', 'paid', '6月租金') AS tmp
WHERE NOT EXISTS (
    SELECT house_id, user_id, payment_amount, payment_date FROM payment 
    WHERE house_id = 2 AND user_id = 2 AND payment_amount = 2500.00 AND payment_date = '2023-06-01'
) LIMIT 1;

INSERT INTO payment (house_id, user_id, payment_amount, payment_date, payment_method, payment_status, remarks)
SELECT * FROM (SELECT 5, 3, 3000.00, '2023-06-02', 'cash', 'paid', '6月租金') AS tmp
WHERE NOT EXISTS (
    SELECT house_id, user_id, payment_amount, payment_date FROM payment 
    WHERE house_id = 5 AND user_id = 3 AND payment_amount = 3000.00 AND payment_date = '2023-06-02'
) LIMIT 1;

INSERT INTO payment (house_id, user_id, payment_amount, payment_date, payment_method, payment_status, remarks)
SELECT * FROM (SELECT 2, 2, 2500.00, '2023-07-01', 'bank_transfer', 'paid', '7月租金') AS tmp
WHERE NOT EXISTS (
    SELECT house_id, user_id, payment_amount, payment_date FROM payment 
    WHERE house_id = 2 AND user_id = 2 AND payment_amount = 2500.00 AND payment_date = '2023-07-01'
) LIMIT 1;

INSERT INTO payment (house_id, user_id, payment_amount, payment_date, payment_method, payment_status, remarks)
SELECT * FROM (SELECT 5, 3, 3000.00, '2023-07-02', 'cash', 'paid', '7月租金') AS tmp
WHERE NOT EXISTS (
    SELECT house_id, user_id, payment_amount, payment_date FROM payment 
    WHERE house_id = 5 AND user_id = 3 AND payment_amount = 3000.00 AND payment_date = '2023-07-02'
) LIMIT 1;

INSERT INTO payment (house_id, user_id, payment_amount, payment_date, payment_method, payment_status, remarks)
SELECT * FROM (SELECT 2, 2, 2500.00, '2023-08-01', 'bank_transfer', 'unpaid', '8月租金') AS tmp
WHERE NOT EXISTS (
    SELECT house_id, user_id, payment_amount, payment_date FROM payment 
    WHERE house_id = 2 AND user_id = 2 AND payment_amount = 2500.00 AND payment_date = '2023-08-01'
) LIMIT 1;

-- 删除已存在的存储过程（如果存在）
DROP PROCEDURE IF EXISTS UpdateHouseStatus;
DROP PROCEDURE IF EXISTS ProcessRentPayment;
DROP PROCEDURE IF EXISTS AuthenticateUser;
DROP PROCEDURE IF EXISTS CheckOverduePayments;
DROP PROCEDURE IF EXISTS GetHouseStatistics;
DROP PROCEDURE IF EXISTS GetIncomeStatistics;
DROP PROCEDURE IF EXISTS LeaseHouse;
DROP PROCEDURE IF EXISTS ReleaseHouse;

-- 删除已存在的函数（如果存在）
DROP FUNCTION IF EXISTS CalculateTotalRent;
DROP FUNCTION IF EXISTS GetHouseStatusDescription;
DROP FUNCTION IF EXISTS CalculateOverdueDays;
DROP FUNCTION IF EXISTS FormatCurrency;

-- 删除已存在的触发器（如果存在）
DROP TRIGGER IF EXISTS tr_payment_insert;
DROP TRIGGER IF EXISTS tr_house_status_update;
DROP TRIGGER IF EXISTS tr_user_update;
DROP TRIGGER IF EXISTS tr_payment_status_update;

-- 删除已存在的视图（如果存在）
DROP VIEW IF EXISTS v_house_payment_details;
DROP VIEW IF EXISTS v_user_rental_history;
DROP VIEW IF EXISTS v_house_status_statistics;
DROP VIEW IF EXISTS v_overdue_payments;

-- 创建存储过程
-- 1. 房屋状态更新存储过程
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

-- 2. 租金支付处理存储过程
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

-- 3. 用户认证存储过程
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

-- 4. 逾期支付检查存储过程
DELIMITER //
CREATE PROCEDURE CheckOverduePayments()
BEGIN
    UPDATE payment 
    SET payment_status = 'overdue'
    WHERE payment_date < DATE_SUB(CURDATE(), INTERVAL 30 DAY)
    AND payment_status = 'unpaid';
END //
DELIMITER ;

-- 5. 房屋统计信息存储过程
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

-- 6. 收入统计存储过程
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

-- 事务处理示例
-- 1. 房屋租赁事务：包括创建租赁记录和更新房屋状态
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

-- 2. 房屋退租事务：包括更新房屋状态和生成退租记录
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

-- 创建自定义函数
-- 1. 计算租金总额函数：根据月租金和租赁月数计算总租金
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

-- 2. 获取房屋状态描述函数：将房屋状态枚举值转换为中文描述
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

-- 3. 计算逾期天数函数：计算支付逾期天数
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

-- 4. 格式化金额函数：将金额格式化为带货币符号的字符串
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

-- 创建触发器
-- 1. 支付记录插入触发器：当插入新的支付记录时，自动更新房屋的最后更新时间
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

-- 2. 房屋状态更新触发器：当房屋状态更新时，记录状态变更日志
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

-- 3. 用户信息更新触发器：当用户信息更新时，记录更新时间
DELIMITER //
CREATE TRIGGER tr_user_update
BEFORE UPDATE ON userlist
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END //
DELIMITER ;

-- 4. 支付状态变更触发器：当支付状态变更为逾期时，发送通知
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

-- 创建视图
-- 1. 房屋详细信息视图：包含房屋及其相关支付信息
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

-- 2. 用户租赁历史视图：显示用户的租赁和支付历史
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

-- 3. 房屋状态统计视图：统计各类房屋数量
CREATE VIEW v_house_status_statistics AS
SELECT 
    GetHouseStatusDescription(house_status) AS status_description,
    house_status AS status_code,
    COUNT(*) AS count
FROM houselist
GROUP BY house_status;

-- 4. 逾期支付视图：显示所有逾期支付记录
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