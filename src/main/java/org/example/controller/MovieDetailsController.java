package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.example.model.Film;
import org.example.model.Idopont;
import org.example.service.MockMoziService;
import org.example.service.MoziService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MovieDetailsController implements BaseController {

    private Stage stage;
    private Film currentFilm;

    @FXML private Label titleLabel;
    @FXML private ImageView posterView;
    @FXML private Label durationLabel;
    @FXML private Label ageRatingLabel;
    @FXML private Label descriptionLabel;
    @FXML private FlowPane showtimesFlowPane;

    private final MoziService service = new MockMoziService();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("MM.dd. HH:mm");

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMovie(Film film) {
        this.currentFilm = film;

        // Adatok kiírása (biztonsági ellenőrzéssel)
        if (titleLabel != null) titleLabel.setText(film.getCim());
        if (durationLabel != null) durationLabel.setText("Játékidő: " + film.getJatekido() + " perc");
        if (ageRatingLabel != null) ageRatingLabel.setText("Korhatár: " + film.getKorhatar() + "+");
        if (descriptionLabel != null) descriptionLabel.setText(film.getFilmLeiras());

        // Kép betöltése
        if (posterView != null) {
            try {
                String url = film.getPoszterUrl();
                if (url != null && !url.isEmpty()) {
                    posterView.setImage(new Image(url, true));
                }
            } catch (Exception e) {
                System.err.println("Hiba a kép betöltésekor: " + e.getMessage());
            }
        }

        displayShowtimes(film);
    }

    private void displayShowtimes(Film film) {
        if (showtimesFlowPane == null) return;

        showtimesFlowPane.getChildren().clear();
        List<Idopont> idopontok = service.getIdopontokFilmhez(film.getFilmId());

        if (idopontok.isEmpty()) {
            showtimesFlowPane.getChildren().add(new Label("Nincs elérhető vetítés."));
            return;
        }

        for (Idopont idopont : idopontok) {
            javafx.scene.control.Button timeButton = new javafx.scene.control.Button(
                    idopont.getKezdesIdopont().format(TIME_FORMATTER)
            );
            timeButton.setStyle("-fx-background-color: #ff6600; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 5;");

            timeButton.setOnAction(e -> {
                System.out.println("Foglalás kiválasztva: " + film.getCim() + " - " + idopont.getKezdesIdopont());
                // Ide jön majd a foglalás ablakra ugrás
            });

            showtimesFlowPane.getChildren().add(timeButton);
        }
    }

    @FXML
    private void goBackToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MovieList.fxml"));
            Pane root = loader.load();

            MovieListController controller = loader.getController();
            controller.setStage(stage); // Fontos: Stage átadása visszafelé is!

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}