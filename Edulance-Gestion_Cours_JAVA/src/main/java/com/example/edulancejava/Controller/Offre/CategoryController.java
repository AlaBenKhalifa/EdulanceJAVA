package com.example.edulancejava.Controller.Offre;

import com.example.edulancejava.Connectors.DeleteCategoryQuery;
import com.example.edulancejava.Connectors.Entities.Category;
import com.example.edulancejava.Connectors.GetCategoriesQuery;
import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML
    private TableView<Category> categoryTableView;

    @FXML
    private TableColumn<Category, Integer> idColumn;

    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TableColumn<Category, Void> editColumn;

    @FXML
    private TableColumn<Category, Void> deleteColumn;

    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        loadCategories();
    }

    private void initTable() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        categoryTableView.setItems(categoryList);

        editColumn.setCellFactory(param -> new TableCell<Category, Void>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    handleEditButtonAction(event, category);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<Category, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    handleDeleteButtonAction(event, category);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event, Category categoryToDelete) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this Category?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                DeleteCategoryQuery query = new DeleteCategoryQuery();
                query.deleteCategory(categoryToDelete.getId(), result -> {
                    if (result) {
                        categoryList.remove(categoryToDelete);
                    } else {
                        showAlert("Cannot delete category. Offers are associated with it.");
                    }
                });
            }
        });
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
    private void handleEditButtonAction(ActionEvent event, Category value) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/EditCategory.fxml"));
        try {
            Parent root = loader.load();
            EditCategoryController editCategoryController = loader.getController();
            editCategoryController.initData(value);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void loadCategories() {
        try {
            GetCategoriesQuery query = new GetCategoriesQuery();
            List<Category> categories = query.getCategories();
            categoryList.addAll(categories);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void switchToAnotherViewaddcategory(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/category.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToAnotherViewListJobAdmin(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/DisplayJobAdmin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void switchToAnotherViewadd(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/addcategory.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCategory(Category category) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "UPDATE categorie SET titre = ? WHERE id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, category.getTitre());
            statement.setInt(2, category.getId()); // Assuming you have an ID field in Offre class

            statement.executeUpdate();

            System.out.println("Category updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating category: " + e.getMessage());
        }
    }

}
