package org.example.dao;

import org.example.database.DatabaseManager;
import org.example.model.Terem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeremDAO {

    public Terem findById(int id) {
        String sql = "SELECT * FROM Terem WHERE teremId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Terem t = new Terem();
                t.setTeremId(rs.getInt("teremId"));
                t.setTeremNev(rs.getString("teremNev"));
                t.setUlohelyekSzama(rs.getInt("ulohelyekSzama"));
                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}