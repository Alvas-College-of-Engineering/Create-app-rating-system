package com.apprating.controller;

import com.apprating.dao.ApplicationDAO;
import com.apprating.dao.RatingDAO;
import com.apprating.dao.UserDAO;
import com.apprating.model.Application;
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

@WebServlet("/api/admin")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private ApplicationDAO applicationDAO;
    private RatingDAO ratingDAO;

    public AdminServlet() {
        this.userDAO = new UserDAO();
        this.applicationDAO = new ApplicationDAO();
        this.ratingDAO = new RatingDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"admin".equals(user.getRole())) {
            response.setStatus(403);
            response.getWriter().write(JsonResponse.error("Admin access required"));
            return;
        }

        String section = request.getParameter("section");
        if (section == null) section = "dashboard";

        Map<String, Object> data = new HashMap<>();
        data.put("section", section);

        switch (section) {
            case "users":
                List<User> users = userDAO.getAllUsers();
                data.put("users", users);
                break;
            case "applications":
                List<Application> apps = applicationDAO.getAllApplications();
                data.put("apps", apps);
                break;
            case "ratings":
                List<Rating> ratings = ratingDAO.getAllRatings();
                data.put("ratings", ratings);
                break;
            default:
                int totalUsers = userDAO.getUserCount();
                int totalApps = applicationDAO.getApplicationCount();
                int totalRatings = ratingDAO.getTotalRatingCount();
                double overallAvg = ratingDAO.getOverallAverageRating();
                data.put("totalUsers", totalUsers);
                data.put("totalApps", totalApps);
                data.put("totalRatings", totalRatings);
                data.put("overallAvg", overallAvg);
                break;
        }

        response.getWriter().write(JsonResponse.success(data));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"admin".equals(user.getRole())) {
            response.setStatus(403);
            response.getWriter().write(JsonResponse.error("Admin access required"));
            return;
        }

        String action = request.getParameter("action");

        if ("deleteRating".equals(action)) {
            int ratingId = Integer.parseInt(request.getParameter("ratingId"));
            ratingDAO.deleteRating(ratingId);
            response.getWriter().write(JsonResponse.success("Rating deleted successfully"));
        } else if ("addApp".equals(action)) {
            String appName = request.getParameter("appName");
            String developerName = request.getParameter("developerName");
            String category = request.getParameter("category");
            String version = request.getParameter("version");
            String description = request.getParameter("description");
            String releaseDate = request.getParameter("releaseDate");
            Application app = new Application(appName, developerName, category,
                                            version, description, releaseDate, "active");
            applicationDAO.addApplication(app);
            response.getWriter().write(JsonResponse.success("Application added successfully"));
        } else if ("deleteApp".equals(action)) {
            int appId = Integer.parseInt(request.getParameter("appId"));
            applicationDAO.deleteApplication(appId);
            response.getWriter().write(JsonResponse.success("Application deleted successfully"));
        } else {
            response.getWriter().write(JsonResponse.error("Invalid action"));
        }
    }
}
