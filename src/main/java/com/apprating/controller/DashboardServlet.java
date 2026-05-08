package com.apprating.controller;

import com.apprating.dao.ApplicationDAO;
import com.apprating.dao.RatingDAO;
import com.apprating.dao.UserDAO;
import com.apprating.model.Application;
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

@WebServlet("/api/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ApplicationDAO applicationDAO;
    private RatingDAO ratingDAO;
    private UserDAO userDAO;

    public DashboardServlet() {
        this.applicationDAO = new ApplicationDAO();
        this.ratingDAO = new RatingDAO();
        this.userDAO = new UserDAO();
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

        List<Application> apps = applicationDAO.getAllApplicationsWithRatings();
        int totalApps = applicationDAO.getApplicationCount();
        int totalRatings = ratingDAO.getTotalRatingCount();
        double overallAvg = ratingDAO.getOverallAverageRating();
        int totalUsers = userDAO.getUserCount();

        Map<String, Object> data = new HashMap<>();
        data.put("apps", apps);
        data.put("totalApps", totalApps);
        data.put("totalRatings", totalRatings);
        data.put("overallAvg", overallAvg);
        data.put("totalUsers", totalUsers);
        data.put("username", user.getUsername());
        data.put("role", user.getRole());

        response.getWriter().write(JsonResponse.success(data));
    }
}
