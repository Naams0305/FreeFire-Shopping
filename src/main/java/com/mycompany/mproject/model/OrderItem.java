// OrderItem.java
package com.mycompany.mproject.model;

public class OrderItem {
    private String orderId;
    private int productId;
    private String productName;
    private String productImage;
    private long price;
    private int quantity;
    private long totalPrice;
    
    // Constructors
    public OrderItem() {}
    
    public OrderItem(String orderId, int productId, String productName, 
                    long price, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = price * quantity;
    }
    
    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
    
    public long getPrice() { return price; }
    public void setPrice(long price) { 
        this.price = price;
        this.totalPrice = price * quantity;
    }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        this.totalPrice = price * quantity;
    }
    
    public long getTotalPrice() { return totalPrice; }
    public void setTotalPrice(long totalPrice) { this.totalPrice = totalPrice; }
}