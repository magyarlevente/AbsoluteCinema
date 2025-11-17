package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Vezérlő a Főmenühöz (Main.fxml). Kezeli a navigációt.
 */
public class MainController {

    private Stage stage;

    // Beállítja a Stage-et a Main.java-ból a navigációhoz
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void goToMovieList() {
        // Navigáció a Filmlista oldalra
        loadScene("view/MovieList.fxml", "Filmlista");
    }

    @FXML
    private void goToMyBookings() {
        System.out.println("Navigáció a Foglalásaim oldalra (implementálás alatt)...");
        // Itt jönne a loadScene("view/Bookings.fxml", "Foglalásaim") hívás.
    }

    @FXML
    private void goToProfile() {
        System.out.println("Navigáció a Profil oldalra (implementálás alatt)...");
        // Itt jönne a loadScene("view/Profile.fxml", "Profil") hívás.
    }

    /**
     * Segédmetódus a Scene cseréhez.
     */
    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
            Pane root = loader.load();

            // Átadjuk a Stage-et az új Controller-nek, ha van "setStage" metódusa
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setStage(stage);
            }

            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.setTitle("Absolute Cinema - " + title);
        } catch (IOException e) {
            System.err.println("Hiba a Scene betöltésekor (" + fxmlPath + "): " + e.getMessage());
            e.printStackTrace();
        }
    }
}

// Egyszerű alap interfész/osztály a Stage átadás segítésére
interface BaseController {
    void setStage(Stage stage);
}