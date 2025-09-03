import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    // 获取所有支付记录
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payment";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setHouseId(rs.getInt("house_id"));
                payment.setUserId(rs.getInt("user_id"));
                payment.setPaymentAmount(rs.getBigDecimal("payment_amount"));
                payment.setPaymentDate(rs.getDate("payment_date"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setPaymentStatus(rs.getString("payment_status"));
                payment.setRemarks(rs.getString("remarks"));
                payment.setCreatedAt(rs.getTimestamp("created_at"));
                payment.setUpdatedAt(rs.getTimestamp("updated_at"));
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return payments;
    }
    
    // 根据ID获取支付记录
    public Payment getPaymentById(int paymentId) {
        Payment payment = null;
        String sql = "SELECT * FROM payment WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, paymentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    payment = new Payment();
                    payment.setPaymentId(rs.getInt("payment_id"));
                    payment.setHouseId(rs.getInt("house_id"));
                    payment.setUserId(rs.getInt("user_id"));
                    payment.setPaymentAmount(rs.getBigDecimal("payment_amount"));
                    payment.setPaymentDate(rs.getDate("payment_date"));
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setPaymentStatus(rs.getString("payment_status"));
                    payment.setRemarks(rs.getString("remarks"));
                    payment.setCreatedAt(rs.getTimestamp("created_at"));
                    payment.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return payment;
    }
    
    // 添加新支付记录
    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payment (house_id, user_id, payment_amount, payment_date, payment_method, payment_status, remarks) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, payment.getHouseId());
            stmt.setInt(2, payment.getUserId());
            stmt.setBigDecimal(3, payment.getPaymentAmount());
            stmt.setDate(4, payment.getPaymentDate());
            stmt.setString(5, payment.getPaymentMethod());
            stmt.setString(6, payment.getPaymentStatus());
            stmt.setString(7, payment.getRemarks());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 更新支付信息
    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE payment SET house_id = ?, user_id = ?, payment_amount = ?, payment_date = ?, payment_method = ?, payment_status = ?, remarks = ? WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, payment.getHouseId());
            stmt.setInt(2, payment.getUserId());
            stmt.setBigDecimal(3, payment.getPaymentAmount());
            stmt.setDate(4, payment.getPaymentDate());
            stmt.setString(5, payment.getPaymentMethod());
            stmt.setString(6, payment.getPaymentStatus());
            stmt.setString(7, payment.getRemarks());
            stmt.setInt(8, payment.getPaymentId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 删除支付记录
    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM payment WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, paymentId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 调用存储过程处理租金支付
    public boolean processRentPayment(int houseId, int userId, BigDecimal amount, String paymentMethod, String remarks) {
        String sql = "{CALL ProcessRentPayment(?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, houseId);
            stmt.setInt(2, userId);
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, paymentMethod);
            stmt.setString(5, remarks);
            
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 调用存储过程检查逾期支付
    public boolean checkOverduePayments() {
        String sql = "{CALL CheckOverduePayments()}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}