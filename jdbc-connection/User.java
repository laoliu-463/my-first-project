import java.sql.Timestamp;

public class User {
    private int userId;
    private String userName;
    private String userPassword;
    private String userStatus;
    private String userPhone;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public User() {}
    
    public User(int userId, String userName, String userPassword, String userStatus, 
                String userPhone, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userStatus = userStatus;
        this.userPhone = userPhone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters
    public int getUserId() {
        return userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public String getUserPassword() {
        return userPassword;
    }
    
    public String getUserStatus() {
        return userStatus;
    }
    
    public String getUserPhone() {
        return userPhone;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
    
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}