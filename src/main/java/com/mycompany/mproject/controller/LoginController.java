package com.mycompany.mproject.controller;

import com.mycompany.mproject.model.User;
import com.mycompany.mproject.services.AuthService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginController", urlPatterns = {"/Login"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //validate check null,...
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "null");
            request.getRequestDispatcher("views/login.jsp").forward(request, response);
            return;
        }

        //call service check login
        AuthService authService = new AuthService();
        User userLogin = authService.checkLogin(username, password);

        if (userLogin != null) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("userLogin", userLogin);

            //cookie
            Cookie cookie = new Cookie("userLogin", userLogin.getName());
            cookie.setMaxAge(7);

            response.addCookie(cookie);

            request.getRequestDispatcher("Home").forward(request, response);

        } else {
            request.setAttribute("error", "fail");
            request.getRequestDispatcher("views/login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
