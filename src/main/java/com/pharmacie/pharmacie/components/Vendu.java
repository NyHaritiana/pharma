package com.pharmacie.pharmacie.components;

import com.pharmacie.pharmacie.DbFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;

import static com.pharmacie.pharmacie.components.Entree.db;

public class Vendu {
    private String numero;
    private String medoc;
    private int nombre;
    DbFunctions db = new DbFunctions();


    public Vendu(String numero, String medoc, int nombre) {
        this.numero = numero;
        this.medoc = medoc;
        this.nombre = nombre;
    }

    public Vendu() {}

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getMedoc() {
        return medoc;
    }

    public void setMedoc(String medoc) {
        this.medoc = medoc;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public ObservableList<Vendu> medicamentsPlusVendu() {
        ObservableList<Vendu> medocVendu = FXCollections.observableArrayList();
        String sql = "SELECT A.\"numMedoc\", M.\"Design\", SUM(A.nbr) AS totalVd FROM \"ACHAT\" A JOIN \"MEDICAMENT\" M ON A.\"numMedoc\" = M.\"numMedoc\" GROUP BY A.\"numMedoc\", M.\"Design\" ORDER BY totalVd DESC LIMIT 5";
        try (Connection conn = db.connect_to_db();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String numero = rs.getString("numMedoc");
                String medoc = rs.getString("Design");
                int nombre = rs.getInt("totalVd");


                Vendu vendu = new Vendu(numero, medoc, nombre);
                medocVendu.add(vendu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medocVendu;
    }
}
