<%-- 
    Document   : products
    Created on : Jun 5, 2025, 2:00:04 PM
    Author     : Administrator
--%>
<%@page import="com.mycompany.mproject.model.Product"%>
<%@page import="com.mycompany.mproject.data.ProductData"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh sách sản phẩm - OtterShop</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ProductCSS.css">
    </head>
    <body>
       <div class="header">
            <div class="text-header">
                <img src="images/otter.png" alt="alt"/>
                <h1>OtterShop </h1>
            </div>
            <div class="menu">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/Home">Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/Product" class="active">Danh sách sản phẩm</a></li>
                    <li><a href="${pageContext.request.contextPath}/Cart">Giỏ hàng</a></li>
                </ul>
            </div>
        </div>
        
        <div class="content">
            <!-- Thanh tìm kiếm -->
            <div class="search-bar">
                <form action="${pageContext.request.contextPath}/Product" method="get">
                    <input type="text" name="search" class="search-input" 
                           placeholder="Tìm kiếm sản phẩm..." 
                           value="${param.search}">
                    <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                </form>
            </div>

            <!-- Danh sách sản phẩm -->
            <div class="products-container">
                <h2>Danh sách sản phẩm</h2>
                
                <%
                    // Lấy danh sách sản phẩm từ ProductData
                    List<Product> products;
                    String searchKeyword = request.getParameter("search");
                    
                    if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                        products = ProductData.searchProducts(searchKeyword);
                    } else {
                        products = ProductData.getAllProducts();
                    }
                %>
                
                <% if (products != null && !products.isEmpty()) { %>
                    <div class="products-grid">
                        <% for (Product product : products) { %>
                            <div class="product-card">
                                <div class="product-image">
                                    <img src="<%= product.getImgUrl() %>" alt="<%= product.getName() %>">
                                </div>
                                <div class="product-info">
                                    <h3 class="product-name"><%= product.getName() %></h3>
                                    <p class="product-description"><%= product.getDescription() %></p>
                                    <div class="product-price">
                                        <%= String.format("%,d", product.getPrice()) %> VNĐ
                                    </div>
                                    <div class="product-quantity">
                                        Còn lại: <%= product.getQuantity() %> sản phẩm
                                    </div>
                                    <div class="product-actions">
                                        <% if (product.getQuantity() > 0) { %>
                                            <form action="${pageContext.request.contextPath}/Cart" method="post" style="display: inline;">
                                                <input type="hidden" name="action" value="add">
                                                <input type="hidden" name="productId" value="<%= product.getId() %>">
                                                <button type="submit" class="btn btn-primary">Thêm vào giỏ</button>
                                            </form>
                                        <% } else { %>
                                            <button class="btn btn-disabled" disabled>Hết hàng</button>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="no-products">
                        <p>Không tìm thấy sản phẩm nào.</p>
                        <% if (searchKeyword != null && !searchKeyword.trim().isEmpty()) { %>
                            <a href="${pageContext.request.contextPath}/Product" class="btn btn-primary">
                                Xem tất cả sản phẩm
                            </a>
                        <% } %>
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