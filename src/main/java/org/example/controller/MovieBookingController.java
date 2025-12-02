package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.Film;
import org.example.model.Foglalas;
import org.example.model.Idopont;
import org.example.model.Ulohely;
import org.example.service.DatabaseMoziService;
import org.example.service.MoziService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieBookingController implements BaseController {

    private Stage stage;
    private Film currentFilm;
    private Idopont currentIdopont;

    // HEADER ELEMEK
    @FXML private Button backButton;
    @FXML private Label headerTitleLabel;

    // FŐ ELEMEK
    @FXML private VBox seatContainer;
    @FXML private Button confirmButton;

    private final MoziService service = new DatabaseMoziService();

    // Csak egyszer létezik!
    private Map<Ulohely, Button> seatButtons = new HashMap<>();
    private Ulohely selectedSeat = null;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setFilmAndIdopont(Film film, Idopont idopont) {
        this.currentFilm = film;
        this.currentIdopont = idopont;

        // HEADER BEÁLLÍTÁSA
        headerTitleLabel.setText(film.getCim());
        backButton.setOnAction(e -> goBackToList());

        // ÜLÉSEK LEKÉRÉSE
        List<Ulohely> ulohelyek = service.getUlohelyekIdoponthoz(idopont.getIdopontId());

        if (ulohelyek.isEmpty()) {
            goBackToList();
            return;
        }

        List<Foglalas> foglalasok = service.getFoglalasokIdoponthoz(idopont.getIdopontId());

        // Rendezés sor → székszám szerint
        ulohelyek.sort(
                Comparator.comparing(Ulohely::getSorJele)
                        .thenComparing(Ulohely::getUlohelySzam)
        );

        seatContainer.getChildren().clear();
        seatButtons.clear();

        String currentRow = "";
        HBox rowBox = null;

        // Soronként feltöltés
        for (Ulohely u : ulohelyek) {

            // ÚJ SOR
            if (!u.getSorJele().equals(currentRow)) {
                currentRow = u.getSorJele();
                rowBox = new HBox(10);

                Label rowLabel = new Label(currentRow);
                rowLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                rowBox.getChildren().add(rowLabel);
                seatContainer.getChildren().add(rowBox);
            }

            Button seatBtn = new Button(String.valueOf(u.getUlohelySzam()));
            seatBtn.setPrefSize(40, 40);

            boolean foglalt = foglalasok.stream()
                    .anyMatch(f -> f.getUlohelyId() == u.getUlohelyId());

            if (foglalt) {
                seatBtn.setStyle(
                        "-fx-background-color: #cc0000;" +
                                "-fx-text-fill: white; -fx-font-weight: bold;"
                );
                seatBtn.setDisable(true);
            } else {
                seatBtn.setStyle(
                        "-fx-background-color: #00cc66;" +
                                "-fx-text-fill: white; -fx-font-weight: bold;"
                );
                seatBtn.setOnAction(e -> selectSeat(u));
            }

            rowBox.getChildren().add(seatBtn);
            seatButtons.put(u, seatBtn);
        }

        confirmButton.setDisable(false);
        confirmButton.setOnAction(e -> confirmBooking());
    }


    private void selectSeat(Ulohely u) {
        if (selectedSeat != null && seatButtons.containsKey(selectedSeat)) {
            Button oldBtn = seatButtons.get(selectedSeat);
            oldBtn.setStyle(
                    "-fx-background-color: #00cc66;" +
                            "-fx-text-fill: white; -fx-font-weight: bold;"
            );
        }

        selectedSeat = u;
        Button selectedBtn = seatButtons.get(u);
        selectedBtn.setStyle(
                "-fx-background-color: #ffaa00;" +
                        "-fx-text-fill: white; -fx-font-weight: bold;"
        );
    }

    private void confirmBooking() {
        if (selectedSeat == null) return;

        // 1. Megszerezzük a bejelentkezett felhasználót a MainController-ből
        int userId;
        if (MainController.loggedInUser != null) {
            userId = MainController.loggedInUser.getFelhasznaloId();
        } else {
            System.out.println("Hiba: Nincs bejelentkezett felhasználó!");
            return; // Megállítjuk a folyamatot, ha senki nincs bejelentkezve
        }

        // 2. A 'userId'-t adjuk át a szolgáltatásnak
        service.megprobalFoglalni(currentIdopont.getIdopontId(), selectedSeat.getUlohelyId(), userId);

        // Gomb színezése és tiltása
        Button selectedBtn = seatButtons.get(selectedSeat);
        selectedBtn.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white; -fx-font-weight: bold;");
        selectedBtn.setDisable(true);

        confirmButton.setText("Foglalás kész!");
        confirmButton.setDisable(true);

        // Visszalépés (opcionális, vagy várhatsz egy kicsit)
        goBackToList();
    }


    @FXML
    private void goBackToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MovieList.fxml"));
            Pane root = loader.load();

            MovieListController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root, 1000, 700);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
