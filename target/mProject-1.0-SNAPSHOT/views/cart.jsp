<%@page import="com.mycompany.mproject.model.Cart"%>
<%@page import="com.mycompany.mproject.model.CartItem"%>
<%@page import="com.mycompany.mproject.model.User"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng - OtterShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/CartCSS.css">
</head>
<body>
    <!-- Header -->
    <div class="header">
        <div class="text-header">
            <img src="${pageContext.request.contextPath}/images/otter.png" alt="OtterShop"/>
            <h1>OtterShop</h1>
        </div>
        <div class="menu">
            <ul>
                <li><a href="${pageContext.request.contextPath}/Home">Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/Product">Danh sách sản phẩm</a></li>
                <li><a href="${pageContext.request.contextPath}/Cart" class="active">Giỏ hàng</a></li>
            </ul>
        </div>
    </div>

    <div class="content">
        <div class="cart-container">
            <h2>Giỏ hàng của bạn</h2>
            
            <!-- Thông báo lỗi -->
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            
            <%
                Cart cart = (Cart) request.getAttribute("cart");
                if (cart == null) {
                    cart = (Cart) session.getAttribute("cart");
                }
            %>
            
            <% if (cart != null && !cart.isEmpty()) { %>
                <div class="cart-items">
                    <div class="cart-header">
                        <div class="item-info">Sản phẩm</div>
                        <div class="item-price">Giá</div>
                        <div class="item-quantity">Số lượng</div>
                        <div class="item-total">Tổng</div>
                        <div class="item-actions">Thao tác</div>
                    </div>
                    
                    <% for (CartItem item : cart.getItems()) { %>
                        <div class="cart-item">
                            <div class="item-info">
                                <img src="${pageContext.request.contextPath}/<%= item.getProductImage() %>" 
                                     alt="<%= item.getProductName() %>" class="item-image">
                                <div class="item-details">
                                    <h4><%= item.getProductName() %></h4>
                                    <p class="item-description"><%= item.getProduct().getDescription() %></p>
                                </div>
                            </div>
                            
                            <div class="item-price">
                                <%= String.format("%,d", item.getProductPrice()) %> VNĐ
                            </div>
                            
                            <div class="item-quantity">
                                <form action="${pageContext.request.contextPath}/Cart" method="post" class="quantity-form">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="productId" value="<%= item.getProductId() %>">
                                    <div class="quantity-controls">
                                        <button type="button" class="quantity-btn" onclick="changeQuantity(<%= item.getProductId() %>, -1)">-</button>
                                        <input type="number" name="quantity" value="<%= item.getQuantity() %>" 
                                               min="1" max="<%= item.getProduct().getQuantity() %>" 
                                               class="quantity-input" id="qty-<%= item.getProductId() %>">
                                        <button type="button" class="quantity-btn" onclick="changeQuantity(<%= item.getProductId() %>, 1)">+</button>
                                    </div>
                                    <button type="submit" class="btn btn-update">Cập nhật</button>
                                </form>
                            </div>
                            
                            <div class="item-total">
                                <%= String.format("%,d", item.getTotalPrice()) %> VNĐ
                            </div>
                            
                            <div class="item-actions">
                                <form action="${pageContext.request.contextPath}/Cart" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="productId" value="<%= item.getProductId() %>">
                                    <button type="submit" class="btn btn-remove" 
                                            onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">
                                        Xóa
                                    </button>
                                </form>
                            </div>
                        </div>
                    <% } %>
                </div>
                
                <!-- Tổng kết giỏ hàng -->
                <div class="cart-summary">
                    <div class="summary-row">
                        <span>Tổng số lượng:</span>
                        <span><%= cart.getTotalQuantity() %> sản phẩm</span>
                    </div>
                    <div class="summary-row total">
                        <span>Tổng tiền:</span>
                        <span><%= String.format("%,d", cart.getTotalAmount()) %> VNĐ</span>
                    </div>
                </div>
                
                <!-- Các nút thao tác -->
                <div class="cart-actions">
                    <a href="${pageContext.request.contextPath}/Product" class="btn btn-continue">
                        Tiếp tục mua hàng
                    </a>
                    
                    <form action="${pageContext.request.contextPath}/Cart" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="clear">
                        <button type="submit" class="btn btn-clear" 
                                onclick="return confirm('Bạn có chắc muốn xóa toàn bộ giỏ hàng?')">
                            Xóa tất cả
                        </button>
                    </form>
                    
                    <form action="${pageContext.request.contextPath}/Cart" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="checkout">
                        <button type="submit" class="btn btn-checkout">
                            Thanh toán
                        </button>
                    </form>
                </div>
                
            <% } else { %>
                <div class="empty-cart">
                    <img src="${pageContext.request.contextPath}/images/empty-cart.jpg" alt="Giỏ hàng trống" class="empty-cart-image">
                    <h3>Giỏ hàng của bạn đang trống</h3>
                    <p>Hãy thêm một số sản phẩm vào giỏ hàng để tiếp tục mua sắm.</p>
                    <a href="${pageContext.request.contextPath}/Product" class="btn btn-primary">
                        Bắt đầu mua sắm
                    </a>
                </div>
            <% } %>
        </div>
    </div>

    <!-- Footer -->
    <div class="footer">
        <p>&copy; 2025 OtterShop. Tất cả quyền được bảo lưu.</p>
    </div>

    <script>
        function changeQuantity(productId, change) {
            const input = document.getElementById('qty-' + productId);
            const currentValue = parseInt(input.value);
            const newValue = currentValue + change;
            const maxValue = parseInt(input.max);
            
            if (newValue >= 1 && newValue <= maxValue) {
                input.value = newValue;
            }
        }
    </script>
</body>
</html>