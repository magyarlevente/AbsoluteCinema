package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox; // Új import a függőleges elrendezéshez
import javafx.stage.Stage;
import model.Movie;
import model.Showtime;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Dátum formázó
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Vezérlő a Film Részletező oldalhoz (MovieDetails.fxml).
 */
public class MovieDetailsController implements BaseController {

    private Stage stage;
    private Movie currentMovie;

    @FXML private Label titleLabel;
    @FXML private ImageView posterView;
    @FXML private Label durationLabel;
    @FXML private Label ageRatingLabel;
    @FXML private Label descriptionLabel;
    // Megjegyzés: A showtimesFlowPane-t most VBox-ként fogjuk használni a jobb strukturáltság érdekében
    @FXML private FlowPane showtimesFlowPane;
    @FXML private Button backButton;

    // Dátum formázó a fejléchez (pl. "2025. november 18. (Ma)")
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy. MMMM dd.");


    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Beállítja és megjeleníti a kiválasztott film adatait.
     */
    public void setMovie(Movie movie) {
        this.currentMovie = movie;
        titleLabel.setText(movie.getTitle());
        durationLabel.setText("Játékidő: " + movie.getDurationMinutes() + " perc");
        ageRatingLabel.setText("Korhatár: " + movie.getAgeRating() + "+");
        descriptionLabel.setText(movie.getDescription());

        // Poszter megjelenítése
        try {
            posterView.setImage(new Image(movie.getPosterUrl(), true));
        } catch (Exception e) {
            posterView.setImage(new Image("https://placehold.co/200x300/ff6600/ffffff?text=NINCS+KÉP"));
        }

        // Vetítési időpontok megjelenítése
        displayShowtimes(movie);
    }

    /**
     * Dinamikusan létrehozza a gombokat az időpontokhoz, dátum szerint csoportosítva.
     */
    private void displayShowtimes(Movie movie) {
        showtimesFlowPane.getChildren().clear();
        LocalDate today = LocalDate.now();

        if (movie.getShowtimes().isEmpty()) {
            showtimesFlowPane.getChildren().add(new Label("Ezen a napon nincs vetítési időpont."));
            return;
        }

        // 1. Vetítések csoportosítása dátum szerint
        Map<LocalDate, List<Showtime>> showtimesByDate = movie.getShowtimes().stream()
                .sorted(Comparator.comparing(Showtime::getTime)) // Rendezés idő szerint
                .collect(Collectors.groupingBy(showtime -> showtime.getTime().toLocalDate()));

        // 2. Csoportok megjelenítése
        showtimesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Rendezés dátum szerint (Ma, Holnap, stb.)
                .forEach(entry -> {
                    LocalDate date = entry.getKey();
                    List<Showtime> dailyShowtimes = entry.getValue();

                    // Dátum Fejléc címke
                    Label dateHeader = new Label(formatDateHeader(date, today));
                    dateHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-padding: 15 0 5 0;");

                    showtimesFlowPane.getChildren().add(dateHeader);

                    // Egy FlowPane csak az adott nap vetítéseinek gombjaihoz
                    FlowPane dailyShowtimesContainer = new FlowPane(10, 10); // 10px horizontális és vertikális távolság

                    dailyShowtimes.forEach(showtime -> {
                        Button timeButton = new Button(showtime.toString());
                        timeButton.setStyle("-fx-background-color: #ff6600; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 15; -fx-background-radius: 5; -fx-cursor: hand;");
                        timeButton.setOnAction(e -> handleShowtimeSelection(showtime));
                        dailyShowtimesContainer.getChildren().add(timeButton);
                    });

                    showtimesFlowPane.getChildren().add(dailyShowtimesContainer);
                });
    }

    /**
     * Segédmetódus a dátum fejléc formázásához (pl. "2025. november 18. (Ma)").
     */
    private String formatDateHeader(LocalDate date, LocalDate today) {
        String formattedDate = date.format(DATE_FORMATTER);
        if (date.isEqual(today)) {
            return formattedDate + " (Ma)";
        } else if (date.isEqual(today.plusDays(1))) {
            return formattedDate + " (Holnap)";
        } else {
            return formattedDate;
        }
    }


    /**
     * Időpont kiválasztása. Ez a metódus adja át az adatokat a következő fejlesztőnek.
     */
    private void handleShowtimeSelection(Showtime showtime) {
        String movieTitle = currentMovie.getTitle();
        String showtimeDetails = showtime.toString();

        // LOGIKA: Továbblépés a Helyfoglalás oldalra (a másik fejlesztő feladata)
        System.out.println("Időpont kiválasztva! Tovább a helyfoglaláshoz:");
        System.out.println("  Film azonosító: " + currentMovie.getId());
        System.out.println("  Film címe: " + movieTitle);
        System.out.println("  Időpont azonosító: " + showtime.getShowtimeId());
        System.out.println("  Időpont: " + showtimeDetails);

        // Egy egyszerű üzenet a felhasználónak (NEM alert()!)
        Label confirmation = new Label("Sikeresen kiválasztva: " + showtimeDetails + ". Tovább a foglaláshoz...");
        confirmation.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 20 0 0 0;");
        // Hozzáadjuk az üzenetet egy új VBox-ba, hogy felülírja az időpontokat
        VBox messageBox = new VBox(confirmation);
        messageBox.setSpacing(10);

        showtimesFlowPane.getChildren().clear();
        showtimesFlowPane.getChildren().add(messageBox);
    }

    @FXML
    private void goBackToList() {
        try {
            // Vissza a filmlista oldalra
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/MovieList.fxml"));
            Pane root = loader.load();

            MovieListController controller = loader.getController();
            controller.setStage(stage);

            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.setTitle("Absolute Cinema - Filmlista");
        } catch (IOException e) {
            System.err.println("Hiba a MovieList.fxml betöltésekor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}