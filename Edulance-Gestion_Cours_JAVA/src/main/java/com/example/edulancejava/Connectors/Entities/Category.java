package com.example.edulancejava.Connectors.Entities;

public class Category {
    private Integer id;
    private String titre;

    // Constructor
    public Category() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                '}';
    }
}
