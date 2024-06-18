package com.pharmacie.pharmacie.controller.modif;

import com.pharmacie.pharmacie.components.Achat;
import com.pharmacie.pharmacie.components.Entree;
import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.components.Pdf;
import com.pharmacie.pharmacie.controller.AchatWindow;
import com.pharmacie.pharmacie.controller.tab.CombinedAchat;
import com.pharmacie.pharmacie.model.AchatModel;
import com.pharmacie.pharmacie.model.MedicamentModel;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AchatModif {
    @FXML
    private TextField numAchatField;

    @FXML
    private TextField nomClientField;

    @FXML
    private DatePicker dateAchatField;

    @FXML
    private ComboBox medicamentField;

    @FXML
    private TextField nombreField;

    @FXML
    private VBox medicamentVBox;
    @FXML
    private HBox initialHBox;
    @FXML
    private Button modifyachat;

    private static AchatModif instance;

    public AchatModif() {
        instance = this;
    }

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
    public static void setAchatData(CombinedAchat achat) {
        if (instance != null && achat != null) {
            instance.numAchatField.setText(achat.getNumAchat());
            instance.nomClientField.setText(achat.getNomClient());
            instance.dateAchatField.setValue(LocalDate.parse(String.valueOf(achat.getDateAchat())));
            instance.medicamentVBox.getChildren().clear();
            instance.medicamentVBox.getChildren().add(instance.initialHBox);

            List<CombinedAchat.MedicamentInfo> medicaments = achat.getMedicaments();

            List<String> allMedicaments = Entree.getAllNumMedoc();


            if (!medicaments.isEmpty()) {
                // Placer le premier élément dans initialHBox
                CombinedAchat.MedicamentInfo firstMedInfo = medicaments.get(0);
                ComboBox<String> initialComboBox = (ComboBox<String>) instance.initialHBox.getChildren().get(0);
                initialComboBox.getItems().addAll(allMedicaments);
                initialComboBox.setValue(firstMedInfo.getNumMedoc());
                ((TextField) instance.initialHBox.getChildren().get(1)).setText(String.valueOf(firstMedInfo.getNbr()));

                // Ajouter les autres éléments dans de nouveaux HBox
                for (int i = 1; i < medicaments.size(); i++) {
                    CombinedAchat.MedicamentInfo medInfo = medicaments.get(i);

                    HBox hbox = new HBox(5);
                    ComboBox<String> medField = new ComboBox<>();
                    medField.getItems().addAll(allMedicaments);
                    medField.setPrefHeight(25.0);
                    medField.setPrefWidth(200);
                    medField.setEditable(true);
                    medField.setValue(medInfo.getNumMedoc());

                    TextField nbrField = new TextField(String.valueOf(medInfo.getNbr()));
                    nbrField.setPrefHeight(25.0);
                    nbrField.setPrefWidth(40);
                    nbrField.setEditable(true);

                    Button deleteButton = new Button("-");
                    deleteButton.setTranslateX(5);
                    deleteButton.setPrefWidth(25);
                    deleteButton.setOnAction(e -> instance.medicamentVBox.getChildren().remove(hbox));

                    hbox.getChildren().addAll(medField, nbrField, deleteButton);
                    instance.medicamentVBox.getChildren().add(hbox);
                }
            }
        } else {
            System.err.println("One or more fields are null");
        }
    }


    @FXML
    private void modifyAchat(ActionEvent event) {
        try {
            String numAchat = numAchatField.getText();
            String nomClient = nomClientField.getText();
            LocalDate dateAchat = dateAchatField.getValue();

            Achat achatsup = new Achat();
            achatsup.supprimerAchat(numAchat);

            if(medicamentField.getValue()!=null && nombreField.getText()!=null){
                Achat achat = new Achat(numAchat, (String) medicamentField.getValue(), nomClient, Integer.parseInt(nombreField.getText()), dateAchat);
                achat.ajouterAchat(achat);
            }

            List<AchatWindow.MedicamentQuantity> medicaments = new ArrayList<>();
            for (int i = 1; i < medicamentVBox.getChildren().size(); i++) { // Skip the initial HBox
                HBox hBox = (HBox) medicamentVBox.getChildren().get(i);
                ComboBox<String> medField = (ComboBox<String>) hBox.getChildren().get(0);
                TextField nbrField = (TextField) hBox.getChildren().get(1);
                medicaments.add(new AchatWindow.MedicamentQuantity(medField.getValue(), Integer.parseInt(nbrField.getText())));
            }

            for (AchatWindow.MedicamentQuantity medicament : medicaments) {
                String numMedoc = medicament.getNumMedoc();
                int nbr = medicament.getNbr();
                try {
                    Achat achat = new Achat(numAchat, numMedoc, nomClient, nbr, dateAchat);
                    achat.ajouterAchat(achat);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Achat modifié avec succès!");
            alert.showAndWait();

            CombinedAchat newAchat = new CombinedAchat(numAchat, nomClient, dateAchat);
            AchatModel.getInstance().addAchat(newAchat);

            clearFields();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Veuiller entrer des donnees corectes");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout de l'Achat");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    void supprimerAchat(ActionEvent event) {
        try {
            String numAchat = numAchatField.getText();
            // Assuming you have a delete method in your Medicament class
            Achat achat = new Achat();
            achat.supprimerAchat(numAchat);

            CombinedAchat newAchat = new CombinedAchat(null,null,null);
            AchatModel.getInstance().addAchat(newAchat);

            clearFields();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Achat supprimé avec succès!");
            alert.showAndWait();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFields() {
        numAchatField.clear();
        nomClientField.clear();
        medicamentVBox.getChildren().clear();
        medicamentField.setValue(null);
    }

    @FXML
    private void handleAddMedicament() {
        String medicament = (String) medicamentField.getValue();
        String nombre = nombreField.getText();

        if (!medicament.isEmpty() && !nombre.isEmpty()) {
            HBox newHBox = new HBox();
            newHBox.setTranslateY(2);

            ComboBox<String> newMedicamentField = new ComboBox<>();
            newMedicamentField.getItems().addAll(Entree.getAllNumMedoc());
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
    private ImageView pdf;

    @FXML
    private void genererPdf(MouseEvent event){
        Pdf pdf = new Pdf();
        pdf.creerFacture(numAchatField.getText());
    }
}
