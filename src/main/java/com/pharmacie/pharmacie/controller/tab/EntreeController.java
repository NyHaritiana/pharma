package com.pharmacie.pharmacie.controller.tab;

import com.pharmacie.pharmacie.components.Entree;
import com.pharmacie.pharmacie.model.EntreeModel;
import com.pharmacie.pharmacie.controller.modif.StockModif;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class EntreeController {

    @FXML
    private TableView<Entree> entreeTableView;

    @FXML
    private TableColumn<Entree, String> numEntreeColumn;

    @FXML
    private TableColumn<Entree, String> numMedocColumn;

    @FXML
    private TableColumn<Entree, Integer> nombreEntreeColumn;

    @FXML
    private TableColumn<Entree, LocalDate> dateEntreeColumn;


    @FXML
    public void initialize() {
        // Initialize the columns
        numEntreeColumn.setCellValueFactory(new PropertyValueFactory<>("numEntree"));
        numMedocColumn.setCellValueFactory(new PropertyValueFactory<>("numMedoc"));
        nombreEntreeColumn.setCellValueFactory(new PropertyValueFactory<>("stockEntree"));
        dateEntreeColumn.setCellValueFactory(new PropertyValueFactory<>("dateEntree"));

        entreeTableView.setRowFactory(tv -> {
            TableRow<Entree> row = new TableRow<>() {
                @Override
                protected void updateItem(Entree item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setPrefHeight(40);
                        setOpacity(0);
                        setStyle("-fx-border-color: grey; -fx-border-width: 0.2;");
                        setVisible(false);
                        setManaged(false);
                    } else {
                        setPrefHeight(40);
                        setOpacity(0.8);
                        setStyle("-fx-border-color: grey; -fx-border-width: 0.2;");
                        setVisible(true);
                        setManaged(true);
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Entree rowData = row.getItem();
                    StockModif.setEntreeData(rowData);
                }
            });

            return row;
        });

        // Load data from the database
        loadEntreeData();

        EntreeModel.getInstance().getEntreeList().addListener((ListChangeListener<Entree>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasUpdated() || change.wasReplaced()) {
                    entreeTableView.setItems(EntreeModel.getInstance().getEntreeList());
                    loadEntreeData();
                }
            }
        });

    }

    private void loadEntreeData() {
        ObservableList<Entree> emtreeList = null;
        try {
            Entree med = new Entree();
            emtreeList = med.getAllEntree();
        }  catch (Exception e) {
            e.printStackTrace();
        }finally {
            entreeTableView.setItems(emtreeList);
        }

    }
}



