import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL; // Hozzáadtuk

/**
 * Az Absolute Cinema JavaFX alkalmazás belépési pontja.
 */
public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Absolute Cinema - Jegyfoglalás");

        // Főmenü betöltése
        try {
            // A getClass().getClassLoader().getResource() használata a "view/Main.fxml" elérési úttal.
            // Ez megbízhatóbb, ha a Main osztály a default (gyökér) package-ben van.
            URL fxmlUrl = getClass().getClassLoader().getResource("view/Main.fxml");

            if (fxmlUrl == null) {
                // Hiba, ha az FXML fájl nem található
                throw new IOException("FXML fájl nem található: view/Main.fxml. Kérlek ellenőrizd, hogy a fájl a src/main/resources/view/ mappában van-e.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Pane root = loader.load();

            MainController controller = loader.getController();
            // A Stage átadása a Controller-nek a navigációhoz
            controller.setStage(primaryStage);

            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("FATAL Hiba a Main.fxml betöltésekor: " + e.getMessage());
            e.printStackTrace();
            // Megjelenítünk egy üres ablakot, de a konzolban hibaüzenet van
            if (stage.getScene() == null) {
                // Biztosítjuk, hogy legalább egy üres ablak megjelenjen, ha nem crash-elünk
                stage.setScene(new Scene(new Pane(), 1000, 700));
                stage.show();
            }
        }
    }

    // A Main metódus most csak meghívja az Application.launch()-t.
    public static void main(String[] args) {
        launch(args);
    }
}