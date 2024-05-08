package com.example.edulancejava.Connectors.Entities;
public class NormalUser extends GlobalUser {


    private String CV;


    private String experienceLevel;


    private int score;

    public NormalUser(String name, String familyName, String image, Integer phoneNumber, String email, String nationality, String language, String description, String password) {
        super(name,familyName,image,phoneNumber,email,nationality,language,description,password);
    }



    public NormalUser() {
        super();
    }

    public String getCV() {
        return CV;
    }



    public void setCV(String CV) {
        this.CV = CV;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

