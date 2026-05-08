package com.apprating.controller;

import com.apprating.dao.RatingDAO;
import com.apprating.model.Rating;
import com.apprating.model.User;
import com.apprating.util.JsonResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/api/history")
public class HistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RatingDAO ratingDAO;

    public HistoryServlet() {
        this.ratingDAO = new RatingDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.setStatus(401);
            response.getWriter().write(JsonResponse.error("Not authenticated"));
            return;
        }

        List<Rating> ratingHistory = ratingDAO.getRatingsByUserId(user.getUserId());

        Map<String, Object> data = new HashMap<>();
        data.put("ratings", ratingHistory);
        data.put("username", user.getUsername());

        response.getWriter().write(JsonResponse.success(data));
    }
}
