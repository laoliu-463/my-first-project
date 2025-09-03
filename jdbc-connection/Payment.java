import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int houseId;
    private int userId;
    private BigDecimal paymentAmount;
    private Date paymentDate;
    private String paymentMethod;
    private String paymentStatus;
    private String remarks;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public Payment() {}
    
    public Payment(int paymentId, int houseId, int userId, BigDecimal paymentAmount, 
                   Date paymentDate, String paymentMethod, String paymentStatus, 
                   String remarks, Timestamp createdAt, Timestamp updatedAt) {
        this.paymentId = paymentId;
        this.houseId = houseId;
        this.userId = userId;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters
    public int getPaymentId() {
        return paymentId;
    }
    
    public int getHouseId() {
        return houseId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }
    
    public Date getPaymentDate() {
        return paymentDate;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    // Setters
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", houseId=" + houseId +
                ", userId=" + userId +
                ", paymentAmount=" + paymentAmount +
                ", paymentDate=" + paymentDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}