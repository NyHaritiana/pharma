package com.pharmacie.pharmacie.controller.modif;

import com.pharmacie.pharmacie.components.Entree;
import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.model.EntreeModel;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.pharmacie.pharmacie.model.MedicamentModel;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StockModif {
    @FXML
    private TextField numentreeField;

    @FXML
    private ComboBox<String> numMedocentreeField;

    @FXML
    private TextField nombreentreeField;

    @FXML
    private DatePicker dateentreeField;

    @FXML
    private Button majstock;

    @FXML
    private Button deletestock;


    private static StockModif instance;

    public StockModif() {
        instance = this;
    }

    @FXML
    public void initialize() {
        List<String> numMedocList = Entree.getAllNumMedoc();
        numMedocentreeField.getItems().addAll(numMedocList);

        MedicamentModel.getInstance().getMedicamentList().addListener((ListChangeListener<Medicament>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Medicament addedMedicament : change.getAddedSubList()) {
                        numMedocentreeField.getItems().add(addedMedicament.getNumMedoc());
                    }
                }
            }
        });
    }

    // Method to set the medicament data in the form
    public static void setEntreeData(Entree entree) {
        if (instance != null && entree != null) {
            instance.numentreeField.setText(entree.getNumEntree());
            instance.numMedocentreeField.setValue(entree.getNumMedoc());
            instance.nombreentreeField.setText(String.valueOf(entree.getStockEntree()));
            LocalDate localDateEntree = entree.getDateEntree();
            if (localDateEntree != null) {
                instance.dateentreeField.setValue(localDateEntree);
            }
        } else {
            System.err.println("One or more fields are null");
        }
    }

    @FXML
    void ModifyStock(ActionEvent event) {
        try {
            String numEntree = numentreeField.getText();
            String numMedoc = numMedocentreeField.getValue();
            int nombreEntree = Integer.parseInt(nombreentreeField.getText());
            LocalDate dateEntree = dateentreeField.getValue();

            Entree entree = new Entree(numEntree, numMedoc, nombreEntree,dateEntree);
            entree.updateEntree(entree);

            // Add the new medicament to the shared model
            EntreeModel.getInstance().addEntree(entree);

            // Clear the form fields
            clearFields();


            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Medicament modified successfully!");
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

    @FXML
    void SupprimerStock(ActionEvent event) {
        try {
            String numEntree = numentreeField.getText();
            Entree med = new Entree();
            med.deleteEntree(numEntree);

            // Add the new medicament to the shared model
            Entree entree = new Entree();
            EntreeModel.getInstance().addEntree(entree);

            clearFields();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Medicament deleted successfully!");
            alert.showAndWait();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // Method to clear all form fields
    private void clearFields() {
        numentreeField.clear();
        nombreentreeField.clear();
    }
}
