package com.mycompany.mproject.model;

import java.util.*;

/**
 * Cart Model - Quản lý giỏ hàng
 */
public class Cart {
    private Map<Integer, CartItem> items;
    private int userId;
    
    public Cart() {
        this.items = new HashMap<>();
    }
    
    public Cart(int userId) {
        this.userId = userId;
        this.items = new HashMap<>();
    }
    
    // Thêm sản phẩm vào giỏ
    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) return;
        
        int productId = product.getId();
        if (items.containsKey(productId)) {
            CartItem existingItem = items.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(product, quantity);
            items.put(productId, newItem);
        }
    }
    
    // Cập nhật số lượng sản phẩm
    public void updateQuantity(int productId, int quantity) {
        if (quantity <= 0) {
            removeItem(productId);
        } else if (items.containsKey(productId)) {
            items.get(productId).setQuantity(quantity);
        }
    }
    
    // Xóa sản phẩm khỏi giỏ
    public void removeItem(int productId) {
        items.remove(productId);
    }
    
    // Lấy tất cả items trong giỏ
    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }
    
    // Tính tổng tiền
    public int getTotalAmount() {
        int total = 0;
        for (CartItem item : items.values()) {
            total += item.getTotalPrice();
        }
        return total;
    }
    
    // Tính tổng số lượng sản phẩm
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : items.values()) {
            total += item.getQuantity();
        }
        return total;
    }
    
    // Kiểm tra giỏ hàng rỗng
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    // Xóa toàn bộ giỏ hàng
    public void clear() {
        items.clear();
    }
    
    // Lấy item theo product ID
    public CartItem getItem(int productId) {
        return items.get(productId);
    }
    
    // Getter/Setter
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getItemCount() {
        return items.size();
    }
}