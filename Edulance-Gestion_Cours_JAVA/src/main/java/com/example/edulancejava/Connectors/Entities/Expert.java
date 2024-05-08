package com.example.edulancejava.Connectors.Entities;


public class Expert extends NormalUser  {

    public Expert(String name, String familyName, String image, Integer phoneNumber, String email, String nationality, String language, String description, String password) {
        super(name,familyName,image,phoneNumber,email,nationality,language,description,password);
    }

    public Expert() {

    }
}
