package com.mycompany.mproject.services;

import com.mycompany.mproject.data.ProductData;
import com.mycompany.mproject.model.Product;
import java.util.List;

/**
 * ProductService - Service layer để xử lý business logic cho sản phẩm
 */
public class ProductService {
    
    /**
     * Lấy tất cả sản phẩm
     */
    public List<Product> getAllProducts() {
        return ProductData.getAllProducts();
    }
    
    /**
     * Lấy sản phẩm theo ID
     */
    public Product getProductById(int id) {
        return ProductData.getProductById(id);
    }
    
    /**
     * Tìm kiếm sản phẩm
     */
    public List<Product> searchProducts(String keyword) {
        return ProductData.searchProducts(keyword);
    }
    
    /**
     * Lấy sản phẩm có sẵn
     */
    public List<Product> getAvailableProducts() {
        return ProductData.getAvailableProducts();
    }
    
    /**
     * Lấy sản phẩm theo khoảng giá
     */
    public List<Product> getProductsByPriceRange(int minPrice, int maxPrice) {
        return ProductData.getProductsByPriceRange(minPrice, maxPrice);
    }
    
    /**
     * Lấy sản phẩm nổi bật
     */
    public List<Product> getFeaturedProducts(int limit) {
        return ProductData.getFeaturedProducts(limit);
    }
    
    /**
     * Lấy sản phẩm với phân trang
     */
    public List<Product> getProductsWithPagination(int page, int pageSize) {
        return ProductData.getProductsWithPagination(page, pageSize);
    }
    
    /**
     * Thêm sản phẩm
     */
    public boolean addProduct(Product product) {
        return ProductData.addProduct(product);
    }
    
    /**
     * Cập nhật sản phẩm
     */
    public boolean updateProduct(Product product) {
        return ProductData.updateProduct(product);
    }
    
    /**
     * Xóa sản phẩm
     */
    public boolean deleteProduct(int id) {
        return ProductData.deleteProduct(id);
    }
    
    /**
     * Kiểm tra tồn kho
     */
    public boolean isProductAvailable(int productId, int quantity) {
        Product product = getProductById(productId);
        return product != null && product.getQuantity() >= quantity;
    }
    
    /**
     * Giảm số lượng sản phẩm (khi đặt hàng)
     */
    public boolean decreaseProductQuantity(int productId, int quantity) {
        Product product = getProductById(productId);
        if (product != null && product.getQuantity() >= quantity) {
            product.setQuantity(product.getQuantity() - quantity);
            return updateProduct(product);
        }
        return false;
    }
    
    /**
     * Tăng số lượng sản phẩm (khi hủy đơn)
     */
    public boolean increaseProductQuantity(int productId, int quantity) {
        Product product = getProductById(productId);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            return updateProduct(product);
        }
        return false;
    }
    
    /**
     * Lấy tổng số sản phẩm
     */
    public int getTotalProductCount() {
        return ProductData.getProductCount();
    }
    
    /**
     * Tính tổng số trang
     */
    public int getTotalPages(int pageSize) {
        int totalProducts = getTotalProductCount();
        return (int) Math.ceil((double) totalProducts / pageSize);
    }
}