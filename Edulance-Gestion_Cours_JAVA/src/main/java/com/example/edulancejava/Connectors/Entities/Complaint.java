package com.example.edulancejava.Connectors.Entities;
import java.util.Date;


public class Complaint {
    private int id_reclamation;
    private Date date;
    private boolean seenByUser;
    private GlobalUser user;
    private String type;
    private String description;
    private String status;
    private int priority;
    private String image_path;

    public Complaint(String description, String type) {
        this.description = description;
        this.type = type;
    }

    public Complaint(GlobalUser user, Date date, String type, String description, String status, String priority, String imagePath) {

        this.user = user;
        this.date = date;
        this.type = type;
        this.description = description;
        this.status = status;
        this.priority = Integer.parseInt(priority);
    }

    public Complaint() {
        this.date = new Date();
    }


    @Override
    public String toString() {
        return "Type: " + type + ", Description: " + description + ", Priority: " + priority + ", Date: " + date;
    }

    public boolean isSeenByUser() {
        return seenByUser;
    }

    public void setSeenByUser(boolean seenByUser) {
        this.seenByUser = seenByUser;
    }
    public int getId_reclamation() {
        return id_reclamation;
    }

    public void setId_reclamation(int id_reclamation) {
        this.id_reclamation = id_reclamation;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getImage_path() {
        return image_path;
    }
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }


    public String getImagePath() {
        return image_path;
    }
    public void setImagePath(String imagePath) {
        this.image_path = imagePath;
    }


    public GlobalUser getUser() {
        return user;
    }

    public void setUser(GlobalUser user) {
        this.user = user;
    }

    public int getUserId() {
        if (user != null) {
            return user.getId();
        } else {
            return 4;
        }
    }
}