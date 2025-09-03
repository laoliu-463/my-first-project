import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    // 获取所有用户
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM userlist";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setUserPassword(rs.getString("user_password"));
                user.setUserStatus(rs.getString("user_status"));
                user.setUserPhone(rs.getString("user_phone"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    // 根据ID获取用户
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * FROM userlist WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setUserPassword(rs.getString("user_password"));
                    user.setUserStatus(rs.getString("user_status"));
                    user.setUserPhone(rs.getString("user_phone"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }
    
    // 根据用户名获取用户
    public User getUserByName(String userName) {
        User user = null;
        String sql = "SELECT * FROM userlist WHERE user_name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setUserPassword(rs.getString("user_password"));
                    user.setUserStatus(rs.getString("user_status"));
                    user.setUserPhone(rs.getString("user_phone"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }
    
    // 添加新用户
    public boolean addUser(User user) {
        String sql = "INSERT INTO userlist (user_name, user_password, user_status, user_phone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getUserPassword());
            stmt.setString(3, user.getUserStatus());
            stmt.setString(4, user.getUserPhone());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 更新用户信息
    public boolean updateUser(User user) {
        String sql = "UPDATE userlist SET user_name = ?, user_password = ?, user_status = ?, user_phone = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getUserPassword());
            stmt.setString(3, user.getUserStatus());
            stmt.setString(4, user.getUserPhone());
            stmt.setInt(5, user.getUserId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 删除用户
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM userlist WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 调用存储过程进行用户认证
    public boolean authenticateUser(String userName, String userPassword) {
        String sql = "{CALL AuthenticateUser(?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, userName);
            stmt.setString(2, userPassword);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            
            stmt.execute();
            return stmt.getBoolean(3);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}