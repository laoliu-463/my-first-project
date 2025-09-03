import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HouseDAO {
    
    // 获取所有房屋列表
    public List<House> getAllHouses() {
        List<House> houses = new ArrayList<>();
        String sql = "SELECT * FROM houselist";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                House house = new House();
                house.setHouseId(rs.getInt("house_id"));
                house.setHouseNumber(rs.getString("house_number"));
                house.setHouseStatus(rs.getString("house_status"));
                house.setHouseDetail(rs.getString("house_detail"));
                house.setHouseAddress(rs.getString("house_address"));
                house.setHousePrice(rs.getBigDecimal("house_price"));
                house.setCreatedAt(rs.getTimestamp("created_at"));
                house.setUpdatedAt(rs.getTimestamp("updated_at"));
                houses.add(house);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return houses;
    }
    
    // 根据ID获取房屋
    public House getHouseById(int houseId) {
        House house = null;
        String sql = "SELECT * FROM houselist WHERE house_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, houseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    house = new House();
                    house.setHouseId(rs.getInt("house_id"));
                    house.setHouseNumber(rs.getString("house_number"));
                    house.setHouseStatus(rs.getString("house_status"));
                    house.setHouseDetail(rs.getString("house_detail"));
                    house.setHouseAddress(rs.getString("house_address"));
                    house.setHousePrice(rs.getBigDecimal("house_price"));
                    house.setCreatedAt(rs.getTimestamp("created_at"));
                    house.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return house;
    }
    
    // 添加新房屋
    public boolean addHouse(House house) {
        // 先检查房屋编号是否已存在
        if (isHouseNumberExists(house.getHouseNumber())) {
            System.out.println("房屋编号 '" + house.getHouseNumber() + "' 已存在，无法添加重复的房屋编号。");
            return false;
        }
        
        String sql = "INSERT INTO houselist (house_number, house_status, house_detail, house_address, house_price) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, house.getHouseNumber());
            stmt.setString(2, house.getHouseStatus());
            stmt.setString(3, house.getHouseDetail());
            stmt.setString(4, house.getHouseAddress());
            stmt.setBigDecimal(5, house.getHousePrice());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 更新房屋信息
    public boolean updateHouse(House house) {
        String sql = "UPDATE houselist SET house_number = ?, house_status = ?, house_detail = ?, house_address = ?, house_price = ? WHERE house_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, house.getHouseNumber());
            stmt.setString(2, house.getHouseStatus());
            stmt.setString(3, house.getHouseDetail());
            stmt.setString(4, house.getHouseAddress());
            stmt.setBigDecimal(5, house.getHousePrice());
            stmt.setInt(6, house.getHouseId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 删除房屋
    public boolean deleteHouse(int houseId) {
        String sql = "DELETE FROM houselist WHERE house_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, houseId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 调用存储过程更新房屋状态
    public boolean updateHouseStatus(int houseId, String newStatus) {
        String sql = "{CALL UpdateHouseStatus(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, houseId);
            stmt.setString(2, newStatus);
            
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 检查房屋编号是否已存在
    public boolean isHouseNumberExists(String houseNumber) {
        String sql = "SELECT COUNT(*) FROM houselist WHERE house_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, houseNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}