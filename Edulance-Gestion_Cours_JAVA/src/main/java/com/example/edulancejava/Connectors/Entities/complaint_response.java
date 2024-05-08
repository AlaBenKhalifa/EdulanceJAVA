package com.example.edulancejava.Connectors.Entities;

import java.util.Date;

public class complaint_response {
    private int id;
    private int complaintId;
    private Date date;
    private static String response;
    private String status;



    // Constructors
    public complaint_response() {
    }

    public complaint_response(int id, int complaintId, Date date, String response, String status) {
        this.id = id;
        this.complaintId = complaintId;
        this.date = date;
        this.response = response;
        this.status=status;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static String getResponse() {
        return response;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }



    public void setResponse(String response) {
        this.response = response;
    }





    // toString method
    @Override
    public String toString() {
        return "ComplaintResponse{" +
                "id=" + id +
                "status" + status +
                ", complaintId=" + complaintId +
                ", date=" + date +
                ", response='" + response + '\'' +

                '}';
    }
}

