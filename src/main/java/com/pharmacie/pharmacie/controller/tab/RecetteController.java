package com.pharmacie.pharmacie.controller.tab;

import com.pharmacie.pharmacie.components.Finance;
import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.components.Vendu;
import com.pharmacie.pharmacie.model.AchatModel;
import com.pharmacie.pharmacie.model.MedicamentModel;
import com.pharmacie.pharmacie.model.RecetteModel;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.util.Locale;


public class RecetteController {
    @FXML
    private TableView<Vendu> plusVenduTab;

    @FXML
    private TableColumn<Vendu, String> numeroColonne;

    @FXML
    private TableColumn<Vendu, String> medocColonne;

    @FXML
    private TableColumn<Vendu, Integer> nombreColonne;

    @FXML
    private TextField recetteTotal;

    @FXML
    public void initialize() {
        // Initialize the columns
        numeroColonne.setCellValueFactory(new PropertyValueFactory<>("numero"));
        medocColonne.setCellValueFactory(new PropertyValueFactory<>("medoc"));
        nombreColonne.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        plusVenduTab.setRowFactory(tv -> {
            TableRow<Vendu> row = new TableRow<>() {
                @Override
                protected void updateItem(Vendu item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setPrefHeight(25);
                        setOpacity(0);
                    } else {
                        setPrefHeight(25);
                        setOpacity(0.8);
                    }
                }
            };

            return row;
        });

        loadVenduData();
        loadRecetteTotal();

        RecetteModel.getInstance().getRecetteList().addListener((ListChangeListener<Vendu>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasUpdated() || change.wasReplaced()) {
                    plusVenduTab.setItems(RecetteModel.getInstance().getRecetteList());
                    loadVenduData();
                }
            }
        });

        AchatModel.getInstance().getCombinedAchatListe().addListener((ListChangeListener<CombinedAchat>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasUpdated() || change.wasReplaced()) {
                    plusVenduTab.setItems(RecetteModel.getInstance().getRecetteList());
                    loadVenduData();
                    loadRecetteTotal();
                }
            }
        });
    }
    private void loadVenduData() {
        ObservableList<Vendu> venduList = null;
        try {
            Vendu vd = new Vendu();
            venduList = vd.medicamentsPlusVendu();
        }  catch (Exception e) {
            e.printStackTrace();
        }finally {
            plusVenduTab.setItems(venduList);
        }
    }

    private void loadRecetteTotal(){
        Finance F = new Finance();
        int res = F.recetteTotal();
        NumberFormat rec = NumberFormat.getNumberInstance(Locale.US);
        String total = rec.format(res);
        recetteTotal.setText("Ar "+total);
    }


}
