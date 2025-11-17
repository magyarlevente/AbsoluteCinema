package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Reprezentál egy filmet.
 */
public class Movie {
    private String id;
    private String title;
    private int durationMinutes;
    private int ageRating;
    private String genre;
    private String description;
    private String posterUrl; // A poszter online elérési útja
    private List<Showtime> showtimes;

    public Movie(String id, String title, int durationMinutes, int ageRating, String genre, String description, String posterUrl, List<Showtime> showtimes) {
        this.id = id;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.ageRating = ageRating;
        this.genre = genre;
        this.description = description;
        this.posterUrl = posterUrl;
        this.showtimes = showtimes;
    }

    // --- Getterek ---

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getAgeRating() {
        return ageRating;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }
}