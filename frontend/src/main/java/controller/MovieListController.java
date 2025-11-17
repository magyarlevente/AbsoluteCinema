package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Movie;
import model.MovieData;
import model.Showtime;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vezérlő a Filmlista oldalhoz (MovieList.fxml).
 */
public class MovieListController implements BaseController {

    private Stage stage;
    private ObservableList<Movie> allMovies;

    @FXML private VBox movieListContainer;
    @FXML private ScrollPane movieScrollPane;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> genreFilter;
    @FXML private ComboBox<String> cinemaFilter; // Új szűrő a mozihoz/városhoz
    @FXML private Button backButton;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Dummy adatok betöltése
        allMovies = FXCollections.observableArrayList(MovieData.createDummyMovies());

        // Szűrő ComboBox-ok feltöltése
        List<String> genres = allMovies.stream()
                .map(Movie::getGenre)
                .distinct()
                .collect(Collectors.toList());
        genreFilter.setItems(FXCollections.observableArrayList(genres));
        genreFilter.setPromptText("Műfaj szűrés...");

        // Mozi nevek gyűjtése (a dummy adatokból)
        List<String> cinemas = allMovies.stream()
                .flatMap(m -> m.getShowtimes().stream())
                .map(Showtime::getCinemaName)
                .distinct()
                .collect(Collectors.toList());
        cinemaFilter.setItems(FXCollections.observableArrayList(cinemas));
        cinemaFilter.setPromptText("Mozi / Város szűrés...");


        // Eseménykezelők beállítása (a szűrés meghívásához)
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> filterMovies());
        genreFilter.valueProperty().addListener((obs, oldVal, newVal) -> filterMovies());
        cinemaFilter.valueProperty().addListener((obs, oldVal, newVal) -> filterMovies());

        // Kezdeti lista megjelenítése
        displayMovies(allMovies);
    }

    @FXML
    private void filterMovies() {
        LocalDate selectedDate = datePicker.getValue();
        String selectedGenre = genreFilter.getSelectionModel().getSelectedItem();
        String selectedCinema = cinemaFilter.getSelectionModel().getSelectedItem();

        List<Movie> filtered = allMovies.stream()
                .filter(m -> {
                    // Műfaj szűrés
                    boolean genreMatch = selectedGenre == null || m.getGenre().equals(selectedGenre);

                    // Mozi/Város és Dátum szűrés (Összekapcsolva)
                    boolean hasMatchingShowtime = m.getShowtimes().stream()
                            .anyMatch(st -> {
                                boolean dateMatch = selectedDate == null || st.getTime().toLocalDate().isEqual(selectedDate);
                                boolean cinemaMatch = selectedCinema == null || st.getCinemaName().equals(selectedCinema);

                                return dateMatch && cinemaMatch;
                            });

                    return genreMatch && hasMatchingShowtime;
                })
                .collect(Collectors.toList());

        displayMovies(FXCollections.observableArrayList(filtered));
    }

    /**
     * Filmek listájának dinamikus megjelenítése.
     */
    private void displayMovies(ObservableList<Movie> movies) {
        movieListContainer.getChildren().clear(); // Előző elemek törlése
        movieListContainer.setStyle("-fx-background-color: white;"); // A VBox háttere

        if (movies.isEmpty()) {
            Label noResults = new Label("Nincsenek a feltételeknek megfelelő filmek.");
            noResults.setStyle("-fx-font-size: 18px; -fx-text-fill: #555; -fx-padding: 20px;");
            movieListContainer.getChildren().add(noResults);
            return;
        }

        for (Movie movie : movies) {
            HBox movieRow = createMovieRow(movie);
            movieListContainer.getChildren().add(movieRow);
        }
    }

    /**
     * Létrehozza egy film sorát a listában, esztétikus stílussal.
     */
    private HBox createMovieRow(Movie movie) {
        HBox row = new HBox(20); // HBox a sor elrendezéséhez, 20px távolság
        row.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0; -fx-padding: 15; -fx-alignment: center-left;");

        // Poszter kép
        ImageView poster = new ImageView();
        try {
            poster.setImage(new Image(movie.getPosterUrl(), true));
        } catch (Exception e) {
            // Hiba esetén placeholder kép
            poster.setImage(new Image("https://placehold.co/100x150/ff6600/ffffff?text=NINCS+KÉP"));
        }
        poster.setFitWidth(100);
        poster.setFitHeight(150);
        poster.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.0, 1, 1);");


        // Film adatok
        VBox infoBox = new VBox(5);
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");

        Label detailsLabel = new Label(String.format(
                "Játékidő: %d perc | Korhatár: %d+ | Műfaj: %s",
                movie.getDurationMinutes(),
                movie.getAgeRating(),
                movie.getGenre()
        ));
        detailsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        // Leírás (rövid kivonat)
        Label descriptionPreview = new Label(movie.getDescription().substring(0, Math.min(movie.getDescription().length(), 150)) + "...");
        descriptionPreview.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
        descriptionPreview.setWrapText(true);
        descriptionPreview.setMaxWidth(600);


        Button selectButton = new Button("Részletek és időpontok");
        selectButton.setOnAction(e -> goToMovieDetails(movie));
        selectButton.setStyle("-fx-background-color: #ff6600; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 5; -fx-cursor: hand;");

        infoBox.getChildren().addAll(titleLabel, detailsLabel, descriptionPreview, selectButton);

        row.getChildren().addAll(poster, infoBox);

        return row;
    }

    /**
     * Navigáció a Film Részletező oldalra.
     */
    private void goToMovieDetails(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/MovieDetails.fxml"));
            Pane root = loader.load();

            MovieDetailsController controller = loader.getController();
            controller.setStage(stage);
            // Átadjuk a kiválasztott film adatait a részletező oldalnak
            controller.setMovie(movie);

            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.setTitle("Absolute Cinema - Film Részletek: " + movie.getTitle());
        } catch (IOException e) {
            System.err.println("Hiba a MovieDetails.fxml betöltésekor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackToMain() {
        // Vissza a főmenübe
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/Main.fxml"));
            Pane root = loader.load();

            MainController controller = loader.getController();
            controller.setStage(stage);

            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.setTitle("Absolute Cinema - Jegyfoglalás");
        } catch (IOException e) {
            System.err.println("Hiba a Main.fxml betöltésekor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}