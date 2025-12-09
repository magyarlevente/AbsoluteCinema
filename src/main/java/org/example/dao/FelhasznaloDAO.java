package org.example.dao;

import org.example.database.DatabaseManager;
import org.example.model.Felhasznalo;
import java.sql.*;

//felhasznalo ellenorzese ha letezik, regisztracio elvegzese es mentese adatbazisba

public class FelhasznaloDAO {
    public Felhasznalo login(String username, String password) {
        String sql = "SELECT * FROM Felhasznalo WHERE felhasznaloNev = ? AND jelszoHASH = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Felhasznalo user = new Felhasznalo();
                user.setFelhasznaloId(rs.getInt("felhasznaloId"));
                user.setFelhasznaloNev(rs.getString("felhasznaloNev"));
                user.setJelszoHASH(rs.getString("jelszoHASH"));
                return user;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean register(String username, String password) {
        String sql = "INSERT INTO Felhasznalo (felhasznaloNev, jelszoHASH) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }
}