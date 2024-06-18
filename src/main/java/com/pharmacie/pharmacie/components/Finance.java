package com.pharmacie.pharmacie.components;

import com.pharmacie.pharmacie.DbFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Finance {

    static DbFunctions db = new DbFunctions();
    private int recetteTotal;

    public int recetteTotal() {
        String query = "SELECT \"numMedoc\",SUM(\"nbr\") AS nombre FROM \"ACHAT\" GROUP BY \"numMedoc\"";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String numMedoc = rs.getString("numMedoc");
                int nbr = rs.getInt("nombre");
                String query1 = "SELECT \"prix_unitaire\" AS nombre FROM \"MEDICAMENT\" WHERE \"numMedoc\"='"+numMedoc+"'";
                try (Connection conn1 = db.connect_to_db();
                     PreparedStatement pstmt1 = conn1.prepareStatement(query1)) {
                    ResultSet rs1 = pstmt1.executeQuery();
                    if(rs1.next()){
                        int prix_unitaire= rs1.getInt("nombre");
                        int prixMedoc = prix_unitaire*nbr;
                        recetteTotal+=prixMedoc;
                    }
                }
            }
            System.out.println(recetteTotal);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return recetteTotal;
    }

    public static Map<String, Integer> recetteParMois() {
        Map<String, Integer> revenueMap = new LinkedHashMap<>();
        String sql = "WITH last_five_months AS (" +
                "    SELECT TO_CHAR(DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month' * i, 'Month YYYY') AS month " +
                "    FROM generate_series(0, 4) AS s(i) " +
                ") " +
                "SELECT lfm.month, " +
                "       COALESCE(SUM(A.\"nbr\" * M.\"prix_unitaire\"), 0) AS recetteMensuelle " +
                "FROM last_five_months lfm " +
                "LEFT JOIN \"ACHAT\" A ON TO_CHAR(DATE_TRUNC('month', A.\"dateAchat\"), 'Month YYYY') = lfm.month " +
                "LEFT JOIN \"MEDICAMENT\" M ON A.\"numMedoc\" = M.\"numMedoc\" " +
                "GROUP BY lfm.month " +
                "ORDER BY DATE_TRUNC('month', TO_DATE(lfm.month, 'Month YYYY')) DESC;";

        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("month").trim();
                int revenue = rs.getInt("recetteMensuelle");
                revenueMap.put(month, revenue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenueMap;
    }

}