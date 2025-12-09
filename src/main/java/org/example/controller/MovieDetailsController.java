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
import org.example.service.DatabaseMoziService;
import org.example.service.MoziService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

//kiirja az adott Film objektum adatait es a hozzatartozo vetitesi idopontokat

public class MovieDetailsController implements BaseController {

    private Stage stage;
    private Film currentFilm;
    private LocalDate filterDate;

    // FXML elemek a sötét dizájnhoz
    @FXML private Label titleLabel;
    @FXML private ImageView posterView;
    @FXML private Label durationLabel;
    @FXML private Label ageRatingLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label dateLabel;
    @FXML private Label genreLabel;
    @FXML private Label originalTitleLabel;

    @FXML private FlowPane showtimesFlowPane;

    private final MoziService service = new DatabaseMoziService();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy. MMMM dd.");

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setFilterDate(LocalDate date) {
        this.filterDate = date;
    }

    public void setMovie(Film film) {
        this.currentFilm = film;

        // 1. Alap adatok kitöltése
        if (titleLabel != null) titleLabel.setText(film.getCim().toUpperCase()); // Nagybetűs cím
        if (originalTitleLabel != null) originalTitleLabel.setText(film.getCim()); // Eredeti cím helyett is a cím
        if (genreLabel != null) genreLabel.setText(film.getMufaj());
        if (durationLabel != null) durationLabel.setText(film.getJatekido() + " perc");
        if (ageRatingLabel != null) ageRatingLabel.setText(String.valueOf(film.getKorhatar()));
        if (descriptionLabel != null) descriptionLabel.setText(film.getFilmLeiras());

        // 2. Dátum beállítása a fejlécbe
        LocalDate displayDate = (filterDate != null) ? filterDate : LocalDate.now();
        if (dateLabel != null) {
            dateLabel.setText(displayDate.format(DATE_FORMATTER));
        }

        // 3. Poszter betöltése
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

        // 4. Vetítések listázása
        displayShowtimes(film);
    }

    private void displayShowtimes(Film film) {
        if (showtimesFlowPane == null) return;

        showtimesFlowPane.getChildren().clear();
        List<Idopont> idopontok = service.getIdopontokFilmhez(film.getFilmId());

        if (idopontok.isEmpty()) {
            Label l = new Label("Nincs elérhető vetítés.");
            l.setStyle("-fx-text-fill: #999999;");
            showtimesFlowPane.getChildren().add(l);
            return;
        }

        boolean talaltVetitest = false;

        for (Idopont idopont : idopontok) {
            // SZŰRÉS LOGIKA
            if (filterDate != null && !idopont.getKezdesIdopont().toLocalDate().equals(filterDate)) {
                continue;
            }

            talaltVetitest = true;

            javafx.scene.control.Button timeButton = new javafx.scene.control.Button(
                    idopont.getKezdesIdopont().format(TIME_FORMATTER)
            );
            timeButton.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-text-fill: #ffcc00; " +
                            "-fx-border-color: #ffcc00; " +
                            "-fx-border-width: 2; " +
                            "-fx-font-weight: bold; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 8 20; " +
                            "-fx-font-size: 14px;"
            );

            timeButton.setOnMouseEntered(e -> timeButton.setStyle(
                    "-fx-background-color: #ffcc00; -fx-text-fill: black; -fx-border-color: #ffcc00; -fx-border-width: 2; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 20; -fx-font-size: 14px;"
            ));
            timeButton.setOnMouseExited(e -> timeButton.setStyle(
                    "-fx-background-color: transparent; -fx-text-fill: #ffcc00; -fx-border-color: #ffcc00; -fx-border-width: 2; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 20; -fx-font-size: 14px;"
            ));

            timeButton.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MovieBooking.fxml"));
                    Pane root = loader.load();

                    MovieBookingController controller = loader.getController();
                    controller.setStage(stage);
                    controller.setFilmAndIdopont(film, idopont);

                    stage.setScene(new Scene(root));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            showtimesFlowPane.getChildren().add(timeButton);
        }

        if (!talaltVetitest) {
            Label l = new Label("Nincs vetítés a kiválasztott napon.");
            l.setStyle("-fx-text-fill: #999999;");
            showtimesFlowPane.getChildren().add(l);
        }
    }

    @FXML
    private void goBackToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MovieList.fxml"));
            Pane root = loader.load();

            MovieListController controller = loader.getController();
            controller.setStage(stage);

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}