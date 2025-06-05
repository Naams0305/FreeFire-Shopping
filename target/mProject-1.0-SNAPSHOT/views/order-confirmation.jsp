<%@page import="com.mycompany.mproject.model.Order"%>
<%@page import="com.mycompany.mproject.model.OrderItem"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác nhận đơn hàng - OtterShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/OrderConfirmationCSS.css">
    
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
                <li><a href="${pageContext.request.contextPath}/Cart">Giỏ hàng</a></li>
            </ul>
        </div>
    </div>

    <div class="content">
        <div class="confirmation-container">
            
            <!-- Thông báo -->
            <% if (request.getAttribute("successMessage") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("successMessage") %>
                </div>
            <% } %>
            
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            
            <%
                Order order = (Order) request.getAttribute("order");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            %>
            
            <% if (order != null) { %>
                <!-- Header thành công -->
                <div class="success-header">
                    <div class="success-icon">✓</div>
                    <h2>Đặt hàng thành công!</h2>
                    <p>Cảm ơn bạn đã mua hàng tại OtterShop</p>
                </div>
                
                <!-- Thông tin đơn hàng -->
                <div class="order-info">
                    <div class="info-section">
                        <h4>Thông tin đơn hàng</h4>
                        <p><strong>Mã đơn hàng:</strong> <%= order.getId() %></p>
                        <p><strong>Ngày đặt:</strong> <%= dateFormat.format(order.getOrderDate()) %></p>
                        <p><strong>Trạng thái:</strong> 
                            <span class="status-badge status-<%= order.getStatus().toLowerCase() %>">
                                <%= order.getStatusText() %>
                            </span>
                        </p>
                        <p><strong>Phương thức thanh toán:</strong> <%= order.getPaymentMethodText() %></p>
                    </div>
                    
                    <div class="info-section">
                        <h4>Thông tin giao hàng</h4>
                        <p><strong>Người nhận:</strong> <%= order.getShippingName() %></p>
                        <p><strong>Điện thoại:</strong> <%= order.getShippingPhone() %></p>
                        <p><strong>Email:</strong> <%= order.getShippingEmail() %></p>
                        <p><strong>Địa chỉ:</strong> <%= order.getShippingAddress() %></p>
                        <% if (order.getNotes() != null && !order.getNotes().trim().isEmpty()) { %>
                            <p><strong>Ghi chú:</strong> <%= order.getNotes() %></p>
                        <% } %>
                    </div>
                </div>
                
                <!-- Danh sách sản phẩm -->
                <% if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) { %>
                    <div class="order-items">
                        <h4>Sản phẩm đã đặt</h4>
                        <% for (OrderItem item : order.getOrderItems()) { %>
                            <div class="order-item">
                                <img src="${pageContext.request.contextPath}/<%= item.getProductImage() %>" 
                                     alt="<%= item.getProductName() %>" class="item-image">
                                <div class="item-details">
                                    <h5><%= item.getProductName() %></h5>
                                    <p>Số lượng: <%= item.getQuantity() %></p>
                                </div>
                                <div class="item-price">
                                    <%= String.format("%,d", item.getTotalPrice()) %> VNĐ
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>
                
                <!-- Tổng kết đơn hàng -->
                <div class="order-summary">
                    <div class="summary-row">
                        <span>Tạm tính:</span>
                        <span><%= String.format("%,d", order.getSubtotal()) %> VNĐ</span>
                    </div>
                    <div class="summary-row">
                        <span>Phí vận chuyển:</span>
                        <span><%= String.format("%,d", order.getShippingFee()) %> VNĐ</span>
                    </div>
                    <div class="summary-row total">
                        <span>Tổng cộng:</span>
                        <span><%= String.format("%,d", order.getTotalAmount()) %> VNĐ</span>
                    </div>
                </div>
                
                <!-- Các nút thao tác -->
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/Home" class="btn btn-primary">
                        Tiếp tục mua hàng
                    </a>
                    
                    <a href="${pageContext.request.contextPath}/OrderHistory" class="btn btn-secondary">
                        Xem đơn hàng của tôi
                    </a>
                    
                    <% if ("PENDING".equals(order.getStatus())) { %>
                        <form action="${pageContext.request.contextPath}/OrderConfirmation" method="post" style="display: inline;">
                            <input type="hidden" name="action" value="cancelOrder">
                            <input type="hidden" name="orderId" value="<%= order.getId() %>">
                            <button type="submit" class="btn btn-danger" 
                                    onclick="return confirm('Bạn có chắc muốn hủy đơn hàng này?')">
                                Hủy đơn hàng
                            </button>
                        </form>
                    <% } %>
                </div>
                
            <% } else { %>
                <div class="success-header">
                    <h2>Không tìm thấy thông tin đơn hàng</h2>
                    <p>Vui lòng kiểm tra lại hoặc liên hệ với chúng tôi</p>
                    <a href="${pageContext.request.contextPath}/Home" class="btn btn-primary">
                        Về trang chủ
                    </a>
                </div>
            <% } %>
        </div>
    </div>

    <!-- Footer -->
    <div class="footer">
        <p>&copy; 2025 OtterShop. Tất cả quyền được bảo lưu.</p>
    </div>
</body>
</html>