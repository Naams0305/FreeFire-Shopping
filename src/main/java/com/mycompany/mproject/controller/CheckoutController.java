package com.mycompany.mproject.controller;

import com.mycompany.mproject.model.User;
import com.mycompany.mproject.model.Cart;
import com.mycompany.mproject.model.CartItem;
import com.mycompany.mproject.model.Order;
import com.mycompany.mproject.model.OrderItem;
import com.mycompany.mproject.data.ProductData;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

/**
 * CheckoutController - Xử lý thanh toán và đặt hàng
 */
@WebServlet(name = "CheckoutController", urlPatterns = {"/Checkout"})
public class CheckoutController extends HttpServlet {
    
    private static final int SHIPPING_FEE = 30000; // Phí ship cố định
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User userLogin = (User) session.getAttribute("userLogin");
        
        if (userLogin == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("errorMessage", "Giỏ hàng trống!");
            response.sendRedirect(request.getContextPath() + "/Cart");
            return;
        }
        
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("views/checkout.jsp").forward(request, response);
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
        
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("errorMessage", "Giỏ hàng trống!");
            response.sendRedirect(request.getContextPath() + "/Cart");
            return;
        }
        
        try {
            // Lấy thông tin từ form
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String city = request.getParameter("city");
            String district = request.getParameter("district");
            String notes = request.getParameter("notes");
            String paymentMethod = request.getParameter("paymentMethod");
            
            // Validate thông tin bắt buộc
            if (fullName == null || phone == null || email == null || 
                address == null || city == null || district == null || paymentMethod == null ||
                fullName.trim().isEmpty() || phone.trim().isEmpty() || email.trim().isEmpty() ||
                address.trim().isEmpty() || city.trim().isEmpty() || district.trim().isEmpty()) {
                
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin!");
                request.setAttribute("cart", cart);
                request.getRequestDispatcher("views/checkout.jsp").forward(request, response);
                return;
            }
            
            // Kiểm tra tồn kho một lần nữa
            if (!checkStockAvailability(cart)) {
                request.setAttribute("errorMessage", "Một số sản phẩm không đủ hàng!");
                request.setAttribute("cart", cart);
                request.getRequestDispatcher("views/checkout.jsp").forward(request, response);
                return;
            }
            
            // Tạo đơn hàng
            Order order = createOrder(userLogin, cart, fullName, phone, email, 
                                    address, city, district, notes, paymentMethod);
            
            // Lưu đơn hàng vào session và cookie
            saveOrderToSession(session, order);
            saveOrderToCookie(response, order);
            
            // Cập nhật tồn kho
            updateProductStock(cart);
            
            // Xóa giỏ hàng sau khi đặt hàng thành công
            session.removeAttribute("cart");
            
            // Chuyển đến trang xác nhận đơn hàng
            response.sendRedirect(request.getContextPath() + "/OrderConfirmation?orderId=" + order.getId());
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý đơn hàng: " + e.getMessage());
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("views/checkout.jsp").forward(request, response);
        }
    }
    
    private boolean checkStockAvailability(Cart cart) {
        for (CartItem item : cart.getItems()) {
            var product = ProductData.getProductById(item.getProductId());
            if (product == null || product.getQuantity() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }
    
    private Order createOrder(User user, Cart cart, String fullName, String phone, String email,
                            String address, String city, String district, String notes, String paymentMethod) {
        
        Order order = new Order();
        order.setId(generateOrderId());
        order.setUserId(user.getId());
        order.setOrderDate(new Date());
        order.setStatus("PENDING"); // Đang chờ xử lý
        
        // Thông tin giao hàng
        order.setShippingName(fullName);
        order.setShippingPhone(phone);
        order.setShippingEmail(email);
        order.setShippingAddress(address + ", " + district + ", " + city);
        order.setNotes(notes);
        
        // Thông tin thanh toán
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(paymentMethod.equals("cod") ? "PENDING" : "PAID");
        
        // Tính toán tổng tiền
        long subtotal = cart.getTotalAmount();
        order.setSubtotal(subtotal);
        order.setShippingFee(SHIPPING_FEE);
        order.setTotalAmount(subtotal + SHIPPING_FEE);
        
        // Tạo danh sách OrderItem từ CartItem
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setProductImage(cartItem.getProductImage());
            orderItem.setPrice(cartItem.getProductPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        
        return order;
    }
    
    private String generateOrderId() {
        // Tạo mã đơn hàng theo format: OTT + timestamp
        return "OTT" + System.currentTimeMillis();
    }
    
    private void saveOrderToSession(HttpSession session, Order order) {
        // Lưu đơn hàng vào session
        session.setAttribute("currentOrder", order);
        
        // Lưu lịch sử đơn hàng vào session
        List<Order> orderHistory = (List<Order>) session.getAttribute("orderHistory");
        if (orderHistory == null) {
            orderHistory = new ArrayList<>();
        }
        orderHistory.add(order);
        session.setAttribute("orderHistory", orderHistory);
    }
    
    private void saveOrderToCookie(HttpServletResponse response, Order order) {
        // Lưu ID đơn hàng vào cookie (có thể mở rộng để lưu nhiều đơn hàng)
        Cookie orderCookie = new Cookie("lastOrderId", order.getId());
        orderCookie.setMaxAge(60 * 60 * 24 * 30); // 30 ngày
        orderCookie.setPath("/");
        response.addCookie(orderCookie);
        
        // Lưu thông tin đơn hàng cơ bản vào cookie
        Cookie orderInfoCookie = new Cookie("lastOrderInfo", 
            order.getId() + "|" + order.getTotalAmount() + "|" + order.getStatus());
        orderInfoCookie.setMaxAge(60 * 60 * 24 * 7); // 7 ngày
        orderInfoCookie.setPath("/");
        response.addCookie(orderInfoCookie);
    }
    
    private void updateProductStock(Cart cart) {
        // Cập nhật số lượng tồn kho của sản phẩm
        for (CartItem item : cart.getItems()) {
            ProductData.updateProductQuantity(item.getProductId(), -item.getQuantity());
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Checkout Controller - Handles order processing and payment";
    }
}