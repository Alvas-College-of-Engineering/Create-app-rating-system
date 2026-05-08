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

@WebServlet("/api/reviews")
public class ReviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ApplicationDAO applicationDAO;
    private RatingDAO ratingDAO;

    public ReviewServlet() {
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

            Map<String, Object> data = new HashMap<>();
            data.put("app", app);
            data.put("ratings", ratings);
            data.put("avgRating", avgRating);

            response.getWriter().write(JsonResponse.success(data));
        } else {
            response.getWriter().write(JsonResponse.error("appId parameter required"));
        }
    }
}
