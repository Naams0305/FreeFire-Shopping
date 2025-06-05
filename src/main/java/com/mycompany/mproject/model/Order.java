// Order.java
package com.mycompany.mproject.model;

import java.util.Date;
import java.util.List;

public class Order {
    private String id;
    private int userId;
    private Date orderDate;
    private String status; // PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    
    // Thông tin giao hàng
    private String shippingName;
    private String shippingPhone;
    private String shippingEmail;
    private String shippingAddress;
    private String notes;
    
    // Thông tin thanh toán
    private String paymentMethod; // cod, bank-transfer, credit-card
    private String paymentStatus; // PENDING, PAID, FAILED
    
    // Thông tin đơn hàng
    private long subtotal;
    private long shippingFee;
    private long totalAmount;
    
    private List<OrderItem> orderItems;
    
    // Constructors
    public Order() {}
    
    public Order(String id, int userId) {
        this.id = id;
        this.userId = userId;
        this.orderDate = new Date();
        this.status = "PENDING";
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getShippingName() { return shippingName; }
    public void setShippingName(String shippingName) { this.shippingName = shippingName; }
    
    public String getShippingPhone() { return shippingPhone; }
    public void setShippingPhone(String shippingPhone) { this.shippingPhone = shippingPhone; }
    
    public String getShippingEmail() { return shippingEmail; }
    public void setShippingEmail(String shippingEmail) { this.shippingEmail = shippingEmail; }
    
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public long getSubtotal() { return subtotal; }
    public void setSubtotal(long subtotal) { this.subtotal = subtotal; }
    
    public long getShippingFee() { return shippingFee; }
    public void setShippingFee(long shippingFee) { this.shippingFee = shippingFee; }
    
    public long getTotalAmount() { return totalAmount; }
    public void setTotalAmount(long totalAmount) { this.totalAmount = totalAmount; }
    
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
    
    // Utility methods
    public int getTotalQuantity() {
        if (orderItems == null) return 0;
        return orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
    }
    
    public String getStatusText() {
        switch (status) {
            case "PENDING": return "Chờ xử lý";
            case "PROCESSING": return "Đang xử lý";
            case "SHIPPED": return "Đã giao vận";
            case "DELIVERED": return "Đã giao hàng";
            case "CANCELLED": return "Đã hủy";
            default: return status;
        }
    }
    
    public String getPaymentMethodText() {
        switch (paymentMethod) {
            case "cod": return "Thanh toán khi nhận hàng";
            case "bank-transfer": return "Chuyển khoản ngân hàng";
            case "credit-card": return "Thẻ tín dụng";
            default: return paymentMethod;
        }
    }
}