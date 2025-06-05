package com.mycompany.mproject.controller;

import com.mycompany.mproject.model.User;
import com.mycompany.mproject.model.Cart;
import com.mycompany.mproject.model.Product;
import com.mycompany.mproject.data.ProductData;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * CartController - Xử lý các thao tác với giỏ hàng
 */
@WebServlet(name = "CartController", urlPatterns = {"/Cart"})
public class CartController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User userLogin = (User) session.getAttribute("userLogin");
        
        if (userLogin == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Lấy giỏ hàng từ session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart(userLogin.getId());
            session.setAttribute("cart", cart);
        }
        
        request.setAttribute("userLogin", userLogin);
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("views/cart.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User userLogin = (User) session.getAttribute("userLogin");
        
        if (userLogin == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart(userLogin.getId());
            session.setAttribute("cart", cart);
        }
        
        try {
            switch (action) {
                case "add":
                    handleAddToCart(request, cart);
                    break;
                case "update":
                    handleUpdateQuantity(request, cart);
                    break;
                case "remove":
                    handleRemoveItem(request, cart);
                    break;
                case "clear":
                    cart.clear();
                    break;
                case "checkout":
                    handleCheckout(request, response, cart);
                    return;
                default:
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        // Chuyển hướng về trang giỏ hàng
        response.sendRedirect(request.getContextPath() + "/Cart");
    }
    
    private void handleAddToCart(HttpServletRequest request, Cart cart) {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = 1; // Mặc định thêm 1 sản phẩm
        
        String quantityParam = request.getParameter("quantity");
        if (quantityParam != null && !quantityParam.isEmpty()) {
            quantity = Integer.parseInt(quantityParam);
        }
        
        Product product = ProductData.getProductById(productId);
        if (product != null && product.getQuantity() >= quantity) {
            cart.addItem(product, quantity);
        }
    }
    
    private void handleUpdateQuantity(HttpServletRequest request, Cart cart) {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        // Kiểm tra số lượng tồn kho
        Product product = ProductData.getProductById(productId);
        if (product != null && quantity <= product.getQuantity()) {
            cart.updateQuantity(productId, quantity);
        }
    }
    
    private void handleRemoveItem(HttpServletRequest request, Cart cart) {
        int productId = Integer.parseInt(request.getParameter("productId"));
        cart.removeItem(productId);
    }
    
    private void handleCheckout(HttpServletRequest request, HttpServletResponse response, Cart cart) 
            throws ServletException, IOException {
        
        if (cart.isEmpty()) {
            request.setAttribute("errorMessage", "Giỏ hàng trống!");
            request.getRequestDispatcher("views/cart.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra tồn kho trước khi thanh toán
        boolean stockAvailable = true;
        for (var item : cart.getItems()) {
            Product product = ProductData.getProductById(item.getProductId());
            if (product == null || product.getQuantity() < item.getQuantity()) {
                stockAvailable = false;
                break;
            }
        }
        
        if (!stockAvailable) {
            request.setAttribute("errorMessage", "Một số sản phẩm không đủ hàng!");
            request.getRequestDispatcher("views/cart.jsp").forward(request, response);
            return;
        }
        
        // Chuyển đến trang thanh toán
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("views/checkout.jsp").forward(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Cart Controller - Handles shopping cart operations";
    }
}