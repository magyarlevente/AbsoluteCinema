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
import javafx.stage.Stage;
import model.Movie;
import model.Showtime;

import java.io.IOException;

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
    @FXML private FlowPane showtimesFlowPane;
    @FXML private Button backButton;

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
     * Dinamikusan létrehozza a gombokat az időpontokhoz.
     */
    private void displayShowtimes(Movie movie) {
        showtimesFlowPane.getChildren().clear();

        if (movie.getShowtimes().isEmpty()) {
            showtimesFlowPane.getChildren().add(new Label("Ezen a napon nincs vetítési időpont."));
            return;
        }

        for (Showtime showtime : movie.getShowtimes()) {
            Button timeButton = new Button(showtime.getTime().toLocalTime().toString() + " (" + showtime.getCinemaName() + ")");
            // Esztétikus stílus a gomboknak
            timeButton.setStyle("-fx-background-color: #0099cc; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 15; -fx-background-radius: 5; -fx-cursor: hand;");

            timeButton.setOnAction(e -> handleShowtimeSelection(showtime));
            showtimesFlowPane.getChildren().add(timeButton);
        }
    }

    /**
     * Időpont kiválasztása. Ez a metódus adja át az adatokat a következő fejlesztőnek.
     */
    private void handleShowtimeSelection(Showtime showtime) {
        String movieTitle = currentMovie.getTitle();
        String showtimeDetails = showtime.getTime().toLocalTime().toString() + " (" + showtime.getCinemaName() + ")";

        // LOGIKA: Továbblépés a Helyfoglalás oldalra (a másik fejlesztő feladata)
        System.out.println("Időpont kiválasztva! Tovább a helyfoglaláshoz:");
        System.out.println("  Film azonosító: " + currentMovie.getId());
        System.out.println("  Film címe: " + movieTitle);
        System.out.println("  Időpont azonosító: " + showtime.getShowtimeId());
        System.out.println("  Időpont: " + showtimeDetails);

        // Itt jönne a loadScene("view/SeatBooking.fxml", "Helyfoglalás") hívás,
        // ahol átadnánk a currentMovie.getId() és showtime.getShowtimeId() adatokat.

        // Egy egyszerű üzenet a felhasználónak (NEM alert()!)
        Label confirmation = new Label("Sikeresen kiválasztva: " + showtimeDetails + ". Tovább a foglaláshoz...");
        confirmation.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 16px;");
        // Hozzáadjuk a FlowPane-hez, de előtte töröljük a korábbi gombokat, hogy az üzenet kiemelkedjen (egyszerű megoldás)
        showtimesFlowPane.getChildren().clear();
        showtimesFlowPane.getChildren().add(confirmation);
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