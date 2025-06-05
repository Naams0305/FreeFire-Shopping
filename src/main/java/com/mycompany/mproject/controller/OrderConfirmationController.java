// OrderConfirmationController.java
package com.mycompany.mproject.controller;

import com.mycompany.mproject.model.User;
import com.mycompany.mproject.model.Order;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

/**
 * OrderConfirmationController - Hiển thị xác nhận đơn hàng
 */
@WebServlet(name = "OrderConfirmationController", urlPatterns = {"/OrderConfirmation"})
public class OrderConfirmationController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User userLogin = (User) session.getAttribute("userLogin");
        
        if (userLogin == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String orderId = request.getParameter("orderId");
        Order order = null;
        
        if (orderId != null) {
            // Tìm đơn hàng từ session
            order = findOrderInSession(session, orderId);
            
            // Nếu không tìm thấy trong session, tìm trong cookie
            if (order == null) {
                order = findOrderInCookie(request, orderId);
            }
        }
        
        if (order == null) {
            // Lấy đơn hàng hiện tại từ session
            order = (Order) session.getAttribute("currentOrder");
        }
        
        if (order == null) {
            request.setAttribute("errorMessage", "Không tìm thấy thông tin đơn hàng!");
            response.sendRedirect(request.getContextPath() + "/Home");
            return;
        }
        
        request.setAttribute("order", order);
        request.getRequestDispatcher("views/order-confirmation.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("cancelOrder".equals(action)) {
            handleCancelOrder(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private Order findOrderInSession(HttpSession session, String orderId) {
        // Tìm trong đơn hàng hiện tại
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if (currentOrder != null && orderId.equals(currentOrder.getId())) {
            return currentOrder;
        }
        
        // Tìm trong lịch sử đơn hàng
        List<Order> orderHistory = (List<Order>) session.getAttribute("orderHistory");
        if (orderHistory != null) {
            for (Order order : orderHistory) {
                if (orderId.equals(order.getId())) {
                    return order;
                }
            }
        }
        
        return null;
    }
    
    private Order findOrderInCookie(HttpServletRequest request, String orderId) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("lastOrderId".equals(cookie.getName()) && 
                    orderId.equals(cookie.getValue())) {
                    
                    // Tìm thông tin đơn hàng từ cookie khác
                    for (Cookie infoCookie : cookies) {
                        if ("lastOrderInfo".equals(infoCookie.getName())) {
                            String[] parts = infoCookie.getValue().split("\\|");
                            if (parts.length >= 3 && orderId.equals(parts[0])) {
                                // Tạo đơn hàng cơ bản từ cookie
                                Order order = new Order();
                                order.setId(parts[0]);
                                order.setTotalAmount(Long.parseLong(parts[1]));
                                order.setStatus(parts[2]);
                                return order;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private void handleCancelOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String orderId = request.getParameter("orderId");
        
        Order order = findOrderInSession(session, orderId);
        if (order != null && "PENDING".equals(order.getStatus())) {
            order.setStatus("CANCELLED");
            request.setAttribute("successMessage", "Đơn hàng đã được hủy thành công!");
        } else {
            request.setAttribute("errorMessage", "Không thể hủy đơn hàng này!");
        }
        
        request.setAttribute("order", order);
        request.getRequestDispatcher("views/order-confirmation.jsp").forward(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Order Confirmation Controller - Displays order confirmation";
    }
}