package com.apprating.model;

public class Rating {
    private int ratingId;
    private int userId;
    private int appId;
    private int ratingValue;
    private String reviewComment;
    private String ratedOn;
    private String username;
    private String appName;

    public Rating() {}

    public Rating(int userId, int appId, int ratingValue, String reviewComment) {
        this.userId = userId;
        this.appId = appId;
        this.ratingValue = ratingValue;
        this.reviewComment = reviewComment;
    }

    public Rating(int ratingId, int userId, int appId, int ratingValue,
                  String reviewComment, String ratedOn) {
        this.ratingId = ratingId;
        this.userId = userId;
        this.appId = appId;
        this.ratingValue = ratingValue;
        this.reviewComment = reviewComment;
        this.ratedOn = ratedOn;
    }

    public int getRatingId() { return ratingId; }
    public void setRatingId(int ratingId) { this.ratingId = ratingId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getAppId() { return appId; }
    public void setAppId(int appId) { this.appId = appId; }
    public int getRatingValue() { return ratingValue; }
    public void setRatingValue(int ratingValue) { this.ratingValue = ratingValue; }
    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
    public String getRatedOn() { return ratedOn; }
    public void setRatedOn(String ratedOn) { this.ratedOn = ratedOn; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
}
