package com.apprating.controller;

import com.apprating.dao.ApplicationDAO;
import com.apprating.dao.RatingDAO;
import com.apprating.model.Application;
import com.apprating.model.Rating;
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
import javax.servlet.http.HttpSession;

@WebServlet("/api/ratings")
public class RatingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RatingDAO ratingDAO;
    private ApplicationDAO applicationDAO;

    public RatingServlet() {
        this.ratingDAO = new RatingDAO();
        this.applicationDAO = new ApplicationDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

        String action = request.getParameter("action");

        if ("submit".equals(action)) {
            int appId = Integer.parseInt(request.getParameter("appId"));
            int ratingValue = Integer.parseInt(request.getParameter("rating"));
            String reviewComment = request.getParameter("reviewComment");

            if (ratingValue < 1 || ratingValue > 5) {
                response.getWriter().write(JsonResponse.error("Rating must be between 1 and 5"));
                return;
            }

            Rating rating = new Rating(user.getUserId(), appId, ratingValue, reviewComment);
            boolean success = ratingDAO.addRating(rating);

            if (success) {
                Map<String, Object> data = new HashMap<>();
                data.put("appId", appId);
                data.put("ratingValue", ratingValue);
                response.getWriter().write(JsonResponse.success("Rating submitted successfully!", data));
            } else {
                response.getWriter().write(JsonResponse.error("Failed to submit rating"));
            }
        } else if ("delete".equals(action)) {
            int appId = Integer.parseInt(request.getParameter("appId"));
            ratingDAO.deleteRatingByUserAndApp(user.getUserId(), appId);
            response.getWriter().write(JsonResponse.success("Rating deleted successfully!"));
        } else {
            response.getWriter().write(JsonResponse.error("Invalid action"));
        }
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

        String action = request.getParameter("action");

        if ("rate".equals(action)) {
            int appId = Integer.parseInt(request.getParameter("appId"));
            Application app = applicationDAO.getApplicationById(appId);
            Rating existingRating = ratingDAO.getRatingByUserAndApp(user.getUserId(), appId);

            Map<String, Object> data = new HashMap<>();
            data.put("app", app);
            data.put("existingRating", existingRating);

            response.getWriter().write(JsonResponse.success(data));
        } else {
            response.getWriter().write(JsonResponse.error("Invalid action"));
        }
    }
}
