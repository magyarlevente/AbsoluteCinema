package model;

import java.time.LocalDateTime;

/**
 * Adatmodell egy adott vetítési időponthoz.
 */
public class Showtime {
    private String showtimeId;
    private LocalDateTime time;
    private String cinemaName;

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

    // Megjelenítéshez
    @Override
    public String toString() {
        return time.toLocalTime() + " - " + cinemaName;
    }
}