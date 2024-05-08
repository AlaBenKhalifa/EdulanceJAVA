package com.example.edulancejava.Controller.Course_Gestion;

public class Course {
    private int id;
    private int ownerId;
    private Integer idExpertId;
    private String title;
    private String thumbnail;
    private String category;
    private double rate;
    private boolean state;
    private String description;
    private String language;
    private double price;
    private double discount;

    public Course() {
    }

    public Course(int id, int ownerId, Integer idExpertId, String title, String thumbnail, String category,
                  double rate, boolean state, String description, String language, double price, double discount) {
        this.id = id;
        this.ownerId = ownerId;
        this.idExpertId = idExpertId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.category = category;
        this.rate = rate;
        this.state = state;
        this.description = description;
        this.language = language;
        this.price = price;
        this.discount = discount;
    }

    public Course(String description) {
        this.description=description;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getIdExpertId() {
        return idExpertId;
    }

    public void setIdExpertId(Integer idExpertId) {
        this.idExpertId = idExpertId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", idExpertId=" + idExpertId +
                ", title='" + title + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", category='" + category + '\'' +
                ", rate=" + rate +
                ", state=" + state +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }
}
