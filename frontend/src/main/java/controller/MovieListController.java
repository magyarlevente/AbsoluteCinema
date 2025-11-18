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
import javafx.scene.layout.Priority; // Az import helye
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Movie;
import model.MovieData;
import model.Showtime;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vezérlő a Filmlista oldalhoz (MovieList.fxml).
 */
public class MovieListController implements BaseController {

    private Stage stage;
    private ObservableList<Movie> allMovies;
    private ObservableList<Movie> filteredMovies; // A szűrt filmek listája

    @FXML private VBox movieListContainer;
    @FXML private ScrollPane movieScrollPane;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> genreFilter;
    @FXML private ComboBox<String> cinemaFilter;
    @FXML private Button backButton;

    // Megjelenítéshez használt dátum formátum
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        // Az inicializálás a Stage beállítása után történjen
        initializeData();
        populateFilters();
        displayMovies(allMovies);
    }

    /**
     * Adatmodellek inicializálása.
     */
    private void initializeData() {
        // Dummy adatok betöltése
        List<Movie> dummyList = MovieData.createDummyMovies();
        this.allMovies = FXCollections.observableArrayList(dummyList);
        this.filteredMovies = FXCollections.observableArrayList(dummyList);
    }

    /**
     * Szűrők (ComboBoxok) feltöltése.
     */
    private void populateFilters() {
        // Műfajok feltöltése
        List<String> genres = allMovies.stream()
                .map(Movie::getGenre)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        ObservableList<String> genreItems = FXCollections.observableArrayList("Minden műfaj");
        genreItems.addAll(genres);
        genreFilter.setItems(genreItems);
        genreFilter.getSelectionModel().selectFirst();

        // Mozi/Város feltöltése (a vetítési időpontokból)
        List<String> cinemas = allMovies.stream()
                .flatMap(movie -> movie.getShowtimes().stream())
                .map(Showtime::getCinemaName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        ObservableList<String> cinemaItems = FXCollections.observableArrayList("Összes mozi");
        cinemaItems.addAll(cinemas);
        cinemaFilter.setItems(cinemaItems);
        cinemaFilter.getSelectionModel().selectFirst();

        // Dátum alapértelmezettre állítása (mai nap)
        datePicker.setValue(LocalDate.now());
    }

    /**
     * A filmlista tartalmának megjelenítése a VBox-ban.
     */
    private void displayMovies(List<Movie> movies) {
        movieListContainer.getChildren().clear();

        if (movies.isEmpty()) {
            Label noResults = new Label("Nincs a szűrőfeltételeknek megfelelő vetítés.");
            noResults.setStyle("-fx-font-size: 18px; -fx-text-fill: #666666; -fx-padding: 30 0 0 0;");
            movieListContainer.getChildren().add(noResults);
            return;
        }

        for (Movie movie : movies) {
            movieListContainer.getChildren().add(createMovieCard(movie));
        }
    }

    /**
     * Létrehoz egy modern, kártya alapú megjelenítést egy filmhez (HBox).
     */
    private HBox createMovieCard(Movie movie) {
        // --- 1. ALAP KÁRTYA ELRENDEZÉS ---
        HBox card = new HBox(20);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.0, 0, 2);");
        card.setPrefHeight(200);
        card.setMaxWidth(Double.MAX_VALUE); // Teljes szélesség kitöltése

        // --- 2. POSZTER (Bal oldal) ---
        ImageView posterView = new ImageView();
        posterView.setFitWidth(100);
        posterView.setFitHeight(150);
        posterView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0.0, 1, 1);");
        try {
            // Betöltjük a képet. Ha hiba van, a fallback kép töltődik be.
            posterView.setImage(new Image(movie.getPosterUrl(), true));
        } catch (Exception e) {
            // Placeholder kép URL
            posterView.setImage(new Image("https://placehold.co/100x150/999999/ffffff?text=FILM+KÉP"));
        }

        // --- 3. INFORMÁCIÓK (Középső VBox) ---
        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(600);
        // JAVÍTVA: A statikus metódust a HBox osztályon kell hívni!
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Cím
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");

        // Műfaj, Korhatár, Játékidő HBox
        HBox metaBox = new HBox(15);
        metaBox.setStyle("-fx-opacity: 0.8;");
        metaBox.getChildren().addAll(
                createTagLabel("Műfaj: " + movie.getGenre(), "#cccccc", "#333333"),
                createTagLabel("Korhatár: " + movie.getAgeRating() + "+", "#ff6600", "white"), // Narancssárga korhatár kiemelés
                new Label("Játékidő: " + movie.getDurationMinutes() + " perc")
        );

        // Leírás (rövidített)
        Label descriptionLabel = new Label(movie.getDescription().substring(0, Math.min(movie.getDescription().length(), 150)) + "...");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        // Vetítési időpontok (dinamikus)
        HBox showtimesBox = new HBox(8);
        showtimesBox.setStyle("-fx-padding: 5 0 0 0;");
        Label showtimesTitle = new Label("Vetítések ma:");
        showtimesTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a1a1a;");
        showtimesBox.getChildren().add(showtimesTitle);

        // Csak a mai napon érvényes vetítések megjelenítése
        movie.getShowtimes().stream()
                .filter(st -> st.getTime().toLocalDate().isEqual(LocalDate.now()))
                .sorted((st1, st2) -> st1.getTime().compareTo(st2.getTime()))
                .limit(5) // Max 5 időpont a listában
                .forEach(showtime -> {
                    Label timeLabel = new Label(showtime.getTime().toLocalTime().format(TIME_FORMATTER));
                    timeLabel.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 2 8; -fx-background-radius: 3; -fx-font-size: 12px;");
                    showtimesBox.getChildren().add(timeLabel);
                });

        infoBox.getChildren().addAll(titleLabel, metaBox, descriptionLabel, showtimesBox);

        // --- 4. AKCIÓ GOMB (Jobb oldal) ---
        VBox actionBox = new VBox();
        actionBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        actionBox.setSpacing(10);
        // JAVÍTVA: A statikus metódust a HBox osztályon kell hívni!
        HBox.setHgrow(actionBox, Priority.NEVER);

        Button detailsButton = new Button("Részletek & Foglalás");
        detailsButton.setStyle("-fx-background-color: #ff6600; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0.0, 0, 1);");
        detailsButton.setOnAction(e -> goToMovieDetails(movie));

        actionBox.getChildren().add(detailsButton);

        // --- 5. ÖSSZEFOGLALÁS ---
        card.getChildren().addAll(posterView, infoBox, actionBox);
        return card;
    }

    /**
     * Segédmetódus a címkék szebb megjelenítéséhez.
     */
    private Label createTagLabel(String text, String bgColor, String textColor) {
        Label label = new Label(text);
        label.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-padding: 2 8; -fx-background-radius: 3; -fx-font-size: 12px; -fx-font-weight: bold;");
        return label;
    }

    @FXML
    public void filterMovies() {
        LocalDate selectedDate = datePicker.getValue();
        String selectedGenre = genreFilter.getSelectionModel().getSelectedItem();
        String selectedCinema = cinemaFilter.getSelectionModel().getSelectedItem();

        // Szűrés a teljes listán
        List<Movie> results = allMovies.stream()
                .filter(movie -> {
                    // Szűrés műfaj szerint
                    boolean genreMatch = selectedGenre == null || selectedGenre.equals("Minden műfaj") || movie.getGenre().equals(selectedGenre);

                    // Szűrés Dátum és Mozi szerint (Vetítések alapján)
                    List<Showtime> relevantShowtimes = movie.getShowtimes().stream()
                            .filter(showtime -> {
                                boolean dateMatch = selectedDate == null || showtime.getTime().toLocalDate().isEqual(selectedDate);
                                boolean cinemaMatch = selectedCinema == null || selectedCinema.equals("Összes mozi") || showtime.getCinemaName().equals(selectedCinema);
                                return dateMatch && cinemaMatch;
                            })
                            .collect(Collectors.toList());

                    // Csak azokat a filmeket tartjuk meg, amelyeknek van releváns vetítése ÉS a műfaj is illeszkedik
                    return genreMatch && !relevantShowtimes.isEmpty();
                })
                .collect(Collectors.toList());

        filteredMovies.setAll(results);
        displayMovies(filteredMovies);
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

            // Mivel a MainController nem implementálja a BaseController-t, csak közvetlenül állítjuk be a stage-et.
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