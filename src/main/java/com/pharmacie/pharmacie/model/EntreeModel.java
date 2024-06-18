package com.pharmacie.pharmacie.model;

import com.pharmacie.pharmacie.components.Entree;
import com.pharmacie.pharmacie.components.Medicament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EntreeModel {
    private static EntreeModel instance;
    private ObservableList<Entree> entreeList;

    private EntreeModel() {
        entreeList = FXCollections.observableArrayList();
    }

    public static EntreeModel getInstance() {
        if (instance == null) {
            instance = new EntreeModel();
        }
        return instance;
    }

    public ObservableList<Entree> getEntreeList() {
        return entreeList;
    }

    public void addEntree(Entree entree) {
        entreeList.add(entree);
    }
}
