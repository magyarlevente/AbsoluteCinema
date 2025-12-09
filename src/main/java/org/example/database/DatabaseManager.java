package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

// Indításkor létrehozza az adatbázist és a táblákat, ha azok nem léteznek.
// automatikusan feltölti kezdőadatokkal: filmekkel, termekkel, vetítésekkel,

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:absolute_cinema.db";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // --- TÁBLÁK LÉTREHOZÁSA ---
            stmt.execute("CREATE TABLE IF NOT EXISTS Felhasznalo (felhasznaloId INTEGER PRIMARY KEY AUTOINCREMENT, felhasznaloNev TEXT NOT NULL UNIQUE, jelszoHASH TEXT NOT NULL);");
            stmt.execute("CREATE TABLE IF NOT EXISTS Film (filmId INTEGER PRIMARY KEY AUTOINCREMENT, cim TEXT NOT NULL, jatekido INTEGER, bemutatoIdeje TEXT, mufaj TEXT, filmLeiras TEXT, poszterUrl TEXT, korhatar INTEGER);");
            stmt.execute("CREATE TABLE IF NOT EXISTS Terem (teremId INTEGER PRIMARY KEY AUTOINCREMENT, teremNev TEXT NOT NULL, ulohelyekSzama INTEGER NOT NULL);");
            stmt.execute("CREATE TABLE IF NOT EXISTS Ulohely (ulohelyId INTEGER PRIMARY KEY AUTOINCREMENT, teremId INTEGER NOT NULL, sorJele TEXT NOT NULL, ulohelySzam INTEGER NOT NULL, FOREIGN KEY (teremId) REFERENCES Terem(teremId));");
            stmt.execute("CREATE TABLE IF NOT EXISTS Idopont (IdopontId INTEGER PRIMARY KEY AUTOINCREMENT, kezdesIdopont TEXT NOT NULL, filmId INTEGER NOT NULL, teremId INTEGER NOT NULL, jegyAr INTEGER NOT NULL, FOREIGN KEY (filmId) REFERENCES Film(filmId), FOREIGN KEY (teremId) REFERENCES Terem(teremId));");
            stmt.execute("CREATE TABLE IF NOT EXISTS Foglalas (foglalasId INTEGER PRIMARY KEY AUTOINCREMENT, felhasznaloId INTEGER NOT NULL, idopontId INTEGER NOT NULL, ulohelyId INTEGER NOT NULL, foglalasDatuma TEXT NOT NULL, fizetve BOOLEAN NOT NULL, FOREIGN KEY (felhasznaloId) REFERENCES Felhasznalo(felhasznaloId), FOREIGN KEY (idopontId) REFERENCES Idopont(IdopontId), FOREIGN KEY (ulohelyId) REFERENCES Ulohely(ulohelyId));");

            // --- ADATOK BETÖLTÉSE ---
            insertInitialData(conn);
            System.out.println("Adatbázis inicializálása kész.");

        } catch (SQLException e) {
            System.err.println("Adatbázis hiba: " + e.getMessage());
        }
    }

    private static void insertInitialData(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {

            boolean vanAdat = false;
            try (var rs = stmt.executeQuery("SELECT count(*) AS total FROM Film")) {
                if (rs.next() && rs.getInt("total") > 0) {
                    vanAdat = true;
                }
            }

            if (!vanAdat) {
                System.out.println("Adatok betöltése (Működő képekkel + 7 napos generálással)...");

                // Cinema City filmek (ID: 1-5)
                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('Interstellar - Csillagok között', 169, 'Sci-fi', 12, " +
                        "'Egy csapat felfedező egy újonnan felfedezett féregjáraton utazik keresztül...', " +
                        "'https://image.tmdb.org/t/p/original/6KiSSndIMLj1swkpPNq2lYppDVQ.jpg');");

                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('Mad Max: A harag útja', 120, 'Akció', 18, " +
                        "'Egy poszt-apokaliptikus sivatagi pusztaságban Max Rockatansky egy női felkelőhöz csatlakozik.', " +
                        "'https://image.tmdb.org/t/p/original/u6nSMyGp9Cc9TKMDDxxTZPGGpiV.jpg');");

                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('John Wick: 4. felvonás', 169, 'Akció', 18, " +
                        "'John Wick felfedezi a módját, hogyan győzze le a Felső Kört...', " +
                        "'https://image.tmdb.org/t/p/original/kQWMSvX3wXfNPZV4a9zrNjYKcwp.jpg');");

                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('Bosszúállók: Végjáték', 181, 'Sci-fi', 12, " +
                        "'A Végtelen Háború pusztító eseményei után az Univerzum romokban hever.', " +
                        "'https://image.tmdb.org/t/p/w600_and_h900_bestv2/or06FN3Dka5tukK1e9sl16pB3iy.jpg');");

                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('Agymanók', 95, 'Animáció', 6, " +
                        "'Rileyt az érzelmei vezérlik: Derű, Bánat, Harag, Majré és Undor.', " +
                        "'https://image.tmdb.org/t/p/original/iCCzj3wc2Z9k5YSRLN7g8D09c0X.jpg');");

                // Apolló Mozi filmek (ID: 6-10)
                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('A Grand Budapest Hotel', 99, 'Vígjáték', 16, " +
                        "'Egy neves európai szálloda portásának és hű inasának kalandjai...', " +
                        "'https://image.tmdb.org/t/p/original/fEHXKrdNjtf5R7YAA0ssVZLJLOa.jpg');");

                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('Démonok között', 112, 'Horror', 18, " +
                        "'Paranormális nyomozók segítenek egy családon, akiket egy sötét jelenlét terrorizál.', " +
                        "'https://image.tmdb.org/t/p/original/rwHzhzKvpXQRlqdcraYnPhoOWYy.jpg');");

                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('Hetedik', 127, 'Thriller', 18, " +
                        "'Két nyomozó, egy újonc és egy veterán, egy sorozatgyilkosra vadászik...', " +
                        "'https://image.tmdb.org/t/p/original/cPHgQvv1ThBco5jC3jWWy6kQdbF.jpg');");

                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('Szerelmünk lapjai', 123, 'Romantikus', 12, " +
                        "'Egy szegény, de szenvedélyes fiatalember beleszeret egy gazdag fiatal nőbe...', " +
                        "'https://image.tmdb.org/t/p/original/vKdATASPpJaLZFatODPB33r3cph.jpg');");

                stmt.execute("INSERT INTO Film (cim, jatekido, mufaj, korhatar, filmLeiras, poszterUrl) VALUES " +
                        "('Érkezés', 116, 'Sci-fi', 12, " +
                        "'Amikor titokzatos űrhajók szállnak le a Föld különböző pontjain...', " +
                        "'https://image.tmdb.org/t/p/original/sLvkaBQ3JONDBDCEi6AVupbYwyh.jpg');");


                // ---------------- 2. TERMEK ÉS HELYEK (4 TEREM) ----------------
                // Külön termek a Cinema Citynek és az Apollónak a szűréshez
                stmt.execute("INSERT INTO Terem (teremId, teremNev, ulohelyekSzama) VALUES (1, 'CC Terem 1 (IMAX)', 30);");
                stmt.execute("INSERT INTO Terem (teremId, teremNev, ulohelyekSzama) VALUES (2, 'CC Terem 2', 20);");
                stmt.execute("INSERT INTO Terem (teremId, teremNev, ulohelyekSzama) VALUES (3, 'Apollo Nagyterem', 40);");
                stmt.execute("INSERT INTO Terem (teremId, teremNev, ulohelyekSzama) VALUES (4, 'Apollo Kisterem', 15);");

                // Helyek generálása mind a 4 teremhez
                for (int t = 1; t <= 4; t++) {
                    int rows = (t == 3 || t == 1) ? 4 : 2; // Nagyobb termek több sor
                    for (int r = 0; r < rows; r++) {
                        char rowChar = (char) ('A' + r);
                        for (int s = 1; s <= 5; s++) {
                            stmt.execute("INSERT INTO Ulohely (teremId, sorJele, ulohelySzam) VALUES (" + t + ", '" + rowChar + "', " + s + ");");
                        }
                    }
                }

                // ---------------- 3. IDŐPONTOK (7 NAPRA ELŐRE) ----------------
                Random rand = new Random();
                LocalDate today = LocalDate.now();

                // 7 napon megyünk végig
                for (int day = 0; day < 7; day++) {
                    LocalDate currentDate = today.plusDays(day);

                    // A) CINEMA CITY VETÍTÉSEK (Film ID: 1-5, Terem ID: 1-2)
                    for (int filmId = 1; filmId <= 5; filmId++) {
                        // Napi 1-2 vetítés
                        int showsPerDay = 1 + rand.nextInt(2);
                        for (int k = 0; k < showsPerDay; k++) {
                            int hour = 14 + rand.nextInt(8); // 14:00 - 22:00
                            int minute = rand.nextInt(12) * 5;
                            int room = 1 + rand.nextInt(2); // Csak CC termek
                            int price = 2500 + rand.nextInt(5) * 100;

                            LocalDateTime showTime = currentDate.atTime(hour, minute);
                            stmt.execute(String.format(
                                    "INSERT INTO Idopont (kezdesIdopont, filmId, teremId, jegyAr) VALUES ('%s', %d, %d, %d);",
                                    showTime.format(FORMATTER), filmId, room, price
                            ));
                        }
                    }

                    // B) APOLLÓ MOZI VETÍTÉSEK (Film ID: 6-10, Terem ID: 3-4)
                    for (int filmId = 6; filmId <= 10; filmId++) {
                        // 50% esély, hogy aznap játsszák
                        if (rand.nextBoolean()) {
                            int hour = 16 + rand.nextInt(5); // 16:00 - 21:00
                            int minute = rand.nextInt(4) * 15;
                            int room = 3 + rand.nextInt(2); // Csak Apollo termek
                            int price = 1800 + rand.nextInt(3) * 100; // Olcsóbb

                            LocalDateTime showTime = currentDate.atTime(hour, minute);
                            stmt.execute(String.format(
                                    "INSERT INTO Idopont (kezdesIdopont, filmId, teremId, jegyAr) VALUES ('%s', %d, %d, %d);",
                                    showTime.format(FORMATTER), filmId, room, price
                            ));
                        }
                    }
                }

                // ---------------- 4. TESZT USER ----------------
                stmt.execute("INSERT INTO Felhasznalo (felhasznaloNev, jelszoHASH) VALUES ('teszt', 'teszt');");

                System.out.println("Kezdő adatok (10 film, működő képekkel, 7 napos bontásban) sikeresen betöltve!");
            }
        }
    }
}