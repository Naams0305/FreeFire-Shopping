<%-- 
    Document   : home
    Created on : Jun 5, 2025, 1:37:49 PM
    Author     : Administrator
--%>

<%@page import="com.mycompany.mproject.model.User"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Lấy user từ request (Servlet đã set trước đó)
    User userLogin = (User) request.getAttribute("userLogin");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang chủ - OtterShop</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/HomeCSS.css">
    </head>
    <body>
        <div class="header">

            <div class="text-header">
                <img src="images/otter.png" alt="alt"/>
                <h1>OtterShop </h1>
            </div>

            <div class="menu">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/Home" class="active">Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/Product">Danh sách sản phẩm</a></li>
                    <li><a href="${pageContext.request.contextPath}/Cart">Giỏ hàng</a></li>
                </ul>
            </div>
        </div>

        <div class="content">
            <div>
                <h1>Chào mừng <%= userLogin.getName()%> đến với OtterShop .</h1>
                <img src="images/chitay.png" alt="">
            </div>
        </div>
    </body>
</html>
