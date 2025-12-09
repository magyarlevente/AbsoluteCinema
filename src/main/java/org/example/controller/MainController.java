package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.Felhasznalo;
import org.example.service.AuthEredmeny;
import org.example.service.DatabaseMoziService;
import org.example.service.MoziService;

import java.io.IOException;

//a fomenu es bejelentkezes controllere.

public class MainController {

    private Stage stage;
    private final MoziService service = new DatabaseMoziService();

    public static Felhasznalo loggedInUser = null;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private VBox loginPanel;
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        if (loggedInUser != null) {
            showLoggedInState();
        }
    }

    @FXML
    public void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        AuthEredmeny eredmeny = service.megprobalBejelentkezni(user, pass);

        if (eredmeny.isSiker()) {
            loggedInUser = eredmeny.getFelhasznalo();
            showLoggedInState();
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText(eredmeny.getUzenet());
        }
    }

    private void showLoggedInState() {
        loginPanel.setVisible(false);
        loginPanel.setManaged(false);

        welcomeLabel.setText("Bejelentkezve: " + loggedInUser.getFelhasznaloNev());
        welcomeLabel.setVisible(true);
        welcomeLabel.setManaged(true);

        logoutButton.setVisible(true);
        logoutButton.setManaged(true);
    }

    @FXML
    public void handleLogout() {
        loggedInUser = null;

        welcomeLabel.setVisible(false);
        welcomeLabel.setManaged(false);
        logoutButton.setVisible(false);
        logoutButton.setManaged(false);

        loginPanel.setVisible(true);
        loginPanel.setManaged(true);

        usernameField.clear();
        passwordField.clear();
        statusLabel.setText("");
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
            if (statusLabel != null) {
                statusLabel.setText("Kérlek, jelentkezz be először!");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MyBookings.fxml"));
            Pane root = loader.load();

            MyBookingsController controller = loader.getController();
            controller.setStage(stage);
            controller.initializeData(loggedInUser);

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToProfile() {
        System.out.println("Profil");
    }
}