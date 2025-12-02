package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.model.Felhasznalo;
import org.example.service.AuthEredmeny;
import org.example.service.DatabaseMoziService;
import org.example.service.MoziService;

import java.io.IOException;

public class MainController {

    private Stage stage;
    private final MoziService service = new DatabaseMoziService();

    // Globális statikus változó a bejelentkezett felhasználónak (egyszerű megoldás)
    public static Felhasznalo loggedInUser = null;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        AuthEredmeny eredmeny = service.megprobalBejelentkezni(user, pass);

        if (eredmeny.isSiker()) {
            loggedInUser = eredmeny.getFelhasznalo();
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Sikeres belépés: " + loggedInUser.getFelhasznaloNev());
            // Itt akár automatikusan tovább is dobhatnád a listára:
            // goToMovieList();
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText(eredmeny.getUzenet());
        }
    }

    @FXML
    public void handleRegister() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        AuthEredmeny eredmeny = service.megprobalRegisztralni(user, pass);

        if (eredmeny.isSiker()) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Regisztráció sikeres! Most lépj be.");
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText(eredmeny.getUzenet());
        }
    }

    @FXML
    public void goToMovieList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MovieList.fxml"));
            Pane root = loader.load();

            MovieListController controller = loader.getController();
            controller.setStage(stage);

            stage.setScene(new Scene(root));
            stage.setTitle("Absolute Cinema - Műsoron");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToMyBookings() {
        if (loggedInUser == null) {
            statusLabel.setText("Kérlek, jelentkezz be először!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MyBookings.fxml"));
            Pane root = loader.load();

            MyBookingsController controller = loader.getController();
            controller.setStage(stage);

            // ÁTADJUK A BEJELENTKEZETT FELHASZNÁLÓT:
            controller.initializeData(loggedInUser);

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToProfile() {
        System.out.println("Profil - Még nincs kész");
    }


}