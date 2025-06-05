package com.mycompany.mproject.data;

import com.mycompany.mproject.model.Product;
import java.util.*;

/**
 * ProductData Class - Quản lý dữ liệu sản phẩm tĩnh Phù hợp với class Product
 * hiện có
 */
public class ProductData {

    private static List<Product> products = new ArrayList<>();
    private static int nextId = 1;

    // Static block để khởi tạo dữ liệu mẫu
    static {
        initializeData();
    }

    /**
     * Khởi tạo dữ liệu mẫu
     */
    private static void initializeData() {

        products.add(new Product(nextId++, "AK Rồng Xanh", 500000, 50,
                "++ Tốc độ bắn + Sát thương - Tốc độ di chuyển",
                "images/ak-rong-xanh.jpg"));

        products.add(new Product(nextId++, "MP Mãng Xà", 450000, 30,
                "++ Sát thương + Tốc độ bắn - Tốc độ thay đạn",
                "images/mp-mang-xa.jpg"));

        products.add(new Product(nextId++, "M1014 Long Tộc", 550000, 25,
                "++ Tốc độ bắn + Sát thương - Tốc độ thay đạn",
                "images/m1014-long-toc.jpg"));

        products.add(new Product(nextId++, "UMP Phong cách", 400000, 35,
                "++ Sát thương + Tốc độ bắn - Tốc độ thay đạn",
                "images/ump-phong-cach.jpg"));

        products.add(new Product(nextId++, "M1014 Huyết Hoả", 800000, 15,
                "++ Tốc độ bắn + Xuyên giáp - Tốc độ di chuyển",
                "images/m1014-huyet-hoa.jpg"));

    }

    /**
     * Lấy tất cả sản phẩm
     */
    public static List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Lấy sản phẩm theo ID
     */
    public static Product getProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    /**
     * Tìm kiếm sản phẩm theo tên
     */
    public static List<Product> searchProducts(String keyword) {
        List<Product> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }

        String searchTerm = keyword.toLowerCase().trim();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(searchTerm)
                    || product.getDescription().toLowerCase().contains(searchTerm)) {
                result.add(product);
            }
        }
        return result;
    }

    /**
     * Lấy sản phẩm có sẵn (còn hàng)
     */
    public static List<Product> getAvailableProducts() {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantity() > 0) {
                result.add(product);
            }
        }
        return result;
    }

    /**
     * Lấy sản phẩm theo khoảng giá
     */
    public static List<Product> getProductsByPriceRange(int minPrice, int maxPrice) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice) {
                result.add(product);
            }
        }
        return result;
    }

    /**
     * Thêm sản phẩm mới
     */
    public static boolean addProduct(Product product) {
        if (product != null && isValidProduct(product)) {
            product.setId(nextId++);
            products.add(product);
            return true;
        }
        return false;
    }

    /**
     * Cập nhật sản phẩm
     */
    public static boolean updateProduct(Product updatedProduct) {
        if (updatedProduct == null || !isValidProduct(updatedProduct)) {
            return false;
        }

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) {
                products.set(i, updatedProduct);
                return true;
            }
        }
        return false;
    }

    /**
     * Cập nhật số lượng sản phẩm
     * @param productId ID của sản phẩm
     * @param quantityChange Số lượng thay đổi (âm để trừ, dương để cộng)
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public static boolean updateProductQuantity(int productId, int quantityChange) {
        for (Product product : products) {
            if (product.getId() == productId) {
                int newQuantity = product.getQuantity() + quantityChange;
                
                // Kiểm tra số lượng không được âm
                if (newQuantity < 0) {
                    return false;
                }
                
                product.setQuantity(newQuantity);
                return true;
            }
        }
        return false; // Không tìm thấy sản phẩm
    }

    /**
     * Cập nhật số lượng sản phẩm trực tiếp
     * @param productId ID của sản phẩm
     * @param newQuantity Số lượng mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public static boolean setProductQuantity(int productId, int newQuantity) {
        if (newQuantity < 0) {
            return false;
        }
        
        for (Product product : products) {
            if (product.getId() == productId) {
                product.setQuantity(newQuantity);
                return true;
            }
        }
        return false; // Không tìm thấy sản phẩm
    }

    /**
     * Kiểm tra tồn kho của sản phẩm
     * @param productId ID của sản phẩm
     * @param requiredQuantity Số lượng cần kiểm tra
     * @return true nếu đủ hàng, false nếu không đủ
     */
    public static boolean checkStock(int productId, int requiredQuantity) {
        Product product = getProductById(productId);
        return product != null && product.getQuantity() >= requiredQuantity;
    }

    /**
     * Xóa sản phẩm
     */
    public static boolean deleteProduct(int id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Lấy số lượng sản phẩm
     */
    public static int getProductCount() {
        return products.size();
    }

    /**
     * Lấy sản phẩm với phân trang
     */
    public static List<Product> getProductsWithPagination(int page, int pageSize) {
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, products.size());

        if (startIndex >= products.size() || startIndex < 0) {
            return new ArrayList<>();
        }

        List<Product> result = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            result.add(products.get(i));
        }
        return result;
    }

    /**
     * Lấy sản phẩm nổi bật (giá cao nhất)
     */
    public static List<Product> getFeaturedProducts(int limit) {
        List<Product> availableProducts = getAvailableProducts();

        // Sắp xếp theo giá giảm dần
        availableProducts.sort((p1, p2) -> Integer.compare(p2.getPrice(), p1.getPrice()));

        // Lấy số lượng sản phẩm theo limit
        List<Product> result = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, availableProducts.size()); i++) {
            result.add(availableProducts.get(i));
        }
        return result;
    }

    /**
     * Kiểm tra sản phẩm hợp lệ
     */
    private static boolean isValidProduct(Product product) {
        return product.getName() != null && !product.getName().trim().isEmpty()
                && product.getPrice() > 0
                && product.getQuantity() >= 0;
    }
}