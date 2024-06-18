package com.pharmacie.pharmacie.components;

import com.pharmacie.pharmacie.DbFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Classe Medicament
public class Entree {
    private String numEntree;
    private String numMedoc;
    private int stockEntree;
    private LocalDate dateEntree;

    // Constructeur

    public Entree(){}
    public Entree(String numEntree, String numMedoc, int stockEntree) {
        this.numEntree = numEntree;
        this.numMedoc = numMedoc;
        this.stockEntree = stockEntree;
        this.dateEntree = LocalDate.now(); //initializena ho date zao
    }

    // Constructeur
    public Entree(String numEntree, String numMedoc, int stockEntree, LocalDate dateEntree) {
        this.numEntree = numEntree;
        this.numMedoc = numMedoc;
        this.stockEntree = stockEntree;
        this.dateEntree = dateEntree;
    }


    // Getters et setters
    public String getNumEntree() {
        return numEntree;
    }

    public void setNumEntree(String NumEntree) {
        this.numMedoc = NumEntree;
    }

    public String getNumMedoc() {
        return numMedoc;
    }

    public void setNumMedoc(String NumMedoc) {
        this.numMedoc = NumMedoc;
    }

    public int getStockEntree() {
        return stockEntree;
    }

    public void setStockEntree(int StockEntree) {
        this.stockEntree = StockEntree;
    }

    public LocalDate getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDate DateEntree) {
        this.dateEntree = DateEntree;
    }

    @Override
    public String toString() {
        return "Entree{numEntree='"+numEntree+"\', numMedoc='"+numMedoc+"\', stockEntree="+stockEntree+", dateEntree="+dateEntree+"}";
    }


    static DbFunctions db = new DbFunctions();
    Medicament medoc = new Medicament();

    // Ajouter un Entree
    public void addEntree(Entree entree) {

        String sql = "INSERT INTO \"ENTREE\" (\"numEntree\", \"numMedoc\", \"stockEntree\", \"dateEntree\") VALUES (?, ?, ?, ?)";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entree.getNumEntree());
            pstmt.setString(2, entree.getNumMedoc());
            pstmt.setInt(3, entree.getStockEntree());
            pstmt.setDate(4, Date.valueOf(dateEntree));
            pstmt.executeUpdate();

            medoc.stockMaj();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllNumMedoc() {
        List<String> numMedocList = new ArrayList<>();
        String query = "SELECT \"numMedoc\" FROM \"MEDICAMENT\"";

        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                numMedocList.add(rs.getString("numMedoc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numMedocList;
    }

    // Mettre à jour un Entree
    public void updateEntree(Entree updatedEntree) {
        String sql = "UPDATE \"ENTREE\" SET \"numMedoc\" = ?, \"stockEntree\" = ?, \"dateEntree\" = ? WHERE \"numEntree\" = ?";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updatedEntree.getNumMedoc());
            pstmt.setInt(2, updatedEntree.getStockEntree());
            pstmt.setDate(3, Date.valueOf(dateEntree));
            pstmt.setString(4, numEntree);
            pstmt.executeUpdate();

            medoc.stockMaj();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un Entree
    public void deleteEntree(String numEntree) {
        String sql = "DELETE FROM \"ENTREE\" WHERE \"numEntree\" = ?";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numEntree);
            pstmt.executeUpdate();

            medoc.stockMaj();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Afficher tous les entree
    public ObservableList<Entree> getAllEntree() {
        ObservableList<Entree> entrees = FXCollections.observableArrayList();
        String sql = "SELECT * FROM \"ENTREE\"";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String numEntree = rs.getString("numEntree");
                String numMedoc = rs.getString("numMedoc");
                int stockEntree = rs.getInt("stockEntree");
                LocalDate dateEntree = rs.getDate("dateEntree").toLocalDate(); // Conversion de java.sql.Date en LocalDate
                Entree entree = new Entree(numEntree, numMedoc, stockEntree, dateEntree);
                entrees.add(entree);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entrees;
    }

}

