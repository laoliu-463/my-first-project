import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class RentalManagementSystem {
    
    public static void main(String[] args) {
        System.out.println("租房管理系统演示");
        
        // 演示房屋操作
        demonstrateHouseOperations();
        
        // 演示用户操作
        demonstrateUserOperations();
        
        // 演示支付操作
        demonstratePaymentOperations();
    }
    
    private static void demonstrateHouseOperations() {
        System.out.println("\n=== 房屋操作演示 ===");
        HouseDAO houseDAO = new HouseDAO();
        
        // 获取所有房屋
        List<House> houses = houseDAO.getAllHouses();
        System.out.println("所有房屋:");
        for (House house : houses) {
            System.out.println(house);
        }
        
        // 添加新房屋
        House newHouse = new House();
        newHouse.setHouseNumber("H006");
        newHouse.setHouseStatus("available");
        newHouse.setHouseDetail("三室一厅，精装修");
        newHouse.setHouseAddress("北京市海淀区某某街道6号");
        newHouse.setHousePrice(new BigDecimal("4500.00"));
        
        if (houseDAO.addHouse(newHouse)) {
            System.out.println("成功添加新房屋");
        }
        
        // 更新房屋状态
        if (houseDAO.updateHouseStatus(1, "maintenance")) {
            System.out.println("成功将房屋ID为1的状态更新为维修中");
        }
    }
    
    private static void demonstrateUserOperations() {
        System.out.println("\n=== 用户操作演示 ===");
        UserDAO userDAO = new UserDAO();
        
        // 获取所有用户
        List<User> users = userDAO.getAllUsers();
        System.out.println("所有用户:");
        for (User user : users) {
            System.out.println(user);
        }
        
        // 用户认证示例
        boolean isAuthenticated = userDAO.authenticateUser("张三", "123456");
        System.out.println("用户'张三'认证结果: " + (isAuthenticated ? "成功" : "失败"));
    }
    
    private static void demonstratePaymentOperations() {
        System.out.println("\n=== 支付操作演示 ===");
        PaymentDAO paymentDAO = new PaymentDAO();
        
        // 获取所有支付记录
        List<Payment> payments = paymentDAO.getAllPayments();
        System.out.println("所有支付记录:");
        for (Payment payment : payments) {
            System.out.println(payment);
        }
        
        // 添加新支付记录
        Payment newPayment = new Payment();
        newPayment.setHouseId(1);
        newPayment.setUserId(2);
        newPayment.setPaymentAmount(new BigDecimal("2000.00"));
        newPayment.setPaymentDate(Date.valueOf("2023-09-01"));
        newPayment.setPaymentMethod("bank_transfer");
        newPayment.setPaymentStatus("paid");
        newPayment.setRemarks("9月租金");
        
        if (paymentDAO.addPayment(newPayment)) {
            System.out.println("成功添加新支付记录");
        }
        
        // 处理租金支付（调用存储过程）
        if (paymentDAO.processRentPayment(2, 3, new BigDecimal("3000.00"), "cash", "10月租金")) {
            System.out.println("成功处理租金支付");
        }
        
        // 检查逾期支付（调用存储过程）
        if (paymentDAO.checkOverduePayments()) {
            System.out.println("成功检查逾期支付");
        }
    }
}