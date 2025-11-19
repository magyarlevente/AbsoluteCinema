package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.controller.MainController;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // 1. Megkeressük az FXML fájlt
            URL fxmlUrl = getClass().getResource("/view/Main.fxml");

            if (fxmlUrl == null) {
                System.err.println("HIBA: Nem található a /view/Main.fxml fájl!");
                return;
            }

            // 2. Betöltjük az ablakot
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Pane root = loader.load();

            // 3. Beállítjuk a Controllert
            MainController controller = loader.getController();
            controller.setStage(stage);

            // 4. Megjelenítjük
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