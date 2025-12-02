package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.*;
import org.example.service.DatabaseMoziService;
import org.example.service.MoziService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyBookingsController implements BaseController {

    private Stage stage;
    private final MoziService service = new DatabaseMoziService();
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm");

    @FXML private VBox bookingsContainer;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initializeData(Felhasznalo user) {
        bookingsContainer.getChildren().clear();

        // 1. Foglalások lekérése
        List<Foglalas> foglalasok = service.getFoglalasokFelhasznalonak(user.getFelhasznaloId());

        if (foglalasok.isEmpty()) {
            bookingsContainer.getChildren().add(new Label("Még nincs foglalásod."));
            return;
        }

        // 2. Kártyák létrehozása minden foglaláshoz
        for (Foglalas f : foglalasok) {
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);");
            card.setPadding(new Insets(10));

            // Adatok kikeresése ID alapján
            Idopont idopont = service.getIdopontById(f.getIdopontId());
            Film film = (idopont != null) ? service.getFilmById(idopont.getFilmId()) : null;
            Ulohely ulohely = service.getUlohelyById(f.getUlohelyId());

            // Szövegek összeállítása
            String filmCim = (film != null) ? film.getCim() : "Ismeretlen film";
            String datum = (idopont != null) ? idopont.getKezdesIdopont().format(DF) : "-";
            String szekInfo = (ulohely != null) ? (ulohely.getSorJele() + " sor, " + ulohely.getUlohelySzam() + ". szék") : "-";

            VBox details = new VBox(5);
            Label lblCim = new Label(filmCim);
            lblCim.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

            Label lblDatum = new Label("Időpont: " + datum);
            Label lblSzek = new Label("Hely: " + szekInfo);

            details.getChildren().addAll(lblCim, lblDatum, lblSzek);
            card.getChildren().add(details);

            bookingsContainer.getChildren().add(card);
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
            Pane root = loader.load();
            MainController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}