package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.example.model.Film;
import org.example.model.Idopont;
import org.example.service.MockMoziService;
import org.example.service.MoziService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MovieListController implements BaseController {

    private Stage stage;
    private ObservableList<Film> allMovies;

    @FXML private VBox movieListContainer;
    @FXML private ScrollPane movieScrollPane;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> genreFilter;
    @FXML private ComboBox<String> cinemaFilter;
    @FXML private Button backButton;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private final MoziService service = new MockMoziService();

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        initializeData();
        populateFilters();
        displayMovies(allMovies);
    }

    private void initializeData() {
        List<Film> backendFilmek = service.getMindenFilm();
        this.allMovies = FXCollections.observableArrayList(backendFilmek);
    }

    private void populateFilters() {
        // Műfajok
        List<String> genres = allMovies.stream()
                .map(Film::getMufaj)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        ObservableList<String> genreItems = FXCollections.observableArrayList("Minden műfaj");
        genreItems.addAll(genres);
        genreFilter.setItems(genreItems);
        genreFilter.getSelectionModel().selectFirst();

        // Mozik (Ideiglenes, mivel nincs Mozi modell)
        ObservableList<String> cinemaItems = FXCollections.observableArrayList("Minden mozi", "Cinema City Debrecen", "Apolló Mozi");
        cinemaFilter.setItems(cinemaItems);
        cinemaFilter.getSelectionModel().selectFirst();

        datePicker.setValue(LocalDate.now());
    }

    @FXML
    public void filterMovies() {
        String selectedGenre = genreFilter.getValue();
        String selectedCinema = cinemaFilter.getValue(); // Ezt egyelőre nem használjuk a logikában, mert nincs mozi ID
        LocalDate selectedDate = datePicker.getValue();

        List<Film> filteredList = allMovies.stream()
                .filter(film -> {
                    // 1. Műfaj szűrés
                    if (selectedGenre != null && !"Minden műfaj".equals(selectedGenre)) {
                        if (!film.getMufaj().equalsIgnoreCase(selectedGenre)) {
                            return false;
                        }
                    }
                    // 2. Dátum szűrés (Van-e vetítés aznap?)
                    if (selectedDate != null) {
                        List<Idopont> idopontok = service.getIdopontokFilmhez(film.getFilmId());
                        boolean vanVetitesAznap = idopontok.stream()
                                .anyMatch(i -> i.getKezdesIdopont().toLocalDate().equals(selectedDate));
                        if (!vanVetitesAznap) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

        displayMovies(filteredList);
    }

    private void displayMovies(List<Film> movies) {
        movieListContainer.getChildren().clear();

        if (movies.isEmpty()) {
            Label placeholder = new Label("Nincs találat a megadott feltételekkel.");
            placeholder.setStyle("-fx-font-size: 16px; -fx-padding: 20;");
            movieListContainer.getChildren().add(placeholder);
            return;
        }

        for (Film film : movies) {
            movieListContainer.getChildren().add(createMovieCard(film));
        }
    }

    private HBox createMovieCard(Film film) {
        HBox card = new HBox(20);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.0, 0, 2);");

        // POSZTER
        ImageView posterView = new ImageView();
        posterView.setFitWidth(100);
        posterView.setFitHeight(150);
        try {
            String url = film.getPoszterUrl();
            if(url != null && !url.isEmpty()) {
                posterView.setImage(new Image(url, true));
            }
        } catch (Exception e) {
            // Ha hiba van a képpel, nem teszünk semmit (üres marad)
        }

        // INFO
        VBox infoBox = new VBox(5);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label titleLabel = new Label(film.getCim());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label metaLabel = new Label(film.getMufaj() + " | " + film.getJatekido() + " perc | " + film.getKorhatar() + "+");
        metaLabel.setStyle("-fx-text-fill: #666666;");

        // IDŐPONTOK
        HBox showtimesBox = new HBox(10);
        List<Idopont> idopontok = service.getIdopontokFilmhez(film.getFilmId());

        // Csak azokat az időpontokat mutatjuk, amik a kiválasztott napra esnek (vagy maira, ha nincs kiválasztva)
        LocalDate filterDate = datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now();

        for (Idopont idopont : idopontok) {
            if (idopont.getKezdesIdopont().toLocalDate().equals(filterDate)) {
                Label timeLabel = new Label(idopont.getKezdesIdopont().format(TIME_FORMATTER));
                timeLabel.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 4 8; -fx-background-radius: 4; -fx-text-fill: #333;");
                showtimesBox.getChildren().add(timeLabel);
            }
        }

        if(showtimesBox.getChildren().isEmpty()) {
            Label noShowLabel = new Label("Nincs vetítés ezen a napon");
            noShowLabel.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            showtimesBox.getChildren().add(noShowLabel);
        }

        infoBox.getChildren().addAll(titleLabel, metaLabel, showtimesBox);

        // GOMB
        Button detailsButton = new Button("Részletek");
        detailsButton.setStyle("-fx-background-color: #ff6600; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        detailsButton.setOnAction(e -> goToMovieDetails(film));

        VBox actionBox = new VBox(detailsButton);
        actionBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        card.getChildren().addAll(posterView, infoBox, actionBox);
        return card;
    }

    private void goToMovieDetails(Film film) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MovieDetails.fxml"));
            Pane root = loader.load();

            MovieDetailsController controller = loader.getController();
            controller.setStage(stage);
            controller.setMovie(film);

            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
            Pane root = loader.load();
            MainController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}