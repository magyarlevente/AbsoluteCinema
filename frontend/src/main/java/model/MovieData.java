package model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Dummy adatok a filmekhez és vetítési időpontokhoz.
 */
public class MovieData {

    public static List<Movie> createDummyMovies() {
        List<Movie> movies = new ArrayList<>();

        // JAVÍTVA: Csak a mai és holnapi dátumot használjuk a fix időpontokhoz
        LocalDateTime today = LocalDateTime.now().toLocalDate().atTime(LocalTime.MIN); // Mai nap 00:00:00
        LocalDateTime tomorrow = today.plusDays(1); // Holnapi nap 00:00:00

        // --- Film 1: Interstellar (Sci-fi)
        List<Showtime> showtimes1 = Arrays.asList(
                // MAI VETÍTÉSEK
                // JAVÍTVA: Fix 17:45 időpont
                new Showtime("S001", today.withHour(17).withMinute(45), "Terem 1"),
                // JAVÍTVA: Fix 20:30 időpont
                new Showtime("S002", today.withHour(20).withMinute(30), "Terem 5"),
                // HOLNAPI VETÍTÉS
                // JAVÍTVA: Holnap 22:00 időpont
                new Showtime("S003", tomorrow.withHour(22).withMinute(0), "Terem 2")
        );
        movies.add(new Movie(
                "M001",
                "Interstellar - Csillagok között",
                169,
                12,
                "Sci-fi",
                "Egy csapat felfedező egy újonnan felfedezett féregjáraton utazik keresztül, hogy túllépjen az emberi űrutazás korlátain, és legyőzze az emberiség határait.",
                "https://image.tmdb.org/t/p/w600_and_h900_bestv2/6KiSSndIMLj1swkpPNq2lYppDVQ.jpg",
                showtimes1
        ));

        // --- Film 2: The Grand Budapest Hotel (Vígjáték)
        List<Showtime> showtimes2 = Arrays.asList(
                // MAI VETÍTÉSEK
                new Showtime("S004", today.withHour(16).withMinute(0), "Terem 3"),
                new Showtime("S005", today.withHour(18).withMinute(0), "Terem 4")
        );
        movies.add(new Movie(
                "M002",
                "A Grand Budapest Hotel",
                99,
                16,
                "Vígjáték",
                "Egy neves európai szálloda portásának és hű inasának kalandjai a világháborúk közötti Európában.",
                "https://image.tmdb.org/t/p/w600_and_h900_bestv2/fEHXKrdNjtf5R7YAA0ssVZLJLOa.jpg",
                showtimes2
        ));

        // --- Film 3: Mad Max: Fury Road (Akció)
        List<Showtime> showtimes3 = Arrays.asList(
                // MAI VETÍTÉSEK
                new Showtime("S006", today.withHour(19).withMinute(0), "Terem 6"),
                new Showtime("S007", today.withHour(22).withMinute(15), "Terem 1"),
                // HOLNAPI VETÍTÉS
                new Showtime("S008", tomorrow.withHour(18).withMinute(30), "Terem 5")
        );
        movies.add(new Movie(
                "M003",
                "Mad Max: A harag útja",
                120,
                18,
                "Akció",
                "Egy poszt-apokaliptikus sivatagi pusztaságban Max Rockatansky egy női felkelőhöz csatlakozik, hogy megmentsenek egy csoport rabszolgát a zsarnoki Immortan Joe-tól.",
                "https://image.tmdb.org/t/p/w600_and_h900_bestv2/u6nSMyGp9Cc9TKMDDxxTZPGGpiV.jpg",
                showtimes3
        ));

        // --- Film 4: Spirited Away (Animáció)
        List<Showtime> showtimes4 = Arrays.asList(
                // MAI VETÍTÉS
                new Showtime("S009", today.withHour(15).withMinute(30), "Terem 2")
        );
        movies.add(new Movie(
                "M004",
                "Chihiro Szellemországban",
                125,
                6,
                "Animáció",
                "Egy 10 éves lány eltéved egy szellemek által lakott mágikus világba, ahol meg kell találnia a módját, hogy megmentse a szüleit és hazatérjen.",
                "https://image.tmdb.org/t/p/w600_and_h900_bestv2/2bZSC3KJcVLLTE9beTI21Nd0v6E.jpg",
                showtimes4
        ));

        return movies;
    }
}