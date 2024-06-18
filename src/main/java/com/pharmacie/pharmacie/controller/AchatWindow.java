package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.components.Achat;
import com.pharmacie.pharmacie.components.Entree;
import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.controller.tab.CombinedAchat;
import com.pharmacie.pharmacie.model.AchatModel;
import com.pharmacie.pharmacie.model.MedicamentModel;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AchatWindow {

    @FXML
    public TextField nomClientField;
    @FXML
    public DatePicker dateAchatField;

    @FXML
    private TextField numAchatField;

    @FXML
    private Button ajachat;

    @FXML
    private Button cancelachat;

    @FXML
    private VBox medicamentVBox;
    @FXML
    private ComboBox medicamentField;
    @FXML
    private TextField nombreField;

    @FXML
    public void initialize() {
        List<String> numMedocList = Entree.getAllNumMedoc();
        medicamentField.getItems().addAll(numMedocList);

        MedicamentModel.getInstance().getMedicamentList().addListener((ListChangeListener<Medicament>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Medicament addedMedicament : change.getAddedSubList()) {
                        medicamentField.getItems().add(addedMedicament.getNumMedoc());
                    }
                }
            }
        });
    }

    @FXML
    private void handleAddMedicament() {
        String medicament = (String) medicamentField.getValue();
        String nombre = nombreField.getText();

        List<String> allMedicaments = Entree.getAllNumMedoc();

        if (!medicament.isEmpty() && !nombre.isEmpty()) {
            HBox newHBox = new HBox();
            newHBox.setTranslateY(2);

            ComboBox<String> newMedicamentField = new ComboBox<>();
            newMedicamentField.getItems().addAll(allMedicaments);
            newMedicamentField.setPrefHeight(25.0);
            newMedicamentField.setPrefWidth(200);
            newMedicamentField.setEditable(true);
            newMedicamentField.setValue(medicament);

            TextField newNombreField = new TextField(nombre);
            newNombreField.setTranslateX(2);
            newNombreField.setStyle("-fx-max-width: 40px;");
            newNombreField.setEditable(true);

            Button deleteButton = new Button("-");
            deleteButton.setTranslateX(5);
            deleteButton.setPrefWidth(25);
            deleteButton.setOnAction(e -> medicamentVBox.getChildren().remove(newHBox));

            newHBox.getChildren().addAll(newMedicamentField, newNombreField, deleteButton);
            medicamentVBox.getChildren().add(newHBox);

            // Clear the initial text fields for new input
            medicamentField.setValue(null);
            nombreField.clear();
        }
    }

    @FXML
    private void handleAddAchat(ActionEvent event) {
        String numAchat = numAchatField.getText();
        String nomClient = nomClientField.getText();
        LocalDate dateAchat = dateAchatField.getValue();

        if(medicamentField.getValue()!=null && nombreField.getText()!=null){
            Achat achat = new Achat(numAchat, (String) medicamentField.getValue(), nomClient, Integer.parseInt(nombreField.getText()), dateAchat);
            achat.ajouterAchat(achat);
        }

        List<MedicamentQuantity> medicaments = new ArrayList<>();
        for (int i = 1; i < medicamentVBox.getChildren().size(); i++) {
            HBox hBox = (HBox) medicamentVBox.getChildren().get(i);
            ComboBox<String> medField = (ComboBox<String>) hBox.getChildren().get(0);
            TextField nbrField = (TextField) hBox.getChildren().get(1);
            medicaments.add(new MedicamentQuantity(medField.getValue(), Integer.parseInt(nbrField.getText())));
        }

        for (MedicamentQuantity medicament : medicaments) {
            String numMedoc = medicament.getNumMedoc();
            int nbr = medicament.getNbr();
            try {
                Achat achat = new Achat(numAchat, numMedoc, nomClient, nbr, dateAchat);
                achat.ajouterAchat(achat);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        CombinedAchat newAchat = new CombinedAchat(numAchat, nomClient, dateAchat);
        AchatModel.getInstance().addAchat(newAchat);

        // Clear the form fields
        clearFields();

        // Close the window
        Stage stage = (Stage) ajachat.getScene().getWindow();
        stage.close();
        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Achat ajouté avec succès!");
        alert.showAndWait();

        System.out.println("Achat ajouté avec succès.");
    }

    public static class MedicamentQuantity {
        private final String numMedoc;
        private final int nbr;

        public MedicamentQuantity(String numMedoc, int nbr) {
            this.numMedoc = numMedoc;
            this.nbr = nbr;
        }

        public String getNumMedoc() {
            return numMedoc;
        }

        public int getNbr() {
            return nbr;
        }
    }

    // Method to clear all form fields
    private void clearFields() {
        nomClientField.clear();
        numAchatField.clear();
        nombreField.clear();
        medicamentField.setValue(null);
    }

    // Method to handle canceling the operation
    @FXML
    void Cancel(ActionEvent event) {
        clearFields();

        // Close the window
        Stage stage = (Stage) cancelachat.getScene().getWindow();
        stage.close();
    }

}
