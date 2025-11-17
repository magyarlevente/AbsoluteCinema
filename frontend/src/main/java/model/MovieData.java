package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Dummy adatok a filmekhez és vetítési időpontokhoz.
 */
public class MovieData {

    public static List<Movie> createDummyMovies() {
        List<Movie> movies = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // --- Film 1: Interstellar (Sci-fi)
        List<Showtime> showtimes1 = Arrays.asList(
                // Formátum: new Showtime(id, idő, helyszín/terem)
                new Showtime("S001", now.plusHours(2), "Terem 1"), // Ma, 2 óra múlva
                new Showtime("S002", now.plusHours(5), "Terem 5"), // Ma, 5 óra múlva
                new Showtime("S003", now.plusDays(1).withHour(20).withMinute(0), "Terem 2") // Holnap, 20:00
        );
        movies.add(new Movie(
                "M001",
                "Interstellar - Csillagok között",
                169,
                12,
                "Sci-fi",
                "Egy csapat felfedező egy újonnan felfedezett féregjáraton utazik keresztül, hogy túllépjen az emberi űrutazás korlátain, és legyőzze az emberiség határait.",
                // JAVÍTVA: Megbízható placeholder URL
                "https://image.tmdb.org/t/p/w600_and_h900_bestv2/6KiSSndIMLj1swkpPNq2lYppDVQ.jpg",
                showtimes1
        ));

        // --- Film 2: The Grand Budapest Hotel (Vígjáték)
        List<Showtime> showtimes2 = Arrays.asList(
                new Showtime("S004", now.plusDays(2).withHour(16).withMinute(30), "Terem 3"), // Két nap múlva, 16:30
                new Showtime("S005", now.plusDays(3).withHour(21).withMinute(0), "Terem 4") // Három nap múlva, 21:00
        );
        movies.add(new Movie(
                "M002",
                "Grand Budapest Hotel",
                99,
                16,
                "Vígjáték",
                "Egy legendás portás és a hűséges lobbi inas kalandjai az I. és II. világháború közötti fiktív Zubrowka Köztársaságban. Wes Anderson különleges, szimmetrikus stílusa és a humor jellemzi.",
                // JAVÍTVA: Megbízható placeholder URL
                "https://image.tmdb.org/t/p/w600_and_h900_bestv2/fEHXKrdNjtf5R7YAA0ssVZLJLOa.jpg",
                showtimes2
        ));

        // --- Film 3: Mad Max: Fury Road (Akció)
        List<Showtime> showtimes3 = Arrays.asList(
                new Showtime("S006", now.plusHours(3), "Terem 6"),
                new Showtime("S007", now.plusHours(6), "Terem 1"),
                new Showtime("S008", now.plusDays(1).withHour(18).withMinute(30), "Terem 5")
        );
        movies.add(new Movie(
                "M003",
                "Mad Max: A harag útja",
                120,
                18,
                "Akció",
                "Egy poszt-apokaliptikus sivatagi pusztaságban Max Rockatansky egy női felkelőhöz csatlakozik, hogy megmentsenek egy csoport rabszolgát a zsarnoki Immortan Joe-tól.",
                // JAVÍTVA: Megbízható placeholder URL
                "https://image.tmdb.org/t/p/w600_and_h900_bestv2/u6nSMyGp9Cc9TKMDDxxTZPGGpiV.jpg",
                showtimes3
        ));

        return movies;
    }
}