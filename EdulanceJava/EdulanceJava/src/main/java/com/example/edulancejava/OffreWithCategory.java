package com.example.edulancejava;

import com.example.edulancejava.Connectors.Entities.Offre;

public class OffreWithCategory {
    private Offre offre;
    private int categoryId;
    private String selectedCategoryTitle;

    // Constructor
    public OffreWithCategory(Offre offre, int categoryId, String selectedCategoryTitle) {
        this.offre = offre;
        this.categoryId = categoryId;
        this.selectedCategoryTitle = selectedCategoryTitle;
    }

    // Getters
    public Offre getOffre() {
        return offre;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getSelectedCategoryTitle() {
        return selectedCategoryTitle;
    }
}
