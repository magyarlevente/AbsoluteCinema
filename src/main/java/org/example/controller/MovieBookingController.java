package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import org.example.service.FoglalasiEredmeny;
import org.example.service.MoziService;

import java.util.ArrayList;
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

    // Tároljuk a gombokat, hogy tudjunk rajta stílust váltani
    private Map<Ulohely, Button> seatButtons = new HashMap<>();

    // Lista a kiválasztott székeknek (Többes kijelölés!)
    private List<Ulohely> selectedSeats = new ArrayList<>();

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setFilmAndIdopont(Film film, Idopont idopont) {
        this.currentFilm = film;
        this.currentIdopont = idopont;

        headerTitleLabel.setText(film.getCim());
        backButton.setOnAction(e -> goBackToList());

        List<Ulohely> ulohelyek = service.getUlohelyekIdoponthoz(idopont.getIdopontId());

        if (ulohelyek.isEmpty()) {
            goBackToList();
            return;
        }

        List<Foglalas> foglalasok = service.getFoglalasokIdoponthoz(idopont.getIdopontId());

        ulohelyek.sort(
                Comparator.comparing(Ulohely::getSorJele)
                        .thenComparing(Ulohely::getUlohelySzam)
        );

        seatContainer.getChildren().clear();
        seatButtons.clear();
        selectedSeats.clear(); // Lista törlése belépéskor

        String currentRow = "";
        HBox rowBox = null;

        for (Ulohely u : ulohelyek) {

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
                seatBtn.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white; -fx-font-weight: bold;");
                seatBtn.setDisable(true);
            } else {
                // Alapállapot: Zöld
                seatBtn.setStyle(
                        "-fx-background-color: #00cc66;" +
                                "-fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;"
                );
                seatBtn.setOnAction(e -> toggleSeatSelection(u));
            }

            rowBox.getChildren().add(seatBtn);
            seatButtons.put(u, seatBtn);
        }

        // Kezdőállapot: gomb tiltva
        confirmButton.setText("Válassz helyet!");
        confirmButton.setDisable(true);
        confirmButton.setOnAction(e -> confirmBooking());
    }

    private void toggleSeatSelection(Ulohely u) {
        Button btn = seatButtons.get(u);

        if (selectedSeats.contains(u)) {
            // Ha már benne van -> kivesszük (Deselect)
            selectedSeats.remove(u);
            btn.setStyle("-fx-background-color: #00cc66; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        } else {
            // Ha nincs benne -> beletesszük (Select)
            selectedSeats.add(u);
            btn.setStyle("-fx-background-color: #ffaa00; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        }

        // Alsó gomb frissítése
        if (selectedSeats.isEmpty()) {
            confirmButton.setDisable(true);
            confirmButton.setText("Válassz helyet!");
        } else {
            confirmButton.setDisable(false);
            confirmButton.setText("Foglalás megerősítése (" + selectedSeats.size() + " db)");
        }
    }

    private void confirmBooking() {
        if (selectedSeats.isEmpty()) return;

        // 1. Megszerezzük a bejelentkezett felhasználót
        int userId;
        if (MainController.loggedInUser != null) {
            userId = MainController.loggedInUser.getFelhasznaloId();
        } else {
            System.out.println("Hiba: Nincs bejelentkezett felhasználó!");
            return;
        }

        int sikeresDb = 0;

        // 2. Ciklus a kiválasztott helyeken
        for (Ulohely u : selectedSeats) {
            var eredmeny = service.megprobalFoglalni(currentIdopont.getIdopontId(), u.getUlohelyId(), userId);

            if (eredmeny.isSiker()) {
                sikeresDb++;
                // Gomb pirosra váltása és tiltása
                Button selectedBtn = seatButtons.get(u);
                selectedBtn.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white; -fx-font-weight: bold;");
                selectedBtn.setDisable(true);
            }
        }

        // Visszajelzés
        if (sikeresDb == selectedSeats.size()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sikeres foglalás");
            alert.setHeaderText(null);
            alert.setContentText("Sikeresen lefoglaltál " + sikeresDb + " db helyet!");
            alert.showAndWait();

            goBackToList();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Részleges siker");
            alert.setContentText("Csak " + sikeresDb + " helyet sikerült lefoglalni a kért " + selectedSeats.size() + "-ből.");
            alert.showAndWait();

            // Frissítjük a kijelölést (ami sikerült, az már piros, ami nem, az marad sárga vagy zöld)
            selectedSeats.clear();
            confirmButton.setText("Válassz helyet!");
            confirmButton.setDisable(true);
        }
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