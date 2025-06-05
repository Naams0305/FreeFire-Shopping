<%-- 
    Document   : login
    Created on : Jun 5, 2025, 1:50:59 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Start Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/LoginCSS.css"/>
</head>
<body>
    <div class="login">
        <h1>Login</h1>
        <form action="${pageContext.request.contextPath}/Login" method="POST">
            <span>Username: </span><input type="text" name="username" value="" /> <br>
            <span>Password: </span><input type="password" name="password" value="" /> <br>
            <input type="submit" value="Login" />
        </form>
    </div>
</body>
</html>
