package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.components.Entree;
import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.model.EntreeModel;
import com.pharmacie.pharmacie.model.MedicamentModel;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class StockWindow {
    @FXML
    private TextField numEntreeField;
    @FXML
    private ComboBox<String> numMedocEntreeField;
    @FXML
    private TextField nombreEntreeField;
    @FXML
    private DatePicker dateEntreeField;

    @FXML
    private Button ajstock;

    @FXML
    private Button cancelstock;

    @FXML
    public void initialize() {
        List<String> numMedocList = Entree.getAllNumMedoc();
        numMedocEntreeField.getItems().addAll(numMedocList);

        MedicamentModel.getInstance().getMedicamentList().addListener((ListChangeListener<Medicament>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Medicament addedMedicament : change.getAddedSubList()) {
                        numMedocEntreeField.getItems().add(addedMedicament.getNumMedoc());
                    }
                }
            }
        });
    }

    // Method to handle adding a new entree de stock
    @FXML
    void AddStock(ActionEvent event) {
        try {
            String numEntree = numEntreeField.getText();
            String numMedoc = numMedocEntreeField.getValue();
            int stockEntree = Integer.parseInt(nombreEntreeField.getText());
            LocalDate dateEntree = dateEntreeField.getValue();

            Entree entree = new Entree(numEntree, numMedoc, stockEntree, dateEntree);
            entree.addEntree(entree);

            // Add the new stock to the shared model
            EntreeModel.getInstance().addEntree(entree);

            // Clear the form fields
            clearFields();

            // Close the window
            Stage stage = (Stage) ajstock.getScene().getWindow();
            stage.close();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Entree de Stock added!");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            // Show error message if number format is incorrect
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid numbers for price and quantity.");
            alert.showAndWait();
        } catch (Exception e) {
            // Show error message for other exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the medicament.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    // Method to clear all form fields
    private void clearFields() {
        numEntreeField.clear();
        numEntreeField.clear();
    }

    // Method to handle canceling the operation
    @FXML
    void Cancel(ActionEvent event) {
        clearFields();

        // Close the window
        Stage stage = (Stage) cancelstock.getScene().getWindow();
        stage.close();
    }
}
