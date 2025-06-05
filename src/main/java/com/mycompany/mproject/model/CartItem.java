package com.mycompany.mproject.model;

/**
 * CartItem Model - Item trong giỏ hàng
 */
public class CartItem {
    private Product product;
    private int quantity;
    
    public CartItem() {}
    
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    // Tính tổng giá cho item này
    public int getTotalPrice() {
        return product.getPrice() * quantity;
    }
    
    // Kiểm tra có đủ hàng không
    public boolean isAvailable() {
        return product.getQuantity() >= quantity;
    }
    
    // Getter/Setter
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    // Convenience methods
    public int getProductId() {
        return product != null ? product.getId() : 0;
    }
    
    public String getProductName() {
        return product != null ? product.getName() : "";
    }
    
    public int getProductPrice() {
        return product != null ? product.getPrice() : 0;
    }
    
    public String getProductImage() {
        return product != null ? product.getImgUrl() : "";
    }
}