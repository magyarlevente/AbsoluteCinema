package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Importáljuk a formázót

/**
 * Adatmodell egy adott vetítési időponthoz.
 */
public class Showtime {
    private String showtimeId;
    private LocalDateTime time;
    private String cinemaName;

    // Formázó a szép dátum/idő megjelenítéshez (pl. 11.17. 19:30)
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
     * Megjelenítéshez: Visszaadja a vetítés idejét és helyszínét a kívánt formátumban.
     * @return Formázott dátum és idő, valamint terem string.
     */
    @Override
    public String toString() {
        // JAVÍTVA: A formázott stringet adjuk vissza
        return time.format(FORMATTER) + " (" + cinemaName + ")";
    }
}