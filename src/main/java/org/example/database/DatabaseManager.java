package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:absolute_cinema.db";
    // Ez a formátum felel meg az SQLite TEXT dátum tárolásának
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // --- TÁBLÁK LÉTREHOZÁSA ---

            stmt.execute("CREATE TABLE IF NOT EXISTS Felhasznalo (" +
                    "felhasznaloId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "felhasznaloNev TEXT NOT NULL UNIQUE," +
                    "jelszoHASH TEXT NOT NULL" +
                    ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS Film (" +
                    "filmId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cim TEXT NOT NULL," +
                    "jatekido INTEGER," +
                    "bemutatoIdeje TEXT," +
                    "mufaj TEXT," +
                    "filmLeiras TEXT," +
                    "poszterUrl TEXT," +
                    "korhatar INTEGER" +
                    ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS Terem (" +
                    "teremId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "teremNev TEXT NOT NULL," +
                    "ulohelyekSzama INTEGER NOT NULL" +
                    ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS Ulohely (" +
                    "ulohelyId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "teremId INTEGER NOT NULL," +
                    "sorJele TEXT NOT NULL," +
                    "ulohelySzam INTEGER NOT NULL," +
                    "FOREIGN KEY (teremId) REFERENCES Terem(teremId)" +
                    ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS Idopont (" +
                    "IdopontId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "kezdesIdopont TEXT NOT NULL," +
                    "filmId INTEGER NOT NULL," +
                    "teremId INTEGER NOT NULL," +
                    "jegyAr INTEGER NOT NULL," +
                    "FOREIGN KEY (filmId) REFERENCES Film(filmId)," +
                    "FOREIGN KEY (teremId) REFERENCES Terem(teremId)" +
                    ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS Foglalas (" +
                    "foglalasId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "felhasznaloId INTEGER NOT NULL," +
                    "idopontId INTEGER NOT NULL," +
                    "ulohelyId INTEGER NOT NULL," +
                    "foglalasDatuma TEXT NOT NULL," +
                    "fizetve BOOLEAN NOT NULL," +
                    "FOREIGN KEY (felhasznaloId) REFERENCES Felhasznalo(felhasznaloId)," +
                    "FOREIGN KEY (idopontId) REFERENCES Idopont(IdopontId)," +
                    "FOREIGN KEY (ulohelyId) REFERENCES Ulohely(ulohelyId)" +
                    ");");

            // --- ADATOK BETÖLTÉSE ---
            insertInitialData(conn);
            System.out.println("Adatbázis inicializálása kész.");

        } catch (SQLException e) {
            System.err.println("Adatbázis hiba: " + e.getMessage());
        }
    }

    private static void insertInitialData(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {

            // Ellenőrizzük, hogy van-e már adat (hogy ne duplázzuk meg)
            boolean vanAdat = false;
            try (var rs = stmt.executeQuery("SELECT count(*) AS total FROM Film")) {
                if (rs.next() && rs.getInt("total") > 0) {
                    vanAdat = true;
                }
            }

            if (!vanAdat) {
                System.out.println("Adatok betöltése a Mock adatok alapján...");

                // 1. FILMEK BETÖLTÉSE
                // Interstellar
                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('Interstellar - Csillagok között', 169, 'Sci-fi', 12, " +
                        "'Egy csapat felfedező egy újonnan felfedezett féregjáraton utazik keresztül, hogy túllépjen az emberi űrutazás korlátain.', " +
                        "'https://image.tmdb.org/t/p/w600_and_h900_bestv2/6KiSSndIMLj1swkpPNq2lYppDVQ.jpg');");

                // A Grand Budapest Hotel
                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('A Grand Budapest Hotel', 99, 'Vígjáték', 16, " +
                        "'Egy neves európai szálloda portásának és hű inasának kalandjai a világháborúk közötti Európában.', " +
                        "'https://image.tmdb.org/t/p/w600_and_h900_bestv2/fEHXKrdNjtf5R7YAA0ssVZLJLOa.jpg');");

                // Mad Max: A harag útja
                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('Mad Max: A harag útja', 120, 'Akció', 18, " +
                        "'Egy poszt-apokaliptikus sivatagi pusztaságban Max Rockatansky egy női felkelőhöz csatlakozik.', " +
                        "'https://image.tmdb.org/t/p/w600_and_h900_bestv2/u6nSMyGp9Cc9TKMDDxxTZPGGpiV.jpg');");

                // 2. TEREM ÉS ÜLŐHELYEK LÉTREHOZÁSA
                // Létrehozunk egy termet (ID = 1)
                stmt.execute("INSERT INTO Terem (teremNev, ulohelyekSzama) VALUES ('Nagyterem', 20);");

                // "A" sor: 1-10 szék
                for (int i = 1; i <= 10; i++) {
                    stmt.execute("INSERT INTO Ulohely (teremId, sorJele, ulohelySzam) VALUES (1, 'A', " + i + ");");
                }
                // "B" sor: 1-10 szék
                for (int i = 1; i <= 10; i++) {
                    stmt.execute("INSERT INTO Ulohely (teremId, sorJele, ulohelySzam) VALUES (1, 'B', " + i + ");");
                }

                // 3. VETÍTÉSI IDŐPONTOK (Dinamikusan a MAI napra)
                LocalDateTime now = LocalDateTime.now();

                // Interstellar (FilmID: 1) - Ma 17:45 és 20:30
                String timeInt1 = now.withHour(17).withMinute(45).format(FORMATTER);
                String timeInt2 = now.withHour(20).withMinute(30).format(FORMATTER);

                stmt.execute("INSERT INTO Idopont (kezdesIdopont, filmId, teremId, jegyAr) VALUES ('" + timeInt1 + "', 1, 1, 2500);");
                stmt.execute("INSERT INTO Idopont (kezdesIdopont, filmId, teremId, jegyAr) VALUES ('" + timeInt2 + "', 1, 1, 2800);");

                // Grand Budapest (FilmID: 2) - Ma 16:00
                String timeBud1 = now.withHour(16).withMinute(0).format(FORMATTER);
                stmt.execute("INSERT INTO Idopont (kezdesIdopont, filmId, teremId, jegyAr) VALUES ('" + timeBud1 + "', 2, 1, 2200);");

                // Mad Max (FilmID: 3) - Ma 19:00 és 22:15
                String timeMad1 = now.withHour(19).withMinute(0).format(FORMATTER);
                String timeMad2 = now.withHour(22).withMinute(15).format(FORMATTER);

                stmt.execute("INSERT INTO Idopont (kezdesIdopont, filmId, teremId, jegyAr) VALUES ('" + timeMad1 + "', 3, 1, 2600);");
                stmt.execute("INSERT INTO Idopont (kezdesIdopont, filmId, teremId, jegyAr) VALUES ('" + timeMad2 + "', 3, 1, 2600);");

                // 4. TESZT FELHASZNÁLÓ
                stmt.execute("INSERT INTO Felhasznalo (felhasznaloNev, jelszoHASH) VALUES ('teszt', 'teszt');");

                System.out.println("Kezdő adatok sikeresen betöltve az adatbázisba!");
            }
        }
    }
}