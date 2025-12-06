package org.example.dao;

import org.example.database.DatabaseManager;
import org.example.model.Foglalas;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FoglalasDAO {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public boolean create(Foglalas f) {
        String sql = "INSERT INTO Foglalas (felhasznaloId, idopontId, ulohelyId, foglalasDatuma, fizetve) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, f.getFelhasznaloId());
            pstmt.setInt(2, f.getIdopontId());
            pstmt.setInt(3, f.getUlohelyId());
            pstmt.setString(4, f.getFoglalasDatuma().format(FORMATTER));
            pstmt.setBoolean(5, f.isFizetve());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Foglalas> findByIdopont(int idopontId) {
        List<Foglalas> list = new ArrayList<>();
        String sql = "SELECT * FROM Foglalas WHERE idopontId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idopontId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Foglalas> findByFelhasznaloId(int felhasznaloId) {
        List<Foglalas> list = new ArrayList<>();
        String sql = "SELECT * FROM Foglalas WHERE felhasznaloId = ? ORDER BY foglalasDatuma DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, felhasznaloId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Foglalas mapRow(ResultSet rs) throws SQLException {
        Foglalas f = new Foglalas();
        f.setFoglalasId(rs.getInt("foglalasId"));
        f.setFelhasznaloId(rs.getInt("felhasznaloId"));
        f.setIdopontId(rs.getInt("idopontId"));
        f.setUlohelyId(rs.getInt("ulohelyId"));
        f.setFoglalasDatuma(LocalDateTime.parse(rs.getString("foglalasDatuma"), FORMATTER));
        f.setFizetve(rs.getBoolean("fizetve"));
        return f;
    }

    public boolean delete(int foglalasId) {
        String sql = "DELETE FROM Foglalas WHERE foglalasId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, foglalasId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}