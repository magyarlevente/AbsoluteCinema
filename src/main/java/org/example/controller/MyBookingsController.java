package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
    private Felhasznalo currentUser;

    @FXML private VBox bookingsContainer;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initializeData(Felhasznalo user) {
        this.currentUser = user;
        bookingsContainer.getChildren().clear();

        List<Foglalas> foglalasok = service.getFoglalasokFelhasznalonak(user.getFelhasznaloId());

        if (foglalasok.isEmpty()) {
            bookingsContainer.getChildren().add(new Label("Még nincs foglalásod."));
            return;
        }

        for (Foglalas f : foglalasok) {
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);");
            card.setPadding(new Insets(10));
            card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            Idopont idopont = service.getIdopontById(f.getIdopontId());
            Film film = (idopont != null) ? service.getFilmById(idopont.getFilmId()) : null;
            Ulohely ulohely = service.getUlohelyById(f.getUlohelyId());

            String filmCim = (film != null) ? film.getCim() : "Ismeretlen film";
            String datum = (idopont != null) ? idopont.getKezdesIdopont().format(DF) : "-";
            String szekInfo = (ulohely != null) ? (ulohely.getSorJele() + " sor, " + ulohely.getUlohelySzam() + ". szék") : "-";

            VBox details = new VBox(5);
            Label lblCim = new Label(filmCim);
            lblCim.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

            Label lblDatum = new Label("Időpont: " + datum);
            Label lblSzek = new Label("Hely: " + szekInfo);

            details.getChildren().addAll(lblCim, lblDatum, lblSzek);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button deleteBtn = new Button("Törlés");
            deleteBtn.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
            deleteBtn.setOnAction(e -> handleDelete(f));

            card.getChildren().addAll(details, spacer, deleteBtn);
            bookingsContainer.getChildren().add(card);
        }
    }

    private void handleDelete(Foglalas f) {
        boolean siker = service.torolFoglalas(f.getFoglalasId());
        if (siker) {
            initializeData(currentUser);
        } else {
            System.out.println("Hiba a törléskor");
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