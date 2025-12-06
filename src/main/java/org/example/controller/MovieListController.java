package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.example.model.Film;
import org.example.model.Idopont;
import org.example.model.Terem;
import org.example.service.DatabaseMoziService;
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
    private final MoziService service = new DatabaseMoziService();

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
        List<String> genres = allMovies.stream()
                .map(Film::getMufaj)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        ObservableList<String> genreItems = FXCollections.observableArrayList("Minden műfaj");
        genreItems.addAll(genres);
        genreFilter.setItems(genreItems);
        genreFilter.getSelectionModel().selectFirst();

        ObservableList<String> cinemaItems = FXCollections.observableArrayList("Minden mozi", "Cinema City Debrecen", "Apolló Mozi");
        cinemaFilter.setItems(cinemaItems);
        cinemaFilter.getSelectionModel().selectFirst();

        datePicker.setValue(LocalDate.now());
    }

    @FXML
    public void filterMovies() {
        String selectedGenre = genreFilter.getValue();
        String selectedCinema = cinemaFilter.getValue();
        LocalDate selectedDate = datePicker.getValue();

        List<Film> filteredList = allMovies.stream()
                .filter(film -> {
                    if (selectedGenre != null && !"Minden műfaj".equals(selectedGenre)) {
                        if (!film.getMufaj().toLowerCase().contains(selectedGenre.toLowerCase())) {
                            return false;
                        }
                    }

                    List<Idopont> idopontok = service.getIdopontokFilmhez(film.getFilmId());
                    return idopontok.stream().anyMatch(idopont -> {
                        boolean datumOk = (selectedDate == null) || idopont.getKezdesIdopont().toLocalDate().equals(selectedDate);
                        boolean moziOk = true;
                        if (selectedCinema != null && !"Minden mozi".equals(selectedCinema)) {
                            Terem terem = service.getTeremById(idopont.getTeremId());
                            if (terem != null) {
                                if (selectedCinema.contains("Cinema City") && !terem.getTeremNev().startsWith("CC")) moziOk = false;
                                else if (selectedCinema.contains("Apolló") && !terem.getTeremNev().startsWith("Apollo")) moziOk = false;
                            }
                        }
                        return datumOk && moziOk;
                    });
                })
                .collect(Collectors.toList());

        displayMovies(filteredList);
    }

    private void displayMovies(List<Film> movies) {
        movieListContainer.getChildren().clear();

        if (movies.isEmpty()) {
            Label placeholder = new Label("Nincs találat a megadott feltételekkel.");
            placeholder.setStyle("-fx-font-size: 18px; -fx-padding: 30; -fx-text-fill: #999999;");
            movieListContainer.getChildren().add(placeholder);
            return;
        }

        for (Film film : movies) {
            movieListContainer.getChildren().add(createMovieCard(film));
        }
    }

    private HBox createMovieCard(Film film) {
        HBox card = new HBox(25);

        card.setStyle("-fx-background-color: #222222; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.0, 0, 5);");

        ImageView posterView = new ImageView();
        posterView.setFitWidth(160);
        posterView.setFitHeight(240);
        posterView.setPreserveRatio(true);
        posterView.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.05), 10, 0, 0, 0);");

        try {
            String url = film.getPoszterUrl();
            if(url != null && !url.isEmpty()) {
                posterView.setImage(new Image(url, true));
            }
        } catch (Exception e) {}

        VBox infoBox = new VBox(10);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        infoBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label titleLabel = new Label(film.getCim());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        titleLabel.setWrapText(true);


        Label metaLabel = new Label("Műfaj: " + film.getMufaj() + " | Játékidő: " + film.getJatekido() + " perc | Korhatár: " + film.getKorhatar() + "+");
        metaLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 14px;");

        Label descLabel = new Label(film.getFilmLeiras());
        descLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 13px;");
        descLabel.setWrapText(true);
        descLabel.setMaxHeight(60);

        HBox showtimesBox = new HBox(10);
        List<Idopont> idopontok = service.getIdopontokFilmhez(film.getFilmId());
        LocalDate filterDate = datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now();
        String filterCinema = cinemaFilter.getValue();

        for (Idopont idopont : idopontok) {
            if (idopont.getKezdesIdopont().toLocalDate().equals(filterDate)) {
                boolean show = true;
                if (filterCinema != null && !"Minden mozi".equals(filterCinema)) {
                    Terem t = service.getTeremById(idopont.getTeremId());
                    if (t != null) {
                        if (filterCinema.contains("Cinema City") && !t.getTeremNev().startsWith("CC")) show = false;
                        else if (filterCinema.contains("Apolló") && !t.getTeremNev().startsWith("Apollo")) show = false;
                    }
                }

                if (show) {
                    Label timeLabel = new Label(idopont.getKezdesIdopont().format(TIME_FORMATTER));
                    timeLabel.setStyle("-fx-background-color: #333333; -fx-padding: 5 10; -fx-background-radius: 5; -fx-text-fill: #ffcc00; -fx-font-weight: bold;");
                    showtimesBox.getChildren().add(timeLabel);
                }
            }
        }

        if(showtimesBox.getChildren().isEmpty()) {
            Label noShowLabel = new Label("Nincs vetítés a kiválasztott feltételekkel");
            noShowLabel.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
            showtimesBox.getChildren().add(noShowLabel);
        }

        infoBox.getChildren().addAll(titleLabel, metaLabel, descLabel, showtimesBox);

        Button detailsButton = new Button("RÉSZLETEK");
        detailsButton.setPrefHeight(50);
        detailsButton.setPrefWidth(120);
        detailsButton.setStyle("-fx-background-color: #ff6600; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand; -fx-background-radius: 5;");
        detailsButton.setOnAction(e -> goToMovieDetails(film));

        VBox actionBox = new VBox(detailsButton);
        actionBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        card.getChildren().addAll(posterView, infoBox, actionBox);
        return card;
    }

    private void goToMovieDetails(Film film) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MovieDetails.fxml"));
            Parent root = loader.load();

            MovieDetailsController controller = loader.getController();
            controller.setStage(stage);
            controller.setFilterDate(datePicker.getValue());
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
            Parent root = loader.load();
            MainController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}