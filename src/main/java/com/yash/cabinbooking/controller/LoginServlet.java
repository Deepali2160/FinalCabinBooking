package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.model.User;
import com.yash.cabinbooking.service.UserService;
import com.yash.cabinbooking.serviceimpl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/login"}
)
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userService.loginUser(email, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Use context path for reliable redirects
            String contextPath = request.getContextPath();

            if ("admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(contextPath + "/admin/dashboard.jsp");
            } else {
                response.sendRedirect(contextPath + "/dashboard.jsp");
            }
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}