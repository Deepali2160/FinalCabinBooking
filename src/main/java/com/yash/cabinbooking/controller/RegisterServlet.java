package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.model.User;
import com.yash.cabinbooking.service.UserService;
import com.yash.cabinbooking.serviceimpl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "RegisterServlet",
        urlPatterns = {"/register"}
)
public class RegisterServlet extends HttpServlet {
    private UserService userService = new UserServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User(name, email, password);

        if (userService.isEmailExists(email)) {
            request.setAttribute("error", "Email already exists");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } else {
            if (userService.registerUser(user)) {
                response.sendRedirect("login.jsp?registration=success");
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        }
    }
}