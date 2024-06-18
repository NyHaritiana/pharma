package com.pharmacie.pharmacie.model;

import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.components.Vendu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RecetteModel {
    private static RecetteModel instance;
    private ObservableList<Vendu> venduList;

    private RecetteModel() {
        venduList = FXCollections.observableArrayList();
    }

    public static RecetteModel getInstance() {
        if (instance == null) {
            instance = new RecetteModel();
        }
        return instance;
    }

    public ObservableList<Vendu> getRecetteList() {
        return venduList;
    }
}
