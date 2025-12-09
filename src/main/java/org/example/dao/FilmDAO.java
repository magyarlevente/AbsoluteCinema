package org.example.dao;

import org.example.database.DatabaseManager;
import org.example.model.Film;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//az adatbazisbol kiolvassa a filmeket es atalakitja Film objektumokka

public class FilmDAO {
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM Film";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Film film = new Film();
                film.setFilmId(rs.getInt("filmId"));
                film.setCim(rs.getString("cim"));
                film.setJatekido(rs.getInt("jatekido"));
                String dateStr = rs.getString("bemutatoIdeje");
                if (dateStr != null && !dateStr.isEmpty()) film.setBemutatoIdeje(LocalDate.parse(dateStr));
                film.setMufaj(rs.getString("mufaj"));
                film.setFilmLeiras(rs.getString("filmLeiras"));
                film.setPoszterUrl(rs.getString("poszterUrl"));
                film.setKorhatar(rs.getInt("korhatar"));
                films.add(film);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return films;
    }

    public Film findById(int id) {
        String sql = "SELECT * FROM Film WHERE filmId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Film film = new Film();
                film.setFilmId(rs.getInt("filmId"));
                film.setCim(rs.getString("cim"));
                // ... többi mező betöltése (egyszerűsítve a megjelenítéshez) ...
                return film;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}