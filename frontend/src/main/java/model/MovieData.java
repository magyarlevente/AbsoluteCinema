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
                new Showtime("S001", now.plusHours(2), "Cinema Center - Pest"),
                new Showtime("S002", now.plusHours(5), "Cinema Center - Pest"),
                new Showtime("S003", now.plusDays(1).withHour(20).withMinute(0), "Cinema Nova - Buda")
        );
        movies.add(new Movie(
                "M001",
                "Interstellar - Csillagok között",
                169,
                12,
                "Sci-fi",
                "Egy csapat felfedező egy újonnan felfedezett féregjáraton utazik keresztül, hogy túllépjen az emberi űrutazás korlátain, és legyőzze az emberiség határait.",
                "https://www.themoviedb.org/movie/157336-interstellar#", // Egyedi kép URL
                showtimes1
        ));

        // --- Film 2: The Grand Budapest Hotel (Vígjáték)
        List<Showtime> showtimes2 = Arrays.asList(
                new Showtime("S004", now.plusHours(1), "Cinema Retro - Szeged"),
                new Showtime("S005", now.plusHours(4), "Cinema Retro - Szeged")
        );
        movies.add(new Movie(
                "M002",
                "A Grand Budapest Hotel",
                99,
                16,
                "Vígjáték",
                "Egy neves európai szálloda portásának és hű inasának kalandjai a világháborúk közötti Európában.",
                "https://placehold.co/100x150/8a004a/ffffff?text=BUDAPEST", // Egyedi kép URL
                showtimes2
        ));

        // --- Film 3: Mad Max: Fury Road (Akció)
        List<Showtime> showtimes3 = Arrays.asList(
                new Showtime("S006", now.plusHours(3), "Cinema Center - Pest"),
                new Showtime("S007", now.plusHours(6), "Cinema Nova - Buda"),
                new Showtime("S008", now.plusDays(1).withHour(18).withMinute(30), "Cinema Center - Pest")
        );
        movies.add(new Movie(
                "M003",
                "Mad Max: A harag útja",
                120,
                18,
                "Akció",
                "Egy poszt-apokaliptikus sivatagi pusztaságban Max Rockatansky egy női felkelőhöz csatlakozik, hogy megmentsenek egy csoport rabszolgát a zsarnoki Immortan Joe-tól.",
                "https://placehold.co/100x150/cc0000/ffffff?text=MAD+MAX", // Egyedi kép URL
                showtimes3
        ));

        // --- Film 4: Spirited Away (Animáció)
        List<Showtime> showtimes4 = Arrays.asList(
                new Showtime("S009", now.plusHours(1), "Cinema Nova - Buda")
        );
        movies.add(new Movie(
                "M004",
                "Chihiro Szellemországban",
                125,
                6,
                "Animáció",
                "Egy 10 éves lány eltéved egy szellemek által lakott mágikus világba, ahol meg kell találnia a módját, hogy megmentse a szüleit és hazatérjen.",
                "https://placehold.co/100x150/008080/ffffff?text=CHIHIRO", // Egyedi kép URL
                showtimes4
        ));

        return movies;
    }
}