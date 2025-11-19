package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Fontos import a formázáshoz

/**
 * Adatmodell egy adott vetítési időponthoz.
 */
public class Showtime {
    private String showtimeId;
    private LocalDateTime time;
    private String cinemaName;

    // JAVÍTÁS: Létrehozunk egy formázót a "szép" dátumhoz
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM.dd. HH:mm");

    // Konstruktor
    public Showtime(String showtimeId, LocalDateTime time, String cinemaName) {
        this.showtimeId = showtimeId;
        this.time = time;
        this.cinemaName = cinemaName;
    }

    // Getters
    public String getShowtimeId() { return showtimeId; }
    public LocalDateTime getTime() { return time; }
    public String getCinemaName() { return cinemaName; }

    /**
     * Megjelenítéshez
     */
    @Override
    public String toString() {
        // JAVÍTÁS: A formázott stringet adjuk vissza a "fura" dátum helyett
        // Eredmény: "11.18. 12:30 (Terem 1)"
        return time.format(FORMATTER) + " (" + cinemaName + ")";
    }
}