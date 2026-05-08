package com.apprating.controller;

import com.apprating.dao.UserDAO;
import com.apprating.model.User;
import com.apprating.util.JsonResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public RegisterServlet() {
        this.userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || email == null || password == null || confirmPassword == null ||
            username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            response.getWriter().write(JsonResponse.error("All fields are required"));
            return;
        }

        if (!password.equals(confirmPassword)) {
            response.getWriter().write(JsonResponse.error("Passwords do not match"));
            return;
        }

        if (password.length() < 6) {
            response.getWriter().write(JsonResponse.error("Password must be at least 6 characters"));
            return;
        }

        if (userDAO.isUsernameExists(username)) {
            response.getWriter().write(JsonResponse.error("Username already exists"));
            return;
        }

        if (userDAO.isEmailExists(email)) {
            response.getWriter().write(JsonResponse.error("Email already registered"));
            return;
        }

        User user = new User(username, email, password, "user");
        boolean registered = userDAO.registerUser(user);

        if (registered) {
            Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("email", email);
            response.getWriter().write(JsonResponse.success("Registration successful! Please login.", data));
        } else {
            response.getWriter().write(JsonResponse.error("Registration failed. Please try again."));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JsonResponse.error("Method not allowed"));
    }
}
