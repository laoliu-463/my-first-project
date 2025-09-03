import java.math.BigDecimal;
import java.sql.Timestamp;

public class House {
    private int houseId;
    private String houseNumber;
    private String houseStatus;
    private String houseDetail;
    private String houseAddress;
    private BigDecimal housePrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public House() {}
    
    public House(int houseId, String houseNumber, String houseStatus, String houseDetail, 
                 String houseAddress, BigDecimal housePrice, Timestamp createdAt, Timestamp updatedAt) {
        this.houseId = houseId;
        this.houseNumber = houseNumber;
        this.houseStatus = houseStatus;
        this.houseDetail = houseDetail;
        this.houseAddress = houseAddress;
        this.housePrice = housePrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters
    public int getHouseId() {
        return houseId;
    }
    
    public String getHouseNumber() {
        return houseNumber;
    }
    
    public String getHouseStatus() {
        return houseStatus;
    }
    
    public String getHouseDetail() {
        return houseDetail;
    }
    
    public String getHouseAddress() {
        return houseAddress;
    }
    
    public BigDecimal getHousePrice() {
        return housePrice;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    // Setters
    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }
    
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
    
    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }
    
    public void setHouseDetail(String houseDetail) {
        this.houseDetail = houseDetail;
    }
    
    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }
    
    public void setHousePrice(BigDecimal housePrice) {
        this.housePrice = housePrice;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "House{" +
                "houseId=" + houseId +
                ", houseNumber='" + houseNumber + '\'' +
                ", houseStatus='" + houseStatus + '\'' +
                ", houseDetail='" + houseDetail + '\'' +
                ", houseAddress='" + houseAddress + '\'' +
                ", housePrice=" + housePrice +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}