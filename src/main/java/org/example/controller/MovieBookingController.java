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

import java.util.*;


//szekfoglalas controllere


public class MovieBookingController implements BaseController {

    private Stage stage;
    private Film currentFilm;
    private Idopont currentIdopont;

    @FXML private Button backButton;
    @FXML private Label headerTitleLabel;

    @FXML private VBox seatContainer;
    @FXML private Button confirmButton;

    private final MoziService service = new DatabaseMoziService();

    private Map<Ulohely, Button> seatButtons = new HashMap<>();

    private final Set<Ulohely> selectedSeats = new HashSet<>();

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
        selectedSeats.clear();

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
                seatBtn.setStyle("-fx-background-color: #00cc66; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                seatBtn.setOnAction(e -> toggleSeatSelection(u, seatBtn));
            }

            rowBox.getChildren().add(seatBtn);
            seatButtons.put(u, seatBtn);
        }

        confirmButton.setDisable(false);
        confirmButton.setOnAction(e -> confirmBooking());
    }

    private void toggleSeatSelection(Ulohely u, Button btn) {
        if (selectedSeats.contains(u)) {
            selectedSeats.remove(u);
            btn.setStyle("-fx-background-color: #00cc66; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        } else {
            selectedSeats.add(u);
            btn.setStyle("-fx-background-color: #ffaa00; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #ffffff; -fx-border-width: 2;");
        }

        if (selectedSeats.isEmpty()) {
            confirmButton.setText("Válassz helyet!");
        } else {
            confirmButton.setText("Foglalás megerősítése (" + selectedSeats.size() + " db)");
        }
    }

    private void confirmBooking() {
        if (selectedSeats.isEmpty()) {
            showAlert("Nincs kiválasztott hely", "Kérlek válassz legalább egy ülőhelyet!");
            return;
        }

        int userId;
        if (MainController.loggedInUser != null) {
            userId = MainController.loggedInUser.getFelhasznaloId();
        } else {
            System.out.println("Hiba: Nincs bejelentkezett felhasználó!");
            return;
        }

        int sikeres = 0;
        int sikertelen = 0;

        for (Ulohely u : selectedSeats) {
            FoglalasiEredmeny eredmeny = service.megprobalFoglalni(currentIdopont.getIdopontId(), u.getUlohelyId(), userId);

            if (eredmeny.isSiker()) {
                sikeres++;
                Button btn = seatButtons.get(u);
                if (btn != null) {
                    btn.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white; -fx-font-weight: bold;");
                    btn.setDisable(true);
                }
            } else {
                sikertelen++;
            }
        }

        selectedSeats.clear();
        confirmButton.setText("Foglalás kész!");
        confirmButton.setDisable(true);

        String uzenet = sikeres + " hely sikeresen lefoglalva.";
        if (sikertelen > 0) {
            uzenet += "\n" + sikertelen + " helyet nem sikerült lefoglalni (már foglalt).";
        }

        showAlert("Foglalás eredménye", uzenet);

        // goBackToList();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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