package org.example.dao;

import org.example.database.DatabaseManager;
import org.example.model.Idopont;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//Ez az osztály felel azért, hogy megtudjuk, mikor és melyik teremben vetítenek egy filmet

public class IdopontDAO {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<Idopont> findByFilmId(int filmId) {
        List<Idopont> list = new ArrayList<>();
        String sql = "SELECT * FROM Idopont WHERE filmId = ? ORDER BY kezdesIdopont";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, filmId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Idopont i = new Idopont();
                i.setIdopontId(rs.getInt("IdopontId"));
                i.setFilmId(rs.getInt("filmId"));
                i.setTeremId(rs.getInt("teremId"));

                i.setKezdesIdopont(LocalDateTime.parse(rs.getString("kezdesIdopont"), FORMATTER));
                list.add(i);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Idopont findById(int id) {
        String sql = "SELECT * FROM Idopont WHERE IdopontId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Idopont i = new Idopont();
                i.setIdopontId(rs.getInt("IdopontId"));
                i.setFilmId(rs.getInt("filmId"));
                i.setKezdesIdopont(java.time.LocalDateTime.parse(rs.getString("kezdesIdopont"), java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                return i;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}