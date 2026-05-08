package com.apprating.model;

public class Application {
    private int appId;
    private String appName;
    private String developerName;
    private String category;
    private String version;
    private String description;
    private String releaseDate;
    private String status;
    private String createdAt;
    private double averageRating;
    private int totalRatings;

    public Application() {}

    public Application(String appName, String developerName, String category,
                       String version, String description, String releaseDate, String status) {
        this.appName = appName;
        this.developerName = developerName;
        this.category = category;
        this.version = version;
        this.description = description;
        this.releaseDate = releaseDate;
        this.status = status;
    }

    public Application(int appId, String appName, String developerName, String category,
                       String version, String description, String releaseDate,
                       String status, String createdAt) {
        this.appId = appId;
        this.appName = appName;
        this.developerName = developerName;
        this.category = category;
        this.version = version;
        this.description = description;
        this.releaseDate = releaseDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getAppId() { return appId; }
    public void setAppId(int appId) { this.appId = appId; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getDeveloperName() { return developerName; }
    public void setDeveloperName(String developerName) { this.developerName = developerName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public int getTotalRatings() { return totalRatings; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }
}
