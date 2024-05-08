package com.example.edulancejava.Connectors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class DeleteCategoryQuery {
    public void deleteCategory(int categoryId, Consumer<Boolean> callback) {
        MySQLConnectors connection = new MySQLConnectors();
        String checkOfferQuery = "SELECT COUNT(*) FROM offre WHERE id_category_id = ?";
        String deleteCategoryQuery = "DELETE FROM categorie WHERE id = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement checkOfferStatement = conn.prepareStatement(checkOfferQuery);
             PreparedStatement deleteCategoryStatement = conn.prepareStatement(deleteCategoryQuery)) {

            checkOfferStatement.setInt(1, categoryId);
            ResultSet resultSet = checkOfferStatement.executeQuery();
            if (resultSet.next()) {
                int offersCount = resultSet.getInt(1);

                if (offersCount > 0) {
                    callback.accept(false);
                    return;
                }
            }

            deleteCategoryStatement.setInt(1, categoryId);
            int rowsDeleted = deleteCategoryStatement.executeUpdate();

            if (rowsDeleted > 0) {
                callback.accept(true);
            } else {
                callback.accept(false);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting category: " + e.getMessage());
            callback.accept(false);
        }
    }
}