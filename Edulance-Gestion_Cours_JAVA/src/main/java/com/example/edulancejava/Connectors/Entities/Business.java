package com.example.edulancejava.Connectors.Entities;

public class Business extends GlobalUser {

    private String webSite;

    public Business(){

    }
    public Business(String name, String familyName, String image, Integer phoneNumber, String email, String nationality, String language, String description, String password) {
        super(name,familyName,image,phoneNumber,email,nationality,language,description,password);
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
}
