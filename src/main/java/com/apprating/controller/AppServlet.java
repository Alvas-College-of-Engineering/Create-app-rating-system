package com.apprating.controller;

import com.apprating.dao.ApplicationDAO;
import com.apprating.dao.RatingDAO;
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

@WebServlet("/api/apps")
public class AppServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ApplicationDAO applicationDAO;
    private RatingDAO ratingDAO;

    public AppServlet() {
        this.applicationDAO = new ApplicationDAO();
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

        String appIdParam = request.getParameter("appId");

        if (appIdParam != null) {
            int appId = Integer.parseInt(appIdParam);
            Application app = applicationDAO.getApplicationById(appId);
            List<Rating> ratings = ratingDAO.getRatingsByAppId(appId);
            double avgRating = ratingDAO.calculateAverageRating(appId);
            Rating userRating = ratingDAO.getRatingByUserAndApp(user.getUserId(), appId);

            Map<String, Object> data = new HashMap<>();
            data.put("app", app);
            data.put("ratings", ratings);
            data.put("avgRating", avgRating);
            data.put("userRating", userRating);

            response.getWriter().write(JsonResponse.success(data));
        } else {
            String keyword = request.getParameter("search");
            List<Application> apps;
            if (keyword != null && !keyword.trim().isEmpty()) {
                apps = applicationDAO.searchApplications(keyword);
            } else {
                apps = applicationDAO.getAllApplicationsWithRatings();
            }
            Map<String, Object> data = new HashMap<>();
            data.put("apps", apps);
            data.put("keyword", keyword);
            response.getWriter().write(JsonResponse.success(data));
        }
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

        if ("add".equals(action)) {
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
        } else if ("delete".equals(action)) {
            int appId = Integer.parseInt(request.getParameter("appId"));
            applicationDAO.deleteApplication(appId);
            response.getWriter().write(JsonResponse.success("Application deleted successfully"));
        } else {
            response.getWriter().write(JsonResponse.error("Invalid action"));
        }
    }
}
