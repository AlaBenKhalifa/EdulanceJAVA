package com.example.edulance.Entities;
public class Admin extends Expert {
    public Admin(String name, String familyName, String image, Integer phoneNumber, String email, String nationality, String language, String description, String password) {
        super(name,familyName,image,phoneNumber,email,nationality,language,description,password);
    }

    public Admin(){
        super();
    }
}

