package com.example.edulancejava.Connectors.Entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.converter.IntegerStringConverter;

public class GlobalUser {

    private Integer id;



    private String name;
    private String familyName;
    private String image;
    private Integer phoneNumber;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    private String nationality;
    private String language;
    private Integer reputation;
    private String description;
    private String password;
    private String archieve;

    public String getFamilyName() {
        return familyName;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GlobalUser() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }


    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public Integer getReputation() {
        return reputation;
    }
    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getArchieve() {
        return archieve;
    }

    public GlobalUser(String name, String familyName, String image, Integer phoneNumber, String email, String nationality, String language, String description, String password) {
        this.name = name;
        this.familyName = familyName;
        this.image = image;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nationality = nationality;
        this.language = language;
        this.reputation = 0;
        this.description = description;
        this.password = password;
        archieve = "Active";
    }

    public void setArchieve(String archieve) {
        this.archieve = archieve;
    }
}
