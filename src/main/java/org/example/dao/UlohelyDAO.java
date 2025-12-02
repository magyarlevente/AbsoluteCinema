package org.example.dao;

import org.example.database.DatabaseManager;
import org.example.model.Ulohely;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UlohelyDAO {
    public List<Ulohely> findByIdopont(int idopontId) {
        List<Ulohely> seats = new ArrayList<>();
        // Először lekérjük a terem ID-t az időpontból, majd a székeket
        String sql = "SELECT u.* FROM Ulohely u JOIN Idopont i ON u.teremId = i.teremId WHERE i.IdopontId = ? ORDER BY u.sorJele, u.ulohelySzam";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idopontId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Ulohely u = new Ulohely();
                u.setUlohelyId(rs.getInt("ulohelyId"));
                u.setTeremId(rs.getInt("teremId"));
                u.setSorJele(rs.getString("sorJele"));
                u.setUlohelySzam(rs.getInt("ulohelySzam"));
                seats.add(u);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return seats;
    }

    public Ulohely findById(int id) {
        String sql = "SELECT * FROM Ulohely WHERE ulohelyId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Ulohely u = new Ulohely();
                u.setUlohelyId(rs.getInt("ulohelyId"));
                u.setSorJele(rs.getString("sorJele"));
                u.setUlohelySzam(rs.getInt("ulohelySzam"));
                return u;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}