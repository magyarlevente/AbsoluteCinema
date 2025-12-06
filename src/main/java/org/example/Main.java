package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.controller.MainController;
import org.example.database.DatabaseManager;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {

            // Létrehozza az adatbázist és a táblákat (Filmek, Felhasználók, Foglalások)
            DatabaseManager.initializeDatabase();

            URL fxmlUrl = getClass().getResource("/view/Main.fxml");

            if (fxmlUrl == null) {
                System.err.println("HIBA: Nem található a /view/Main.fxml fájl!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Pane root = loader.load();

            MainController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setTitle("Absolute Cinema");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}